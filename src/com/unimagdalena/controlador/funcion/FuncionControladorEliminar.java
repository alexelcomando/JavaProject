package com.unimagdalena.controlador.funcion;

import com.unimagdalena.servicio.FuncionServicio;

public class FuncionControladorEliminar {
    public static Boolean borrar(Integer indice) {
        Boolean correcto;
        FuncionServicio funcionServicio = new FuncionServicio();
        correcto = funcionServicio.deleteFrom(indice);
        return correcto;
    }
}
