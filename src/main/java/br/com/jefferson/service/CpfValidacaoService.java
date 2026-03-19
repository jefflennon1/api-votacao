package br.com.jefferson.service;

import br.com.jefferson.client.CpfValidacaoClient;
import br.com.jefferson.dto.CpfValidacaoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CpfValidacaoService {

    private final CpfValidacaoClient cpfValidacaoClient;

    public CpfValidacaoResponse validarCpf(String cpf) {
        log.info("Requisição de validação para CPF: {}", cpf);
        return cpfValidacaoClient.validar(cpf);
    }
}