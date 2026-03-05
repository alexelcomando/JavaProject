package com.unimagdalena.controlador.pelicula;

import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.servicio.PeliculaServicio;

public class PeliculaControladorUna {

    public static PeliculaDto obtenerPelicula(int indice) {
        PeliculaServicio miDao = new PeliculaServicio();
        return miDao.getOne(indice);
    }
}