package br.com.jefferson.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CpfValidacaoResponse {

    private String status;
}