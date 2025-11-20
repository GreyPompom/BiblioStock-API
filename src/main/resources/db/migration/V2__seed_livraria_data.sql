BEGIN;

-- 1. Usuários
INSERT INTO public.users (username, full_name, email)
VALUES
('admin', 'Administrador', 'admin@livraria.com'),
('estoquista', 'João Silva', 'joao@livraria.com');

-- 2. Categorias (inclui Romance porque você usa nos reajustes)
INSERT INTO public.categories (name, size, packaging_type, default_adjustment_percent)
VALUES
('Ficção', 'Médio', 'Capa Dura', 5.00),
('Não-Ficção', 'Grande', 'Capa Comum', 3.00),
('Romance', 'Médio', 'Capa Comum', 2.00);

-- 3. Autores
INSERT INTO public.authors (full_name, nationality, birth_date, biography)
VALUES
('Paulo Coelho', 'Brasileira', '1947-08-24', 'Autor brasileiro de renome internacional.'),
('George Orwell', 'Britânica', '1903-06-25', 'Autor de clássicos como 1984 e A Revolução dos Bichos');

-- 4. Produtos
INSERT INTO public.products (sku, name, product_type, price, unit, stock_qty, min_qty, max_qty, category_id, publisher, isbn)
VALUES
('SKU001', 'O Alquimista', 'Livro', 39.90, 'unidade', 10, 2, 50,
 (SELECT id FROM public.categories WHERE name='Ficção'),
 'Editora Rocco', '9788576653727'),
('SKU002', '1984', 'Livro', 45.00, 'unidade', 5, 2, 30,
 (SELECT id FROM public.categories WHERE name='Não-Ficção'),
 'Companhia das Letras', '9788535922271'),
('SKU003', 'O Diário de Anne Frank', 'Livro', 35.50, 'unidade', 8, 1, 25,
 (SELECT id FROM public.categories WHERE name='Não-Ficção'),
 'Editora Record', '9788501109872');

-- 5. Produtos x Autores
INSERT INTO public.product_authors (product_id, author_id, primary_author)
VALUES
((SELECT id FROM public.products WHERE name='O Alquimista'),
 (SELECT id FROM public.authors WHERE full_name='Paulo Coelho'),
 TRUE),
((SELECT id FROM public.products WHERE name='1984'),
 (SELECT id FROM public.authors WHERE full_name='George Orwell'),
 TRUE),
((SELECT id FROM public.products WHERE name='O Diário de Anne Frank'),
 (SELECT id FROM public.authors WHERE full_name='George Orwell'),
 TRUE);

-- 6. Movimentações de Estoque (usa trigger para ajustar estoque/alertas)
INSERT INTO public.movements (product_id, product_name_snapshot, quantity, movement_type, note, user_id)
VALUES
((SELECT id FROM public.products WHERE name='O Alquimista'),
 'O Alquimista', 10, 'ENTRADA', 'Estoque inicial',
 (SELECT id FROM public.users WHERE username='estoquista')),
((SELECT id FROM public.products WHERE name='1984'),
 '1984', 5, 'ENTRADA', 'Estoque inicial',
 (SELECT id FROM public.users WHERE username='estoquista')),
((SELECT id FROM public.products WHERE name='O Diário de Anne Frank'),
 'O Diário de Anne Frank', 8, 'ENTRADA', 'Estoque inicial',
 (SELECT id FROM public.users WHERE username='estoquista'));

INSERT INTO public.movements (product_id, product_name_snapshot, quantity, movement_type, note, user_id)
VALUES
((SELECT id FROM public.products WHERE name='O Alquimista'),
 'O Alquimista', 2, 'SAIDA', 'Venda realizada',
 (SELECT id FROM public.users WHERE username='estoquista')),
((SELECT id FROM public.products WHERE name='O Diário de Anne Frank'),
 'O Diário de Anne Frank', 1, 'SAIDA', 'Venda realizada',
 (SELECT id FROM public.users WHERE username='estoquista'));

-- 7. Reajustes iniciais
INSERT INTO public.price_adjustments (applied_at, applied_by, scope_type, category_id, percent, note)
VALUES
(NOW(), (SELECT id FROM public.users WHERE username='admin'),
 'CATEGORIA', (SELECT id FROM public.categories WHERE name='Ficção'), 0.05, 'Percentual inicial Ficção'),
(NOW(), (SELECT id FROM public.users WHERE username='admin'),
 'CATEGORIA', (SELECT id FROM public.categories WHERE name='Romance'), 0.02, 'Percentual inicial Romance'),
(NOW(), (SELECT id FROM public.users WHERE username='admin'),
 'CATEGORIA', (SELECT id FROM public.categories WHERE name='Não-Ficção'), 0.04, 'Percentual inicial Não-ficção'),
(NOW(), (SELECT id FROM public.users WHERE username='admin'),
 'GLOBAL', NULL, 0.03, 'Percentual global inicial');

-- 8. Atualizar price_with_percent com base nos reajustes mais recentes
UPDATE public.products p
SET price_with_percent = ROUND(
    p.price * 
    (1 + COALESCE(
        (SELECT percent FROM public.price_adjustments pa 
         WHERE pa.scope_type = 'GLOBAL' 
         ORDER BY applied_at DESC LIMIT 1), 0)) *
    (1 + COALESCE(
        (SELECT percent FROM public.price_adjustments pa 
         WHERE pa.scope_type = 'CATEGORIA' AND pa.category_id = p.category_id
         ORDER BY applied_at DESC LIMIT 1), 0))
, 2);

COMMIT;
