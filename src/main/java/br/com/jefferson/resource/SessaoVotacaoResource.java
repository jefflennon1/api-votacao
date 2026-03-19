package br.com.jefferson.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jefferson.dto.SessaoVotacaoRequest;
import br.com.jefferson.dto.SessaoVotacaoResponse;
import br.com.jefferson.service.SessaoVotacaoService;
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
@Tag(name = "Sessões de Votação", description = "Endpoints para gerenciamento de sessões de votação")
public class SessaoVotacaoResource {

    private final SessaoVotacaoService sessaoVotacaoService;

    @Operation(summary = "Abrir sessão de votação para uma pauta",
               description = "Se a duração não for informada, a sessão ficará aberta por 1 minuto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sessão aberta com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada"),
        @ApiResponse(responseCode = "400", description = "Já existe uma sessão para esta pauta")
    })
    
    @PostMapping("/{pautaId}/sessao")
    public ResponseEntity<SessaoVotacaoResponse> abrirSessao(
            @PathVariable Long pautaId,
            @RequestBody(required = false) @Valid SessaoVotacaoRequest request) {
        log.info("Requisição para abrir sessão. Pauta ID: {}", pautaId);

        if (request == null) {
            request = new SessaoVotacaoRequest();
        }

        SessaoVotacaoResponse response = sessaoVotacaoService.abrirSessao(pautaId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}