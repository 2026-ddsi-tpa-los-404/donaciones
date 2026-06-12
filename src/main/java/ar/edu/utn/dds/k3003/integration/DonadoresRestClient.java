package ar.edu.utn.dds.k3003.integration;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorStatsDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class DonadoresRestClient implements FachadaDonadoresYEntidades {

  private final RestTemplate restTemplate;
  private final String baseUrl;

  public DonadoresRestClient(RestTemplate restTemplate, String baseUrl) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
  }

  @Override
  public DonadorDTO agregarDonador(DonadorDTO donadorDTO) {
    return restTemplate.postForObject(baseUrl + "/donadores", donadorDTO, DonadorDTO.class);
  }

  @Override
  public DonadorDTO buscarDonadorPorID(String donadorID) throws NoSuchElementException {
    try {
      return restTemplate.getForObject(baseUrl + "/donadores/" + donadorID, DonadorDTO.class);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NoSuchElementException("Donador no encontrado: " + donadorID);
    }
  }

  @Override
  public EntidadBeneficaDTO agregarEntidad(EntidadBeneficaDTO entidadBeneficaDTO) {
    return restTemplate.postForObject(
        baseUrl + "/entidades", entidadBeneficaDTO, EntidadBeneficaDTO.class);
  }

  @Override
  public EntidadBeneficaDTO buscarEntidadPorID(String entidadID) throws NoSuchElementException {
    try {
      return restTemplate.getForObject(
          baseUrl + "/entidades/" + entidadID, EntidadBeneficaDTO.class);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NoSuchElementException("Entidad no encontrada: " + entidadID);
    }
  }

  @Override
  public NecesidadMaterialDTO registrarNecesidad(NecesidadMaterialDTO necesidadMaterialDTO) {
    return restTemplate.postForObject(
        baseUrl + "/necesidades", necesidadMaterialDTO, NecesidadMaterialDTO.class);
  }

  @Override
  public QuejaDTO agregarQueja(QuejaDTO quejaDTO) throws NoSuchElementException {
    try {
      return restTemplate.postForObject(baseUrl + "/quejas", quejaDTO, QuejaDTO.class);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NoSuchElementException("Donador no encontrado al registrar queja");
    }
  }

  @Override
  public Boolean puedeDonar(String donadorID) throws NoSuchElementException {
    try {
      return restTemplate.getForObject(
          baseUrl + "/donadores/" + donadorID + "/puede-donar", Boolean.class);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NoSuchElementException("Donador no encontrado: " + donadorID);
    }
  }

  @Override
  public List<QuejaDTO> obtenerQuejasDe(String donadorID) throws NoSuchElementException {
    try {
      ResponseEntity<List<QuejaDTO>> response =
          restTemplate.exchange(
              baseUrl + "/donadores/" + donadorID + "/quejas",
              HttpMethod.GET,
              null,
              new ParameterizedTypeReference<>() {});
      return response.getBody();
    } catch (HttpClientErrorException.NotFound e) {
      throw new NoSuchElementException("Donador no encontrado: " + donadorID);
    }
  }

  @Override
  public DonadorDTO modificarEstado(String donadorID, EstadoDonadorEnum estado)
      throws NoSuchElementException {
    try {
      HttpEntity<Map<String, String>> entity = new HttpEntity<>(Map.of("estado", estado.name()));
      ResponseEntity<DonadorDTO> response =
          restTemplate.exchange(
              baseUrl + "/donadores/" + donadorID + "/estado",
              HttpMethod.PATCH,
              entity,
              DonadorDTO.class);
      return response.getBody();
    } catch (HttpClientErrorException.NotFound e) {
      throw new NoSuchElementException("Donador no encontrado: " + donadorID);
    }
  }

  @Override
  public DonadorDTO modifcarCategoria(String donadorID, String categoria)
      throws NoSuchElementException {
    try {
      HttpEntity<Map<String, String>> entity = new HttpEntity<>(Map.of("categoria", categoria));
      ResponseEntity<DonadorDTO> response =
          restTemplate.exchange(
              baseUrl + "/donadores/" + donadorID + "/categoria",
              HttpMethod.PATCH,
              entity,
              DonadorDTO.class);
      return response.getBody();
    } catch (HttpClientErrorException.NotFound e) {
      throw new NoSuchElementException("Donador no encontrado: " + donadorID);
    }
  }

  @Override
  public List<NecesidadMaterialDTO> obtenerNecesidadesInsatisfechasDe(
      String productoSolicitadoID) {
    ResponseEntity<List<NecesidadMaterialDTO>> response =
        restTemplate.exchange(
            baseUrl + "/necesidades?productoSolicitadoID=" + productoSolicitadoID,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
    return response.getBody();
  }

  @Override
  public NecesidadMaterialDTO satisfacerNecesidad(String necesidadID, Integer cantidad)
      throws NoSuchElementException {
    try {
      HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(Map.of("cantidad", cantidad));
      ResponseEntity<NecesidadMaterialDTO> response =
          restTemplate.exchange(
              baseUrl + "/necesidades/" + necesidadID + "/satisfacer",
              HttpMethod.PATCH,
              entity,
              NecesidadMaterialDTO.class);
      return response.getBody();
    } catch (HttpClientErrorException.NotFound e) {
      throw new NoSuchElementException("Necesidad no encontrada: " + necesidadID);
    }
  }

  @Override
  public DonadorStatsDTO estadisticasDonador(String donadorID) {
    try {
      return restTemplate.getForObject(
          baseUrl + "/donadores/" + donadorID + "/estadisticas", DonadorStatsDTO.class);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NoSuchElementException("Donador no encontrado: " + donadorID);
    }
  }

  @Override
  public void setFachadaIncentivos(FachadaIncentivos fachadaIncentivos) {
    // no-op: solo relevante en el modulo DonadoresYEntidades
  }
}
