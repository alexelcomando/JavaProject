
package com.unimagdalena.controlador.confiteria;

import com.unimagdalena.servicio.ConfiteriaServicio;
import com.unimagdalena.dto.ConfiteriaDto;

public class ConfiteriaControladorGrabar {
     public static Boolean crearConfiteria(ConfiteriaDto dto, String laRuta)
    {
        boolean correcto = false;
        ConfiteriaServicio confiteriaServicio = new ConfiteriaServicio();
        ConfiteriaDto dtoRespuesta = confiteriaServicio.insertInto(dto, laRuta);
        if (dtoRespuesta != null) 
        {
            correcto = true;
        }
        return correcto;
    }
}
