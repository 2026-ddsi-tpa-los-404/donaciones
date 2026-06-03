package ar.edu.utn.dds.k3003.repositories.Repository;

import ar.edu.utn.dds.k3003.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {

  Optional<Categoria> findById(String id);

  Categoria save(Categoria categoria);

  List<Categoria> findAll();

  void deleteById(String id);
}
