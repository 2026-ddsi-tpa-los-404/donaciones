package ar.edu.utn.dds.k3003.integration;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.logistica.AsignacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.logistica.DepositoDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.logistica.PaqueteDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.logistica.TipoAlgoritmoEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaLogistica;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class LogisticaRestClient implements FachadaLogistica {

  private final RestTemplate restTemplate;
  private final String baseUrl;

  public LogisticaRestClient(RestTemplate restTemplate, String baseUrl) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
  }

  @Override
  public DepositoDTO agregarDeposito(DepositoDTO deposito) {
    return restTemplate.postForObject(baseUrl + "/depositos", deposito, DepositoDTO.class);
  }

  @Override
  public DepositoDTO buscarDepositoPorID(String depositoID) throws NoSuchElementException {
    try {
      return restTemplate.getForObject(baseUrl + "/depositos/" + depositoID, DepositoDTO.class);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NoSuchElementException("Deposito no encontrado: " + depositoID);
    }
  }

  @Override
  public AsignacionDTO buscarAsignacionPorPaqueteID(String paqueteID)
      throws NoSuchElementException {
    try {
      return restTemplate.getForObject(
          baseUrl + "/paquetes/" + paqueteID + "/asignacion", AsignacionDTO.class);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NoSuchElementException("Asignacion no encontrada para paquete: " + paqueteID);
    }
  }

  @Override
  public DepositoDTO gestionarDonacion(
      String depositoID, String donacionID, String productoID, Integer cantidad)
      throws NoSuchElementException {
    Map<String, Object> body = new HashMap<>();
    body.put("id", donacionID);
    body.put("productoID", productoID);
    body.put("cantidad", cantidad);
    try {
      return restTemplate.postForObject(
          baseUrl + "/depositos/" + depositoID + "/donacion", body, DepositoDTO.class);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NoSuchElementException("Deposito no encontrado: " + depositoID);
    }
  }

  @Override
  public void setAlgoritmoMM(String depositoID, TipoAlgoritmoEnum tipoAlgoritmo) {
    HttpEntity<Map<String, String>> entity =
        new HttpEntity<>(Map.of("algoritmo", tipoAlgoritmo.name()));
    restTemplate.exchange(
        baseUrl + "/depositos/" + depositoID + "/algoritmo",
        HttpMethod.PATCH,
        entity,
        Void.class);
  }

  @Override
  public AsignacionDTO ejecutarMatchmaking(
      String depositoID, PaqueteDTO paqueteDTO, List<NecesidadMaterialDTO> necesidades) {
    Map<String, Object> body = new HashMap<>();
    body.put("paquete", paqueteDTO);
    body.put("necesidades", necesidades);
    return restTemplate.postForObject(
        baseUrl + "/depositos/" + depositoID + "/matchmaking", body, AsignacionDTO.class);
  }

  @Override
  public void reportarEntrega(PaqueteDTO paqueteDTO) {
    restTemplate.postForObject(baseUrl + "/entregas", paqueteDTO, Void.class);
  }

  @Override
  public void setFachadaDonadoresYEntidades(
      FachadaDonadoresYEntidades fachadaDonadoresYEntidades) {
    // no-op: solo relevante en el modulo Logistica
  }

  @Override
  public void setFachadaDonaciones(FachadaDonaciones fachadaDonaciones) {
    // no-op: solo relevante en el modulo Logistica
  }
}
