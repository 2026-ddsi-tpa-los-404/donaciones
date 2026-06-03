package ar.edu.utn.dds.k3003.model;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import java.time.LocalDateTime;
import java.util.List;

public class Donacion {
    private String id;
    private String donadorID;
    private String depositoID ;
    private String descripcion;
    private String productoID;
    private Integer cantidad;
    
    private EstadoDonacionEnum estado;
    private LocalDateTime fecha;

    private List<EstadoDonacion> historialEstados;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDonadorID() {
        return donadorID;
    }

    public void setDonadorID(String donadorID) {
        this.donadorID = donadorID;
    }

    public String getDepositoID() {
        return depositoID;
    }

    public void setDepositoID(String depositoID) {
        this.depositoID = depositoID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProductoID() {
        return productoID;
    }

    public void setProductoID(String productoID) {
        this.productoID = productoID;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public EstadoDonacionEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoDonacionEnum estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<EstadoDonacion> getHistorialEstados() {
        return historialEstados;
    }

    public void setHistorialEstados(List<EstadoDonacion> historialEstados) {
        this.historialEstados = historialEstados;
    }
}



