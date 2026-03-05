
package com.unimagdalena.controlador.genero;

import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.servicio.GeneroServicio;
import java.util.List;

public class GeneroControladorListar
{
    public static List<GeneroDto> arregloGeneros()
    {
        GeneroServicio miDao = new GeneroServicio();
        List<GeneroDto> arreglo = miDao.SelectFrom();
        return arreglo;
    }
    
    public static List<GeneroDto> arregloGenerosActivos()
    {
        GeneroServicio miDao = new GeneroServicio();
        List<GeneroDto> arreglo = miDao.selectFromWhereEstadoTrue();
        return arreglo;
    }
    public static int cantidadGeneros()
    {
        GeneroServicio miDao = new GeneroServicio();
        int cant = miDao.numRows();
        return cant;
    }
}
