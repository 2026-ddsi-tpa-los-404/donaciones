package ar.edu.utn.dds.k3003.api.controller;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.CategoriaDTO;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

  private final Fachada fachada;

  public CategoriaController(Fachada fachada) {
    this.fachada = fachada;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoriaDTO agregar(@RequestBody CategoriaDTO categoriaDTO) {
    return fachada.agregarCategoria(categoriaDTO);
  }

  @GetMapping
  public List<CategoriaDTO> listar() {
    return fachada.listarCategorias();
  }

  @GetMapping("/{id}")
  public CategoriaDTO buscarPorId(@PathVariable String id) {
    return fachada.buscarCategoriaPorID(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable String id) {
    fachada.eliminarCategoriaPorID(id);
  }
}
