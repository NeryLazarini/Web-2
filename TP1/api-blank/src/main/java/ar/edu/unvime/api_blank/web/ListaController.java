package ar.edu.unvime.api_blank.web;

import ar.edu.unvime.api_blank.domain.Favorito;
import ar.edu.unvime.api_blank.domain.Lista;
import ar.edu.unvime.api_blank.dto.FavoritoOutputDTO; // Importá tu DTO de favorito
import ar.edu.unvime.api_blank.dto.ListaInputDTO;
import ar.edu.unvime.api_blank.dto.ListaOutputDTO;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import ar.edu.unvime.api_blank.service.ListaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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
        Lista nuevaLista = new Lista(null, dto.getNombre());
        Lista guardada = listaService.guardar(nuevaLista);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ListaOutputDTO(guardada.getId(), guardada.getNombre()));
    }

    @GetMapping
    public ResponseEntity<List<ListaOutputDTO>> listarListas() {
        List<ListaOutputDTO> listas = listaService.obtenerTodas().stream()
                .map(l -> new ListaOutputDTO(l.getId(), l.getNombre()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(listas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaOutputDTO> obtenerLista(@PathVariable Long id) {
        Lista lista = listaService.obtenerPorId(id);
        return ResponseEntity.ok(new ListaOutputDTO(lista.getId(), lista.getNombre()));
    }

    @GetMapping("/{id}/favoritos")
    public ResponseEntity<List<FavoritoOutputDTO>> obtenerFavoritosDeLista(@PathVariable Long id) {
        // Verificamos que la lista exista primero
        listaService.obtenerPorId(id);
        
        List<FavoritoOutputDTO> favoritos = favoritoRepository.findByListaId(id).stream()
                .map(f -> {
                    FavoritoOutputDTO dto = new FavoritoOutputDTO();
                    dto.setId(f.getId());
                    dto.setProductoId(f.getProductoId());
                    dto.setNotaPersonal(f.getNotaPersonal());
                    dto.setListaId(f.getListaId());
                    // Agregá la fecha si la tenés en tu DTO
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
}