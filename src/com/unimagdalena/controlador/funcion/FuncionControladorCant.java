package com.unimagdalena.controlador.funcion;

import com.unimagdalena.servicio.FuncionServicio;

public class FuncionControladorCant {
    public static int obtener(){
        int cantidad;
        
        FuncionServicio funcionServicio = new FuncionServicio();
        cantidad = funcionServicio.numRows();
        
        return cantidad;
    }
}
