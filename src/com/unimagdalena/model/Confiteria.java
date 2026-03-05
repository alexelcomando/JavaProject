package com.unimagdalena.model;

import java.time.LocalDate;

public class Confiteria {
    private int idConfiteria;
    private String nombreProductoConfiteria;
    private Double precioProductoConfiteria;
    private String tipoProductoConfiteria; 
    private Boolean productoDisponibleConfiteria;   
    private LocalDate fechaCompraProducto;
    private String metodoPagoProducto;
    private String nombreImagenPublicoConfiteria;
    private String nombreImagenPrivadoConfiteria;

    public Confiteria() {
    }

    public Confiteria(int idConfiteria, String nombreProductoConfiteria, Double precioProductoConfiteria, String tipoProductoConfiteria, Boolean productoDisponibleConfiteria, LocalDate fechaCompraProducto, String metodoPagoProducto, String nombreImagenPublicoConfiteria, String nombreImagenPrivadoConfiteria) {
        this.idConfiteria = idConfiteria;
        this.nombreProductoConfiteria = nombreProductoConfiteria;
        this.precioProductoConfiteria = precioProductoConfiteria;
        this.tipoProductoConfiteria = tipoProductoConfiteria;
        this.productoDisponibleConfiteria = productoDisponibleConfiteria;
        this.fechaCompraProducto = fechaCompraProducto;
        this.metodoPagoProducto = metodoPagoProducto;
        this.nombreImagenPublicoConfiteria = nombreImagenPublicoConfiteria;
        this.nombreImagenPrivadoConfiteria = nombreImagenPrivadoConfiteria;
    }

    public int getIdConfiteria() {
        return idConfiteria;
    }

    public void setIdConfiteria(int idConfiteria) {
        this.idConfiteria = idConfiteria;
    }

    public String getNombreProductoConfiteria() {
        return nombreProductoConfiteria;
    }

    public void setNombreProductoConfiteria(String nombreProductoConfiteria) {
        this.nombreProductoConfiteria = nombreProductoConfiteria;
    }

    public Double getPrecioProductoConfiteria() {
        return precioProductoConfiteria;
    }

    public void setPrecioProductoConfiteria(Double precioProductoConfiteria) {
        this.precioProductoConfiteria = precioProductoConfiteria;
    }

    public String getTipoProductoConfiteria() {
        return tipoProductoConfiteria;
    }

    public void setTipoProductoConfiteria(String tipoProductoConfiteria) {
        this.tipoProductoConfiteria = tipoProductoConfiteria;
    }

    public Boolean getProductoDisponibleConfiteria() {
        return productoDisponibleConfiteria;
    }

    public void setProductoDisponibleConfiteria(Boolean productoDisponibleConfiteria) {
        this.productoDisponibleConfiteria = productoDisponibleConfiteria;
    }

    public LocalDate getFechaCompraProducto() {
        return fechaCompraProducto;
    }

    public void setFechaCompraProducto(LocalDate fechaCompraProducto) {
        this.fechaCompraProducto = fechaCompraProducto;
    }

    public String getMetodoPagoProducto() {
        return metodoPagoProducto;
    }

    public void setMetodoPagoProducto(String metodoPagoProducto) {
        this.metodoPagoProducto = metodoPagoProducto;
    }

    public String getNombreImagenPublicoConfiteria() {
        return nombreImagenPublicoConfiteria;
    }

    public void setNombreImagenPublicoConfiteria(String nombreImagenPublicoConfiteria) {
        this.nombreImagenPublicoConfiteria = nombreImagenPublicoConfiteria;
    }

    public String getNombreImagenPrivadoConfiteria() {
        return nombreImagenPrivadoConfiteria;
    }

    public void setNombreImagenPrivadoConfiteria(String nombreImagenPrivadoConfiteria) {
        this.nombreImagenPrivadoConfiteria = nombreImagenPrivadoConfiteria;
    }

    @Override
    public String toString() {
        return "Confiteria{" + "idConfiteria=" + idConfiteria + ", nombreProductoConfiteria=" + nombreProductoConfiteria + '}';
    }
    
}
