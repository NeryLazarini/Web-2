package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.domain.Favorito;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FavoritoRepositoryAdapter implements FavoritoRepository {

    private final FavoritoJpaRepository jpaRepository;

    public FavoritoRepositoryAdapter(FavoritoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Favorito> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Favorito> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Favorito save(Favorito favorito) {
        FavoritoEntity entity = new FavoritoEntity();
        
        // Pasamos los datos del dominio a la entidad para la base de datos
        if (favorito.getId() != null) {
            entity.setId(favorito.getId());
        }
        entity.setProductoId(favorito.getProductoId());
        entity.setNota(favorito.getNotaPersonal());
        
        if (favorito.getFechaAgregado() != null) {
            // Convertimos tu LocalDate a LocalDateTime para la BD
            entity.setFechaAlta(favorito.getFechaAgregado().atStartOfDay());
        }
        if (favorito.getListaId() != null) {
        ListaEntity listaRef = new ListaEntity();
        listaRef.setId(favorito.getListaId());
        entity.setLista(listaRef);
    }

        // Guardamos en PostgreSQL
        FavoritoEntity saved = jpaRepository.save(entity);

        // Devolvemos el objeto limpio para tu aplicación
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
    
    // Método auxiliar (Mapper) para armar tu Favorito usando tus Setters
    private Favorito toDomain(FavoritoEntity entity) {
        Favorito favorito = new Favorito();
        favorito.setId(entity.getId());
        favorito.setProductoId(entity.getProductoId());
        favorito.setNotaPersonal(entity.getNota());
        
        if (entity.getFechaAlta() != null) {
            // Convertimos el LocalDateTime de la BD a tu LocalDate
            favorito.setFechaAgregado(entity.getFechaAlta().toLocalDate());
        }
        if (entity.getLista() != null) {
        favorito.setListaId(entity.getLista().getId());
    }
        return favorito;
    }
    @Override
    public List<Favorito> findByListaId(Long listaId) {
        return jpaRepository.findByListaId(listaId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
}
}

