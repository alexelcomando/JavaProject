package com.unimagdalena.controlador.confiteria;

import com.unimagdalena.servicio.ConfiteriaServicio;

public class ConfiteriaControladorEliminar {

    // Mantenemos una sola instancia
    private static final ConfiteriaServicio confiteriaServicio = new ConfiteriaServicio();

    public static Boolean borrar(Integer indice) {
        return confiteriaServicio.deleteFrom(indice);
    }
}
