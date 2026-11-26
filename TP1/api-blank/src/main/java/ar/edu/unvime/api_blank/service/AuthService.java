package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.domain.Usuario;
import ar.edu.unvime.api_blank.dto.LoginInputDTO;
import ar.edu.unvime.api_blank.dto.LoginOutputDTO;
import ar.edu.unvime.api_blank.exception.CredencialesInvalidasException;
import ar.edu.unvime.api_blank.repository.UsuarioRepository;
import ar.edu.unvime.api_blank.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginOutputDTO login(LoginInputDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CredencialesInvalidasException("Email o contrasena incorrectos"));

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException("Email o contrasena incorrectos");
        }

        String token = jwtService.generarToken(usuario.getId(), usuario.getRol());
        return new LoginOutputDTO(token);
    }
}