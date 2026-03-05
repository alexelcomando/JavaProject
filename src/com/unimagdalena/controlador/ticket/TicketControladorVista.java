// ==================== TicketControladorVista.java ====================
package com.unimagdalena.controlador.ticket;

import com.unimagdalena.controlador.ControladorEfecto;
import com.unimagdalena.dto.TicketDto;
import com.unimagdalena.helpers.utilidad.Mensaje;
import com.unimagdalena.vista.ticket.VistaTicketAdministrar;
import com.unimagdalena.vista.ticket.VistaTicketCarrusel;
import com.unimagdalena.vista.ticket.VistaTicketCrear;
import com.unimagdalena.vista.ticket.VistaTicketListar;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TicketControladorVista {
    
    public static StackPane abrirCrear(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaTicketCrear(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane abrirListar(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaTicketListar(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane abrirAdministrar(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaTicketAdministrar(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane mostrarEditar(Stage esce, TicketDto dto, int indice,
                                         BorderPane panelPrincipal, Pane panelAnterior) {
        return TicketControladorEditar.abrirEditar(esce, panelPrincipal, panelAnterior, dto, indice);
    }
    
    public static StackPane abrirCarrusel(Stage esce, BorderPane panelPrincipal, 
                                         Pane panelAnterior, int indice, 
                                         double ancho, double alto) {
        StackPane contenedor = new VistaTicketCarrusel(esce, panelPrincipal, panelAnterior, indice);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane abrirCarruselDesdeMenu(Stage esce, BorderPane panelPrincipal, 
                                                   double ancho, double alto) {
        int cantidadTickets = TicketControladorListar.cantidadTickets();
        
        if (cantidadTickets == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, esce, "Sin tickets", 
                    "No hay tickets registrados para mostrar en el carrusel");
            return abrirAdministrar(esce, ancho, alto);
        }
        
        return abrirCarrusel(esce, panelPrincipal, null, 0, ancho, alto);
    }
}