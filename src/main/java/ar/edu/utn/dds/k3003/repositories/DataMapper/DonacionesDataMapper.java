package ar.edu.utn.dds.k3003.repositories.DataMapper;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.model.Donacion;
import java.util.ArrayList;

public class DonacionesDataMapper {

  public DonacionDTO toDonacionDTO(Donacion donacion) {
    return new DonacionDTO(
        donacion.getId(),
        donacion.getDonadorID(),
        donacion.getDepositoID(),
        donacion.getDescripcion(),
        donacion.getProductoID(),
        donacion.getCantidad(),
        donacion.getEstado());
  }

  public Donacion toDonacion(DonacionDTO dto) {
    Donacion donacion = new Donacion();
    donacion.setDonadorID(dto.donadorID());
    donacion.setDepositoID(dto.depositoID());
    donacion.setDescripcion(dto.descripcion());
    donacion.setProductoID(dto.productoID());
    donacion.setCantidad(dto.cantidad());
    donacion.setHistorialEstados(new ArrayList<>());
    return donacion;
  }
}
