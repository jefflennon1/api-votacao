package br.com.jefferson.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PautaResponse {

    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataCriacao;
}
