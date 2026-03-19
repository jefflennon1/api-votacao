package br.com.jefferson.service;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.jefferson.dto.SessaoVotacaoRequest;
import br.com.jefferson.dto.SessaoVotacaoResponse;
import br.com.jefferson.exception.PautaNaoEncontradaException;
import br.com.jefferson.exception.SessaoJaExisteException;
import br.com.jefferson.model.Pauta;
import br.com.jefferson.model.SessaoVotacao;
import br.com.jefferson.repository.PautaRepository;
import br.com.jefferson.repository.SessaoVotacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessaoVotacaoService {

    private static final int DURACAO_DEFAULT_MINUTOS = 1;

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final PautaRepository pautaRepository;

    public SessaoVotacaoResponse abrirSessao(Long pautaId, SessaoVotacaoRequest request) {
        log.info("Abrindo sessão de votação para pauta ID: {}", pautaId);

        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new PautaNaoEncontradaException(pautaId));

        sessaoVotacaoRepository.findByPautaId(pautaId).ifPresent(s -> {
        	log.error(s.toString());
            throw new SessaoJaExisteException(pautaId);
        });

        int duracao = (request.getDuracaoEmMinutos() != null && request.getDuracaoEmMinutos() > 0)
                ? request.getDuracaoEmMinutos()
                : DURACAO_DEFAULT_MINUTOS;

        LocalDateTime agora = LocalDateTime.now();

        SessaoVotacao sessao = SessaoVotacao.builder()
                .pauta(pauta)
                .dataAbertura(agora)
                .dataEncerramento(agora.plusMinutes(duracao))
                .build();

        SessaoVotacao sessaoSalva = sessaoVotacaoRepository.save(sessao);
        log.info("Sessão aberta com sucesso. Encerra em: {}", sessaoSalva.getDataEncerramento());

        return toResponse(sessaoSalva);
    }

    public boolean isSessaoAberta(Long pautaId) {
        return sessaoVotacaoRepository.findByPautaId(pautaId)
                .map(sessao -> LocalDateTime.now().isBefore(sessao.getDataEncerramento()))
                .orElse(false);
    }

    private SessaoVotacaoResponse toResponse(SessaoVotacao sessao) {
        return SessaoVotacaoResponse.builder()
                .id(sessao.getId())
                .pautaId(sessao.getPauta().getId())
                .dataAbertura(sessao.getDataAbertura())
                .dataEncerramento(sessao.getDataEncerramento())
                .build();
    }
}