package com.unimagdalena.controlador.empleado;

import com.unimagdalena.dto.EmpleadoDto;
import com.unimagdalena.servicio.EmpleadoServicio;

public class EmpleadoControladorUna {
    public static EmpleadoDto obtenerEmpleado(int indice) {
        EmpleadoServicio miDao = new EmpleadoServicio();
        return miDao.getOne(indice);
    }
}
