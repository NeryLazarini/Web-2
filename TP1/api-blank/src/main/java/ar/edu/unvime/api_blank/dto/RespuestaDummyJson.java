package ar.edu.unvime.api_blank.dto;

import java.util.List;

public class RespuestaDummyJson {
    private List<ProductoDTO> products;

    public RespuestaDummyJson() {}

    public List<ProductoDTO> getProducts() { 
        return products; 
    }
    
    public void setProducts(List<ProductoDTO> products) { 
        this.products = products; 
    }
}