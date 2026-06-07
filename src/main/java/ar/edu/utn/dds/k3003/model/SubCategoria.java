package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subcategorias")
public class SubCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nombre;
    private String descripcion;

    @Column(name = "categoria_id")
    private String categoriaID;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCategoriaID() { return categoriaID; }
    public void setCategoriaID(String categoriaID) { this.categoriaID = categoriaID; }
}