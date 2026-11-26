package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.domain.Rol;
import ar.edu.unvime.api_blank.domain.Usuario;
import ar.edu.unvime.api_blank.dto.RegistroInputDTO;
import ar.edu.unvime.api_blank.exception.EmailDuplicadoException;
import ar.edu.unvime.api_blank.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private PasswordEncoder passwordEncoder;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        usuarioService = new UsuarioService(usuarioRepository, passwordEncoder);
    }

    @Test
    void registrar_datosValidos_nuncaGuardaPasswordEnTextoPlano() {
        when(usuarioRepository.existsByEmail("ana@test.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        RegistroInputDTO dto = new RegistroInputDTO();
        dto.setEmail("ana@test.com");
        dto.setPassword("12345678");

        usuarioService.registrar(dto);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        Usuario guardado = captor.getValue();
        assertThat(guardado.getPasswordHash()).isNotEqualTo("12345678");
        assertThat(passwordEncoder.matches("12345678", guardado.getPasswordHash())).isTrue();
        assertThat(guardado.getRol()).isEqualTo(Rol.USER);
    }

    @Test
    void registrar_emailDuplicado_lanzaExcepcion() {
        when(usuarioRepository.existsByEmail("ana@test.com")).thenReturn(true);

        RegistroInputDTO dto = new RegistroInputDTO();
        dto.setEmail("ana@test.com");
        dto.setPassword("12345678");

        assertThatThrownBy(() -> usuarioService.registrar(dto))
                .isInstanceOf(EmailDuplicadoException.class);
    }
}