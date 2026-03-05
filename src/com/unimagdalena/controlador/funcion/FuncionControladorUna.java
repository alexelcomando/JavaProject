package com.unimagdalena.controlador.funcion;

import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.servicio.FuncionServicio;

public class FuncionControladorUna {
    public static FuncionDto obtenerFuncion(int indice) {
        FuncionServicio miDao = new FuncionServicio();
        return miDao.getOne(indice);
    }
}
