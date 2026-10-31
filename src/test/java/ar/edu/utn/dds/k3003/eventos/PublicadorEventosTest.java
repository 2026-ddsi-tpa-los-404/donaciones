package ar.edu.utn.dds.k3003.eventos;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import ar.edu.utn.dds.k3003.model.Categoria;
import ar.edu.utn.dds.k3003.model.Donacion;
import ar.edu.utn.dds.k3003.model.EstadoDonacion;
import ar.edu.utn.dds.k3003.model.Producto;
import ar.edu.utn.dds.k3003.repositories.Repository.CategoriaRepository;
import ar.edu.utn.dds.k3003.repositories.Repository.ProductoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PublicadorEventosTest {

  private RabbitTemplate rabbitTemplate;
  private ProductoRepository productoRepository;
  private CategoriaRepository categoriaRepository;
  private ObjectMapper objectMapper;
  private PublicadorEventos publicador;

  @BeforeEach
  void setUp() {
    rabbitTemplate = mock(RabbitTemplate.class);
    productoRepository = mock(ProductoRepository.class);
    categoriaRepository = mock(CategoriaRepository.class);
    objectMapper = new ObjectMapper().findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    publicador = new PublicadorEventos(rabbitTemplate, objectMapper, productoRepository, categoriaRepository);

    Producto fideos = new Producto();
    fideos.setId("7");
    fideos.setNombre("Fideos guiseros");
    fideos.setDescripcion("Paquete de 500 g");
    fideos.setSubCategoriaID("1");
    Categoria alimentos = new Categoria();
    alimentos.setId("1");
    alimentos.setNombre("Alimentos");
    alimentos.setSubcategoriaID("4");
    when(productoRepository.findById("7")).thenReturn(Optional.of(fideos));
    when(categoriaRepository.findById("1")).thenReturn(Optional.of(alimentos));
  }

  private JsonNode mensajeEnviado(String routingKey) throws Exception {
    ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
    verify(rabbitTemplate).send(eq("donatrack.eventos"), eq(routingKey), captor.capture());
    assertEquals("application/json", captor.getValue().getMessageProperties().getContentType());
    return objectMapper.readTree(captor.getValue().getBody());
  }

  private Donacion donacion() {
    Donacion donacion = new Donacion();
    donacion.setId("a3f9");
    donacion.setDonadorID("12");
    donacion.setDepositoID("3");
    donacion.setDescripcion("Fideos para el comedor");
    donacion.setProductoID("7");
    donacion.setCantidad(30);
    donacion.setEstado(EstadoDonacionEnum.ACEPTADA);
    donacion.setFecha(LocalDateTime.of(2026, 11, 2, 14, 0));
    EstadoDonacion ingresada = new EstadoDonacion();
    ingresada.setEstado(EstadoDonacionEnum.INGRESADA);
    ingresada.setTiempo(LocalDateTime.of(2026, 11, 2, 14, 0));
    EstadoDonacion aceptada = new EstadoDonacion();
    aceptada.setEstado(EstadoDonacionEnum.ACEPTADA);
    aceptada.setTiempo(LocalDateTime.of(2026, 11, 3, 10, 15));
    donacion.getHistorialEstados().add(ingresada);
    donacion.getHistorialEstados().add(aceptada);
    return donacion;
  }

  @Test
  void donacionActualizadaPublicaElProductoClasificadoYElHistorial() throws Exception {
    publicador.donacionActualizada(donacion());

    JsonNode evento = mensajeEnviado("donaciones.donacion.actualizada");
    assertEquals("DONACION_ACTUALIZADA", evento.get("tipo").asText());
    assertEquals("donaciones", evento.get("modulo").asText());
    assertEquals("a3f9", evento.get("entidadId").asText());
    assertTrue(evento.get("ocurridoEn").asText().endsWith("Z"));

    JsonNode datos = evento.get("datos");
    assertEquals("12", datos.get("donadorId").asText());
    assertEquals("3", datos.get("depositoId").asText());
    assertEquals("Fideos guiseros", datos.get("producto").get("nombre").asText());
    assertEquals("Alimentos", datos.get("categoria").get("nombre").asText());
    assertEquals("4", datos.get("subcategoria").get("id").asText());
    assertEquals(30, datos.get("cantidad").asInt());
    assertEquals("ACEPTADA", datos.get("estado").asText());
    assertEquals(Evento.enUtc(LocalDateTime.of(2026, 11, 2, 14, 0)).toString(), datos.get("fechaIngreso").asText());
    assertEquals(2, datos.get("historialEstados").size());
    assertEquals("INGRESADA", datos.get("historialEstados").get(0).get("estado").asText());
    assertFalse(datos.get("eliminado").asBoolean());
  }

  @Test
  void productoActualizadoPublicaSuCategoria() throws Exception {
    publicador.productoActualizado(productoRepository.findById("7").orElseThrow());

    JsonNode datos = mensajeEnviado("donaciones.producto.actualizado").get("datos");
    assertEquals("7", datos.get("id").asText());
    assertEquals("Fideos guiseros", datos.get("nombre").asText());
    assertEquals("1", datos.get("categoria").get("id").asText());
    assertEquals("4", datos.get("subcategoria").get("id").asText());
  }

  @Test
  void donacionEliminadaPublicaLaBaja() throws Exception {
    publicador.donacionEliminada(donacion());

    assertTrue(mensajeEnviado("donaciones.donacion.actualizada").get("datos").get("eliminado").asBoolean());
  }

  @Test
  void unProductoSinCategoriaSePublicaIgual() throws Exception {
    Donacion sinProducto = donacion();
    sinProducto.setProductoID("999");

    publicador.donacionActualizada(sinProducto);

    JsonNode datos = mensajeEnviado("donaciones.donacion.actualizada").get("datos");
    assertEquals("999", datos.get("producto").get("id").asText());
    assertTrue(datos.get("categoria").isNull());
  }

  @Test
  void siRabbitFallaLaOperacionNoSeCae() {
    doThrow(new RuntimeException("broker caído")).when(rabbitTemplate).send(anyString(), anyString(), any(Message.class));

    assertDoesNotThrow(() -> publicador.donacionActualizada(donacion()));
  }
}
