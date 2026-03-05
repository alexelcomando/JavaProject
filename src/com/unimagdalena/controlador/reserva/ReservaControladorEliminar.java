package com.unimagdalena.controlador.reserva;

import com.unimagdalena.servicio.ReservaServicio;

public class ReservaControladorEliminar {
    public static Boolean borrar(Integer indice) {
        Boolean correcto;
        ReservaServicio reservaServicio = new ReservaServicio();
        correcto = reservaServicio.deleteFrom(indice);
        return correcto;
    }
}