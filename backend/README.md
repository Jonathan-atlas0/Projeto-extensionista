# FinEduca Comunidade - Backend

API REST do MVP FinEduca Comunidade, uma plataforma simples de educacao financeira para cadastro de receitas, despesas, dashboard mensal, relatorios e conteudos educativos.

## Stack

- Java 17
- Spring Boot 4.0.3
- Spring Security 7
- Spring Data JPA
- PostgreSQL
- Flyway
- JWT com `jjwt`
- Swagger/OpenAPI
- Maven Wrapper

## Requisitos Locais

- JDK 17 ou superior
- PostgreSQL rodando localmente
- PowerShell no Windows

## Banco Local

Crie um banco vazio no PostgreSQL:

```sql
CREATE DATABASE financeiro;
```

As tabelas e dados iniciais sao criados pelo Flyway quando a aplicacao sobe.

## Configuracao Local

O arquivo real `src/main/resources/application.yaml` contem credenciais locais e fica fora do Git.

Para configurar o projeto pela primeira vez, copie:

```text
src/main/resources/application-example.yaml
```

para:

```text
src/main/resources/application.yaml
```

Depois ajuste no seu `application.yaml`:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `jwt.secret`

Use uma chave JWT local com mais de 32 caracteres.

O exemplo ja vem com o profile `dev` ativo e com um admin de demonstracao:

- e-mail: `admin@fineduca.local`
- senha: `123456`

Se nao quiser criar esse admin localmente, remova `spring.profiles.active: dev` do seu `application.yaml`.

## Rodar

```powershell
.\mvnw.cmd spring-boot:run
```

Aplicacao:

- API: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Testes Automatizados

```powershell
.\mvnw.cmd test
```

A suite cobre:

- contexto da aplicacao;
- migrations com Flyway;
- autenticacao JWT;
- 401/403 em rotas protegidas;
- CORS para frontend local;
- Swagger com endpoints reais;
- CRUD HTTP de receitas e despesas;
- isolamento entre usuarios;
- dashboard mensal com dados reais;
- conteudo educativo publico e admin;
- criacao do admin dev.

## Build

```powershell
.\mvnw.cmd package
```

O artefato sera gerado em `target/`.

## Demo Rapida No Swagger

1. Suba a aplicacao com as variaveis de ambiente.
2. Acesse `http://localhost:8080/swagger-ui.html`.
3. Cadastre um usuario em `POST /api/auth/registro`.
4. Faca login em `POST /api/auth/login`.
5. Copie o token retornado.
6. Clique em `Authorize` no Swagger e informe: `Bearer {token}`.
7. Liste categorias, crie receitas/despesas e consulte o dashboard.
8. Para testar admin, faca login com o `ADMIN_EMAIL` e `ADMIN_PASSWORD` configurados no profile `dev`.

Roteiro detalhado: [docs/ROTEIRO_DEMO_SWAGGER.md](docs/ROTEIRO_DEMO_SWAGGER.md).
