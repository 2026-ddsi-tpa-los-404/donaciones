package ar.edu.utn.dds.k3003.repositories.InMemory;

import ar.edu.utn.dds.k3003.model.Identificador;
import ar.edu.utn.dds.k3003.repositories.Repository.IdentificadorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryIdentificadorRepository implements IdentificadorRepository {

  private final List<Identificador> identificadores = new ArrayList<>();
  private final AtomicLong idCounter = new AtomicLong(1);

  @Override
  public Optional<Identificador> findById(String id) {
    return identificadores.stream().filter(d -> d.getId().equals(id)).findFirst();
  }

  @Override
  public Identificador save(Identificador identificador) {
    if (identificador.getId() == null) {
      identificador.setId("identificador" + idCounter.getAndIncrement());
    }
    identificadores.add(identificador);
    return identificador;
  }

  @Override
  public List<Identificador> findAll() {
    return List.copyOf(identificadores);
  }

  @Override
  public void deleteById(String id) {
    identificadores.removeIf(i -> i.getId().equals(id));
  }
}