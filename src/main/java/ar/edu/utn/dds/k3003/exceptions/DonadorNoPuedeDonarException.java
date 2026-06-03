package ar.edu.utn.dds.k3003.exceptions;

public class DonadorNoPuedeDonarException extends RuntimeException {

  public DonadorNoPuedeDonarException(String message) {
    super(message);
  }
}