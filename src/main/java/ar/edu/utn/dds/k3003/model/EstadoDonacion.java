package ar.edu.utn.dds.k3003.model;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import java.time.LocalDateTime;

public class EstadoDonacion {
    private EstadoDonacionEnum estado;
    private LocalDateTime tiempo;

    public EstadoDonacionEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoDonacionEnum estado) {
        this.estado = estado;
    }

    public void setTiempo(LocalDateTime tiempo) {
        this.tiempo = tiempo;
    }
}
