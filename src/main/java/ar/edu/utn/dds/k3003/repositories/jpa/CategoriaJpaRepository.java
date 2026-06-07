package ar.edu.utn.dds.k3003.repositories.jpa;

import ar.edu.utn.dds.k3003.model.Categoria;
import ar.edu.utn.dds.k3003.repositories.Repository.CategoriaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaJpaRepository
    extends JpaRepository<Categoria, String>, CategoriaRepository {}
