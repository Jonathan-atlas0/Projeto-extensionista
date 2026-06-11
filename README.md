# Finanças da Comunidade

Monorepo com front-end React + back-end Spring Boot.

```
financas-corrigido/
├── backend/    → Spring Boot 3.4 + Spring Security + JWT + PostgreSQL
└── frontend/   → React 18 + Vite + TypeScript + Tailwind + shadcn/ui
```

---

## Pré-requisitos

Instale antes de começar:

| Ferramenta | Versão | Link |
|------------|--------|------|
| Java | 17 | https://adoptium.net/ (Temurin 17) |
| Node.js | 18+ | https://nodejs.org/ |
| pnpm | qualquer | instalado via npm (passo abaixo) |
| PostgreSQL | qualquer | https://www.postgresql.org/download/ |

---

## 1. Banco de dados (PostgreSQL)

Abra o **pgAdmin**, clique em **Add New Server** e preencha:

- **Aba General** → Name: `Local`
- **Aba Connection** → Host: `localhost`, Port: `5432`, Username: `postgres`, Password: `1234`

Depois clique com botão direito em **Databases → Create → Database**, coloque o nome `financeiro` e salve.

Se o seu PostgreSQL tiver usuário ou senha diferente, edite o arquivo:

```
backend/src/main/resources/application.yaml
```

```yaml
datasource:
  username: seu_usuario
  password: sua_senha
```

---

## 2. Rodando o back-end

Abra um terminal na pasta `backend` e rode:

**Windows (PowerShell):**
```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```

> Se aparecer o erro `JAVA_HOME is not defined`, defina o Java 17 na sessão:
>
> ```powershell
> $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
> $env:Path = "$env:JAVA_HOME\bin;" + $env:Path
> ```
>
> Depois rode o comando novamente.

Aguarde aparecer:
```
Started FinanceiroApplication in X seconds
```

O back-end sobe em **http://localhost:8080**

- Swagger UI: http://localhost:8080/swagger-ui.html

---

## 3. Rodando o front-end

Abra **outro terminal** (deixe o back-end rodando) na pasta `frontend`.

**Instale o pnpm** (só na primeira vez):
```bash
npm install -g pnpm
```

**Instale as dependências:**
```bash
pnpm install
```

> Se aparecer o erro `ERR_PNPM_IGNORED_BUILDS`, rode:
> ```bash
> pnpm approve-builds
> ```
> Selecione tudo com espaço e confirme com Enter. Depois rode `pnpm install` novamente.

**Inicie o servidor:**
```bash
pnpm dev
```

O front-end sobe em **http://localhost:5173**

O Vite redireciona automaticamente `/api/*` para `http://localhost:8080` — sem necessidade de configurar CORS manualmente em desenvolvimento.

---

## 4. Acessando o sistema

Abra o navegador em **http://localhost:5173**, clique em **Criar conta**, preencha os dados e pronto!

---

## Resumo da ordem de execução

| Passo | O que fazer |
|-------|-------------|
| 1 | Criar o banco `financeiro` no PostgreSQL via pgAdmin |
| 2 | `.\mvnw.cmd spring-boot:run` na pasta `backend` |
| 3 | `pnpm install` na pasta `frontend` (só na primeira vez) |
| 4 | `pnpm dev` na pasta `frontend` |
| 5 | Abrir http://localhost:5173 no navegador |

---

## Endpoints da API

| Método | Rota | Auth | Descrição |
|--------|------|------|-----------|
| POST | /api/auth/registro | ❌ | Criar conta |
| POST | /api/auth/login | ❌ | Login, retorna JWT |
| GET | /api/receitas | ✅ | Listar receitas do usuário |
| POST | /api/receitas | ✅ | Criar receita |
| DELETE | /api/receitas/{id} | ✅ | Deletar receita |
| GET | /api/despesas | ✅ | Listar despesas do usuário |
| POST | /api/despesas | ✅ | Criar despesa |
| DELETE | /api/despesas/{id} | ✅ | Deletar despesa |
| GET | /api/dashboard | ✅ | Resumo financeiro |

---

## Build para produção

**Back-end:**
```powershell
cd backend
.\mvnw.cmd clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

**Front-end:**
```bash
cd frontend
pnpm build
```

Para apontar o front para uma API em outro host:
```bash
VITE_API_BASE=https://sua-api.com/api pnpm build
```
