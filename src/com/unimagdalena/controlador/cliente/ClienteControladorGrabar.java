package com.unimagdalena.controlador.cliente;

import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.servicio.ClienteServicio;

public class ClienteControladorGrabar {
    public static boolean crear(ClienteDto dto, String ruta) {
        return new ClienteServicio().insertInto(dto, ruta) != null;
    }
}