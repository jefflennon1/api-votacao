CREATE TABLE pautas (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    titulo      VARCHAR(255)    NOT NULL,
    descricao   VARCHAR(500),
    data_criacao DATETIME       NOT NULL,
    CONSTRAINT pk_pautas PRIMARY KEY (id)
);