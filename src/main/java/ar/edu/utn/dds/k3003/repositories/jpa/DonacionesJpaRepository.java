package ar.edu.utn.dds.k3003.repositories.jpa;

import ar.edu.utn.dds.k3003.model.Donacion;
import ar.edu.utn.dds.k3003.repositories.Repository.DonacionesRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonacionesJpaRepository
    extends JpaRepository<Donacion, String>, DonacionesRepository {

  List<Donacion> findByDonadorID(String donadorID);
}
