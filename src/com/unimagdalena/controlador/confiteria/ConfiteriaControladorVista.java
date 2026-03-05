package com.unimagdalena.controlador.confiteria;

import com.unimagdalena.vista.confiteria.VistaConfiteriaCrear;
import com.unimagdalena.vista.confiteria.VistaConfiteriaAdministrar;
import com.unimagdalena.vista.confiteria.VistaConfiteriaListar;
import com.unimagdalena.vista.confiteria.VistaConfiteriaCarrusel; // ← AGREGAR ESTE IMPORT
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.unimagdalena.controlador.ControladorEfecto;
import com.unimagdalena.dto.ConfiteriaDto;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class ConfiteriaControladorVista {
    
    public static StackPane abrirCrear(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaConfiteriaCrear(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane abrirListar(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaConfiteriaListar(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane abrirAdministar(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaConfiteriaAdministrar(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane mostrarEditar(Stage esce, ConfiteriaDto dto, BorderPane panelPrincipal, Pane panelAnterior) {
        return ConfiteriaControladorEditar.abrirEditar(
                esce,
                panelPrincipal,
                panelAnterior,
                dto
        );
    }
    
    // ← AGREGAR ESTE MÉTODO NUEVO
    public static StackPane abrirCarrusel(Stage esce, BorderPane panelPrincipal, Pane panelAnterior, int indice, double ancho, double alto) {
        StackPane contenedor = new VistaConfiteriaCarrusel(esce, panelPrincipal, panelAnterior, indice);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
}