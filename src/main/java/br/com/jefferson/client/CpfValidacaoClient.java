package br.com.jefferson.client;

/**
 * 
 * ESSA CLASSE SIMULA UM CLIENTE EXTERNO QUE VALIDA SE O CPF É VALIDO OU NÃO,
 * A REGRA AQUI É ALEATÓRIA, NAO CONDIZ COM A REALIDADE SE UM CPF É VALIDO OU NÃO. 
 * SERVE APENAS PARA TESTES!
 * 
 * **/



import java.util.Random;

import org.springframework.stereotype.Component;

import br.com.jefferson.dto.CpfValidacaoResponse;
import br.com.jefferson.exception.CpfInvalidoException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CpfValidacaoClient {

    private static final String ABLE_TO_VOTE = "ABLE_TO_VOTE";
    private static final String UNABLE_TO_VOTE = "UNABLE_TO_VOTE";

    public CpfValidacaoResponse validar(String cpf) {
        log.info("Validando CPF: {}", cpf);

        if (!isCpfValido(cpf)) {
            log.warn("CPF inválido: {}", cpf);
            throw new CpfInvalidoException(cpf);
        }

        String status = new Random().nextBoolean() ? ABLE_TO_VOTE : UNABLE_TO_VOTE;
        log.info("CPF {} validado com status: {}", cpf, status);

        return CpfValidacaoResponse.builder()
                .status(status)
                .build();
    }

    private boolean isCpfValido(String cpf) {
        if (cpf == null || cpf.isBlank()) return false;

        // Remove qualquer caractere não numérico (máscara, espaços, etc)
        cpf = cpf.replaceAll("\\D", "");

        // Verifica se tem exatamente 11 dígitos após limpeza
        if (cpf.length() != 11) return false;

        // Verifica se todos os dígitos são iguais (ex: 11111111111)
        if (cpf.matches("(\\d)\\1{10}") ) return false;

        // Valida primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int primeiroDigito = soma % 11 < 2 ? 0 : 11 - (soma % 11);
        if (primeiroDigito != (cpf.charAt(9) - '0')) return false;

        // Valida segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        int segundoDigito = soma % 11 < 2 ? 0 : 11 - (soma % 11);

        return segundoDigito == (cpf.charAt(10) - '0');
    }
}