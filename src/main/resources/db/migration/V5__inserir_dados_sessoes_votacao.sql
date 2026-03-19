INSERT INTO sessoes_votacao (pauta_id, data_abertura, data_encerramento) VALUES
(1, NOW(), DATE_ADD(NOW(), INTERVAL 60 MINUTE)),
(2, NOW(), DATE_ADD(NOW(), INTERVAL 30 MINUTE));