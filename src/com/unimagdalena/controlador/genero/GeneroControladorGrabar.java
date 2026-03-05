
package com.unimagdalena.controlador.genero;

import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.servicio.GeneroServicio;

public class GeneroControladorGrabar 
{
    public static Boolean crearGenero(GeneroDto dto, String laRuta)
    {
        boolean correcto = false;
        GeneroServicio generoServicio = new GeneroServicio();
        GeneroDto dtoRespuesta = generoServicio.insertInto(dto, laRuta);
        if (dtoRespuesta != null) 
        {
            correcto = true;
        }
        return correcto;
    }
}
