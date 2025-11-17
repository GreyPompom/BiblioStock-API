BEGIN;

-- 1. SCHEMA
CREATE SCHEMA IF NOT EXISTS public;

-- 2. TABELAS BASE

CREATE TABLE IF NOT EXISTS public.users (
    id BIGSERIAL PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    full_name TEXT,
    email TEXT UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.categories (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    size TEXT CHECK (size IN ('Pequeno','Médio','Grande')) NOT NULL,
    packaging_type TEXT,
    default_adjustment_percent NUMERIC(5,2) DEFAULT 0 CHECK (default_adjustment_percent >= 0),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.authors (
    id BIGSERIAL PRIMARY KEY,
    full_name TEXT NOT NULL,
    nationality TEXT,
    birth_date DATE,
    biography TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.products (
    id BIGSERIAL PRIMARY KEY,
    sku TEXT UNIQUE,
    name TEXT NOT NULL,
    product_type TEXT CHECK (product_type IN ('Livro','Revista','Outro')) DEFAULT 'Livro',
    price NUMERIC(12,2) NOT NULL CHECK (price >= 0),
    unit TEXT NOT NULL DEFAULT 'unidade',
    stock_qty NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (stock_qty >= 0),
    min_qty NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (min_qty >= 0),
    max_qty NUMERIC(12,2) DEFAULT NULL CHECK (max_qty IS NULL OR max_qty >= 0),
    category_id BIGINT NOT NULL REFERENCES public.categories(id) ON DELETE RESTRICT,
    publisher TEXT,
    isbn TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.product_authors (
    product_id BIGINT NOT NULL REFERENCES public.products(id) ON DELETE CASCADE,
    author_id BIGINT NOT NULL REFERENCES public.authors(id) ON DELETE RESTRICT,
    primary_author BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (product_id, author_id)
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_type t
        JOIN pg_namespace n ON n.oid = t.typnamespace
        WHERE t.typname = 'movement_type' AND n.nspname = 'public'
    ) THEN
        CREATE TYPE public.movement_type AS ENUM ('ENTRADA','SAIDA');
    END IF;
END$$;

CREATE TABLE IF NOT EXISTS public.movements (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES public.products(id) ON DELETE RESTRICT,
    product_name_snapshot TEXT NOT NULL,
    movement_date TIMESTAMP WITH TIME ZONE DEFAULT now(),
    quantity NUMERIC(12,2) NOT NULL CHECK (quantity > 0),
    movement_type public.movement_type NOT NULL,
    note TEXT,
    user_id BIGINT REFERENCES public.users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS public.price_adjustments (
    id BIGSERIAL PRIMARY KEY,
    applied_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    applied_by BIGINT REFERENCES public.users(id) ON DELETE SET NULL,
    scope_type TEXT CHECK (scope_type IN ('GLOBAL','CATEGORIA')) NOT NULL,
    category_id BIGINT REFERENCES public.categories(id) ON DELETE SET NULL,
    percent NUMERIC(7,4) NOT NULL,
    note TEXT
);

CREATE TABLE IF NOT EXISTS public.alerts (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES public.products(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    alert_type TEXT CHECK (alert_type IN ('BAIXO_ESTOQUE','EXCEDENTE_ESTOQUE')),
    qty NUMERIC(12,2),
    message TEXT
);

-- Coluna extra de preço ajustado
ALTER TABLE public.products
    ADD COLUMN IF NOT EXISTS price_with_percent NUMERIC(10,2) DEFAULT 0;

-- 2.1 ÍNDICES
CREATE INDEX IF NOT EXISTS idx_products_name ON public.products USING gin (to_tsvector('portuguese', name));
CREATE INDEX IF NOT EXISTS idx_products_isbn ON public.products(isbn);
CREATE INDEX IF NOT EXISTS idx_products_category ON public.products(category_id);
CREATE INDEX IF NOT EXISTS idx_authors_name ON public.authors USING gin (to_tsvector('portuguese', full_name));

-- 3. FUNÇÕES (EXATAMENTE COMO VOCÊ MANDOU)

CREATE OR REPLACE FUNCTION public.fn_apply_movement() RETURNS TRIGGER AS $$
DECLARE
    new_stock NUMERIC;
    msg TEXT;
BEGIN
    IF (TG_OP = 'INSERT') THEN
        IF (NEW.movement_type = 'ENTRADA') THEN
            UPDATE public.products SET stock_qty = stock_qty + NEW.quantity WHERE id = NEW.product_id;
        ELSE
            UPDATE public.products SET stock_qty = stock_qty - NEW.quantity WHERE id = NEW.product_id;
        END IF;
    ELSIF (TG_OP = 'DELETE') THEN
        IF (OLD.movement_type = 'ENTRADA') THEN
            UPDATE public.products SET stock_qty = stock_qty - OLD.quantity WHERE id = OLD.product_id;
        ELSE
            UPDATE public.products SET stock_qty = stock_qty + OLD.quantity WHERE id = OLD.product_id;
        END IF;
    END IF;

    SELECT stock_qty INTO new_stock FROM public.products WHERE id = COALESCE(NEW.product_id, OLD.product_id);

    DELETE FROM public.alerts WHERE product_id = COALESCE(NEW.product_id, OLD.product_id);

    PERFORM 1 FROM public.products WHERE id = COALESCE(NEW.product_id, OLD.product_id);
    IF FOUND THEN
        IF (new_stock < (SELECT min_qty FROM public.products WHERE id = COALESCE(NEW.product_id, OLD.product_id))) THEN
            msg := format('Estoque abaixo do mínimo (atual: %s)', new_stock);
            INSERT INTO public.alerts (product_id, alert_type, qty, message)
              VALUES (COALESCE(NEW.product_id, OLD.product_id), 'BAIXO_ESTOQUE', new_stock, msg);
        ELSIF ((SELECT max_qty FROM public.products WHERE id = COALESCE(NEW.product_id, OLD.product_id)) IS NOT NULL AND
               new_stock > (SELECT max_qty FROM public.products WHERE id = COALESCE(NEW.product_id, OLD.product_id))) THEN
            msg := format('Estoque acima do máximo (atual: %s)', new_stock);
            INSERT INTO public.alerts (product_id, alert_type, qty, message)
              VALUES (COALESCE(NEW.product_id, OLD.product_id), 'EXCEDENTE_ESTOQUE', new_stock, msg);
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_after_movements_insert ON public.movements;
CREATE TRIGGER trg_after_movements_insert
AFTER INSERT OR DELETE ON public.movements
FOR EACH ROW EXECUTE FUNCTION public.fn_apply_movement();

CREATE OR REPLACE FUNCTION public.fn_prevent_author_delete() RETURNS TRIGGER AS $$
DECLARE
    cnt INT;
BEGIN
    SELECT COUNT(*) INTO cnt FROM public.product_authors WHERE author_id = OLD.id;
    IF cnt > 0 THEN
        RAISE EXCEPTION 'Não é permitido excluir o autor % porque existe(m) % livro(s) associado(s).', OLD.full_name, cnt;
    END IF;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_before_author_delete ON public.authors;
CREATE TRIGGER trg_before_author_delete
BEFORE DELETE ON public.authors
FOR EACH ROW EXECUTE FUNCTION public.fn_prevent_author_delete();

CREATE OR REPLACE FUNCTION public.fn_prevent_category_delete() RETURNS TRIGGER AS $$
DECLARE
    cnt INT;
BEGIN
    SELECT COUNT(*) INTO cnt FROM public.products WHERE category_id = OLD.id;
    IF cnt > 0 THEN
        RAISE EXCEPTION 'Não é permitido excluir a categoria % porque % produto(s) estão vinculados.', OLD.name, cnt;
    END IF;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_before_category_delete ON public.categories;
CREATE TRIGGER trg_before_category_delete
BEFORE DELETE ON public.categories
FOR EACH ROW EXECUTE FUNCTION public.fn_prevent_category_delete();

-- Função de reajuste (versão JSON)
CREATE OR REPLACE FUNCTION public.fn_apply_price_adjustment(
    p_scope_type TEXT,
    p_percent NUMERIC,
    p_applied_by BIGINT,
    p_category_id BIGINT DEFAULT NULL,
    p_note TEXT DEFAULT NULL
) RETURNS JSON AS $$
DECLARE
    v_products_updated INTEGER := 0;
    v_product_ids BIGINT[];
    v_result JSON;
BEGIN
    IF p_scope_type NOT IN ('GLOBAL','CATEGORIA') THEN
        RAISE EXCEPTION 'scope_type inválido. Use: GLOBAL ou CATEGORIA';
    END IF;
    
    IF p_scope_type = 'CATEGORIA' AND p_category_id IS NULL THEN
        RAISE EXCEPTION 'category_id é obrigatório para ajuste por CATEGORIA';
    END IF;
    
    IF p_percent IS NULL OR p_percent < -1 OR p_percent > 10 THEN
        RAISE EXCEPTION 'percent deve estar entre -1 e 10';
    END IF;

    INSERT INTO public.price_adjustments (applied_at, applied_by, scope_type, category_id, percent, note)
    VALUES (now(), p_applied_by, p_scope_type, p_category_id, p_percent, p_note);

    IF p_scope_type = 'CATEGORIA' THEN
        UPDATE public.categories 
        SET default_adjustment_percent = p_percent 
        WHERE id = p_category_id;
        
        WITH updated_products AS (
            UPDATE public.products
            SET price_with_percent = round(price * (1 + p_percent), 2)
            WHERE category_id = p_category_id
            RETURNING id
        )
        SELECT array_agg(id), COUNT(*) 
        INTO v_product_ids, v_products_updated
        FROM updated_products;
        
    ELSE
        WITH updated_products AS (
            UPDATE public.products
            SET price_with_percent = round(price * (1 + p_percent), 2)
            RETURNING id
        )
        SELECT array_agg(id), COUNT(*) 
        INTO v_product_ids, v_products_updated
        FROM updated_products;
    END IF;

    v_result := json_build_object(
        'message', 'Ajuste ' || p_scope_type || ' aplicado com sucesso',
        'products_updated', v_products_updated,
        'product_ids', COALESCE(v_product_ids, ARRAY[]::BIGINT[]),
        'scope_type', p_scope_type,
        'percent_applied', p_percent
    );

    RETURN v_result;
END;
$$ LANGUAGE plpgsql;

-- 4. VIEWS

CREATE OR REPLACE VIEW public.vw_price_list AS
SELECT p.id, p.name, p.isbn, p.price, p.unit, p.stock_qty, c.name AS category_name, p.publisher
FROM public.products p
LEFT JOIN public.categories c ON p.category_id = c.id
ORDER BY p.name;

CREATE OR REPLACE VIEW public.vw_balance AS
SELECT p.id, p.name, p.stock_qty, p.price, (p.stock_qty * p.price) AS total_value
FROM public.products p
ORDER BY p.name;

CREATE OR REPLACE VIEW public.vw_below_min AS
SELECT p.id, p.name, p.min_qty, p.stock_qty
FROM public.products p
WHERE p.stock_qty < p.min_qty
ORDER BY p.name;

CREATE OR REPLACE VIEW public.vw_products_per_category AS
SELECT c.id, c.name, COUNT(p.id) AS product_count
FROM public.categories c
LEFT JOIN public.products p ON p.category_id = c.id
GROUP BY c.id, c.name
ORDER BY c.name;

CREATE OR REPLACE VIEW public.vw_products_below_minimum AS
SELECT 
    p.id AS product_id,
    p.name AS product_name,
    c.name AS category_name,
    p.min_qty,
    p.stock_qty,
    (p.min_qty - p.stock_qty) AS deficit
FROM 
    public.products p
LEFT JOIN 
    public.categories c ON c.id = p.category_id
WHERE 
    p.stock_qty < p.min_qty;

-- Limpeza de coisas antigas (para evitar conflitos em futuras execuções)
DROP TRIGGER IF EXISTS trg_after_product_insert_update ON public.products;
DROP TRIGGER IF EXISTS trg_before_product_authors_delete ON public.product_authors;
DROP FUNCTION IF EXISTS public.fn_check_book_has_authors();

COMMIT;
