package ar.edu.unvime.api_blank.web;

import ar.edu.unvime.api_blank.domain.Lista;
import ar.edu.unvime.api_blank.dto.FavoritoOutputDTO;
import ar.edu.unvime.api_blank.dto.ListaInputDTO;
import ar.edu.unvime.api_blank.dto.ListaOutputDTO;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import ar.edu.unvime.api_blank.service.ListaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/listas")
public class ListaController {

    private final ListaService listaService;
    private final FavoritoRepository favoritoRepository;

    public ListaController(ListaService listaService, FavoritoRepository favoritoRepository) {
        this.listaService = listaService;
        this.favoritoRepository = favoritoRepository;
    }

    @PostMapping
    public ResponseEntity<ListaOutputDTO> crearLista(@Valid @RequestBody ListaInputDTO dto) {
        Lista nuevaLista = new Lista(null, dto.getNombre(), null);
        Lista guardada = listaService.guardar(nuevaLista);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ListaOutputDTO(guardada.getId(), guardada.getNombre(), guardada.getUsuarioId()));
    }

    @GetMapping
    public ResponseEntity<List<ListaOutputDTO>> listarListas() {
        List<ListaOutputDTO> listas = listaService.obtenerTodas().stream()
                .map(l -> new ListaOutputDTO(l.getId(), l.getNombre(), l.getUsuarioId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(listas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaOutputDTO> obtenerLista(@PathVariable Long id) {
        Lista lista = listaService.obtenerPorId(id);
        return ResponseEntity.ok(new ListaOutputDTO(lista.getId(), lista.getNombre(), lista.getUsuarioId()));
    }

    @GetMapping("/{id}/favoritos")
    public ResponseEntity<List<FavoritoOutputDTO>> obtenerFavoritosDeLista(@PathVariable Long id) {
        // Verificamos que la lista exista y pertenezca al usuario (o sea ADMIN)
        listaService.obtenerPorId(id);

        List<FavoritoOutputDTO> favoritos = favoritoRepository.findByListaId(id).stream()
                .map(f -> {
                    FavoritoOutputDTO dto = new FavoritoOutputDTO();
                    dto.setId(f.getId());
                    dto.setProductoId(f.getProductoId());
                    dto.setNotaPersonal(f.getNotaPersonal());
                    dto.setListaId(f.getListaId());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(favoritos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLista(@PathVariable Long id) {
        listaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{origenId}/mover-favoritos")
    public ResponseEntity<Void> moverFavoritos(
            @PathVariable Long origenId,
            @RequestParam Long destinoId) {
        listaService.moverFavoritos(origenId, destinoId);
        return ResponseEntity.noContent().build();
    }
}