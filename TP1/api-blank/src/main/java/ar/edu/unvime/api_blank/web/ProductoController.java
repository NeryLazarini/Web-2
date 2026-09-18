package ar.edu.unvime.api_blank.web;

import ar.edu.unvime.api_blank.dto.ProductoDTO;
import ar.edu.unvime.api_blank.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Listar todos los productos del catálogo externo")
    @GetMapping
    public List<ProductoDTO> listarProductos() {
        return productoService.obtenerTodos();
    }

    @Operation(summary = "Obtener un producto específico por su ID")
    @GetMapping("/{id}")
    public ProductoDTO obtenerProductoPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }
}