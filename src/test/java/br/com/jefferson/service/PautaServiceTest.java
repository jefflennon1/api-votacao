package br.com.jefferson.service;

import br.com.jefferson.dto.PautaRequest;
import br.com.jefferson.dto.PautaResponse;
import br.com.jefferson.exception.PautaNaoEncontradaException;
import br.com.jefferson.model.Pauta;
import br.com.jefferson.repository.PautaRepository;
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
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    private Pauta pauta;
    private PautaRequest pautaRequest;

    @BeforeEach
    void setUp() {
        pauta = Pauta.builder()
                .id(1L)
                .titulo("Título da pauta")
                .descricao("Descrição da pauta")
                .dataCriacao(LocalDateTime.now())
                .build();

        pautaRequest = new PautaRequest();
        pautaRequest.setTitulo("Título da pauta");
        pautaRequest.setDescricao("Descrição da pauta");
    }

    @Test
    void deveCadastrarPautaComSucesso() {
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        PautaResponse response = pautaService.cadastrar(pautaRequest);

        assertNotNull(response);
        assertEquals(pauta.getId(), response.getId());
        assertEquals(pauta.getTitulo(), response.getTitulo());
        assertEquals(pauta.getDescricao(), response.getDescricao());
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    void deveBuscarPautaPorIdComSucesso() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        PautaResponse response = pautaService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(pauta.getId(), response.getId());
        assertEquals(pauta.getTitulo(), response.getTitulo());
        verify(pautaRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoEncontrada() {
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PautaNaoEncontradaException.class,
                () -> pautaService.buscarPorId(99L));
        verify(pautaRepository, times(1)).findById(99L);
    }

    @Test
    void deveListarTodasAsPautas() {
        when(pautaRepository.findAll()).thenReturn(List.of(pauta));

        List<PautaResponse> response = pautaService.listarTodas();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        verify(pautaRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverPautas() {
        when(pautaRepository.findAll()).thenReturn(List.of());

        List<PautaResponse> response = pautaService.listarTodas();

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(pautaRepository, times(1)).findAll();
    }
}