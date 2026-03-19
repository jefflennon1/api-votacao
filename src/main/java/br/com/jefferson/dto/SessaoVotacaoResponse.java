package br.com.jefferson.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessaoVotacaoResponse {

    private Long id;
    private Long pautaId;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataEncerramento;
}