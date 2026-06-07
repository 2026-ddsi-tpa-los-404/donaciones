package ar.edu.utn.dds.k3003.repositories.InMemory;

import ar.edu.utn.dds.k3003.model.Donacion;
import ar.edu.utn.dds.k3003.repositories.Repository.DonacionesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryDonacionesRepository implements DonacionesRepository {

  private final List<Donacion> donaciones = new ArrayList<>();
  private final AtomicLong idCounter = new AtomicLong(1);

  @Override
  public Optional<Donacion> findById(String id) {
    return donaciones.stream().filter(d -> d.getId().equals(id)).findFirst();
  }

  @Override
  public Donacion save(Donacion donacion) {
    if (donacion.getId() == null) {
      donacion.setId("donacion" + idCounter.getAndIncrement());
    }
    donaciones.add(donacion);
    return donacion;
  }

  @Override
  public List<Donacion> findAll() {
    return List.copyOf(donaciones);
  }

  @Override
  public void deleteById(String id) {
    donaciones.removeIf(donacion -> donacion.getId().equals(id));
  }

  @Override
  public List<Donacion> findByDonadorID(String donadorID) {
    return donaciones.stream()
        .filter(d -> d.getDonadorID().equals(donadorID))
        .toList();
  }
}
