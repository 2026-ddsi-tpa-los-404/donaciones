package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.IdentificadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.ProductoDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.TipoIdentificadorEnum;
import java.util.List;
import java.util.NoSuchElementException;
import ar.edu.utn.dds.k3003.app.Application;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class ProductosPropiasTest {

  @Autowired
  Fachada instancia;

  @Test
  void agregarProductoSinIdentificadorGuardaYRetornaConID() {
    ProductoDTO dto = new ProductoDTO(null, "Ropa", "Ropa de abrigo", null, null);
    ProductoDTO resultado = instancia.agregarProducto(dto);

    Assertions.assertNotNull(resultado.id());
    Assertions.assertEquals("Ropa", resultado.nombre());
  }

  @Test
  void buscarProductoPorIDDevuelveProductoCorrecto() {
    ProductoDTO guardado = instancia.agregarProducto(
        new ProductoDTO(null, "Alimentos", "Productos no perecederos", null, null));

    ProductoDTO encontrado = instancia.buscarProductoPorID(guardado.id());

    Assertions.assertEquals(guardado.id(), encontrado.id());
    Assertions.assertEquals("Alimentos", encontrado.nombre());
  }

  @Test
  void buscarProductoPorIDInexistenteLanzaExcepcion() {
    Assertions.assertThrows(NoSuchElementException.class,
        () -> instancia.buscarProductoPorID("id-inexistente"));
  }

  @Test
  void listarProductosDevuelveProductosRegistrados() {
    instancia.agregarProducto(new ProductoDTO(null, "Juguetes", "Para ninos", null, null));
    instancia.agregarProducto(new ProductoDTO(null, "Libros", "Educativos", null, null));

    List<ProductoDTO> lista = instancia.listarProductos();

    Assertions.assertEquals(2, lista.size());
  }

  @Test
  void actualizarProductoModificaDatos() {
    ProductoDTO guardado = instancia.agregarProducto(
        new ProductoDTO(null, "Viejo", "Descripcion vieja", null, null));

    ProductoDTO actualizado = instancia.actualizarProducto(
        guardado.id(), new ProductoDTO(null, "Nuevo", "Descripcion nueva", null, null));

    Assertions.assertEquals("Nuevo", actualizado.nombre());
    Assertions.assertEquals("Descripcion nueva", actualizado.descripcion());
  }

  @Test
  void actualizarProductoInexistenteLanzaExcepcion() {
    Assertions.assertThrows(NoSuchElementException.class,
        () -> instancia.actualizarProducto("id-inexistente",
            new ProductoDTO(null, "x", "y", null, null)));
  }

  @Test
  void eliminarProductoPorIDEliminaProducto() {
    ProductoDTO guardado = instancia.agregarProducto(
        new ProductoDTO(null, "Para eliminar", "desc", null, null));

    instancia.eliminarProductoPorID(guardado.id());

    Assertions.assertThrows(NoSuchElementException.class,
        () -> instancia.buscarProductoPorID(guardado.id()));
  }

  @Test
  void eliminarProductoPorIDInexistenteLanzaExcepcion() {
    Assertions.assertThrows(NoSuchElementException.class,
        () -> instancia.eliminarProductoPorID("id-inexistente"));
  }

  @Test
  void agregarProductoConCodigoDeBarrasYDescripcionCortaLanzaExcepcion() {
    IdentificadorDTO identificador = instancia.agregarIdentificador(
        new IdentificadorDTO(null, TipoIdentificadorEnum.CODIGODEBARRAS, "barras"));

    // "solo dos" tiene 2 palabras, el minimo requerido es 3
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        instancia.agregarProducto(
            new ProductoDTO(null, "Ropa", "solo dos", null, identificador.id())));
  }

  @Test
  void agregarProductoConCodigoDeBarrasYDescripcionLargaEsValido() {
    IdentificadorDTO identificador = instancia.agregarIdentificador(
        new IdentificadorDTO(null, TipoIdentificadorEnum.CODIGODEBARRAS, "barras"));

    ProductoDTO resultado = instancia.agregarProducto(
        new ProductoDTO(null, "Ropa", "ropa de abrigo invierno", null, identificador.id()));

    Assertions.assertNotNull(resultado.id());
  }

  @Test
  void agregarProductoConQRYNombreConLetrasImparesLanzaExcepcion() {
    IdentificadorDTO identificador = instancia.agregarIdentificador(
        new IdentificadorDTO(null, TipoIdentificadorEnum.QR, "qr"));

    // "Ropas" tiene 5 letras (impar) → invalido
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        instancia.agregarProducto(
            new ProductoDTO(null, "Ropas", "descripcion del producto", null, identificador.id())));
  }

  @Test
  void agregarProductoConQRYNombreConLetrasParesEsValido() {
    IdentificadorDTO identificador = instancia.agregarIdentificador(
        new IdentificadorDTO(null, TipoIdentificadorEnum.QR, "qr"));

    // "Ropa" tiene 4 letras (par) → valido
    ProductoDTO resultado = instancia.agregarProducto(
        new ProductoDTO(null, "Ropa", "descripcion del producto", null, identificador.id()));

    Assertions.assertNotNull(resultado.id());
  }
}
