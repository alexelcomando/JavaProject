package com.unimagdalena.controlador.cliente;

import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.servicio.ClienteServicio;

public class ClienteControladorEditar {
    public static Boolean actualizar(int indice, ClienteDto dto, String laRuta) {
        return new ClienteServicio().updateSet(indice, dto, laRuta);
    }
}