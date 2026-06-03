package ar.edu.utn.dds.k3003.repositories.InMemory;

import ar.edu.utn.dds.k3003.model.Producto;
import ar.edu.utn.dds.k3003.repositories.Repository.ProductoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryProductoRepository implements ProductoRepository {

  private final List<Producto> productos = new ArrayList<>();
  private final AtomicLong idCounter = new AtomicLong(1);

  @Override
  public Optional<Producto> findById(String id) {
    return productos.stream().filter(d -> d.getId().equals(id)).findFirst();
  }

  @Override
  public Producto save(Producto producto) {
    if (producto.getId() == null) {
      producto.setId("producto" + idCounter.getAndIncrement());
    }
    productos.add(producto);
    return producto;
  }

  @Override
  public List<Producto> findAll() {
    return List.copyOf(productos);
  }

  @Override
  public void deleteById(String id) {
    productos.removeIf(producto -> producto.getId().equals(id));
  }
}