package ar.edu.unvime.api_blank.web;

import ar.edu.unvime.api_blank.dto.FavoritoInputDTO;
import ar.edu.unvime.api_blank.dto.FavoritoOutputDTO;
import ar.edu.unvime.api_blank.service.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Favoritos", description = "Gestion de productos favoritos. Requiere autenticacion mediante token JWT (Authorization: Bearer <token>).")
@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    private final FavoritoService service;

    public FavoritoController(FavoritoService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un nuevo producto favorito", description = "Requiere autenticacion JWT.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritoOutputDTO crear(@Valid @RequestBody FavoritoInputDTO input) {
        return service.crear(input);
    }

    @Operation(summary = "Listar todos los productos favoritos guardados", description = "Requiere autenticacion JWT.")
    @GetMapping
    public List<FavoritoOutputDTO> listar() {
        return service.listarTodos();
    }

    @Operation(summary = "Buscar un producto favorito por su ID", description = "Requiere autenticacion JWT.")
    @GetMapping("/{id}")
    public FavoritoOutputDTO obtenerUno(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @Operation(summary = "Actualizar la informacion de un favorito existente", description = "Requiere autenticacion JWT.")
    @PutMapping("/{id}")
    public FavoritoOutputDTO actualizar(@PathVariable Long id, @Valid @RequestBody FavoritoInputDTO input) {
        return service.actualizar(id, input);
    }

    @Operation(summary = "Eliminar un favorito por su ID", description = "Requiere autenticacion JWT.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}