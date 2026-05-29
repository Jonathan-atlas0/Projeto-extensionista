INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Salário', 'RECEITA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Salário' AND tipo = 'RECEITA');

INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Freelance', 'RECEITA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Freelance' AND tipo = 'RECEITA');

INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Ajuda Familiar', 'RECEITA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Ajuda Familiar' AND tipo = 'RECEITA');

INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Venda', 'RECEITA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Venda' AND tipo = 'RECEITA');

INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Outro', 'RECEITA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Outro' AND tipo = 'RECEITA');

INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Alimentação', 'DESPESA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Alimentação' AND tipo = 'DESPESA');

INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Transporte', 'DESPESA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Transporte' AND tipo = 'DESPESA');

INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Moradia', 'DESPESA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Moradia' AND tipo = 'DESPESA');

INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Saúde', 'DESPESA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Saúde' AND tipo = 'DESPESA');

INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Educação', 'DESPESA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Educação' AND tipo = 'DESPESA');

INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Lazer', 'DESPESA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Lazer' AND tipo = 'DESPESA');

INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Dívidas', 'DESPESA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Dívidas' AND tipo = 'DESPESA');

INSERT INTO categorias (nome, tipo, padrao)
SELECT 'Outros', 'DESPESA', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nome = 'Outros' AND tipo = 'DESPESA');
