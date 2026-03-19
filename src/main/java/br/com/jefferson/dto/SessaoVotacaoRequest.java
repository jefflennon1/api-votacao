package br.com.jefferson.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessaoVotacaoRequest {

    // tempo em minutos, se nulo será 1 minuto por default
    private Integer duracaoEmMinutos;
}