package com.unimagdalena.controlador.reserva;

import com.unimagdalena.dto.ReservaDto;
import com.unimagdalena.servicio.ReservaServicio;

public class ReservaControladorEditar {

    public static Boolean actualizar(int indice, ReservaDto dto, String laRuta) {
        boolean correcto = false;
        ReservaServicio reservaServicio = new ReservaServicio();
        if (reservaServicio.updateSet(indice, dto, laRuta)) {
            correcto = true;
        }
        return correcto;
    }
}