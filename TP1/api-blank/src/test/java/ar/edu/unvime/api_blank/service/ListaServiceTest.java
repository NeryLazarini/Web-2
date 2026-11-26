package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.domain.Lista;
import ar.edu.unvime.api_blank.exception.AccesoDenegadoException;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import ar.edu.unvime.api_blank.repository.ListaRepository;
import ar.edu.unvime.api_blank.security.ContextoSeguridad;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListaServiceTest {

    @Mock
    private ListaRepository listaRepository;

    @Mock
    private FavoritoRepository favoritoRepository;

    @Mock
    private ContextoSeguridad contextoSeguridad;

    @InjectMocks
    private ListaService listaService;

    @Test
    void guardar_usuarioAutenticado_asignaUsuarioIdDelContexto() {
        when(contextoSeguridad.usuarioActualId()).thenReturn(10L);
        when(listaRepository.save(any(Lista.class))).thenAnswer(inv -> inv.getArgument(0));

        Lista nueva = new Lista(null, "Compras", null);
        Lista resultado = listaService.guardar(nueva);

        ArgumentCaptor<Lista> captor = ArgumentCaptor.forClass(Lista.class);
        verify(listaRepository).save(captor.capture());

        assertThat(captor.getValue().getUsuarioId()).isEqualTo(10L);
        assertThat(resultado.getUsuarioId()).isEqualTo(10L);
    }

    @Test
    void obtenerPorId_listaPropia_devuelveLaLista() {
        Lista listaDeAna = new Lista(7L, "De Ana", 1L);
        when(listaRepository.findById(7L)).thenReturn(Optional.of(listaDeAna));
        when(contextoSeguridad.usuarioActualId()).thenReturn(1L);

        Lista resultado = listaService.obtenerPorId(7L);

        assertThat(resultado.getId()).isEqualTo(7L);
        assertThat(resultado.getNombre()).isEqualTo("De Ana");
    }

    @Test
    void obtenerPorId_listaAjena_lanzaAccesoDenegado() {
        Lista listaDeAna = new Lista(7L, "De Ana", 1L);
        when(listaRepository.findById(7L)).thenReturn(Optional.of(listaDeAna));
        when(contextoSeguridad.usuarioActualId()).thenReturn(2L);
        when(contextoSeguridad.esAdmin()).thenReturn(false);

        assertThatThrownBy(() -> listaService.obtenerPorId(7L))
                .isInstanceOf(AccesoDenegadoException.class);
    }

    @Test
    void obtenerPorId_adminSobreListaAjena_devuelveLaLista() {
        Lista listaDeAna = new Lista(7L, "De Ana", 1L);
        when(listaRepository.findById(7L)).thenReturn(Optional.of(listaDeAna));
        when(contextoSeguridad.usuarioActualId()).thenReturn(99L);
        when(contextoSeguridad.esAdmin()).thenReturn(true);

        Lista resultado = listaService.obtenerPorId(7L);

        assertThat(resultado.getId()).isEqualTo(7L);
    }
}