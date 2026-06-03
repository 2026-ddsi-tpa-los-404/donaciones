package ar.edu.utn.dds.k3003.repositories.InMemory;

import ar.edu.utn.dds.k3003.model.Categoria;
import ar.edu.utn.dds.k3003.repositories.Repository.CategoriaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCategoriaRepository implements CategoriaRepository {

  private final List<Categoria> categorias = new ArrayList<>();
  private final AtomicLong idCounter = new AtomicLong(1);

  @Override
  public Optional<Categoria> findById(String id) {
    return categorias.stream().filter(c -> c.getId().equals(id)).findFirst();
  }

  @Override
  public Categoria save(Categoria categoria) {
    if (categoria.getId() == null) {
      categoria.setId("categoria" + idCounter.getAndIncrement());
    }
    categorias.add(categoria);
    return categoria;
  }

  @Override
  public List<Categoria> findAll() {
    return List.copyOf(categorias);
  }

  @Override
  public void deleteById(String id) {
    categorias.removeIf(c -> c.getId().equals(id));
  }
}
