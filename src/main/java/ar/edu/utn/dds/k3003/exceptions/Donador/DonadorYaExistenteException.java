package ar.edu.utn.dds.k3003.exceptions.Donador;

public class DonadorYaExistenteException extends RuntimeException {
  public DonadorYaExistenteException(String mensaje) {
    super(mensaje);
  }
}
