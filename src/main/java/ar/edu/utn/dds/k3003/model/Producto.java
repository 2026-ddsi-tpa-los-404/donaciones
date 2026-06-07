package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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