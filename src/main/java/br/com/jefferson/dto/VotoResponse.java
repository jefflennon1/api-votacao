package br.com.jefferson.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VotoResponse {

    private Long id;
    private Long pautaId;
    private String associadoId;
    private Boolean voto;
    private LocalDateTime dataVoto;
}