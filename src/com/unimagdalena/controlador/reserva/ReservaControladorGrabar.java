package com.unimagdalena.controlador.reserva;

import com.unimagdalena.dto.ReservaDto;
import com.unimagdalena.servicio.ReservaServicio;

public class ReservaControladorGrabar {
    public static Boolean crearReserva(ReservaDto dto, String laRuta) {
        boolean correcto = false;
        ReservaServicio reservaServicio = new ReservaServicio();

        ReservaDto dtoRespuesta = reservaServicio.insertInto(dto, laRuta);
        if (dtoRespuesta != null) {
            correcto = true;
        }
        return correcto;
    }
}