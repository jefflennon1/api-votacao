package br.com.jefferson.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jefferson.dto.ResultadoVotacaoResponse;
import br.com.jefferson.dto.VotoRequest;
import br.com.jefferson.dto.VotoResponse;
import br.com.jefferson.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
@Tag(name = "Votos", description = "Endpoints para votação e resultado")
public class VotoResource {

    private final VotoService votoService;

    @Operation(summary = "Registrar voto em uma pauta",
               description = "O associado pode votar apenas uma vez por pauta. Voto: true = Sim, false = Não")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada"),
        @ApiResponse(responseCode = "400", description = "Sessão encerrada ou associado já votou")
    })
    
    @PostMapping("/{pautaId}/votos")
    public ResponseEntity<VotoResponse> votar(
            @PathVariable Long pautaId,
            @RequestBody @Valid VotoRequest request) {
        log.info("Requisição para votar. Pauta ID: {}", pautaId);
        VotoResponse response = votoService.votar(pautaId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obter resultado da votação de uma pauta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    })
    @GetMapping("/{pautaId}/resultado")
    public ResponseEntity<ResultadoVotacaoResponse> obterResultado(
            @PathVariable Long pautaId) {
        log.info("Requisição para obter resultado. Pauta ID: {}", pautaId);
        ResultadoVotacaoResponse response = votoService.obterResultado(pautaId);
        return ResponseEntity.ok(response);
    }
}