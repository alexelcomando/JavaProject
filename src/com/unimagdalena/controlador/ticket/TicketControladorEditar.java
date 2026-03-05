// ==================== TicketControladorEditar.java ====================
package com.unimagdalena.controlador.ticket;

import com.unimagdalena.dto.TicketDto;
import com.unimagdalena.servicio.TicketServicio;
import com.unimagdalena.vista.ticket.VistaTicketEditar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TicketControladorEditar {
    
    public static boolean actualizar(TicketDto dto, String rutaImagen, int indice) {
        TicketServicio servicio = new TicketServicio();
        return servicio.updateSet(indice, dto, rutaImagen);
    }
    
    public static StackPane abrirEditar(Stage esce, BorderPane panelPrincipal, 
                                        Pane panelAnterior, TicketDto dto, int indice) {
        return new VistaTicketEditar(esce, panelPrincipal, panelAnterior, dto, indice);
    }
}
