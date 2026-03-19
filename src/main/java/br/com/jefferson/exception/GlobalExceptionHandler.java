package br.com.jefferson.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PautaNaoEncontradaException.class)
    public ResponseEntity<ErroResponse> handlePautaNaoEncontrada(PautaNaoEncontradaException ex) {
        log.error("Pauta não encontrada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErroResponse(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(SessaoJaExisteException.class)
    public ResponseEntity<ErroResponse> handleSessaoJaExiste(SessaoJaExisteException ex) {
        log.error("Sessão já existe: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErroResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(SessaoEncerradaException.class)
    public ResponseEntity<ErroResponse> handleSessaoEncerrada(SessaoEncerradaException ex) {
        log.error("Sessão encerrada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErroResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(AssociadoJaVotouException.class)
    public ResponseEntity<ErroResponse> handleAssociadoJaVotou(AssociadoJaVotouException ex) {
        log.error("Associado já votou: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErroResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce("", (a, b) -> a + b + "; ");
        log.error("Erro de validação: {}", mensagem);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErroResponse(HttpStatus.BAD_REQUEST, mensagem));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleErroGenerico(Exception ex) {
        log.error("Erro inesperado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErroResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor"));
    }
    
    @ExceptionHandler(CpfInvalidoException.class)
    public ResponseEntity<ErroResponse> handleCpfInvalido(CpfInvalidoException ex) {
        log.error("CPF inválido: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErroResponse(HttpStatus.NOT_FOUND, ex.getMessage()));
    }
    

    private ErroResponse buildErroResponse(HttpStatus status, String mensagem) {
        return ErroResponse.builder()
                .status(status.value())
                .mensagem(mensagem)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
