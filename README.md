# 📚 Sistema de Controle de Estoque – Livraria API

API desenvolvida para o **Sistema de Controle de Estoque de uma Livraria**, permitindo o gerenciamento de produtos, categorias, autores, movimentações, reajustes de preço e relatórios.

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
7. [🧾 Scripts Úteis](#-scripts-úteis)
8. [👥 Equipe](#-equipe)
9. [📄 Licença](#-licença)

---

## 🚀 Tecnologias Utilizadas

| Tecnologia | Descrição |
|-------------|------------|
| **Java 17+** | Linguagem principal da aplicação |
| **Spring Boot 3** | Framework para construção da API REST |
| **Spring Data JPA** | Acesso e persistência de dados |
| **PostgreSQL** | Banco de dados relacional |
| **Supabase** | Hospedagem do banco de dados PostgreSQL |
| **Lombok** | Redução de boilerplate (getters, setters, builders) |
| **Jakarta Validation** | Validação de campos nos DTOs |
| **JdbcTemplate** | Execução direta de funções SQL no banco |
| **Spring Web** | Exposição de endpoints REST |
| **ControllerAdvice** | Tratamento global de exceções |

---

## ⚙️ Estrutura das Entidades Principais

- **User** → Representa usuários do sistema (Admin, Funcionários).  
- **Category** → Classifica os produtos (ex: Ficção, Romance, Não-Ficção).  
- **Author** → Registra autores dos livros.  
- **Product** → Representa os livros e itens da livraria.  
- **Movement** → Registra movimentações de entrada e saída de estoque.  
- **PriceAdjustment** → Histórico de reajustes globais e por categoria.

---

## 🌐 Endpoints Disponíveis

### 🔸 Usuários
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `GET` | `/api/users` | Lista todos os usuários |
| `GET` | `/api/users/{id}` | Busca um usuário por ID |
| `POST` | `/api/users` | Cria um novo usuário |
| `PUT` | `/api/users/{id}` | Atualiza dados de um usuário |
| `DELETE` | `/api/users/{id}` | Exclui um usuário |

---

### 🔸 Categorias
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `GET` | `/api/categories` | Lista todas as categorias |
| `GET` | `/api/categories/{id}` | Busca uma categoria por ID |
| `POST` | `/api/categories` | Cria uma nova categoria |
| `PUT` | `/api/categories/{id}` | Atualiza uma categoria existente |
| `DELETE` | `/api/categories/{id}` | Exclui uma categoria (se não houver produtos) |

---

### 🔸 Autores
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `GET` | `/api/authors` | Lista todos os autores |
| `GET` | `/api/authors/{id}` | Busca autor por ID |
| `POST` | `/api/authors` | Cria um novo autor |
| `PUT` | `/api/authors/{id}` | Atualiza autor existente |
| `DELETE` | `/api/authors/{id}` | Exclui autor (somente se não houver livros associados) |

---

### 🔸 Produtos
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `GET` | `/api/products` | Lista todos os produtos |
| `GET` | `/api/products/{id}` | Busca produto por ID |
| `POST` | `/api/products` | Cadastra um novo produto |
| `PUT` | `/api/products/{id}` | Atualiza um produto existente |
| `DELETE` | `/api/products/{id}` | Remove um produto |

---

### 🔸 Movimentações de Estoque
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `GET` | `/api/movements` | Lista todas as movimentações |
| `POST` | `/api/movements` | Cria uma movimentação (entrada ou saída de produto) |

---

### 🔸 Reajustes de Preço
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `POST` | `/api/prices/adjust` | Aplica reajuste de preço global ou por categoria |
| `GET` | `/api/prices/history` | Lista histórico de reajustes aplicados |

---

### 🔸 Relatórios
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `GET` | `/api/reports/low-stock` | Lista produtos com estoque abaixo da quantidade mínima |
| `GET` | `/api/reports/categories` | Exibe quantidade de produtos por categoria |
| `GET` | `/api/reports/balance` | Exibe balanço físico e financeiro (quantidades e valores totais) |

---

## 🧠 Regras de Negócio

- Não é permitido excluir uma **categoria** com produtos associados.  
- Um **livro** deve ter pelo menos um **autor**.  
- Não é permitido excluir um **autor** com livros vinculados.  
- Movimentações de saída não podem deixar o estoque negativo.  
- Reajustes percentuais não modificam o preço base (`price`), apenas recalculam `price_with_percent`.  
- Todo reajuste global ou de categoria gera um registro em `price_adjustments`.

---

## 🧩 Requisitos de Ambiente

- **Java 17+**
- **Maven 3.9+**
- **PostgreSQL (ou Supabase)**
- **Docker (opcional, para deploy)**

---

## 🧰 Como Rodar a API

1. **Clone o repositório**
   ```bash
   git clone https://github.com/SeuUsuario/livraria-api.git
   cd livraria-api
   ```

2. **Configure o banco de dados** no arquivo `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://<host>:<port>/<database>
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

3. **Compile e execute a aplicação**
   ```bash
   mvn clean package
   mvn spring-boot:run
   ```

4. **Acesse a API**
   - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - Health Check: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

---

## 👥 Equipe

| Nome | Função |
|------|--------|
| **Emely Santos (GreyPompom)** | Líder Técnica |
| **Hellen** | Desenvolvedora  |
| **Maria Luiza** | Desenvolvedora |
| **Leticia** | Desenvolvedora  |

---

## 📄 Licença

Este projeto é de uso acadêmico, desenvolvido para fins educacionais e de portfólio.
