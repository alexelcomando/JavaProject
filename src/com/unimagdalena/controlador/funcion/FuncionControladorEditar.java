package com.unimagdalena.controlador.funcion;

import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.servicio.FuncionServicio;

public class FuncionControladorEditar {
    public static Boolean actualizar(int indice, FuncionDto dto, String laRuta) {
        boolean correcto = false;
        FuncionServicio funcionServicio = new FuncionServicio();
        if (funcionServicio.updateSet(indice, dto, laRuta)) {
            correcto = true;
        }
        return correcto;
    }
}
