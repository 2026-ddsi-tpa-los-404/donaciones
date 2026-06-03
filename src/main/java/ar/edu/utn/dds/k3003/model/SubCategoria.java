package ar.edu.utn.dds.k3003.model;

public class SubCategoria {
    private String id;
    private String nombre;
    private String descripcion;
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
