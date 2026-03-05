package com.unimagdalena.controlador.genero;

import com.unimagdalena.controlador.ControladorEfecto;
import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.vista.genero.VistaGeneroAdm;
import com.unimagdalena.vista.genero.VistaGeneroCarrusel;
import com.unimagdalena.vista.genero.VistaGeneroCrear;
import com.unimagdalena.vista.genero.VistaGeneroListar;
import com.unimagdalena.vista.genero.VistaGeneroEditar;

import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class GeneroControladorVista {

    public static StackPane abrirCrear(Stage esce, double ancho, double alto) {
        return new VistaGeneroCrear(esce, ancho, alto);
    }

    public static StackPane abrirListar(Stage esce, double ancho, double alto) {
        return new VistaGeneroListar(esce, ancho, alto);
    }

    public static StackPane abrirAdministrar(Stage esce, double ancho, double alto) {
        return new VistaGeneroAdm(esce, ancho, alto);
    }
    
    public static StackPane abrirAdministrar(Stage esce, BorderPane princ, Pane pane, double ancho, double alto) {
        return new VistaGeneroAdm(esce, princ, pane, ancho, alto);
    }
    
    public static StackPane abrirEditar(Stage esce, BorderPane princ, Pane pane, double ancho, double alto, GeneroDto objGenero, int posicion) {
        return new VistaGeneroEditar(esce, princ, pane, ancho, alto, objGenero, posicion);
    }
    
    public static BorderPane carrusel(Stage miEscenario, BorderPane princ, Pane pane,
            double anchoFrm, double altoFrm, int indice) {

        VistaGeneroCarrusel ventana = new VistaGeneroCarrusel(miEscenario, princ, pane, anchoFrm, altoFrm, indice);
        BorderPane contenedor = ventana.getMiBorderPane();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);
        return contenedor;
    }
}