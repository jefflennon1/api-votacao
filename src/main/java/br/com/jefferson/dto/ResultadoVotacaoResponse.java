package br.com.jefferson.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResultadoVotacaoResponse {

    private Long pautaId;
    private String tituloPauta;
    private Long totalVotos;
    private Long votosSim;
    private Long votosNao;
    private String resultado; // "APROVADA" ou "REPROVADA"
}
