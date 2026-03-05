package com.unimagdalena.controlador.pelicula;

import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.servicio.PeliculaServicio;

public class PeliculaControladorEditar {
    public static Boolean actualizar(int indice, PeliculaDto dto, String laRuta) {
        boolean correcto = false;
        PeliculaServicio peliculaServicio = new PeliculaServicio();
        if (peliculaServicio.updateSet(indice, dto, laRuta)) {
            correcto = true;
        }
        return correcto;
    }
}
