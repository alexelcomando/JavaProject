package com.unimagdalena.controlador.cliente;

import com.unimagdalena.controlador.ControladorEfecto;
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.helpers.constante.Contenedor;
import com.unimagdalena.vista.cliente.VistaClienteAdm;
import com.unimagdalena.vista.cliente.VistaClienteCarrusel;
import com.unimagdalena.vista.cliente.VistaClienteCrear;
import com.unimagdalena.vista.cliente.VistaClienteEditar;
import com.unimagdalena.vista.cliente.VistaClienteListar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ClienteControladorVista {
    public static StackPane abrirCrear(Stage stage, double w, double h) {
        StackPane pane = new VistaClienteCrear(stage, w, h);
        ControladorEfecto.aplicarEfecto(pane, w, h);
        return pane;
    }

    public static StackPane abrirListar(Stage stage, double w, double h) {
        StackPane pane = new VistaClienteListar(stage, w, h);
        ControladorEfecto.aplicarEfecto(pane, w, h);
        return pane;
    }
    
    public static StackPane abrirAdministrar(Stage esce, BorderPane princ, Pane pane, double ancho, double alto) {
        VistaClienteAdm ventana = new VistaClienteAdm(esce, princ, pane, ancho, alto);
        StackPane contenedor = (StackPane) ventana.getRoot();
        
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane abrirEditar(Stage miEscenario, BorderPane princ, Pane pane,
            double anchoFrm, double altoFrm, ClienteDto objCliente, int posi) {

        VistaClienteEditar ventana = new VistaClienteEditar(miEscenario, princ, pane, anchoFrm, altoFrm, objCliente, posi);
        StackPane contenedor = (StackPane) ventana.getRoot();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);
        return contenedor;
    }
    
    public static BorderPane carrusel(Stage miEscenario, BorderPane princ, Pane pane,
            double anchoFrm, double altoFrm, int indice) {

        VistaClienteCarrusel ventana = new VistaClienteCarrusel(miEscenario, princ, pane, anchoFrm, altoFrm, indice);
        BorderPane contenedor = ventana.getMiBorderPane();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);
        return contenedor;
    }
}