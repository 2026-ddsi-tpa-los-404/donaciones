package ar.edu.utn.dds.k3003.eventos;

import ar.edu.utn.dds.k3003.config.RabbitMQConfig;
import ar.edu.utn.dds.k3003.model.Donacion;
import ar.edu.utn.dds.k3003.model.Producto;
import ar.edu.utn.dds.k3003.repositories.Repository.CategoriaRepository;
import ar.edu.utn.dds.k3003.repositories.Repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

// El que manda el mensaje al servicio de consultas
@Component
public class PublicadorEventos {

  private static final Logger log = LoggerFactory.getLogger(PublicadorEventos.class);
  private static final String MODULO = "donaciones";

  private final RabbitTemplate rabbitTemplate;
  private final ObjectMapper objectMapper;
  private final ProductoRepository productoRepository;
  private final CategoriaRepository categoriaRepository;

  public PublicadorEventos(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper,
                           ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
    this.rabbitTemplate = rabbitTemplate;
    this.objectMapper = objectMapper;
    this.productoRepository = productoRepository;
    this.categoriaRepository = categoriaRepository;
  }

  public void donacionActualizada(Donacion donacion) {
    publicarDonacion(donacion, false);
  }

  public void donacionEliminada(Donacion donacion) {
    publicarDonacion(donacion, true);
  }

  public void productoActualizado(Producto producto) {
    publicarProducto(producto, false);
  }

  public void productoEliminado(Producto producto) {
    publicarProducto(producto, true);
  }

  private void publicarDonacion(Donacion donacion, boolean eliminado) {
    try {
      Producto producto = donacion.getProductoID() != null
          ? productoRepository.findById(donacion.getProductoID()).orElse(null)
          : null;
      DonacionActualizada datos = DonacionActualizada.de(donacion, producto, clasificacionDe(producto), eliminado);
      enviar("donaciones.donacion.actualizada", Evento.de("DONACION_ACTUALIZADA", MODULO, donacion.getId(), datos));
    } catch (Exception e) {
      log.error("No se pudo armar el evento DONACION_ACTUALIZADA de {}: {}", donacion.getId(), e.getMessage());
    }
  }

  private void publicarProducto(Producto producto, boolean eliminado) {
    try {
      ProductoActualizado datos = ProductoActualizado.de(producto, clasificacionDe(producto), eliminado);
      enviar("donaciones.producto.actualizado", Evento.de("PRODUCTO_ACTUALIZADO", MODULO, producto.getId(), datos));
    } catch (Exception e) {
      log.error("No se pudo armar el evento PRODUCTO_ACTUALIZADO de {}: {}", producto.getId(), e.getMessage());
    }
  }

  private Clasificacion clasificacionDe(Producto producto) {
    if (producto == null || producto.getSubCategoriaID() == null) {
      return Clasificacion.DESCONOCIDA;
    }
    return categoriaRepository.findById(producto.getSubCategoriaID())
        .map(Clasificacion::de)
        .orElse(Clasificacion.DESCONOCIDA);
  }

  private void enviar(String routingKey, Evento<?> evento) {
    try {
      MessageProperties propiedades = new MessageProperties();
      propiedades.setContentType(MessageProperties.CONTENT_TYPE_JSON);
      propiedades.setMessageId(evento.eventoId());
      Message mensaje = new Message(objectMapper.writeValueAsBytes(evento), propiedades);

      rabbitTemplate.send(RabbitMQConfig.EXCHANGE_EVENTOS, routingKey, mensaje);
      log.info("Evento {} publicado para la entidad {} (eventoId {})", evento.tipo(), evento.entidadId(), evento.eventoId());
    } catch (Exception e) {
      log.error("No se pudo publicar el evento {} de la entidad {}: {}. Se corrige republicando.",
          evento.tipo(), evento.entidadId(), e.getMessage());
    }
  }
}
