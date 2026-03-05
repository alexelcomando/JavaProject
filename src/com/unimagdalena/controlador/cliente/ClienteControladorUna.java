package com.unimagdalena.controlador.cliente;

import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.servicio.ClienteServicio;

public class ClienteControladorUna {
    public static ClienteDto obtenerCliente(int indice) {
        return new ClienteServicio().getOne(indice);
    }
}   