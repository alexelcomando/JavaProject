package com.unimagdalena.controlador.empleado;

import com.unimagdalena.dto.EmpleadoDto;
import com.unimagdalena.servicio.EmpleadoServicio;

public class EmpleadoControladorEditar {
    public static Boolean actualizar(int indice, EmpleadoDto dto, String laRuta) {
        boolean correcto = false;
        EmpleadoServicio empleadoServicio = new EmpleadoServicio();
        if (empleadoServicio.updateSet(indice, dto, laRuta)) {
            correcto = true;
        }
        return correcto;
    }
}
