package com.unimagdalena.helpers.constante;

public enum Contenedor {
    ALTO_CABECERA(Configuracion.ALTO_APP
            * Configuracion.CABECERA_ALTO_PORCENTAJE),
    ALTO_CUERPO(Configuracion.ALTO_APP
            - (Configuracion.ALTO_APP
            * Configuracion.CABECERA_ALTO_PORCENTAJE));

    private final double valor;

    private Contenedor(double elValor) {
        this.valor = elValor;
    }

    public double getValor() {
        return valor;
    }
}
