package com.unimagdalena.controlador.reserva;

import com.unimagdalena.dto.ReservaDto;
import com.unimagdalena.servicio.ReservaServicio;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReservaControladorListar {
    public static List<ReservaDto> arregloReservas() {
        ReservaServicio miDao = new ReservaServicio();
        List<ReservaDto> arreglo = miDao.SelectFrom();

        ObservableList<ReservaDto> datosTabla = FXCollections.observableArrayList(arreglo);
        return datosTabla;
    }

    public static int cantidadReservas() {
        ReservaServicio miDao = new ReservaServicio();
        int cant = miDao.numRows();
        return cant;
    }
}