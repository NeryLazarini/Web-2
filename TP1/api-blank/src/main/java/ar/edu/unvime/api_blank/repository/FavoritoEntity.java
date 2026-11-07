package ar.edu.unvime.api_blank.repository; // Ajustá el paquete si es necesario

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favoritos")
public class FavoritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "lista_id")
    private ListaEntity lista;

    public ListaEntity getLista() { return lista; }
    public void setLista(ListaEntity lista) { this.lista = lista; }

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    private String nota;

    @Column(name = "fecha_alta", nullable = false, updatable = false)
    private LocalDateTime fechaAlta = LocalDateTime.now();

    // JPA exige un constructor vacío
    public FavoritoEntity() {}

    public FavoritoEntity(Long productoId, String nota) {
        this.productoId = productoId;
        this.nota = nota;
    }

    // Generá los Getters y Setters acá (o usá @Data si tenés Lombok)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }
    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }
}