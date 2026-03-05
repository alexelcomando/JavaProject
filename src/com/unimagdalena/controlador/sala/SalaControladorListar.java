package com.unimagdalena.controlador.sala;

import com.unimagdalena.servicio.SalaServicio;
import com.unimagdalena.dto.SalaDto;
import java.util.List;

public class SalaControladorListar {
    
    private static final SalaServicio SERVICIO = new SalaServicio();
    
    public static List<SalaDto> arregloSalas() {
        return SERVICIO.SelectFrom();
    }
    
    public static List<SalaDto> arregloSalasDisponibles() {
        return SERVICIO.selectFromWhereDisponibleTrue();
    }
    
    public static int cantidadSalas() {
        return SERVICIO.numRows();
    }
    
    public static SalaDto obtenerUno(int indice) {
        List<SalaDto> lista = arregloSalas();
        if (lista != null && indice >= 0 && indice < lista.size()) {
            return lista.get(indice);
        }
        return null;
    }
    
    public static SalaDto obtenerPorId(int idSala) {
        return SERVICIO.getOne(idSala, true);
    }
}