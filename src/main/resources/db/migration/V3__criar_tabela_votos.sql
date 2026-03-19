CREATE TABLE votos (
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    pauta_id        BIGINT      NOT NULL,
    associado_id    VARCHAR(255) NOT NULL,
    voto            TINYINT(1)  NOT NULL,
    data_voto       DATETIME    NOT NULL,
    CONSTRAINT pk_votos PRIMARY KEY (id),
    CONSTRAINT fk_votos_pauta FOREIGN KEY (pauta_id) 
        REFERENCES pautas (id),
    CONSTRAINT uq_voto_associado UNIQUE (pauta_id, associado_id)
);