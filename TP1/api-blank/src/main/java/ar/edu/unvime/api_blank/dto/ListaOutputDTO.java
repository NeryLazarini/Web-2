package ar.edu.unvime.api_blank.dto;

public class ListaOutputDTO {
    private Long id;
    private String nombre;
    private Long usuarioId;

    public ListaOutputDTO(Long id, String nombre, Long usuarioId) {
        this.id = id;
        this.nombre = nombre;
        this.usuarioId = usuarioId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}