package com.unimagdalena.controlador.pelicula;

import com.unimagdalena.servicio.PeliculaServicio;

public class PeliculaControladorCant {
    public static int obtener(){
        int cantidad;
        
        PeliculaServicio peliculaServicio = new PeliculaServicio();
        cantidad = peliculaServicio.numRows();
        
        return cantidad;
    }
}
