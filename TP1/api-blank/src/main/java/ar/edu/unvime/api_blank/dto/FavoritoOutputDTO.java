package ar.edu.unvime.api_blank.dto;

import java.time.LocalDate;

public class FavoritoOutputDTO {
    private Long id;
    private Long productoId;
    private String notaPersonal;
    private LocalDate fechaAgregado;
    private Long listaId;

    public Long getListaId() { return listaId; }
    public void setListaId(Long listaId) { this.listaId = listaId; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getNotaPersonal() { return notaPersonal; }
    public void setNotaPersonal(String notaPersonal) { this.notaPersonal = notaPersonal; }
    public LocalDate getFechaAgregado() { return fechaAgregado; }
    public void setFechaAgregado(LocalDate fechaAgregado) { this.fechaAgregado = fechaAgregado; }
}