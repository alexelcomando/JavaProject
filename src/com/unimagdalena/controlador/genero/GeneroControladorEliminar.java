package com.unimagdalena.controlador.genero;

import com.unimagdalena.servicio.GeneroServicio;

public class GeneroControladorEliminar {

    public static Boolean borrar(Integer indice) {
        Boolean correcto;
        GeneroServicio generoServicio = new GeneroServicio();
        correcto = generoServicio.deleteFrom(indice);
        return correcto;
    }
}
