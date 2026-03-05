package com.unimagdalena.controlador.empleado;

import com.unimagdalena.servicio.EmpleadoServicio;

public class EmpleadoControladorCant {
    public static int obtener(){
        int cantidad;
        
        EmpleadoServicio empleadoServicio = new EmpleadoServicio();
        cantidad = empleadoServicio.numRows();
        
        return cantidad;
    }
}
