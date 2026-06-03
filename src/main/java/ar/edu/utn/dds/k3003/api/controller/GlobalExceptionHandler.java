package ar.edu.utn.dds.k3003.api.controller;

import ar.edu.utn.dds.k3003.exceptions.DonacionNoEncontradaException;
import ar.edu.utn.dds.k3003.exceptions.DonadorNoPuedeDonarException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({DonacionNoEncontradaException.class, NoSuchElementException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleNotFound(RuntimeException e) {
    return e.getMessage();
  }

  @ExceptionHandler(DonadorNoPuedeDonarException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public String handleNoPuedeDonar(DonadorNoPuedeDonarException e) {
    return e.getMessage();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleBadRequest(IllegalArgumentException e) {
    return e.getMessage();
  }
}