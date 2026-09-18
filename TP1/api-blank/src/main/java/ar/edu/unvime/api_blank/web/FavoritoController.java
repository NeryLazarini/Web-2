package ar.edu.unvime.api_blank.web;

import ar.edu.unvime.api_blank.dto.FavoritoInputDTO;
import ar.edu.unvime.api_blank.dto.FavoritoOutputDTO;
import ar.edu.unvime.api_blank.service.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    private final FavoritoService service;

    public FavoritoController(FavoritoService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un nuevo producto favorito")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritoOutputDTO crear(@Valid @RequestBody FavoritoInputDTO input) {
        return service.crear(input);
    }

    @Operation(summary = "Listar todos los productos favoritos guardados")
    @GetMapping
    public List<FavoritoOutputDTO> listar() {
        return service.listarTodos();
    }

    @Operation(summary = "Buscar un producto favorito por su ID")
    @GetMapping("/{id}")
    public FavoritoOutputDTO obtenerUno(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @Operation(summary = "Actualizar la información de un favorito existente")
    @PutMapping("/{id}")
    public FavoritoOutputDTO actualizar(@PathVariable Long id, @Valid @RequestBody FavoritoInputDTO input) {
        return service.actualizar(id, input);
    }

    @Operation(summary = "Eliminar un favorito por su ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}