## TP2 - Punto 4: Arquitectura Hexagonal y Persistencia

**¿Qué clases se modificaron/agregaron?**
* **Capa de Infraestructura (Adapters):** Se eliminó `InMemoryFavoritoRepository` (que guardaba en memoria) y se crearon `FavoritoRepositoryAdapter` (para traducir entre dominio y JPA), `FavoritoJpaRepository` (interfaz de Spring Data) y `FavoritoEntity` (modelo mapeado a la base de datos).

**¿Qué clases quedaron exactamente iguales?**
* **Dominio y Aplicación:** La clase `Favorito`, `FavoritoService`, `FavoritoController` y todos los DTOs (Request/Response) quedaron intactos, sin una sola línea de código modificada.

**¿Por qué fue posible esto?**
Gracias al uso de Puertos y Adaptadores. La interfaz `FavoritoRepository` actúa como un "Puerto" (un contrato). Nuestro `FavoritoService` solo conoce ese contrato, no le importa cómo o dónde se guardan los datos físicamente. Por lo tanto, pudimos desenchufar el adaptador en memoria del TP1 y enchufar el nuevo adaptador JPA de PostgreSQL del TP2 sin que el núcleo de la aplicación deba alterarse.