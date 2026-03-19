package br.com.jefferson.resource;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jefferson.dto.PautaRequest;
import br.com.jefferson.dto.PautaResponse;
import br.com.jefferson.service.PautaService;
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
@Tag(name = "Pautas", description = "Endpoints para gerenciamento de pautas")
public class PautaResource {

    private final PautaService pautaService;

    @Operation(summary = "Cadastrar nova pauta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pauta cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    
    @PostMapping
    public ResponseEntity<PautaResponse> cadastrar(@RequestBody @Valid PautaRequest request) {
        log.info("Requisição para cadastrar pauta: {}", request.getTitulo());
        PautaResponse response = pautaService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Buscar pauta por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pauta encontrada"),
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    })
    
    
    @GetMapping("/{id}")
    public ResponseEntity<PautaResponse> buscarPorId(@PathVariable Long id) {
        log.info("Requisição para buscar pauta ID: {}", id);
        PautaResponse response = pautaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todas as pautas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<PautaResponse>> listarTodas() {
        log.info("Requisição para listar todas as pautas");
        List<PautaResponse> response = pautaService.listarTodas();
        return ResponseEntity.ok(response);
    }
}