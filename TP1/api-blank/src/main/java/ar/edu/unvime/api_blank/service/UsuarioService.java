package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.domain.Rol;
import ar.edu.unvime.api_blank.domain.Usuario;
import ar.edu.unvime.api_blank.dto.RegistroInputDTO;
import ar.edu.unvime.api_blank.dto.UsuarioOutputDTO;
import ar.edu.unvime.api_blank.exception.EmailDuplicadoException;
import ar.edu.unvime.api_blank.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioOutputDTO registrar(RegistroInputDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailDuplicadoException("Ya existe una cuenta con el email " + dto.getEmail());
        }

        Usuario nuevo = new Usuario(
                null,
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                Rol.USER
        );

        Usuario guardado = usuarioRepository.save(nuevo);
        return new UsuarioOutputDTO(guardado.getId(), guardado.getEmail(), guardado.getRol());
    }
}