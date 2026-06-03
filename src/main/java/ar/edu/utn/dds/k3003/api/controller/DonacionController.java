package ar.edu.utn.dds.k3003.api.controller;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.api.model.EstadoDonacionRequest;
import ar.edu.utn.dds.k3003.api.model.QuejaRequest;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/donaciones")
public class DonacionController {

  private final Fachada fachada;

  public DonacionController(Fachada fachada) {
    this.fachada = fachada;
  }

  @GetMapping("/{id}")
  public DonacionDTO buscarPorId(@PathVariable String id) {
    return fachada.buscarDonacionPorID(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminarPorID(@PathVariable String id){
    fachada.eliminarDonacionPorID(id);
  }

  @GetMapping
  public List<DonacionDTO> listarDonacionDTOs() {
    return fachada.listarDonaciones();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DonacionDTO registrar(@RequestBody DonacionDTO donacionDTO) {
    return fachada.registrarDonacion(donacionDTO);
  }

  @GetMapping("/search")
  public List<DonacionDTO> buscarPorDonadorYFecha(
      @RequestParam String donadorID,
      @RequestParam LocalDate fechaInicio) {
    return fachada.buscarPorDonadorYFechaInicio(donadorID, fechaInicio);
  }

  @PatchMapping("/{id}/estado")
  public DonacionDTO cambiarEstado(
      @PathVariable String id,
      @RequestBody EstadoDonacionRequest body) {
    return fachada.cambiarEstadoDeDonacion(id, body.estado());
  }

  @PostMapping("/{id}/queja")
  @ResponseStatus(HttpStatus.CREATED)
  public DonacionDTO registrarQueja(
      @PathVariable String id,
      @RequestBody QuejaRequest body) {
    return fachada.registrarQuejaEnDonacion(id, body.descripcion());
  }
}
