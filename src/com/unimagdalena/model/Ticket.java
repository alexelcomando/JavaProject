package com.unimagdalena.model;

import java.time.LocalDate;

public class Ticket {
    private int idTicket;
    private String tipoEntradaTicket;
    private Cliente idClienteTicket;
    private Boolean esValidoTicket;
    private LocalDate fechaEmisionTicket;
    private Double precioTicket;
    private String nombreImagenPublicoTicket;
    private String nombreImagenPrivadoTicket;

    public Ticket() {
    }

    public Ticket(int idTicket, String tipoEntradaTicket, Cliente idClienteTicket, Boolean esValidoTicket, LocalDate fechaEmisionTicket, Double precioTicket, String nombreImagenPublicoTicket, String nombreImagenPrivadoTicket) {
        this.idTicket = idTicket;
        this.tipoEntradaTicket = tipoEntradaTicket;
        this.idClienteTicket = idClienteTicket;
        this.esValidoTicket = esValidoTicket;
        this.fechaEmisionTicket = fechaEmisionTicket;
        this.precioTicket = precioTicket;
        this.nombreImagenPublicoTicket = nombreImagenPublicoTicket;
        this.nombreImagenPrivadoTicket = nombreImagenPrivadoTicket;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public String getTipoEntradaTicket() {
        return tipoEntradaTicket;
    }

    public void setTipoEntradaTicket(String tipoEntradaTicket) {
        this.tipoEntradaTicket = tipoEntradaTicket;
    }

    public Cliente getIdClienteTicket() {
        return idClienteTicket;
    }

    public void setIdClienteTicket(Cliente idClienteTicket) {
        this.idClienteTicket = idClienteTicket;
    }

    public Boolean getEsValidoTicket() {
        return esValidoTicket;
    }

    public void setEsValidoTicket(Boolean esValidoTicket) {
        this.esValidoTicket = esValidoTicket;
    }

    public LocalDate getFechaEmisionTicket() {
        return fechaEmisionTicket;
    }

    public void setFechaEmisionTicket(LocalDate fechaEmisionTicket) {
        this.fechaEmisionTicket = fechaEmisionTicket;
    }

    public Double getPrecioTicket() {
        return precioTicket;
    }

    public void setPrecioTicket(Double precioTicket) {
        this.precioTicket = precioTicket;
    }

    public String getNombreImagenPublicoTicket() {
        return nombreImagenPublicoTicket;
    }

    public void setNombreImagenPublicoTicket(String nombreImagenPublicoTicket) {
        this.nombreImagenPublicoTicket = nombreImagenPublicoTicket;
    }

    public String getNombreImagenPrivadoTicket() {
        return nombreImagenPrivadoTicket;
    }

    public void setNombreImagenPrivadoTicket(String nombreImagenPrivadoTicket) {
        this.nombreImagenPrivadoTicket = nombreImagenPrivadoTicket;
    }

    @Override
    public String toString() {
        return "Ticket{" + "idTicket=" + idTicket + ", tipoEntradaTicket=" + tipoEntradaTicket + '}';
    }
   
}
