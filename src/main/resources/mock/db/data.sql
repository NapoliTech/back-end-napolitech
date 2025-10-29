USE backend_pi;

INSERT INTO usuarios_cadastrados (
    id_usuario, nome, email, senha, cpf, confirmar_senha, telefone, data_nasc, tipo_usuario, pedidos
) VALUES (
    1, 'Marcos Paulo', 'marcos@gmail.com', 'P@ssw0rd', '123.123.123-11', 'P@ssw0rd', '(11) 99999-9999', '1989-01-10', 'CLIENTE', 0
);

INSERT INTO endereco (
    id, rua, bairro, numero, complemento, cidade, estado, cep, usuario_id
) VALUES (
    1, 'Rua Haddock Lobo', 'Consolação', 595, 'Digital Building', 'São Paulo', 'SP', '01414-001', 1
);

INSERT INTO estoque_produtos (
    id, nome, preco, quantidade_estoque, ingredientes, categoria_produto
) VALUES
    (1, 'Pizza de Calabresa', 39.90, 10, 'Calabresa, Cebola, Orégano, Queijo Mussarela', 'PIZZA'),
    (2, 'Pizza de Palmito', 59.90, 10, 'Palmito, Cogumelo, Azeite', 'PIZZA'),
    (3, 'Coca-Cola Lata 350Ml', 5.90, 25, '', 'REFRIGERANTE');
