package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.domain.Lista;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ListaRepositoryAdapter implements ListaRepository {

    private final ListaJpaRepository jpaRepository;

    public ListaRepositoryAdapter(ListaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Lista> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Lista> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Lista save(Lista lista) {
        ListaEntity entity = new ListaEntity();
        if (lista.getId() != null) {
            entity.setId(lista.getId());
        }
        entity.setNombre(lista.getNombre());
        
        ListaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean deleteById(Long id) {
        if (jpaRepository.existsById(id)) {
            jpaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Lista toDomain(ListaEntity entity) {
        return new Lista(entity.getId(), entity.getNombre());
    }
}