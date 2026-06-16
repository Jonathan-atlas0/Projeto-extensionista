INSERT INTO conteudo_educativo (titulo, descricao, conteudo, categoria, nivel, visivel)
SELECT
    'Como montar um orçamento mensal',
    'Passos simples para listar receitas, despesas e calcular o saldo do mês.',
    'Comece anotando todo dinheiro que entra no mês. Depois registre os gastos fixos, os gastos variáveis e separe uma pequena reserva sempre que possível. O objetivo é saber para onde o dinheiro está indo antes de decidir onde cortar.',
    'ORCAMENTO',
    'BASICO',
    TRUE
WHERE NOT EXISTS (SELECT 1 FROM conteudo_educativo WHERE titulo = 'Como montar um orçamento mensal');

INSERT INTO conteudo_educativo (titulo, descricao, conteudo, categoria, nivel, visivel)
SELECT
    'Diferença entre necessidade e desejo',
    'Critério prático para priorizar gastos e reduzir compras impulsivas.',
    'Necessidades são gastos essenciais para viver e trabalhar, como alimentação, moradia e transporte. Desejos melhoram o conforto, mas podem esperar. Antes de comprar, pergunte se o gasto cabe no orçamento e se prejudica uma conta mais importante.',
    'CONSUMO',
    'BASICO',
    TRUE
WHERE NOT EXISTS (SELECT 1 FROM conteudo_educativo WHERE titulo = 'Diferença entre necessidade e desejo');

INSERT INTO conteudo_educativo (titulo, descricao, conteudo, categoria, nivel, visivel)
SELECT
    'Primeiros passos para sair das dívidas',
    'Organização das dívidas por valor, juros e prioridade de pagamento.',
    'Liste todas as dívidas, incluindo valor total, parcela, atraso e juros. Priorize as dívidas com juros maiores e negocie antes que elas cresçam. Evite assumir novas parcelas enquanto reorganiza o orçamento.',
    'DIVIDAS',
    'BASICO',
    TRUE
WHERE NOT EXISTS (SELECT 1 FROM conteudo_educativo WHERE titulo = 'Primeiros passos para sair das dívidas');
