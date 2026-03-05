
package com.unimagdalena.controlador.pelicula;

import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.servicio.PeliculaServicio;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PeliculaControladorListar {
    public static List<PeliculaDto> arregloPeliculas() {
        PeliculaServicio miDao = new PeliculaServicio();
        List<PeliculaDto> arreglo = miDao.SelectFrom();
        
        ObservableList<PeliculaDto> datosTabla = FXCollections.observableArrayList(arreglo);
        return datosTabla;
    }

    public static int cantidadPeliculas() {
        PeliculaServicio miDao = new PeliculaServicio();
        int cant = miDao.numRows();
        return cant;
    }
}
