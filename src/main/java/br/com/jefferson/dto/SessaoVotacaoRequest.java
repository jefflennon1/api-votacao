package br.com.jefferson.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessaoVotacaoRequest {

    @Min(value = 1, message = "A duração deve ser de no mínimo 1 minuto")
    private Integer duracaoEmMinutos;
}