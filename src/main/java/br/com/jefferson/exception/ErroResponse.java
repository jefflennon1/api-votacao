package br.com.jefferson.exception;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ErroResponse {

    private int status;
    private String mensagem;
    private LocalDateTime timestamp;
}