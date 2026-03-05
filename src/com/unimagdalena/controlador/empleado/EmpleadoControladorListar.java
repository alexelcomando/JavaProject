package com.unimagdalena.controlador.empleado;

import com.unimagdalena.dto.EmpleadoDto;
import com.unimagdalena.servicio.EmpleadoServicio;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmpleadoControladorListar {
    public static List<EmpleadoDto> arregloEmpleados() {
        EmpleadoServicio miDao = new EmpleadoServicio();
        List<EmpleadoDto> arreglo = miDao.SelectFrom();
        
        ObservableList<EmpleadoDto> datosTabla = FXCollections.observableArrayList(arreglo);
        return datosTabla;
    }

    public static int cantidadEmpleados() {
        EmpleadoServicio miDao = new EmpleadoServicio();
        int cant = miDao.numRows();
        return cant;
    }
}
