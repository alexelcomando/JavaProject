package com.unimagdalena.controlador.funcion;

import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.servicio.FuncionServicio;

public class FuncionControladorGrabar {
    public static Boolean crearFuncion(FuncionDto dto, String laRuta) {
        boolean correcto = false;
        FuncionServicio funcionServicio = new FuncionServicio();
        
        FuncionDto dtoRespuesta = funcionServicio.insertInto(dto, laRuta);
        if (dtoRespuesta != null) {
            correcto = true;
        }
        return correcto;
    }
}
