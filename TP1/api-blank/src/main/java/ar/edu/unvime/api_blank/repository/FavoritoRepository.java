package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.domain.Favorito;
import java.util.List;
import java.util.Optional;

public interface FavoritoRepository {
    List<Favorito> findAll();
    Optional<Favorito> findById(Long id);
    Favorito save(Favorito favorito);
    boolean deleteById(Long id);
}