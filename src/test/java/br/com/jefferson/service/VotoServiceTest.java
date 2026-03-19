package br.com.jefferson.service;

import br.com.jefferson.dto.ResultadoVotacaoResponse;
import br.com.jefferson.dto.VotoRequest;
import br.com.jefferson.dto.VotoResponse;
import br.com.jefferson.exception.AssociadoJaVotouException;
import br.com.jefferson.exception.PautaNaoEncontradaException;
import br.com.jefferson.exception.SessaoEncerradaException;
import br.com.jefferson.model.Pauta;
import br.com.jefferson.model.Voto;
import br.com.jefferson.repository.PautaRepository;
import br.com.jefferson.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @InjectMocks
    private VotoService votoService;

    private Pauta pauta;
    private Voto voto;
    private VotoRequest votoRequest;

    @BeforeEach
    void setUp() {
        pauta = Pauta.builder()
                .id(1L)
                .titulo("Título da pauta")
                .dataCriacao(LocalDateTime.now())
                .build();

        votoRequest = new VotoRequest();
        votoRequest.setAssociadoId("associado-001");
        votoRequest.setVoto(true);

        voto = Voto.builder()
                .id(1L)
                .pauta(pauta)
                .associadoId("associado-001")
                .voto(true)
                .dataVoto(LocalDateTime.now())
                .build();
    }

    @Test
    void deveRegistrarVotoComSucesso() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoService.isSessaoAberta(1L)).thenReturn(true);
        when(votoRepository.existsByPautaIdAndAssociadoId(1L, "associado-001")).thenReturn(false);
        when(votoRepository.save(any(Voto.class))).thenReturn(voto);

        VotoResponse response = votoService.votar(1L, votoRequest);

        assertNotNull(response);
        assertEquals(voto.getId(), response.getId());
        assertEquals("associado-001", response.getAssociadoId());
        assertTrue(response.getVoto());
        verify(votoRepository, times(1)).save(any(Voto.class));
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoEncontrada() {
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PautaNaoEncontradaException.class,
                () -> votoService.votar(99L, votoRequest));
        verify(votoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoSessaoEncerrada() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoService.isSessaoAberta(1L)).thenReturn(false);

        assertThrows(SessaoEncerradaException.class,
                () -> votoService.votar(1L, votoRequest));
        verify(votoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoJaVotou() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoService.isSessaoAberta(1L)).thenReturn(true);
        when(votoRepository.existsByPautaIdAndAssociadoId(1L, "associado-001")).thenReturn(true);

        assertThrows(AssociadoJaVotouException.class,
                () -> votoService.votar(1L, votoRequest));
        verify(votoRepository, never()).save(any());
    }

    @Test
    void deveObterResultadoComVotacaoAprovada() {
        Voto votoSim1 = Voto.builder().pauta(pauta).voto(true).build();
        Voto votoSim2 = Voto.builder().pauta(pauta).voto(true).build();
        Voto votoNao = Voto.builder().pauta(pauta).voto(false).build();

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(votoRepository.findByPautaId(1L)).thenReturn(List.of(votoSim1, votoSim2, votoNao));

        ResultadoVotacaoResponse response = votoService.obterResultado(1L);

        assertNotNull(response);
        assertEquals(3L, response.getTotalVotos());
        assertEquals(2L, response.getVotosSim());
        assertEquals(1L, response.getVotosNao());
        assertEquals("APROVADA", response.getResultado());
    }

    @Test
    void deveObterResultadoComVotacaoReprovada() {
        Voto votoSim = Voto.builder().pauta(pauta).voto(true).build();
        Voto votoNao1 = Voto.builder().pauta(pauta).voto(false).build();
        Voto votoNao2 = Voto.builder().pauta(pauta).voto(false).build();

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(votoRepository.findByPautaId(1L)).thenReturn(List.of(votoSim, votoNao1, votoNao2));

        ResultadoVotacaoResponse response = votoService.obterResultado(1L);

        assertNotNull(response);
        assertEquals(3L, response.getTotalVotos());
        assertEquals(1L, response.getVotosSim());
        assertEquals(2L, response.getVotosNao());
        assertEquals("REPROVADA", response.getResultado());
    }

    @Test
    void deveObterResultadoComEmpate() {
        Voto votoSim = Voto.builder().pauta(pauta).voto(true).build();
        Voto votoNao = Voto.builder().pauta(pauta).voto(false).build();

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(votoRepository.findByPautaId(1L)).thenReturn(List.of(votoSim, votoNao));

        ResultadoVotacaoResponse response = votoService.obterResultado(1L);

        assertNotNull(response);
        assertEquals(2L, response.getTotalVotos());
        assertEquals("REPROVADA", response.getResultado());
    }
}