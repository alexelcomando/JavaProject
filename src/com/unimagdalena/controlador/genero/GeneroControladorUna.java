package com.unimagdalena.controlador.genero;

import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.servicio.GeneroServicio;

public class GeneroControladorUna {

    public static GeneroDto obtenerGenero(int indice) {
        GeneroServicio miDao = new GeneroServicio();
        return miDao.getOne(indice);
    }
}