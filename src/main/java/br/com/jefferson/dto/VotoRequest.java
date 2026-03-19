package br.com.jefferson.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotoRequest {

    private String associadoId;
    private Boolean voto; // true = Sim, false = Não
}