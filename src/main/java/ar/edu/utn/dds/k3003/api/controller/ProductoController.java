package ar.edu.utn.dds.k3003.api.controller;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.ProductoDTO;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
public class ProductoController {

  private final Fachada fachada;

  public ProductoController(Fachada fachada) {
    this.fachada = fachada;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductoDTO agregar(@RequestBody ProductoDTO productoDTO) {
    return fachada.agregarProducto(productoDTO);
  }

  @GetMapping
  public List<ProductoDTO> listar() {
    return fachada.listarProductos();
  }

  @GetMapping("/{id}")
  public ProductoDTO buscarPorId(@PathVariable String id) {
    return fachada.buscarProductoPorID(id);
  }

  @PutMapping("/{id}")
  public ProductoDTO actualizar(@PathVariable String id, @RequestBody ProductoDTO productoDTO) {
    return fachada.actualizarProducto(id, productoDTO);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable String id) {
    fachada.eliminarProductoPorID(id);
  }
}
