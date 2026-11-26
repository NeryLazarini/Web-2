package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.domain.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);
    boolean existsByEmail(String email);
}