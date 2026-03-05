package com.unimagdalena.controlador.sala;

import com.unimagdalena.vista.sala.VistaSalaCrear;
import com.unimagdalena.vista.sala.VistaSalaAdministrar;
import com.unimagdalena.vista.sala.VistaSalaListar;
import com.unimagdalena.vista.sala.VistaSalaCarrusel;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.controlador.ControladorEfecto;
import com.unimagdalena.helpers.utilidad.Mensaje;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SalaControladorVista {
    
    public static StackPane abrirCrear(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaSalaCrear(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane abrirListar(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaSalaListar(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane abrirAdministrar(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaSalaAdministrar(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane mostrarEditar(Stage esce, SalaDto dto, 
                                         BorderPane panelPrincipal, Pane panelAnterior) {
        return SalaControladorEditar.abrirEditar(esce, panelPrincipal, panelAnterior, dto);
    }
    
    public static StackPane abrirCarrusel(Stage esce, BorderPane panelPrincipal, 
                                         Pane panelAnterior, int indice, 
                                         double ancho, double alto) {
        StackPane contenedor = new VistaSalaCarrusel(esce, panelPrincipal, panelAnterior, indice);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane abrirCarruselDesdeMenu(Stage esce, BorderPane panelPrincipal, 
                                                   double ancho, double alto) {
        int cantidadSalas = SalaControladorListar.cantidadSalas();
        
        if (cantidadSalas == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, esce, "Sin salas", 
                    "No hay salas registradas para mostrar en el carrusel");
            return abrirAdministrar(esce, ancho, alto);
        }
        
        return abrirCarrusel(esce, panelPrincipal, null, 0, ancho, alto);
    }
}