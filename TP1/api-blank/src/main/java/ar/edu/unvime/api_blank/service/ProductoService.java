package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.dto.ProductoDTO;
import ar.edu.unvime.api_blank.dto.RespuestaDummyJson;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ProductoService {
    private final RestClient restClient;
    public ProductoService() {
        this.restClient = RestClient.create("https://dummyjson.com");
    }

    public List<ProductoDTO> obtenerTodos() {
        RespuestaDummyJson respuesta = restClient.get()
                .uri("/products")
                .retrieve()
                .body(RespuestaDummyJson.class);
        return respuesta.getProducts();
    }

    public ProductoDTO obtenerPorId(Long id) {
        return restClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .body(ProductoDTO.class);
    }
}