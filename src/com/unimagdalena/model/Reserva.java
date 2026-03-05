package com.unimagdalena.model;

import java.time.LocalDate;

public class Reserva {
    private int idReserva;
    private String nombreReserva;
    private String metodoPagoReserva; 
    private LocalDate fechaReserva;
    private Cliente idClienteReserva;
    private Funcion idFuncionReserva;
    private Boolean incluyeBebidasReserva;
    private String nombreImagenPublicoReserva;
    private String nombreImagenPrivadoReserva;

    public Reserva() {
    }

    public Reserva(int idReserva, String nombreReserva, String metodoPagoReserva, LocalDate fechaReserva, Cliente idClienteReserva, Funcion idFuncionReserva, Boolean incluyeBebidasReserva, String nombreImagenPublicoReserva, String nombreImagenPrivadoReserva) {
        this.idReserva = idReserva;
        this.nombreReserva = nombreReserva;
        this.metodoPagoReserva = metodoPagoReserva;
        this.fechaReserva = fechaReserva;
        this.idClienteReserva = idClienteReserva;
        this.idFuncionReserva = idFuncionReserva;
        this.incluyeBebidasReserva = incluyeBebidasReserva;
        this.nombreImagenPublicoReserva = nombreImagenPublicoReserva;
        this.nombreImagenPrivadoReserva = nombreImagenPrivadoReserva;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public String getNombreReserva() {
        return nombreReserva;
    }

    public void setNombreReserva(String nombreReserva) {
        this.nombreReserva = nombreReserva;
    }

    public String getMetodoPagoReserva() {
        return metodoPagoReserva;
    }

    public void setMetodoPagoReserva(String metodoPagoReserva) {
        this.metodoPagoReserva = metodoPagoReserva;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Cliente getIdClienteReserva() {
        return idClienteReserva;
    }

    public void setIdClienteReserva(Cliente idClienteReserva) {
        this.idClienteReserva = idClienteReserva;
    }

    public Funcion getIdFuncionReserva() {
        return idFuncionReserva;
    }

    public void setIdFuncionReserva(Funcion idFuncionReserva) {
        this.idFuncionReserva = idFuncionReserva;
    }

    public Boolean getIncluyeBebidasReserva() {
        return incluyeBebidasReserva;
    }

    public void setIncluyeBebidasReserva(Boolean incluyeBebidasReserva) {
        this.incluyeBebidasReserva = incluyeBebidasReserva;
    }

    public String getNombreImagenPublicoReserva() {
        return nombreImagenPublicoReserva;
    }

    public void setNombreImagenPublicoReserva(String nombreImagenPublicoReserva) {
        this.nombreImagenPublicoReserva = nombreImagenPublicoReserva;
    }

    public String getNombreImagenPrivadoReserva() {
        return nombreImagenPrivadoReserva;
    }

    public void setNombreImagenPrivadoReserva(String nombreImagenPrivadoReserva) {
        this.nombreImagenPrivadoReserva = nombreImagenPrivadoReserva;
    }

    @Override
    public String toString() {
        return "Reserva{" + "idReserva=" + idReserva + ", metodoPagoReserva=" + metodoPagoReserva + '}';
    }
    
}
