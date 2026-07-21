package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.model.generator.SequentialStringIdGenerator;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(generator = "productos_id_gen")
    @GenericGenerator(
        name = "productos_id_gen",
        type = SequentialStringIdGenerator.class,
        parameters = @Parameter(name = "sequence_name", value = "productos_id_seq"))
    private String id;

    private String nombre;
    private String descripcion;

    @Column(name = "sub_categoria_id")
    private String subCategoriaID;

    @Column(name = "identificador_id")
    private String identificadorID;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getSubCategoriaID() { return subCategoriaID; }
    public void setSubCategoriaID(String subCategoriaID) { this.subCategoriaID = subCategoriaID; }
    public String getIdentificadorID() { return identificadorID; }
    public void setIdentificadorID(String identificadorID) { this.identificadorID = identificadorID; }
}