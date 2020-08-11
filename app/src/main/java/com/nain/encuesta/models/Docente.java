package com.nain.encuesta.models;

public class Docente {
    private String id;
    private String username;;
    private String descripcion;
    private String image_docente;
    private String puntaje;
    private String total;

    public Docente() {

    }

    public Docente(String id, String username, String descripcion, String image_docente, String puntaje, String total) {
        this.id = id;
        this.username = username;
        this.descripcion = descripcion;
        this.image_docente = image_docente;
        this.puntaje = puntaje;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImage_docente() {
        return image_docente;
    }

    public void setImage_docente(String image_docente) {
        this.image_docente = image_docente;
    }

    public String getPuntaje() { return puntaje; }

    public void setPuntaje(String puntaje) { this.puntaje = puntaje; }

    public String getTotal() { return total; }

    public void setTotal(String total) { this.total = total; }
}
