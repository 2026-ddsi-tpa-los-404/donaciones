package ar.edu.utn.dds.k3003.repositories.InMemory;

import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.repositories.Repository.DonadoresRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
public class InMemoryDonadoresRepo implements DonadoresRepository {

  private List<Donador> donadores;
  private AtomicLong idSecuencial = new AtomicLong(1);

  public InMemoryDonadoresRepo() {
    this.donadores = new ArrayList<>();
  }

  @Override
  public Optional<Donador> findById(String id) {
    return this.donadores.stream().filter(d -> d.getId().equals(id)).findFirst();
  }

  @Override
  public Donador save(Donador donador) {
    Donador donadorConID = donador;
    donadorConID.setId(String.valueOf(idSecuencial.getAndIncrement()));

    this.donadores.add(donadorConID);
    return this.findById(donadorConID.getId()).get();
  }

  @Override
  public Donador deleteById(String id) {
    Optional<Donador> donador = this.findById(id);
    this.donadores.remove(donador.get());
    return donador.get();
  }
}
