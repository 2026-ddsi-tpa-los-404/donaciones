package ar.edu.utn.dds.k3003.repositories.jpa;

import ar.edu.utn.dds.k3003.model.Identificador;
import ar.edu.utn.dds.k3003.repositories.Repository.IdentificadorRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificadorJpaRepository
    extends JpaRepository<Identificador, String>, IdentificadorRepository {}
