## TP2 - Punto 4: Arquitectura Hexagonal y Persistencia

**¿Qué clases se modificaron/agregaron?**
* **Capa de Infraestructura (Adapters):** Se eliminó `InMemoryFavoritoRepository` (que guardaba en memoria) y se crearon `FavoritoRepositoryAdapter` (para traducir entre dominio y JPA), `FavoritoJpaRepository` (interfaz de Spring Data) y `FavoritoEntity` (modelo mapeado a la base de datos).

**¿Qué clases quedaron exactamente iguales?**
* **Dominio y Aplicación:** La clase `Favorito`, `FavoritoService`, `FavoritoController` y todos los DTOs (Request/Response) quedaron intactos, sin una sola línea de código modificada.

**¿Por qué fue posible esto?**
Gracias al uso de Puertos y Adaptadores. La interfaz `FavoritoRepository` actúa como un "Puerto" (un contrato). Nuestro `FavoritoService` solo conoce ese contrato, no le importa cómo o dónde se guardan los datos físicamente. Por lo tanto, pudimos desenchufar el adaptador en memoria del TP1 y enchufar el nuevo adaptador JPA de PostgreSQL del TP2 sin que el núcleo de la aplicación deba alterarse.


## TP2 - Punto 6: Evolución del esquema
Se resolvió con una migración nueva (V4) en lugar de modificar las anteriores porque Flyway ya ejecutó y registró (con un checksum) las versiones V1, V2 y V3 en la base de datos. Modificar un script del pasado generaría un error de validación en Flyway y rompería los entornos que ya aplicaron esos cambios.

## TP2 - Punto 7: Transacciones
El método `moverFavoritos` del `ListaService` fue marcado con `@Transactional`. Esta operación realiza múltiples escrituras en la base de datos: primero actualiza los favoritos (UPDATE) y luego elimina la lista origen (DELETE). 

Si sacáramos el `@Transactional` y el sistema fallara justo después de actualizar los favoritos pero antes de eliminar la lista origen (por un corte de luz o un error del servidor), la base de datos perdería su consistencia (principio de Atomicidad de ACID). Los favoritos estarían en la lista nueva, pero la lista vieja seguiría existiendo vacía y sin sentido. Con `@Transactional`, garantizamos que o se completan todas las escrituras, o no se guarda ninguna (se hace un *rollback* automático).