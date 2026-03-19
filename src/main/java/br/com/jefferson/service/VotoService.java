package br.com.jefferson.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.jefferson.dto.ResultadoVotacaoResponse;
import br.com.jefferson.dto.VotoRequest;
import br.com.jefferson.dto.VotoResponse;
import br.com.jefferson.model.Pauta;
import br.com.jefferson.model.Voto;
import br.com.jefferson.repository.PautaRepository;
import br.com.jefferson.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final PautaRepository pautaRepository;
    private final SessaoVotacaoService sessaoVotacaoService;

    public VotoResponse votar(Long pautaId, VotoRequest request) {
        log.info("Registrando voto. Pauta ID: {}, Associado ID: {}", pautaId, request.getAssociadoId());

        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new RuntimeException("Pauta não encontrada com ID: " + pautaId));

        if (!sessaoVotacaoService.isSessaoAberta(pautaId)) {
            throw new RuntimeException("Sessão de votação encerrada ou não iniciada para esta pauta");
        }

        if (votoRepository.existsByPautaIdAndAssociadoId(pautaId, request.getAssociadoId())) {
            throw new RuntimeException("Associado já votou nesta pauta");
        }

        Voto voto = Voto.builder()
                .pauta(pauta)
                .associadoId(request.getAssociadoId())
                .voto(request.getVoto())
                .dataVoto(LocalDateTime.now())
                .build();

        Voto votoSalvo = votoRepository.save(voto);
        log.info("Voto registrado com sucesso. ID: {}", votoSalvo.getId());

        return toResponse(votoSalvo);
    }

    public ResultadoVotacaoResponse obterResultado(Long pautaId) {
        log.info("Obtendo resultado da votação. Pauta ID: {}", pautaId);

        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new RuntimeException("Pauta não encontrada com ID: " + pautaId));

        List<Voto> votos = votoRepository.findByPautaId(pautaId);

        long votosSim = votos.stream().filter(Voto::getVoto).count();
        long votosNao = votos.stream().filter(v -> !v.getVoto()).count();
        long totalVotos = votos.size();

        String resultado = votosSim > votosNao ? "APROVADA" : "REPROVADA";

        log.info("Resultado apurado. Total: {}, Sim: {}, Não: {}, Resultado: {}",
                totalVotos, votosSim, votosNao, resultado);

        return ResultadoVotacaoResponse.builder()
                .pautaId(pautaId)
                .tituloPauta(pauta.getTitulo())
                .totalVotos(totalVotos)
                .votosSim(votosSim)
                .votosNao(votosNao)
                .resultado(resultado)
                .build();
    }

    private VotoResponse toResponse(Voto voto) {
        return VotoResponse.builder()
                .id(voto.getId())
                .pautaId(voto.getPauta().getId())
                .associadoId(voto.getAssociadoId())
                .voto(voto.getVoto())
                .dataVoto(voto.getDataVoto())
                .build();
    }
}