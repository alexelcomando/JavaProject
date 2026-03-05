package com.unimagdalena.controlador.confiteria;

import com.unimagdalena.servicio.ConfiteriaServicio;
import com.unimagdalena.dto.ConfiteriaDto;
import java.util.List;

public class ConfiteriaControladorListar {
    
    public static List<ConfiteriaDto> arregloConfiteria() {
        ConfiteriaServicio miDao = new ConfiteriaServicio();
        List<ConfiteriaDto> arreglo = miDao.SelectFrom();
        return arreglo;
    }
    
    public static int cantidadConfiteria() {
        ConfiteriaServicio miDao = new ConfiteriaServicio();
        int cant = miDao.numRows();
        return cant;
    }
    
    /**
     * Obtiene un producto de confitería por su índice en la lista
     * @param indice posición del producto en la lista (0-based)
     * @return ConfiteriaDto o null si el índice está fuera de rango
     */
    public static ConfiteriaDto obtenerUno(int indice) {
        List<ConfiteriaDto> lista = arregloConfiteria();
        if (lista != null && indice >= 0 && indice < lista.size()) {
            return lista.get(indice);
        }
        return null;
    }
}