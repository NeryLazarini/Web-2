package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.domain.Favorito;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class FavoritoRepositoryImpl implements FavoritoRepository {

    // Nuestra "base de datos" en memoria
    private final Map<Long, Favorito> baseDeDatos = new HashMap<>();
    private Long generadorDeId = 1L;

    @Override
    public List<Favorito> findAll() {
        return new ArrayList<>(baseDeDatos.values());
    }

    @Override
    public Optional<Favorito> findById(Long id) {
        return Optional.ofNullable(baseDeDatos.get(id));
    }

    @Override
    public Favorito save(Favorito favorito) {
        if (favorito.getId() == null) {
            favorito.setId(generadorDeId++);
        }
        baseDeDatos.put(favorito.getId(), favorito);
        return favorito;
    }

    @Override
    public boolean deleteById(Long id) {
        return baseDeDatos.remove(id) != null;
    }
}