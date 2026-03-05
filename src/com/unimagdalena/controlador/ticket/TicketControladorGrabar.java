// ==================== TicketControladorGrabar.java ====================
package com.unimagdalena.controlador.ticket;

import com.unimagdalena.dto.TicketDto;
import com.unimagdalena.servicio.TicketServicio;

public class TicketControladorGrabar {
    
    public static boolean crearTicket(TicketDto dto, String rutaImagen) {
        TicketServicio servicio = new TicketServicio();
        TicketDto resultado = servicio.insertInto(dto, rutaImagen);
        return resultado != null;
    }
}