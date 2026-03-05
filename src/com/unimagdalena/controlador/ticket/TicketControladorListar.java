// ==================== TicketControladorListar.java ====================
package com.unimagdalena.controlador.ticket;

import com.unimagdalena.dto.TicketDto;
import com.unimagdalena.servicio.TicketServicio;
import java.util.List;

public class TicketControladorListar {
    
    public static List<TicketDto> arregloTickets() {
        return new TicketServicio().SelectFrom();
    }
    
    public static int cantidadTickets() {
        return new TicketServicio().numRows();
    }
    
    public static TicketDto obtenerTicket(int indice) {
        return new TicketServicio().getOne(indice);
    }
}