package ar.edu.utn.dds.k3003.eventos;

import ar.edu.utn.dds.k3003.model.Categoria;


 // el Servicio de Consultas no puede preguntarle a Donaciones
 // la categoría y la subcategoría --> evento lleva la categoría con su nombre
public record Clasificacion(Referencia categoria, Referencia subcategoria) {

  public record Referencia(String id, String nombre) {}

  public static final Clasificacion DESCONOCIDA = new Clasificacion(null, null);

  public static Clasificacion de(Categoria categoria) {
    if (categoria == null) {
      return DESCONOCIDA;
    }
    Referencia subcategoria = categoria.getSubcategoriaID() != null
        ? new Referencia(categoria.getSubcategoriaID(), null)
        : null;
    return new Clasificacion(new Referencia(categoria.getId(), categoria.getNombre()), subcategoria);
  }
}
