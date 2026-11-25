package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.domain.Lista;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import ar.edu.unvime.api_blank.repository.ListaRepository;
import ar.edu.unvime.api_blank.exception.ListaConFavoritosException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.unvime.api_blank.domain.Favorito;
import java.util.List;

@Service
public class ListaService {
    private final ListaRepository listaRepository;
    private final FavoritoRepository favoritoRepository;

    public ListaService(ListaRepository listaRepository, FavoritoRepository favoritoRepository) {
        this.listaRepository = listaRepository;
        this.favoritoRepository = favoritoRepository;
    }


    
    public Lista guardar(Lista lista) {
        return listaRepository.save(lista);
    }

    public List<Lista> obtenerTodas() {
        return listaRepository.findAll();
    }

    public Lista obtenerPorId(Long id) {
        return listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada"));
    }

    public void eliminar(Long id) {
        // Verificamos si la lista tiene favoritos antes de borrar
        if (!favoritoRepository.findByListaId(id).isEmpty()) {
            throw new ListaConFavoritosException("No se puede eliminar la lista porque tiene favoritos asociados.");
        }
        listaRepository.deleteById(id);
    }

    @Transactional
    public void moverFavoritos(Long origenId, Long destinoId) {
        // 1. Validar que ambas listas existan (obtenerPorId lanza error si no existen)
        obtenerPorId(origenId);
        obtenerPorId(destinoId);

        // 2. Obtener todos los favoritos de la lista origen
        List<Favorito> favoritos = favoritoRepository.findByListaId(origenId);

        // 3. Cambiarles el ID a la lista destino y guardarlos
        for (Favorito f : favoritos) {
            f.setListaId(destinoId);
            favoritoRepository.save(f);
        }

        // 4. Eliminar la lista de origen (forzamos el borrado directo en el repositorio 
        // ya que ahora está vacía)
        listaRepository.deleteById(origenId);
    }
}