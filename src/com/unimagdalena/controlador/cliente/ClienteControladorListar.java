package com.unimagdalena.controlador.cliente;

import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.servicio.ClienteServicio;
import java.util.List;

public class ClienteControladorListar {
    public static List<ClienteDto> obtenerTodos() {
        return new ClienteServicio().SelectFrom();
    }
    
    public static int contar() {
        return new ClienteServicio().numRows();
    }
}