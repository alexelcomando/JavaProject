package com.unimagdalena.model;

import java.time.LocalDate;

public class Pelicula {
    private int idPelicula;
    private String nombrePelicula;
    private LocalDate fechaEstrenoPelicula;
    private Genero idGeneroPelicula;
    private Boolean es3dPelicula;
    private String clasificacionEdadPelicula;
    private String nombreImagenPublicoPelicula;
    private String nombreImagenPrivadoPelicula;

    public Pelicula() {
    }

    public Pelicula(int idPelicula, String nombrePelicula, LocalDate fechaEstrenoPelicula, Genero idGeneroPelicula, Boolean es3dPelicula, String clasificacionEdadPelicula, String nombreImagenPublicoPelicula, String nombreImagenPrivadoPelicula) {
        this.idPelicula = idPelicula;
        this.nombrePelicula = nombrePelicula;
        this.fechaEstrenoPelicula = fechaEstrenoPelicula;
        this.idGeneroPelicula = idGeneroPelicula;
        this.es3dPelicula = es3dPelicula;
        this.clasificacionEdadPelicula = clasificacionEdadPelicula;
        this.nombreImagenPublicoPelicula = nombreImagenPublicoPelicula;
        this.nombreImagenPrivadoPelicula = nombreImagenPrivadoPelicula;
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getNombrePelicula() {
        return nombrePelicula;
    }

    public void setNombrePelicula(String nombrePelicula) {
        this.nombrePelicula = nombrePelicula;
    }

    public LocalDate getFechaEstrenoPelicula() {
        return fechaEstrenoPelicula;
    }

    public void setFechaEstrenoPelicula(LocalDate fechaEstrenoPelicula) {
        this.fechaEstrenoPelicula = fechaEstrenoPelicula;
    }

    public Genero getIdGeneroPelicula() {
        return idGeneroPelicula;
    }

    public void setIdGeneroPelicula(Genero idGeneroPelicula) {
        this.idGeneroPelicula = idGeneroPelicula;
    }

    public Boolean getEs3dPelicula() {
        return es3dPelicula;
    }

    public void setEs3dPelicula(Boolean es3dPelicula) {
        this.es3dPelicula = es3dPelicula;
    }

    public String getClasificacionEdadPelicula() {
        return clasificacionEdadPelicula;
    }

    public void setClasificacionEdadPelicula(String clasificacionEdadPelicula) {
        this.clasificacionEdadPelicula = clasificacionEdadPelicula;
    }

    public String getNombreImagenPublicoPelicula() {
        return nombreImagenPublicoPelicula;
    }

    public void setNombreImagenPublicoPelicula(String nombreImagenPublicoPelicula) {
        this.nombreImagenPublicoPelicula = nombreImagenPublicoPelicula;
    }

    public String getNombreImagenPrivadoPelicula() {
        return nombreImagenPrivadoPelicula;
    }

    public void setNombreImagenPrivadoPelicula(String nombreImagenPrivadoPelicula) {
        this.nombreImagenPrivadoPelicula = nombreImagenPrivadoPelicula;
    }

    @Override
    public String toString() {
        return "Pelicula{" + "idPelicula=" + idPelicula + ", nombrePelicula=" + nombrePelicula + '}';
    }
    
}
