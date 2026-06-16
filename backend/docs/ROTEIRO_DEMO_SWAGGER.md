# Roteiro De Demo Manual - Swagger

Use este roteiro para validar visualmente o backend antes da apresentacao e para demonstrar o MVP no Swagger.

## 1. Preparar Ambiente

O projeto usa `src/main/resources/application.yml` com defaults locais e variaveis de ambiente. O profile `dev` fica ativo por padrao e carrega `src/main/resources/application-dev.yml`.

Para rodar localmente, garanta que existe um banco PostgreSQL vazio:

```sql
CREATE DATABASE financeiro;
```

Se quiser sobrescrever as configuracoes locais:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/financeiro"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="sua_senha"
$env:JWT_SECRET="chave-local-com-mais-de-32-caracteres"
```

Depois rode:

```powershell
.\mvnw.cmd spring-boot:run
```

Abrir:

```text
http://localhost:8080/swagger-ui.html
```

## 2. Validar Rotas Publicas

### Conteudos educativos

`GET /api/conteudos`

Esperado:

- HTTP `200`
- lista JSON com conteudos visiveis

### OpenAPI

`GET /api-docs`

Esperado:

- HTTP `200`
- JSON contendo auth, usuarios, categorias, receitas, despesas, dashboard, relatorios e conteudos

## 3. Criar Usuario Comum

`POST /api/auth/registro`

Body:

```json
{
  "nome": "Usuario Demo",
  "email": "usuario.demo@fineduca.local",
  "senha": "123456"
}
```

Esperado:

- HTTP `201`
- resposta sem campo `senha`
- `role` igual a `ROLE_USER`

## 4. Login Usuario Comum

`POST /api/auth/login`

Body:

```json
{
  "email": "usuario.demo@fineduca.local",
  "senha": "123456"
}
```

Esperado:

- HTTP `200`
- campo `token`
- campo `refreshToken`
- campo `tipo` igual a `Bearer`

No Swagger, clique em `Authorize` e informe:

```text
Bearer COLE_O_TOKEN_AQUI
```

## 5. Refresh Token E Logout

### Renovar token

`POST /api/auth/refresh`

Body:

```json
{
  "refreshToken": "COLE_O_REFRESH_TOKEN_AQUI"
}
```

Esperado:

- HTTP `200`
- novo `token`
- novo `refreshToken`
- o refresh token anterior deixa de ser valido

### Logout

`POST /api/auth/logout`

Body:

```json
{
  "refreshToken": "COLE_O_REFRESH_TOKEN_ATUAL_AQUI"
}
```

Esperado:

- HTTP `204`
- refresh token informado fica revogado

## 6. Perfil E Senha

### Ver perfil

`GET /api/usuarios/perfil`

Esperado:

- HTTP `200`
- dados do usuario logado
- sem campo `senha`

### Atualizar nome

`PUT /api/usuarios/perfil`

Body:

```json
{
  "nome": "Usuario Demo Atualizado"
}
```

Esperado:

- HTTP `200`
- nome atualizado

### Alterar senha

`PUT /api/usuarios/perfil/senha`

Body:

```json
{
  "senhaAtual": "123456",
  "novaSenha": "novaSenha123",
  "confirmacaoNovaSenha": "novaSenha123"
}
```

Esperado:

- HTTP `204`
- login com senha antiga passa a falhar
- login com nova senha passa a funcionar

## 7. Categorias Para Usuario Comum

`GET /api/categorias`

Esperado:

- HTTP `200`
- categorias de `RECEITA` e `DESPESA`

`GET /api/categorias/tipo?tipo=RECEITA`

Anote um `id` de receita.

`GET /api/categorias/tipo?tipo=DESPESA`

Anote um `id` de despesa.

## 8. Receitas

`POST /api/receitas`

Body:

```json
{
  "descricao": "Salario",
  "valor": 1500.00,
  "data": "2026-05-10",
  "categoriaId": 1
}
```

Troque `categoriaId` pelo ID de uma categoria `RECEITA`.

Esperado:

- HTTP `201`
- retorno com `id`, `descricao`, `valor`, `categoriaId`

Depois valide:

- `GET /api/receitas`
- `GET /api/receitas/{id}`
- `PUT /api/receitas/{id}`
- `DELETE /api/receitas/{id}`

## 9. Despesas

`POST /api/despesas`

Body:

```json
{
  "descricao": "Mercado",
  "valor": 400.00,
  "data": "2026-05-12",
  "categoriaId": 6
}
```

Troque `categoriaId` pelo ID de uma categoria `DESPESA`.

Esperado:

- HTTP `201`
- retorno com `id`, `descricao`, `valor`, `categoriaId`

Depois valide:

- `GET /api/despesas`
- `GET /api/despesas/{id}`
- `PUT /api/despesas/{id}`
- `DELETE /api/despesas/{id}`

## 10. Dashboard E Relatorio

Crie pelo menos uma receita e uma despesa no mesmo mes.

`GET /api/dashboard?mes=5&ano=2026`

Esperado:

- HTTP `200`
- `totalReceitas`
- `totalDespesas`
- `saldo`
- `maiorCategoriaGasto`
- `gastosPorCategoria`

`GET /api/relatorios/mensal?mes=5&ano=2026`

Esperado:

- HTTP `200`
- totais do mes
- total de transacoes
- gastos por categoria

`GET /api/relatorios/grafico-gastos?mes=5&ano=2026`

Esperado:

- HTTP `200`
- lista de categorias com total e percentual

## 11. Admin

Faca login com:

```json
{
  "email": "admin@fineduca.local",
  "senha": "123456"
}
```

Autorize o Swagger com o token do admin.

### Criar conteudo educativo

`POST /api/conteudos`

Body:

```json
{
  "titulo": "Reserva de emergencia",
  "descricao": "Como comecar uma reserva com pouco dinheiro.",
  "conteudo": "Separe uma pequena quantia todo mes antes de gastar com itens nao essenciais.",
  "categoriaConteudo": "POUPANCA",
  "nivelDificuldade": "BASICO",
  "visivel": true
}
```

Esperado:

- HTTP `201`
- conteudo criado

Depois faca login com usuario comum e tente o mesmo `POST /api/conteudos`.

Esperado:

- HTTP `403`
- erro `Acesso negado`

### Gerenciar categorias globais

`POST /api/categorias/admin`

Body:

```json
{
  "nome": "Investimentos",
  "tipo": "RECEITA"
}
```

Esperado:

- HTTP `201`
- `padrao` igual a `false`

Depois valide:

- `GET /api/categorias/admin/{id}`
- `PUT /api/categorias/admin/{id}`
- `DELETE /api/categorias/admin/{id}`

Regras esperadas:

- usuario comum recebe `403` nas rotas `/api/categorias/admin/**`
- categoria padrao nao pode ser alterada nem deletada
- categoria em uso por receita/despesa nao pode ser deletada
- tipo da categoria nao pode ser alterado

## 12. Testar Regras De Erro

### Sem token

Limpe a autorizacao do Swagger e execute:

`GET /api/receitas`

Esperado:

- HTTP `401`
- JSON com `status` e `erro`

### Login invalido

`POST /api/auth/login`

Body:

```json
{
  "email": "usuario.demo@fineduca.local",
  "senha": "senhaerrada"
}
```

Esperado:

- HTTP `401`
- erro `E-mail ou senha inválidos`

### Categoria errada

Tente criar uma receita usando `categoriaId` de despesa.

Esperado:

- HTTP `400`
- erro informando tipo esperado `RECEITA`

### Refresh token reutilizado

Use um refresh token antigo depois de chamar `/api/auth/refresh`.

Esperado:

- HTTP `400`
- erro `Refresh token inválido ou expirado`
