package br.com.jefferson.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.jefferson.dto.PautaRequest;
import br.com.jefferson.dto.PautaResponse;
import br.com.jefferson.model.Pauta;
import br.com.jefferson.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PautaService {

	
    private final PautaRepository pautaRepository ;

    public PautaResponse cadastrar(PautaRequest request) {
        log.info("Cadastrando nova pauta: {}", request.getTitulo());

        Pauta pauta = Pauta.builder()
                .titulo(request.getTitulo())
                .descricao(request.getDescricao())
                .dataCriacao(LocalDateTime.now())
                .build();

        Pauta pautaSalva = pautaRepository.save(pauta);
        log.info("Pauta cadastrada com sucesso. ID: {}", pautaSalva.getId());

        return toResponse(pautaSalva);
    }

    public PautaResponse buscarPorId(Long id) {
        log.info("Buscando pauta por ID: {}", id);

        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pauta não encontrada com ID: " + id));

        return toResponse(pauta);
    }

    public List<PautaResponse> listarTodas() {
        log.info("Listando todas as pautas");

        return pautaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PautaResponse toResponse(Pauta pauta) {
        return PautaResponse.builder()
                .id(pauta.getId())
                .titulo(pauta.getTitulo())
                .descricao(pauta.getDescricao())
                .dataCriacao(pauta.getDataCriacao())
                .build();
    }
}
