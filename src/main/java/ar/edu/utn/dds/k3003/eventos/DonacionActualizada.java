package ar.edu.utn.dds.k3003.eventos;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import ar.edu.utn.dds.k3003.model.Donacion;
import ar.edu.utn.dds.k3003.model.Producto;

import java.time.Instant;
import java.util.List;

// Los datos del mensaje del evento de una donacion
public record DonacionActualizada(String id, String donadorId, String depositoId, String descripcion,
                                  Clasificacion.Referencia producto,
                                  Clasificacion.Referencia subcategoria,
                                  Clasificacion.Referencia categoria,
                                  Integer cantidad, EstadoDonacionEnum estado, Instant fechaIngreso,
                                  List<CambioDeEstado> historialEstados, boolean eliminado) {

  public record CambioDeEstado(EstadoDonacionEnum estado, Instant fecha) {}

  public static DonacionActualizada de(Donacion donacion, Producto producto, Clasificacion clasificacion,
                                       boolean eliminado) {
    return new DonacionActualizada(
        donacion.getId(),
        donacion.getDonadorID(),
        donacion.getDepositoID(),
        donacion.getDescripcion(),
        new Clasificacion.Referencia(donacion.getProductoID(), producto != null ? producto.getNombre() : null),
        clasificacion.subcategoria(),
        clasificacion.categoria(),
        donacion.getCantidad(),
        donacion.getEstado(),
        Evento.enUtc(donacion.getFecha()),
        donacion.getHistorialEstados().stream()
            .map(cambio -> new CambioDeEstado(cambio.getEstado(), Evento.enUtc(cambio.getTiempo())))
            .toList(),
        eliminado);
  }
}
