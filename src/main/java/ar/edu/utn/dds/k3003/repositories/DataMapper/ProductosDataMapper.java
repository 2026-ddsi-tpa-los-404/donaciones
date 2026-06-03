package ar.edu.utn.dds.k3003.repositories.DataMapper;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.ProductoDTO;
import ar.edu.utn.dds.k3003.model.Producto;

public class ProductosDataMapper {

  public ProductoDTO toProductoDTO(Producto producto) {
    return new ProductoDTO(
        producto.getId(),
        producto.getNombre(),
        producto.getDescripcion(),
        producto.getSubCategoriaID(),
        producto.getIdentificadorID());
  }

  public Producto toProducto(ProductoDTO dto) {
    Producto producto = new Producto();
    producto.setNombre(dto.nombre());
    producto.setDescripcion(dto.descripcion());
    producto.setSubCategoriaID(dto.categoriaID());
    producto.setIdentificadorID(dto.identificadorID());
    return producto;
  }
}
