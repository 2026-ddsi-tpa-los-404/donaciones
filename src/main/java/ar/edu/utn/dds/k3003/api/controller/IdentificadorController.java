package ar.edu.utn.dds.k3003.api.controller;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.IdentificadorDTO;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identificadores")
public class IdentificadorController {

  private final Fachada fachada;

  public IdentificadorController(Fachada fachada) {
    this.fachada = fachada;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public IdentificadorDTO agregar(@RequestBody IdentificadorDTO identificadorDTO) {
    return fachada.agregarIdentificador(identificadorDTO);
  }

  @GetMapping
  public List<IdentificadorDTO> listar() {
    return fachada.listarIdentificadores();
  }

  @GetMapping("/{id}")
  public IdentificadorDTO buscarPorId(@PathVariable String id) {
    return fachada.buscarIdentificadorPorID(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable String id) {
    fachada.eliminarIdentificadorPorID(id);
  }
}
