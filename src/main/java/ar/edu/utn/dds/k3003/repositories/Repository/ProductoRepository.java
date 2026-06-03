package ar.edu.utn.dds.k3003.repositories.Repository;

import ar.edu.utn.dds.k3003.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository {

  Optional<Producto> findById(String id);

  Producto save(Producto producto);

  List<Producto> findAll();

  void deleteById(String id);
}
