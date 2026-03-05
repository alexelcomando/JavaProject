
package com.unimagdalena.controlador.pelicula;

import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.servicio.PeliculaServicio;

public class PeliculaControladorGrabar {
    public static Boolean crearPelicula(PeliculaDto dto, String laRuta) {
        boolean correcto = false;
        PeliculaServicio peliculaServicio = new PeliculaServicio();
        
        PeliculaDto dtoRespuesta = peliculaServicio.insertInto(dto, laRuta);
        if (dtoRespuesta != null) {
            correcto = true;
        }
        return correcto;
    }
}
