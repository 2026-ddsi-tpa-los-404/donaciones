package ar.edu.utn.dds.k3003.repositories.DataMapper;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.CategoriaDTO;
import ar.edu.utn.dds.k3003.model.Categoria;

public class CategoriasDataMapper {

  public CategoriaDTO toCategoriaDTO(Categoria categoria) {
    return new CategoriaDTO(
        categoria.getId(),
        categoria.getNombre(),
        categoria.getDescripcion(),
        categoria.getSubcategoriaID());
  }

  public Categoria toCategoria(CategoriaDTO dto) {
    Categoria categoria = new Categoria();
    categoria.setNombre(dto.nombre());
    categoria.setDescripcion(dto.descripcion());
    categoria.setSubcategoriaID(dto.subcategoriaID());
    return categoria;
  }
}
