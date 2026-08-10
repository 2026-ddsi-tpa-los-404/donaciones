package ar.edu.utn.dds.k3003.api.controller;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.api.model.EstadoDonacionRequest;
import ar.edu.utn.dds.k3003.api.model.QuejaRequest;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/donaciones")
public class DonacionController {

  private final Fachada fachada;
  private final MeterRegistry meterRegistry;

  public DonacionController(Fachada fachada, MeterRegistry meterRegistry) {
    this.fachada = fachada;
    this.meterRegistry = meterRegistry;
  }

  @GetMapping("/{id}")
  public DonacionDTO buscarPorId(@PathVariable String id) {
    return fachada.buscarDonacionPorID(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminarPorID(@PathVariable String id) {
    fachada.eliminarDonacionPorID(id);
  }

  @GetMapping
  public List<DonacionDTO> listarDonacionDTOs() {
    return fachada.listarDonaciones();
  }

  /**
   * El tiempo total incluye la notificacion sincronica a Logistica, que desde
   * la Entrega 4 encola el trabajo de asignacion internamente. Medir la
   * duracion completa permite detectar si esa integracion empieza a demorar
   * o a fallar, sin depender de metricas propias de Logistica.
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DonacionDTO registrar(@RequestBody DonacionDTO donacionDTO) {
    Timer.Sample muestra = Timer.start(meterRegistry);
    String resultado = "exitoso";
    try {
      DonacionDTO donacionRegistrada = fachada.registrarDonacion(donacionDTO);
      meterRegistry.counter("donaciones.registradas").increment();
      return donacionRegistrada;
    } catch (RuntimeException e) {
      resultado = "error";
      throw e;
    } finally {
      muestra.stop(meterRegistry.timer("donaciones.registro.duracion", "resultado", resultado));
    }
  }

  @GetMapping("/search")
  public List<DonacionDTO> buscarPorDonadorYFecha(
      @RequestParam String donadorID, @RequestParam LocalDate fechaInicio) {
    return fachada.buscarPorDonadorYFechaInicio(donadorID, fechaInicio);
  }

  @PatchMapping("/{id}/estado")
  public DonacionDTO cambiarEstado(
      @PathVariable String id, @RequestBody EstadoDonacionRequest body) {
    DonacionDTO resultado = fachada.cambiarEstadoDeDonacion(id, body.estado());
    meterRegistry
        .counter("donaciones.estado.cambiado", "estado", body.estado().name())
        .increment();
    return resultado;
  }

  @PostMapping("/{id}/queja")
  @ResponseStatus(HttpStatus.CREATED)
  public DonacionDTO registrarQueja(
      @PathVariable String id, @RequestBody QuejaRequest body) {
    DonacionDTO resultado = fachada.registrarQuejaEnDonacion(id, body.descripcion());
    meterRegistry.counter("donaciones.quejas.registradas").increment();
    return resultado;
  }
}
