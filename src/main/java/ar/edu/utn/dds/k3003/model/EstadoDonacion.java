package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estados_donacion")
public class EstadoDonacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoDonacionEnum estado;

    private LocalDateTime tiempo;

    @ManyToOne
    @JoinColumn(name = "donacion_id")
    private Donacion donacion;

    public Long getId() { return id; }

    public EstadoDonacionEnum getEstado() { return estado; }
    public void setEstado(EstadoDonacionEnum estado) { this.estado = estado; }

    public LocalDateTime getTiempo() { return tiempo; }
    public void setTiempo(LocalDateTime tiempo) { this.tiempo = tiempo; }

    public Donacion getDonacion() { return donacion; }
    public void setDonacion(Donacion donacion) { this.donacion = donacion; }
}