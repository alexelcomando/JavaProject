package com.unimagdalena.controlador.genero;

import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.servicio.GeneroServicio;

public class GeneroControladorEditar {

    public static Boolean actualizar(int indice, GeneroDto dto, String laRuta) {
        boolean correcto = false;
        GeneroServicio generoServicio = new GeneroServicio();
        if (generoServicio.updateSet(indice, dto, laRuta)) {
            correcto = true;
        }
        return correcto;
    }
}