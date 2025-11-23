
# 📚 BiblioStock / Livraria API 

API para gerenciamento completo de estoque de livraria, incluindo produtos, categorias, autores, movimentações, reajustes de preço, relatórios, além de instruções para rodar o projeto com Docker e migrations Flyway.

---

## 📖 Sumário
1. [🚀 Tecnologias Utilizadas](#-tecnologias-utilizadas)
2. [⚙️ Estrutura das Entidades Principais](#️-estrutura-das-entidades-principais)
3. [🌐 Endpoints Disponíveis](#-endpoints-disponíveis)
   - [Usuários](#-usuários)
   - [Categorias](#-categorias)
   - [Autores](#-autores)
   - [Produtos](#-produtos)
   - [Movimentações de Estoque](#-movimentações-de-estoque)
   - [Reajustes de Preço](#-reajustes-de-preço)
   - [Relatórios](#-relatórios)
4. [🧠 Regras de Negócio](#-regras-de-negócio)
5. [🧩 Requisitos de Ambiente](#-requisitos-de-ambiente)
6. [🧰 Como Rodar a API](#-como-rodar-a-api)
   - [Rodar com Docker](#️-rodar-com-docker-recomendado)
   - [Migrations Flyway](#-migrations-flyway)
   - [Rodar Localmente](#-rodar-localmente-sem-docker)
7. [👥 Equipe](#-equipe)
8. [📄 Licença](#-licença)

---

## 🚀 Tecnologias Utilizadas

### Backend
- Java 17+
- Spring Boot 3
- Spring Data JPA
- Spring Validation (Jakarta)
- Spring Web
- Springdoc OpenAPI
- Hibernate
- Lombok
- JdbcTemplate
- Flyway (migrations)
- PostgreSQL (Supabase opcional)

### Infra / DevOps
- Docker 24+
- Docker Compose v2+
- Maven 3.9+

---

## ⚙️ Estrutura das Entidades Principais
- **User** – Usuários do sistema (Admin e Funcionários)
- **Category** – Classificação dos produtos
- **Author** – Autores dos livros
- **Product** – Itens registrados no estoque
- **Movement** – Entradas e saídas de estoque
- **PriceAdjustment** – Histórico de reajustes globais ou por categoria

---

## 🌐 Endpoints Disponíveis

### 🔸 Usuários
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/users` | Lista usuários |
| GET | `/api/users/{id}` | Busca por ID |
| POST | `/api/users` | Cria usuário |
| PUT | `/api/users/{id}` | Atualiza usuário |
| DELETE | `/api/users/{id}` | Remove usuário |

---

### 🔸 Categorias
| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| GET | `/api/categories` | Lista categorias |
| GET | `/api/categories/{id}` | Busca por ID |
| POST | `/api/categories` | Cria categoria |
| PUT | `/api/categories/{id}` | Atualiza categoria |
| DELETE | `/api/categories/{id}` | Exclui (somente sem produtos associados) |

---

### 🔸 Autores
| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| GET | `/api/authors` | Lista autores |
| GET | `/api/authors/{id}` | Detalhes + livros |
| POST | `/api/authors` | Cria autor |
| PUT | `/api/authors/{id}` | Atualiza autor |
| DELETE | `/api/authors/{id}` | Remove se sem livros vinculados |

---

### 🔸 Produtos
| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| GET | `/api/products` | Lista produtos (filtros disponíveis) |
| GET | `/api/products/{id}` | Detalhes do produto |
| GET | `/api/products/by-category/{id}` | Lista produtos por categoria |
| POST | `/api/products` | Cria produto |
| PUT | `/api/products/{id}` | Atualiza produto |
| DELETE | `/api/products/{id}` | Remove produto |

---

### 🔸 Movimentações de Estoque
| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| GET | `/api/movements` | Lista movimentações (filtros disponíveis) |
| POST | `/api/movements` | Registra uma nova movimentação |

---

### 🔸 Reajustes de Preço
| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| POST | `/api/prices/adjust` | Reajuste global ou por categoria |
| GET | `/api/prices/history` | Histórico de reajustes |
| GET | `/api/prices/category-percent` | Lista percentuais de ajuste por categoria |
| GET | `/api/prices/category-percent/{categoryId}` | Lista o percentual de ajuste de uma categoria específica |

---

### 🔸 Relatórios
| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| GET | `/api/reports/products-below-minimum` | Produtos abaixo do mínimo |
| GET | `/api/reports/products-per-category` | Produtos por categoria |
| GET | `/api/reports/products-per-category/{categoryId}` | Produtos de uma categoria específica |
| GET | `/api/reports/balance` | Relatório de balanço de estoque |
| GET | `/api/reports/products-per-author/{authorId}` | Relatório de balanço de estoque |

---

## 🧠 Regras de Negócio
- Não excluir categorias com produtos vinculados.
- Movimentações de saída não podem gerar estoque negativo.
- Autores só podem ser removidos se não houver livros associados.
- Reajustes não alteram o preço base (`price`), apenas `price_with_percent`.
- Flyway controla versões do banco e impede mudanças manuais conflitantes.
- Todo reajuste cria um registro em `price_adjustments`.

---

## 🧩 Requisitos de Ambiente
- Java 17+
- Maven 3.9+
- Docker e Docker Compose (opcional, recomendado)
- PostgreSQL 16+

---

## 🧰 Como Rodar a API

### ▶️ Rodar com Docker (recomendado)

1. Certifique-se de que **Docker** e **Docker Compose** estão instalados.
2. Execute:

```bash
docker compose up --build
```

- API: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui/index.html`

### Resetar banco:

```bash
docker compose down -v
docker compose up --build
```

---

## 🗄️ Migrations (Flyway)
Rodadas automaticamente ao iniciar aplicação.

Arquivos em:  
`src/main/resources/db/migration`

- `V1__create_livraria_schema.sql`
- `V2__seed_livraria_data.sql`

---

## 🧪 Rodar Localmente (sem Docker)

### 1. Criar banco:
```sql
CREATE DATABASE bibliostock;
```

### 2. Configurar `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bibliostock
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=validate
```

### 3. Rodar:
```bash
mvn spring-boot:run
```

---

## 👥 Equipe
| Nome | Função |
|------|---------|
| **Emely Santos (GreyPompom)** | Desenvolvedora |
| Hellen | Desenvolvedora |
| Maria Luiza | Desenvolvedora |
| Leticia | Desenvolvedora |

---

## 📄 Licença
Projeto de uso acadêmico e portfólio.
