package ar.edu.utn.dds.k3003.eventos;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

// servicio de consultas
public record Evento<T>(String eventoId, String tipo, String modulo,
                        String entidadId, Instant ocurridoEn, T datos) {

  public static <T> Evento<T> de(String tipo, String modulo, String entidadId, T datos) {
    return new Evento<>(UUID.randomUUID().toString(), tipo, modulo, entidadId,
        Instant.now().truncatedTo(ChronoUnit.MILLIS), datos);
  }

  public static Instant enUtc(LocalDateTime fecha) {
    return fecha == null ? null : fecha.atZone(ZoneId.systemDefault()).toInstant();
  }
}
