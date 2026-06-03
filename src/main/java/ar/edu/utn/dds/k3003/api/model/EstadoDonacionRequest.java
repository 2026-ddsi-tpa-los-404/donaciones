package ar.edu.utn.dds.k3003.api.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;

public record EstadoDonacionRequest(EstadoDonacionEnum estado) {}