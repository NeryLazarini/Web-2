package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.domain.Lista;
import java.util.List;
import java.util.Optional;

public interface ListaRepository {
    List<Lista> findAll();
    Optional<Lista> findById(Long id);
    Lista save(Lista lista);
    boolean deleteById(Long id);
}