package ar.edu.utn.dds.k3003.api.controller;

import ar.edu.utn.dds.k3003.exceptions.DonacionNoEncontradaException;
import ar.edu.utn.dds.k3003.exceptions.DonadorNoPuedeDonarException;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private final MeterRegistry meterRegistry;

  public GlobalExceptionHandler(MeterRegistry meterRegistry) {
    this.meterRegistry = meterRegistry;
  }

  @ExceptionHandler({DonacionNoEncontradaException.class, NoSuchElementException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleNotFound(RuntimeException e) {
    meterRegistry.counter("donaciones.errores", "tipo", "not_found").increment();
    return e.getMessage();
  }

  @ExceptionHandler(DonadorNoPuedeDonarException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public String handleNoPuedeDonar(DonadorNoPuedeDonarException e) {
    meterRegistry.counter("donaciones.errores", "tipo", "donador_no_habilitado").increment();
    return e.getMessage();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleBadRequest(IllegalArgumentException e) {
    meterRegistry.counter("donaciones.errores", "tipo", "bad_request").increment();
    return e.getMessage();
  }
}
