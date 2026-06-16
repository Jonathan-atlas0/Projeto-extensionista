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

O arquivo `src/main/resources/application.yml` usa variaveis de ambiente e defaults locais para desenvolvimento.

Para configurar o projeto localmente, ajuste as variaveis conforme necessario:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `jwt.secret`
- `jwt.refresh-expiration`

Use uma chave JWT local com mais de 32 caracteres.

O profile `dev` fica ativo por padrao e usa `src/main/resources/application-dev.yml` para criar um admin de demonstracao:

- e-mail: `admin@fineduca.local`
- senha: `123456`

Se nao quiser criar esse admin localmente, rode com outro valor em `SPRING_PROFILES_ACTIVE`.

## Configuracao de Producao

O profile `prod` esta em `src/main/resources/application-prod.yml` e nao contem credenciais demo. Ele exige variaveis reais:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION` opcional, default `900000`
- `JWT_REFRESH_EXPIRATION` opcional, default `604800000`

Exemplo:

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
$env:DB_URL="jdbc:postgresql://localhost:5432/financeiro"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="senha-segura"
$env:JWT_SECRET="chave-prod-com-mais-de-32-caracteres"
.\mvnw.cmd spring-boot:run
```

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
- refresh token com rotacao e revogacao;
- 401/403 em rotas protegidas;
- CORS para frontend local;
- Swagger com endpoints reais;
- CRUD HTTP de receitas e despesas;
- CRUD admin de categorias;
- alteracao de senha do usuario logado;
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
5. Copie o `token` retornado. O login tambem retorna `refreshToken`.
6. Clique em `Authorize` no Swagger e informe: `Bearer {token}`.
7. Liste categorias, crie receitas/despesas e consulte o dashboard.
8. Para testar admin, faca login com o `ADMIN_EMAIL` e `ADMIN_PASSWORD` configurados no profile `dev`.

## Rotas de Autenticacao

| Metodo | Rota | Acesso | Observacao |
|---|---|---|---|
| POST | `/api/auth/registro` | publico | cria usuario comum |
| POST | `/api/auth/login` | publico | retorna `token` e `refreshToken` |
| POST | `/api/auth/refresh` | publico | revoga o refresh atual e emite novo par de tokens |
| POST | `/api/auth/logout` | publico | revoga o refresh token informado |

Exemplo de refresh:

```json
{
  "refreshToken": "refresh-token-retornado-no-login"
}
```

## Rotas de Perfil

| Metodo | Rota | Acesso | Observacao |
|---|---|---|---|
| GET | `/api/usuarios/perfil` | autenticado | dados do usuario logado |
| PUT | `/api/usuarios/perfil` | autenticado | altera apenas o nome |
| PUT | `/api/usuarios/perfil/senha` | autenticado | altera senha validando senha atual e confirmacao |
| DELETE | `/api/usuarios/perfil` | autenticado | remove a propria conta |

Exemplo de alteracao de senha:

```json
{
  "senhaAtual": "123456",
  "novaSenha": "novaSenha123",
  "confirmacaoNovaSenha": "novaSenha123"
}
```

## Rotas Admin de Categorias

Categorias padrao continuam vindo das migrations. Admins podem criar categorias globais novas, mas categorias padrao nao podem ser alteradas/removidas e categorias em uso por receita/despesa nao podem ser deletadas.

| Metodo | Rota | Acesso |
|---|---|---|
| GET | `/api/categorias/admin/{id}` | `ROLE_ADMIN` |
| POST | `/api/categorias/admin` | `ROLE_ADMIN` |
| PUT | `/api/categorias/admin/{id}` | `ROLE_ADMIN` |
| DELETE | `/api/categorias/admin/{id}` | `ROLE_ADMIN` |

Exemplo de criacao/atualizacao:

```json
{
  "nome": "Investimentos",
  "tipo": "RECEITA"
}
```

Roteiro detalhado: [docs/ROTEIRO_DEMO_SWAGGER.md](docs/ROTEIRO_DEMO_SWAGGER.md).
