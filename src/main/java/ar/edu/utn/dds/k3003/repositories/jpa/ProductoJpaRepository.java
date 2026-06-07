package ar.edu.utn.dds.k3003.repositories.jpa;

import ar.edu.utn.dds.k3003.model.Producto;
import ar.edu.utn.dds.k3003.repositories.Repository.ProductoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoJpaRepository
    extends JpaRepository<Producto, String>, ProductoRepository {}
