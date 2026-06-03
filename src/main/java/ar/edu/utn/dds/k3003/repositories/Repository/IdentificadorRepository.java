package ar.edu.utn.dds.k3003.repositories.Repository;

import ar.edu.utn.dds.k3003.model.Identificador;
import java.util.List;
import java.util.Optional;

public interface IdentificadorRepository {

  Optional<Identificador> findById(String id);

  Identificador save(Identificador identificador);

  List<Identificador> findAll();

  void deleteById(String id);
}
