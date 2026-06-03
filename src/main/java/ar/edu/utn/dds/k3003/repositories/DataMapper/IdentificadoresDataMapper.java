package ar.edu.utn.dds.k3003.repositories.DataMapper;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.IdentificadorDTO;
import ar.edu.utn.dds.k3003.model.Identificador;

public class IdentificadoresDataMapper {

  public IdentificadorDTO toIdentificadorDTO(Identificador identificador) {
    return new IdentificadorDTO(
        identificador.getId(),
        identificador.getTipo(),
        identificador.getDescripcion());
  }

  public Identificador toIdentificador(IdentificadorDTO dto) {
    Identificador identificador = new Identificador();
    identificador.setTipo(dto.tipo());
    identificador.setDescripcion(dto.descripcion());
    return identificador;
  }
}
