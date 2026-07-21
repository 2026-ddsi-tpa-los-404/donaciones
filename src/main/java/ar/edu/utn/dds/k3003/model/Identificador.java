package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.TipoIdentificadorEnum;
import ar.edu.utn.dds.k3003.model.generator.SequentialStringIdGenerator;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "identificadores")
public class Identificador {

    @Id
    @GeneratedValue(generator = "identificadores_id_gen")
    @GenericGenerator(
        name = "identificadores_id_gen",
        type = SequentialStringIdGenerator.class,
        parameters = @Parameter(name = "sequence_name", value = "identificadores_id_seq"))
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