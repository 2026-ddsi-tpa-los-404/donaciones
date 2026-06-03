package ar.edu.utn.dds.k3003.repositories.Repository;

import ar.edu.utn.dds.k3003.model.Donacion;
import java.util.List;
import java.util.Optional;

public interface DonacionesRepository {

  Optional<Donacion> findById(String id);

  Donacion save(Donacion donacion);

  List<Donacion> findAll();

  void deleteById(String id);
}
