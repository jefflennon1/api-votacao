package br.com.jefferson.service;

import br.com.jefferson.dto.SessaoVotacaoRequest;
import br.com.jefferson.dto.SessaoVotacaoResponse;
import br.com.jefferson.exception.PautaNaoEncontradaException;
import br.com.jefferson.exception.SessaoJaExisteException;
import br.com.jefferson.model.Pauta;
import br.com.jefferson.model.SessaoVotacao;
import br.com.jefferson.repository.PautaRepository;
import br.com.jefferson.repository.SessaoVotacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoVotacaoServiceTest {

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private SessaoVotacaoService sessaoVotacaoService;

    private Pauta pauta;
    private SessaoVotacao sessao;
    private SessaoVotacaoRequest request;

    @BeforeEach
    void setUp() {
        pauta = Pauta.builder()
                .id(1L)
                .titulo("Título da pauta")
                .dataCriacao(LocalDateTime.now())
                .build();

        sessao = SessaoVotacao.builder()
                .id(1L)
                .pauta(pauta)
                .dataAbertura(LocalDateTime.now())
                .dataEncerramento(LocalDateTime.now().plusMinutes(1))
                .build();

        request = new SessaoVotacaoRequest();
    }

    @Test
    void deveAbrirSessaoComDuracaoDefaultComSucesso() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.empty());
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessao);

        SessaoVotacaoResponse response = sessaoVotacaoService.abrirSessao(1L, request);

        assertNotNull(response);
        assertEquals(sessao.getId(), response.getId());
        assertEquals(pauta.getId(), response.getPautaId());
        verify(sessaoVotacaoRepository, times(1)).save(any(SessaoVotacao.class));
    }

    @Test
    void deveAbrirSessaoComDuracaoCustomizadaComSucesso() {
        request.setDuracaoEmMinutos(30);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.empty());
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessao);

        SessaoVotacaoResponse response = sessaoVotacaoService.abrirSessao(1L, request);

        assertNotNull(response);
        verify(sessaoVotacaoRepository, times(1)).save(any(SessaoVotacao.class));
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoEncontrada() {
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PautaNaoEncontradaException.class,
                () -> sessaoVotacaoService.abrirSessao(99L, request));
        verify(sessaoVotacaoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoSessaoJaExiste() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessao));

        assertThrows(SessaoJaExisteException.class,
                () -> sessaoVotacaoService.abrirSessao(1L, request));
        verify(sessaoVotacaoRepository, never()).save(any());
    }

    @Test
    void deveRetornarTrueQuandoSessaoEstaAberta() {
        sessao = SessaoVotacao.builder()
                .id(1L)
                .pauta(pauta)
                .dataAbertura(LocalDateTime.now().minusMinutes(1))
                .dataEncerramento(LocalDateTime.now().plusMinutes(10))
                .build();

        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessao));

        assertTrue(sessaoVotacaoService.isSessaoAberta(1L));
    }

    @Test
    void deveRetornarFalseQuandoSessaoEstaEncerrada() {
        sessao = SessaoVotacao.builder()
                .id(1L)
                .pauta(pauta)
                .dataAbertura(LocalDateTime.now().minusMinutes(10))
                .dataEncerramento(LocalDateTime.now().minusMinutes(5))
                .build();

        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessao));

        assertFalse(sessaoVotacaoService.isSessaoAberta(1L));
    }
}