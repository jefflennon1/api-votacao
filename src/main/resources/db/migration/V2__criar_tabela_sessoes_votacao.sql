CREATE TABLE sessoes_votacao (
    id                  BIGINT      NOT NULL AUTO_INCREMENT,
    pauta_id            BIGINT      NOT NULL,
    data_abertura       DATETIME    NOT NULL,
    data_encerramento   DATETIME    NOT NULL,
    CONSTRAINT pk_sessoes_votacao PRIMARY KEY (id),
    CONSTRAINT fk_sessoes_pauta FOREIGN KEY (pauta_id) 
        REFERENCES pautas (id)
);