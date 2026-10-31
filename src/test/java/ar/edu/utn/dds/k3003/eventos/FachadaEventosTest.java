package ar.edu.utn.dds.k3003.eventos;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.ProductoDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaLogistica;
import ar.edu.utn.dds.k3003.exceptions.DepositoSinCapacidadException;
import ar.edu.utn.dds.k3003.model.Donacion;
import ar.edu.utn.dds.k3003.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class FachadaEventosTest {

  private Fachada fachada;
  private FachadaLogistica logistica;
  private PublicadorEventos publicador;

  @BeforeEach
  void setUp() {
    fachada = new Fachada();
    FachadaDonadoresYEntidades donadores = mock(FachadaDonadoresYEntidades.class);
    logistica = mock(FachadaLogistica.class);
    publicador = mock(PublicadorEventos.class);
    when(donadores.puedeDonar("12")).thenReturn(true);
    fachada.setFachadaDonadoresYEntidades(donadores);
    fachada.setFachadaLogistica(logistica);
    fachada.setPublicadorEventos(publicador);
  }

  private DonacionDTO registrar() {
    return fachada.registrarDonacion(
        new DonacionDTO(null, "12", "3", "Fideos para el comedor", "7", 30, null, null));
  }

  @Test
  void registrarUnaDonacionAceptadaPorLogisticaLaPublica() {
    registrar();

    verify(publicador, times(1)).donacionActualizada(any(Donacion.class));
  }

  @Test
  void unaDonacionRechazadaPorLogisticaNoSePublica() {
    when(logistica.gestionarDonacion(any(), any(), any(), anyInt()))
        .thenThrow(new DepositoSinCapacidadException("sin lugar"));

    assertThrows(DepositoSinCapacidadException.class, this::registrar);

    verifyNoInteractions(publicador);
  }

  @Test
  void cadaCambioDeEstadoSePublica() {
    DonacionDTO donacion = registrar();

    fachada.cambiarEstadoDeDonacion(donacion.id(), EstadoDonacionEnum.ACEPTADA);

    verify(publicador, times(2)).donacionActualizada(any(Donacion.class));
  }

  @Test
  void eliminarUnaDonacionPublicaLaBaja() {
    DonacionDTO donacion = registrar();

    fachada.eliminarDonacionPorID(donacion.id());

    verify(publicador, times(1)).donacionEliminada(any(Donacion.class));
  }

  @Test
  void altaModificacionYBajaDeProductosSePublican() {
    ProductoDTO producto = fachada.agregarProducto(new ProductoDTO(null, "Fideos", "Paquete de 500 g", "1", null));
    fachada.actualizarProducto(producto.id(), new ProductoDTO(null, "Fideos", "Paquete de 1 kg", "1", null));
    fachada.eliminarProductoPorID(producto.id());

    verify(publicador, times(2)).productoActualizado(any(Producto.class));
    verify(publicador, times(1)).productoEliminado(any(Producto.class));
  }

  @Test
  void sinPublicadorLaFachadaFuncionaIgual() {
    Fachada sinEventos = new Fachada();
    assertDoesNotThrow(() -> sinEventos.agregarProducto(new ProductoDTO(null, "Fideos", "Paquete de 500 g", "1", null)));
  }
}
