package ar.edu.utn.dds.k3003.api.controller;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.ProductoDTO;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
public class ProductoController {

  private final Fachada fachada;
  private final MeterRegistry meterRegistry;

  public ProductoController(Fachada fachada, MeterRegistry meterRegistry) {
    this.fachada = fachada;
    this.meterRegistry = meterRegistry;
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

  /**
   * Usado por otros modulos (ej. Donadores y Entidades) para validar si un
   * producto existe antes de registrar una necesidad. Se mide por separado de
   * "encontrado"/"no_encontrado" para poder distinguir validaciones fallidas
   * de errores reales de integracion.
   */
  @GetMapping("/{id}")
  public ProductoDTO buscarPorId(@PathVariable String id) {
    try {
      ProductoDTO producto = fachada.buscarProductoPorID(id);
      meterRegistry.counter("productos.validaciones", "resultado", "encontrado").increment();
      return producto;
    } catch (NoSuchElementException e) {
      meterRegistry.counter("productos.validaciones", "resultado", "no_encontrado").increment();
      throw e;
    }
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
