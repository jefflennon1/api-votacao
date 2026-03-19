package br.com.jefferson.resource;

import br.com.jefferson.dto.CpfValidacaoResponse;
import br.com.jefferson.service.CpfValidacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/cpf")
@RequiredArgsConstructor
@Tag(name = "Validação de CPF", description = "Endpoint para validação de CPF do associado")
public class CpfValidacaoResource {

    private final CpfValidacaoService cpfValidacaoService;

    @Operation(summary = "Validar CPF do associado",
               description = "Valida se o CPF é válido e retorna se o associado pode ou não votar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "CPF válido — retorna ABLE_TO_VOTE ou UNABLE_TO_VOTE"),
        @ApiResponse(responseCode = "404", description = "CPF inválido")
    })
    @GetMapping("/{cpf}")
    public ResponseEntity<CpfValidacaoResponse> validarCpf(@PathVariable String cpf) {
        log.info("Requisição para validar CPF: {}", cpf);
        CpfValidacaoResponse response = cpfValidacaoService.validarCpf(cpf);
        return ResponseEntity.ok(response);
    }
    
    
    /***
     * 
     * O FLUXO QUE EU IMAGINEI FOI:
     * 1 - PRIMEIRO ENVIAMOS O CPF PARA ENDPOINT ACIMA E ELE RETORNA
     * SE O CPF É VALIDO OU NÃO.
     * 
     * 2 - DEPOIS DE TER A RESPOSTA O CLIENTSERVICE DECIDE SE VAI CONSUMIR OU NÃO OS OUTROS ENDPOINTS DE VOTAÇÃO.
     
     * *****/
}