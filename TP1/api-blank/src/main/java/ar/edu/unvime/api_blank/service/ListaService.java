package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.domain.Favorito;
import ar.edu.unvime.api_blank.domain.Lista;
import ar.edu.unvime.api_blank.exception.AccesoDenegadoException;
import ar.edu.unvime.api_blank.exception.ListaConFavoritosException;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import ar.edu.unvime.api_blank.repository.ListaRepository;
import ar.edu.unvime.api_blank.security.ContextoSeguridad;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListaService {
    private final ListaRepository listaRepository;
    private final FavoritoRepository favoritoRepository;
    private final ContextoSeguridad contextoSeguridad;

    public ListaService(
            ListaRepository listaRepository,
            FavoritoRepository favoritoRepository,
            ContextoSeguridad contextoSeguridad) {
        this.listaRepository = listaRepository;
        this.favoritoRepository = favoritoRepository;
        this.contextoSeguridad = contextoSeguridad;
    }

    public Lista guardar(Lista lista) {
        // El dueno siempre es el usuario autenticado, nunca un dato del body
        lista.setUsuarioId(contextoSeguridad.usuarioActualId());
        return listaRepository.save(lista);
    }

    public List<Lista> obtenerTodas() {
        if (contextoSeguridad.esAdmin()) {
            return listaRepository.findAll();
        }
        return listaRepository.findByUsuarioId(contextoSeguridad.usuarioActualId());
    }

    public Lista obtenerPorId(Long id) {
        return buscarPropiaOFallar(id);
    }

    public void eliminar(Long id) {
        buscarPropiaOFallar(id);

        // Verificamos si la lista tiene favoritos antes de borrar
        if (!favoritoRepository.findByListaId(id).isEmpty()) {
            throw new ListaConFavoritosException("No se puede eliminar la lista porque tiene favoritos asociados.");
        }
        listaRepository.deleteById(id);
    }

    @Transactional
    public void moverFavoritos(Long origenId, Long destinoId) {
        // Validamos que ambas listas existan y pertenezcan al usuario (o sea ADMIN)
        buscarPropiaOFallar(origenId);
        buscarPropiaOFallar(destinoId);

        List<Favorito> favoritos = favoritoRepository.findByListaId(origenId);

        for (Favorito f : favoritos) {
            f.setListaId(destinoId);
            favoritoRepository.save(f);
        }

        listaRepository.deleteById(origenId);
    }

    private Lista buscarPropiaOFallar(Long id) {
        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada"));

        boolean esDueno = lista.getUsuarioId() != null
                && lista.getUsuarioId().equals(contextoSeguridad.usuarioActualId());

        if (!esDueno && !contextoSeguridad.esAdmin()) {
            throw new AccesoDenegadoException("La lista " + id + " no te pertenece");
        }
        return lista;
    }
}