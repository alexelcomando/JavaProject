package com.unimagdalena.dto;

import com.unimagdalena.model.Pelicula;

public class SalaDto {
    private int idSala;
    private String nombreSala;
    private Pelicula idPeliculaSala; 
    private Boolean estaDisponibleSala;
    private int numeroAsientosSala;
    private Boolean servicioVipSala;
    private String nombreImagenPublicoSala;
    private String nombreImagenPrivadoSala;

    public SalaDto() {
    }

    public SalaDto(int idSala, String nombreSala, Pelicula idPeliculaSala, Boolean estaDisponibleSala, 
                   int numeroAsientosSala, Boolean servicioVipSala, String nombreImagenPublicoSala, 
                   String nombreImagenPrivadoSala) {
        this.idSala = idSala;
        this.nombreSala = nombreSala;
        this.idPeliculaSala = idPeliculaSala;
        this.estaDisponibleSala = estaDisponibleSala;
        this.numeroAsientosSala = numeroAsientosSala;
        this.servicioVipSala = servicioVipSala;
        this.nombreImagenPublicoSala = nombreImagenPublicoSala;
        this.nombreImagenPrivadoSala = nombreImagenPrivadoSala;
    }

    // Getters y Setters
    public int getIdSala() { return idSala; }
    public void setIdSala(int idSala) { this.idSala = idSala; }
    
    public String getNombreSala() { return nombreSala; }
    public void setNombreSala(String nombreSala) { this.nombreSala = nombreSala; }
    
    public Pelicula getIdPeliculaSala() { return idPeliculaSala; } 
    public void setIdPeliculaSala(Pelicula idPeliculaSala) { 
        this.idPeliculaSala = idPeliculaSala; 
    }
    
    public Boolean getEstaDisponibleSala() { return estaDisponibleSala; }
    public void setEstaDisponibleSala(Boolean estaDisponibleSala) { 
        this.estaDisponibleSala = estaDisponibleSala; 
    }
    
    public int getNumeroAsientosSala() { return numeroAsientosSala; }
    public void setNumeroAsientosSala(int numeroAsientosSala) { 
        this.numeroAsientosSala = numeroAsientosSala; 
    }
    
    public Boolean getServicioVipSala() { return servicioVipSala; }
    public void setServicioVipSala(Boolean servicioVipSala) { 
        this.servicioVipSala = servicioVipSala; 
    }
    
    public String getNombreImagenPublicoSala() { return nombreImagenPublicoSala; }
    public void setNombreImagenPublicoSala(String nombreImagenPublicoSala) { 
        this.nombreImagenPublicoSala = nombreImagenPublicoSala; 
    }
    
    public String getNombreImagenPrivadoSala() { return nombreImagenPrivadoSala; }
    public void setNombreImagenPrivadoSala(String nombreImagenPrivadoSala) { 
        this.nombreImagenPrivadoSala = nombreImagenPrivadoSala; 
    }

    @Override
    public String toString() {
        return nombreSala;
    }
}