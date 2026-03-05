package com.unimagdalena.model;

import java.time.LocalDate;

public class Cliente {
    private int idCliente;
    private String nombreCliente;
    private String tipoDocumentoCliente;
    private LocalDate fechaDeNacimientoCliente;
    private Boolean sexoCliente;
    private String esAccesoVipCliente;
    private Pelicula idPeliculaCliente;
    private String nombreImagenPublicoCliente;
    private String nombreImagenPrivadoCliente;

    public Cliente() {
    }

    public Cliente(int idCliente, String nombreCliente, String tipoDocumentoCliente, LocalDate fechaDeNacimientoCliente, Boolean sexoCliente, String esAccesoVipCliente, Pelicula idPeliculaCliente, String nombreImagenPublicoCliente, String nombreImagenPrivadoCliente) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.tipoDocumentoCliente = tipoDocumentoCliente;
        this.fechaDeNacimientoCliente = fechaDeNacimientoCliente;
        this.sexoCliente = sexoCliente;
        this.esAccesoVipCliente = esAccesoVipCliente;
        this.idPeliculaCliente = idPeliculaCliente;
        this.nombreImagenPublicoCliente = nombreImagenPublicoCliente;
        this.nombreImagenPrivadoCliente = nombreImagenPrivadoCliente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTipoDocumentoCliente() {
        return tipoDocumentoCliente;
    }

    public void setTipoDocumentoCliente(String tipoDocumentoCliente) {
        this.tipoDocumentoCliente = tipoDocumentoCliente;
    }

    public LocalDate getFechaDeNacimientoCliente() {
        return fechaDeNacimientoCliente;
    }

    public void setFechaDeNacimientoCliente(LocalDate fechaDeNacimientoCliente) {
        this.fechaDeNacimientoCliente = fechaDeNacimientoCliente;
    }

    public Boolean getSexoCliente() {
        return sexoCliente;
    }

    public void setSexoCliente(Boolean sexoCliente) {
        this.sexoCliente = sexoCliente;
    }

    public String getEsAccesoVipCliente() {
        return esAccesoVipCliente;
    }

    public void setEsAccesoVipCliente(String esAccesoVipCliente) {
        this.esAccesoVipCliente = esAccesoVipCliente;
    }

    public Pelicula getIdPeliculaCliente() {
        return idPeliculaCliente;
    }

    public void setIdPeliculaCliente(Pelicula idPeliculaCliente) {
        this.idPeliculaCliente = idPeliculaCliente;
    }

    public String getNombreImagenPublicoCliente() {
        return nombreImagenPublicoCliente;
    }

    public void setNombreImagenPublicoCliente(String nombreImagenPublicoCliente) {
        this.nombreImagenPublicoCliente = nombreImagenPublicoCliente;
    }

    public String getNombreImagenPrivadoCliente() {
        return nombreImagenPrivadoCliente;
    }

    public void setNombreImagenPrivadoCliente(String nombreImagenPrivadoCliente) {
        this.nombreImagenPrivadoCliente = nombreImagenPrivadoCliente;
    }

    @Override
    public String toString() {
        return "Cliente{" + "idCliente=" + idCliente + ", nombreCliente=" + nombreCliente + '}';
    }
    
}