package com.unimagdalena.controlador.empleado;

import com.unimagdalena.dto.EmpleadoDto;
import com.unimagdalena.servicio.EmpleadoServicio;

public class EmpleadoControladorGrabar {
    public static Boolean crearEmpleado(EmpleadoDto dto, String laRuta) {
        boolean correcto = false;
        EmpleadoServicio empleadoServicio = new EmpleadoServicio();
        
        EmpleadoDto dtoRespuesta = empleadoServicio.insertInto(dto, laRuta);
        if (dtoRespuesta != null) {
            correcto = true;
        }
        return correcto;
    }
}
