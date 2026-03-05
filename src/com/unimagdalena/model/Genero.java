package com.unimagdalena.model;

import java.time.LocalDate;

public class Genero {
    private int idGenero;
    private String nombreGenero;
    private Boolean estadoGenero;
    private int cantPeliculasGenero;
    private int calificacionGenero;
    private LocalDate fechaCreacionGenero;
    private String publicoObjetivoGenero;
    private String nombreImagenPublicoGenero;
    private String nombreImagenPrivadoGenero;

    public Genero() {
    }

    public Genero(int idGenero, String nombreGenero, Boolean estadoGenero, int cantPeliculasGenero, int calificacionGenero, LocalDate fechaCreacionGenero, String publicoObjetivoGenero, String nombreImagenPublicoGenero, String nombreImagenPrivadoGenero) {
        this.idGenero = idGenero;
        this.nombreGenero = nombreGenero;
        this.estadoGenero = estadoGenero;
        this.cantPeliculasGenero = cantPeliculasGenero;
        this.calificacionGenero = calificacionGenero;
        this.fechaCreacionGenero = fechaCreacionGenero;
        this.publicoObjetivoGenero = publicoObjetivoGenero;
        this.nombreImagenPublicoGenero = nombreImagenPublicoGenero;
        this.nombreImagenPrivadoGenero = nombreImagenPrivadoGenero;
    }

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public String getNombreGenero() {
        return nombreGenero;
    }

    public void setNombreGenero(String nombreGenero) {
        this.nombreGenero = nombreGenero;
    }

    public Boolean getEstadoGenero() {
        return estadoGenero;
    }

    public void setEstadoGenero(Boolean estadoGenero) {
        this.estadoGenero = estadoGenero;
    }

    public int getCantPeliculasGenero() {
        return cantPeliculasGenero;
    }

    public void setCantPeliculasGenero(int cantPeliculasGenero) {
        this.cantPeliculasGenero = cantPeliculasGenero;
    }

    public int getCalificacionGenero() {
        return calificacionGenero;
    }

    public void setCalificacionGenero(int calificacionGenero) {
        this.calificacionGenero = calificacionGenero;
    }

    public LocalDate getFechaCreacionGenero() {
        return fechaCreacionGenero;
    }

    public void setFechaCreacionGenero(LocalDate fechaCreacionGenero) {
        this.fechaCreacionGenero = fechaCreacionGenero;
    }

    public String getPublicoObjetivoGenero() {
        return publicoObjetivoGenero;
    }

    public void setPublicoObjetivoGenero(String publicoObjetivoGenero) {
        this.publicoObjetivoGenero = publicoObjetivoGenero;
    }

    public String getNombreImagenPublicoGenero() {
        return nombreImagenPublicoGenero;
    }

    public void setNombreImagenPublicoGenero(String nombreImagenPublicoGenero) {
        this.nombreImagenPublicoGenero = nombreImagenPublicoGenero;
    }

    public String getNombreImagenPrivadoGenero() {
        return nombreImagenPrivadoGenero;
    }

    public void setNombreImagenPrivadoGenero(String nombreImagenPrivadoGenero) {
        this.nombreImagenPrivadoGenero = nombreImagenPrivadoGenero;
    }

    
    @Override
    public String toString() {
        return "Genero{" + "idGenero=" + idGenero + ", nombreGenero=" + nombreGenero + '}';
    }
    
}
