// ==================== TicketControladorEliminar.java ====================
package com.unimagdalena.controlador.ticket;

import com.unimagdalena.servicio.TicketServicio;

public class TicketControladorEliminar {
    
    public static boolean eliminar(int indice) {
        TicketServicio servicio = new TicketServicio();
        return servicio.deleteFrom(indice);
    }
}