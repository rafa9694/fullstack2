# JTech TaskList — Fullstack Challenge

Sistema TODO List multiusuário com autenticação JWT, desenvolvido como parte do processo seletivo JTech.

---

## Tecnologias

### Backend
- Java 17 + Spring Boot 3.2
- Spring Security + JWT (jjwt 0.12)
- Spring Data JPA + Hibernate
- PostgreSQL (desenvolvimento/produção)
- H2 (testes automatizados)
- Springdoc OpenAPI 2 (Swagger UI)
- JUnit 5 + Mockito

### Frontend
- Vue 3 + TypeScript
- Vuetify 3 (Material Design)
- Pinia (state management com persistência)
- Vue Router 4 (com guards de rota)
- Axios (com interceptor de refresh token)
- Vitest + Vue Test Utils

---

## Arquitetura

### Backend — Ports and Adapters (Hexagonal)

```
com.jtech.tasklist
├── application
│   ├── core
│   │   ├── domains          # Entidades de domínio puras (User, Task, TaskList)
│   │   ├── usecases         # Casos de uso (lógica de negócio)
│   │   └── exceptions       # Exceções de domínio
│   └── config               # Configurações (OpenAPI)
├── ports
│   ├── input                # Interfaces que os controllers chamam
│   └── output               # Interfaces que os use cases chamam (repositórios, JWT)
├── adapters
│   ├── input
│   │   └── web              # Controllers REST, DTOs, Exception Handler
│   └── output
│       └── persistence      # JPA Entities, Repositories, Mappers
└── infrastructure
    └── security             # JWT Service, Filter, Security Config, UserDetailsService
```

### Princípios SOLID aplicados

- **S** — Cada use case tem uma única responsabilidade
- **O** — Novos use cases implementam as ports sem modificar existentes
- **L** — Adapters substituem ports sem alterar o comportamento do domínio
- **I** — Ports granulares: `CreateTaskPort`, `DeleteTaskPort` etc.
- **D** — Use cases dependem de abstrações (ports), não de implementações

### Fluxo de autenticação

```
Client → POST /auth/login → AuthenticateUserUseCase
       → verifica senha (BCrypt)
       → gera Access Token (24h) + Refresh Token (7d)
       → retorna tokens

Requisições autenticadas:
Client → Bearer Token → JwtAuthenticationFilter
       → valida token → extrai email → carrega UserDetails
       → SecurityContext → Controller → UseCase
```

### Frontend — Arquitetura por camadas

```
src/
├── api/          # Chamadas HTTP (authApi, taskListApi, taskApi) + interceptors
├── stores/       # Estado global com Pinia (authStore, taskListStore, taskStore)
├── router/       # Vue Router com guards de autenticação
├── views/        # Páginas (Login, Register, Dashboard, TaskList)
├── components/   # Componentes reutilizáveis (cards, dialogs)
├── layouts/      # AppLayout com sidebar e topbar
└── models/       # Interfaces TypeScript
```

---

## Como executar

### Pré-requisitos

#### Backend

* Java 17+
* Maven 3.9+
* PostgreSQL (opcional, caso não utilize o profile local)

#### Frontend

* Node.js 20+
* npm

#### Alternativa

* Docker Desktop (para execução completa via containers)

---

## Opção 1 — Execução completa com Docker

```bash
docker-compose up --build
```

Acesse:

* Frontend: http://localhost
* Backend: http://localhost:8080
* Swagger UI: http://localhost:8080/swagger-ui/index.html

---

## Opção 2 — Desenvolvimento local com PostgreSQL

### Backend

```bash
cd jtech-tasklist-backend
mvn spring-boot:run
```

### Frontend

```bash
cd jtech-tasklist-frontend
npm install
npm run dev
```

Acesse:

* Frontend: http://localhost:5173
* Backend: http://localhost:8080
* Swagger UI: http://localhost:8080/swagger-ui/index.html

---

## Opção 3 — Desenvolvimento local com H2 (sem PostgreSQL)

### Backend

```bash
cd jtech-tasklist-backend
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Acesse:

* API: http://localhost:8080
* Swagger UI: http://localhost:8080/swagger-ui/index.html
* H2 Console: http://localhost:8080/h2-console

Configuração do H2:

* JDBC URL: `jdbc:h2:mem:tasklist`
* Usuário: `sa`
* Senha: (vazia)

### Frontend

```bash
cd jtech-tasklist-frontend
npm install
npm run dev
```

Acesse:

* Frontend: http://localhost:5173

---

## Testes

### Backend

```bash
cd jtech-tasklist-backend

# Todos os testes
./mvnw test

# Com relatório de cobertura
./mvnw test jacoco:report
```

Cobertura inclui:
- Testes unitários: `RegisterUserUseCase`, `AuthenticateUserUseCase`, `CreateTaskListUseCase`, `CreateTaskUseCase`
- Testes de integração: `AuthControllerIntegrationTest`,
`AuthControllerIntegrationTest`,
`TaskControllerIntegrationTest`,
`TaskListControllerIntegrationTest`

### Frontend

```bash
cd jtech-tasklist-frontend
npm run test:unit
npm run test:coverage
```

Cobertura inclui: 
- `authStore (autenticação e persistência de sessão)`
- `taskListStore (gerenciamento de listas de tarefas)`

---

## Endpoints da API

### Autenticação (públicos)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/register` | Cadastro de novo usuário |
| POST | `/auth/login` | Login, retorna access + refresh token |
| POST | `/auth/refresh` | Renova access token |

### Listas de Tarefas (autenticados)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/task-lists` | Listar listas do usuário |
| POST | `/task-lists` | Criar nova lista |
| PUT | `/task-lists/{id}` | Atualizar lista |
| DELETE | `/task-lists/{id}` | Excluir lista |

### Tarefas (autenticados)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/tasks?taskListId={id}` | Listar tarefas de uma lista |
| GET | `/tasks/{id}` | Buscar tarefa por ID |
| POST | `/tasks` | Criar tarefa |
| PUT | `/tasks/{id}` | Atualizar tarefa |
| PATCH | `/tasks/{id}/complete` | Marcar como concluída |
| DELETE | `/tasks/{id}` | Excluir tarefa |

Documentação interativa completa disponível em `/swagger-ui/index.html`.

---

## Variáveis de ambiente

### Backend

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `DB_URL` | `jdbc:postgresql://localhost:5432/jtasklist` | URL do banco |
| `DB_USERNAME` | `postgres` | Usuário |
| `DB_PASSWORD` | `postgres` | Senha |
| `JWT_SECRET` | (chave padrão) | Chave secreta JWT (hex 256-bit) |

### Frontend

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `VITE_API_URL` | `http://localhost:8080` | URL base da API |

---

## Estrutura do banco de dados

```sql
users (id, name, email, password, created_at)
task_lists (id, name, user_id)
tasks (id, title, description, completed, created_at, task_list_id, user_id)
```

---

## Melhorias futuras

- Paginação nos endpoints de listagem
- Upload de avatar do usuário
- Notificações em tempo real (WebSocket)
- Auditoria de ações (Spring Data Envers)
- Cache com Redis
- CI/CD com GitHub Actions
- Observabilidade (Micrometer + Prometheus + Grafana)
- Tags e prioridades nas tarefas
