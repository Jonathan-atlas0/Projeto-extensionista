# Roteiro De Demo Manual - Swagger

Use este roteiro para validar visualmente o backend antes da apresentacao.

## 1. Preparar Ambiente

Copie `src/main/resources/application-example.yaml` para `src/main/resources/application.yaml`.

No arquivo `application.yaml`, ajuste os dados do seu PostgreSQL local:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

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
- JSON contendo rotas de auth, receitas, despesas, dashboard, relatorios e conteudos

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
- campo `tipo` igual a `Bearer`

No Swagger, clique em `Authorize` e informe:

```text
Bearer COLE_O_TOKEN_AQUI
```

## 5. Categorias

`GET /api/categorias`

Esperado:

- HTTP `200`
- categorias de `RECEITA` e `DESPESA`

`GET /api/categorias/tipo?tipo=RECEITA`

Anote um `id` de receita.

`GET /api/categorias/tipo?tipo=DESPESA`

Anote um `id` de despesa.

## 6. Receitas

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

## 7. Despesas

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

## 8. Dashboard E Relatorio

Crie pelo menos uma receita e uma despesa no mesmo mes.

`GET /api/dashboard?mes=5&ano=2026`

Esperado:

- HTTP `200`
- `totalReceitas`
- `totalDespesas`
- `saldo`
- `gastosPorCategoria`

`GET /api/relatorios/mensal?mes=5&ano=2026`

Esperado:

- HTTP `200`
- totais do mes
- total de transacoes

## 9. Testar Regras De Erro

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

## 10. Admin

Faca login com:

```json
{
  "email": "admin@fineduca.local",
  "senha": "123456"
}
```

Autorize o Swagger com o token do admin.

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
