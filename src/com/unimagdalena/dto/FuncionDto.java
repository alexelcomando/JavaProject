package com.unimagdalena.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class FuncionDto {
    private int idFuncion;
    private String nombreFuncion;
    private SalaDto idSalaFuncion;
    private PeliculaDto idPeliculaFuncion;
    private LocalDate fechaFuncion;
    private LocalTime horaFuncion;
    private Boolean paraMayoresFuncion;
    private String nombreImagenPublicoFuncion;
    private String nombreImagenPrivadoFuncion;

    public FuncionDto() {
    }

    public FuncionDto(int idFuncion, String nombreFuncion, SalaDto idSalaFuncion, PeliculaDto idPeliculaFuncion, LocalDate fechaFuncion, LocalTime horaFuncion, Boolean paraMayoresFuncion, String nombreImagenPublicoFuncion, String nombreImagenPrivadoFuncion) {
        this.idFuncion = idFuncion;
        this.nombreFuncion = nombreFuncion;
        this.idSalaFuncion = idSalaFuncion;
        this.idPeliculaFuncion = idPeliculaFuncion;
        this.fechaFuncion = fechaFuncion;
        this.horaFuncion = horaFuncion;
        this.paraMayoresFuncion = paraMayoresFuncion;
        this.nombreImagenPublicoFuncion = nombreImagenPublicoFuncion;
        this.nombreImagenPrivadoFuncion = nombreImagenPrivadoFuncion;
    }

    public int getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }

    public String getNombreFuncion() {
        return nombreFuncion;
    }

    public void setNombreFuncion(String nombreFuncion) {
        this.nombreFuncion = nombreFuncion;
    }

    public SalaDto getIdSalaFuncion() {
        return idSalaFuncion;
    }

    public void setIdSalaFuncion(SalaDto idSalaFuncion) {
        this.idSalaFuncion = idSalaFuncion;
    }

    public PeliculaDto getIdPeliculaFuncion() {
        return idPeliculaFuncion;
    }

    public void setIdPeliculaFuncion(PeliculaDto idPeliculaFuncion) {
        this.idPeliculaFuncion = idPeliculaFuncion;
    }

    public LocalDate getFechaFuncion() {
        return fechaFuncion;
    }

    public void setFechaFuncion(LocalDate fechaFuncion) {
        this.fechaFuncion = fechaFuncion;
    }

    public LocalTime getHoraFuncion() {
        return horaFuncion;
    }

    public void setHoraFuncion(LocalTime horaFuncion) {
        this.horaFuncion = horaFuncion;
    }

    public Boolean getParaMayoresFuncion() {
        return paraMayoresFuncion;
    }

    public void setParaMayoresFuncion(Boolean paraMayoresFuncion) {
        this.paraMayoresFuncion = paraMayoresFuncion;
    }

    public String getNombreImagenPublicoFuncion() {
        return nombreImagenPublicoFuncion;
    }

    public void setNombreImagenPublicoFuncion(String nombreImagenPublicoFuncion) {
        this.nombreImagenPublicoFuncion = nombreImagenPublicoFuncion;
    }

    public String getNombreImagenPrivadoFuncion() {
        return nombreImagenPrivadoFuncion;
    }

    public void setNombreImagenPrivadoFuncion(String nombreImagenPrivadoFuncion) {
        this.nombreImagenPrivadoFuncion = nombreImagenPrivadoFuncion;
    }

    @Override
    public String toString() {
        return nombreFuncion;
    }
    
}
