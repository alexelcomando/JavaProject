package com.unimagdalena.controlador.funcion;

import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.servicio.FuncionServicio;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FuncionControladorListar {
    public static List<FuncionDto> arregloFunciones() {
        FuncionServicio miDao = new FuncionServicio();
        List<FuncionDto> arreglo = miDao.SelectFrom();
        
        ObservableList<FuncionDto> datosTabla = FXCollections.observableArrayList(arreglo);
        return datosTabla;
    }

    public static int cantidadFunciones() {
        FuncionServicio miDao = new FuncionServicio();
        int cant = miDao.numRows();
        return cant;
    }
}
