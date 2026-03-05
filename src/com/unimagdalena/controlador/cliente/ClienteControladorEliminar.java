package com.unimagdalena.controlador.cliente;

import com.unimagdalena.servicio.ClienteServicio;

public class ClienteControladorEliminar {
    public static Boolean borrar(Integer indice) {
        return new ClienteServicio().deleteFrom(indice);
    }
}