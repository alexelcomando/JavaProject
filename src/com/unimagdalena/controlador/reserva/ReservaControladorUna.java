package com.unimagdalena.controlador.reserva;

import com.unimagdalena.dto.ReservaDto;
import com.unimagdalena.servicio.ReservaServicio;

public class ReservaControladorUna {
    public static ReservaDto obtenerReserva(int indice) {
        ReservaServicio miDao = new ReservaServicio();
        return miDao.getOne(indice);
    }
}   