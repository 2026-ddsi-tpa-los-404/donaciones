package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.TipoIdentificadorEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "identificadores")
public class Identificador {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private TipoIdentificadorEnum tipo;

    private String descripcion;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public TipoIdentificadorEnum getTipo() { return tipo; }
    public void setTipo(TipoIdentificadorEnum tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}