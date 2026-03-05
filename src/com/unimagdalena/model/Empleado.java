package com.unimagdalena.model;

import java.time.LocalTime;

public class Empleado {
    private int idEmpleado;
    private String nombreEmpleado;
    private Boolean esTiempoCompletoEmpleado;
    private String nivelAccesoEmpleado;
    private Sala salaAsignadaEmpleado;
    private LocalTime horaEntradaEmpleado;
    private LocalTime horaSalidaEmpleado;
    private String nombreImagenPublicoEmpleado;
    private String nombreImagenPrivadoEmpleado;

    public Empleado() {
    }

    public Empleado(int idEmpleado, String nombreEmpleado, Boolean esTiempoCompletoEmpleado, String nivelAccesoEmpleado, Sala salaAsignadaEmpleado, LocalTime horaEntradaEmpleado, LocalTime horaSalidaEmpleado, String nombreImagenPublicoEmpleado, String nombreImagenPrivadoEmpleado) {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.esTiempoCompletoEmpleado = esTiempoCompletoEmpleado;
        this.nivelAccesoEmpleado = nivelAccesoEmpleado;
        this.salaAsignadaEmpleado = salaAsignadaEmpleado;
        this.horaEntradaEmpleado = horaEntradaEmpleado;
        this.horaSalidaEmpleado = horaSalidaEmpleado;
        this.nombreImagenPublicoEmpleado = nombreImagenPublicoEmpleado;
        this.nombreImagenPrivadoEmpleado = nombreImagenPrivadoEmpleado;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public Boolean getEsTiempoCompletoEmpleado() {
        return esTiempoCompletoEmpleado;
    }

    public void setEsTiempoCompletoEmpleado(Boolean esTiempoCompletoEmpleado) {
        this.esTiempoCompletoEmpleado = esTiempoCompletoEmpleado;
    }

    public String getNivelAccesoEmpleado() {
        return nivelAccesoEmpleado;
    }

    public void setNivelAccesoEmpleado(String nivelAccesoEmpleado) {
        this.nivelAccesoEmpleado = nivelAccesoEmpleado;
    }

    public Sala getSalaAsignadaEmpleado() {
        return salaAsignadaEmpleado;
    }

    public void setSalaAsignadaEmpleado(Sala salaAsignadaEmpleado) {
        this.salaAsignadaEmpleado = salaAsignadaEmpleado;
    }

    public LocalTime getHoraEntradaEmpleado() {
        return horaEntradaEmpleado;
    }

    public void setHoraEntradaEmpleado(LocalTime horaEntradaEmpleado) {
        this.horaEntradaEmpleado = horaEntradaEmpleado;
    }

    public LocalTime getHoraSalidaEmpleado() {
        return horaSalidaEmpleado;
    }

    public void setHoraSalidaEmpleado(LocalTime horaSalidaEmpleado) {
        this.horaSalidaEmpleado = horaSalidaEmpleado;
    }

    public String getNombreImagenPublicoEmpleado() {
        return nombreImagenPublicoEmpleado;
    }

    public void setNombreImagenPublicoEmpleado(String nombreImagenPublicoEmpleado) {
        this.nombreImagenPublicoEmpleado = nombreImagenPublicoEmpleado;
    }

    public String getNombreImagenPrivadoEmpleado() {
        return nombreImagenPrivadoEmpleado;
    }

    public void setNombreImagenPrivadoEmpleado(String nombreImagenPrivadoEmpleado) {
        this.nombreImagenPrivadoEmpleado = nombreImagenPrivadoEmpleado;
    }

    @Override
    public String toString() {
        return "Empleado{" + "idEmpleado=" + idEmpleado + ", nombreEmpleado=" + nombreEmpleado + '}';
    }
    
}
