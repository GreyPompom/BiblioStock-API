
# BiblioStock API – Documentação Completa

## 📚 Descrição
API para gerenciamento de estoque de livraria — produtos, categorias, autores, movimentações e reajustes de preço. Desenvolvida em Java + Spring Boot, PostgreSQL, Flyway e Docker.

---

## 🚀 Tecnologias Utilizadas
### Backend
- Java 17
- Spring Boot 3.4.10
- Spring Data JPA
- Spring Validation
- Flyway 10.x
- Hibernate 6
- PostgreSQL 16
- Lombok
- Springdoc OpenAPI 2.8.13

### Infra / DevOps
- Docker 24+
- Docker Compose v2
- Maven 3.9+

---

## 🏗️ Arquitetura
- Controller → Service → Repository → Database  
- Flyway controla as migrations em `src/main/resources/db/migration`

---

## 🛣️ Endpoints Principais

### 📌 **Categorias**
**GET /categories**  
Retorna lista de categorias.

**GET /categories/{id}**  
Retorna detalhes de uma categoria.

**POST /categories**  
Cria uma nova categoria.

**PUT /categories/{id}**  
Atualiza categoria existente.

**DELETE /categories/{id}**  
Remove uma categoria (somente se não houver produtos vinculados).

---

### 📌 **Produtos**
**GET /products**  
Lista produtos com filtros (nome, categoria, faixa de preço, estoque etc.).

**GET /products/{id}**  
Retorna detalhes do produto incluindo autores.

**POST /products**  
Cria um novo produto.

**PUT /products/{id}**  
Atualiza um produto existente.

**DELETE /products/{id}**  
Remove produto (estoque pode ser zero ou mais).

---

### 📌 **Autores**
**GET /authors**  
Lista autores com filtros (nome, nacionalidade).

**GET /authors/{id}**  
Retorna autor + livros relacionados.

**POST /authors**  
Cria novo autor.

**PUT /authors/{id}**  
Atualiza dados do autor.

**DELETE /authors/{id}**  
Só permite exclusão se não houver livros vinculados.

---

### 📌 **Movimentações**
**GET /movements**  
Lista movimentações com filtros de data, tipo e produto.

**POST /movements/entry**  
Cria movimentação de ENTRADA (estoque aumenta).

**POST /movements/exit**  
Cria movimentação de SAÍDA (estoque diminui).

> Triggers atualizam estoque e geram alertas automaticamente.

---

### 📌 **Reajustes de Preço**
**POST /price-adjustments/apply**  
Aplica reajuste GLOBAL ou POR CATEGORIA.  
Retorna JSON com produtos afetados.

**GET /price-adjustments/history**  
Lista histórico de reajustes.

---

### 📌 **Relatórios**
**GET /reports/price-list** → View: `vw_price_list`  
**GET /reports/balance** → View: `vw_balance`  
**GET /reports/below-minimum** → View: `vw_below_minimum`  
**GET /reports/per-category** → View: `vw_products_per_category`  
**GET /reports/top-movements** → cálculo de produto com mais entradas e saídas.

---

## 🐳 Como Rodar com Docker

### Subir tudo:
```
docker compose up --build
```

API: http://localhost:8080  
Swagger: http://localhost:8080/swagger-ui/index.html  

---

## 🗄️ Migrations (dbinitializer Flyway)
Rodam automaticamente ao subir a API.

```
V1__create_livraria_schema.sql
V2__seed_livraria_data.sql
```

Para resetar o banco:
```
docker compose down -v
docker compose up --build
```

---

## 🧪 Rodar localmente
Criar banco:
```
CREATE DATABASE bibliostock;
```

Rodar a API:
```
mvn spring-boot:run
```

---
