package com.unimagdalena.controlador.empleado;

import com.unimagdalena.servicio.EmpleadoServicio;

public class EmpleadoControladorEliminar {
    public static Boolean borrar(Integer indice) {
        Boolean correcto;
        EmpleadoServicio empleadoServicio = new EmpleadoServicio();
        correcto = empleadoServicio.deleteFrom(indice);
        return correcto;
    }
}
