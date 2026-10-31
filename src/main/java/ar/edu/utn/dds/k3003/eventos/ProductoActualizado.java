package ar.edu.utn.dds.k3003.eventos;

import ar.edu.utn.dds.k3003.model.Producto;

// Los datos del mensaje del evento de un producto
public record ProductoActualizado(String id, String nombre, String descripcion,
                                  Clasificacion.Referencia subcategoria,
                                  Clasificacion.Referencia categoria,
                                  boolean eliminado) {

  public static ProductoActualizado de(Producto producto, Clasificacion clasificacion, boolean eliminado) {
    return new ProductoActualizado(
        producto.getId(),
        producto.getNombre(),
        producto.getDescripcion(),
        clasificacion.subcategoria(),
        clasificacion.categoria(),
        eliminado);
  }
}
