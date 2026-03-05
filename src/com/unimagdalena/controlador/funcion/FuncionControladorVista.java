package com.unimagdalena.controlador.funcion;

import com.unimagdalena.controlador.ControladorEfecto;
import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.vista.funcion.VistaFuncionAdm;
import com.unimagdalena.vista.funcion.VistaFuncionCarrusel;
import com.unimagdalena.vista.funcion.VistaFuncionCrear;
import com.unimagdalena.vista.funcion.VistaFuncionEditar;
import com.unimagdalena.vista.funcion.VistaFuncionListar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FuncionControladorVista {
    public static StackPane abrirCrear(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaFuncionCrear(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }

    public static StackPane abrirListar(Stage esce, double ancho, double alto) {
         StackPane contenedor = new VistaFuncionListar(esce, ancho, alto);
         ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
         return contenedor;
    }

    public static StackPane abrirAdministrar(Stage esce, BorderPane princ, Pane pane, double ancho, double alto) {
         VistaFuncionAdm ventana = new VistaFuncionAdm(esce, princ, pane, ancho, alto);
         StackPane contenedor = ventana.getMiFormulario();
         ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
         return contenedor;
    }

    public static StackPane editar(Stage miEscenario, BorderPane princ, Pane pane,
            double anchoFrm, double altoFrm, FuncionDto objFuncion, int posi) { 
         VistaFuncionEditar ventana = new VistaFuncionEditar(miEscenario, princ, pane, anchoFrm, altoFrm, objFuncion, posi);
         StackPane contenedor = ventana.getMiFormulario(); 
         ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);
         return contenedor;

    }

    public static BorderPane carrusel(Stage miEscenario, BorderPane princ, Pane pane,
            double anchoFrm, double altoFrm, int indice) {
         VistaFuncionCarrusel ventana = new VistaFuncionCarrusel(miEscenario, princ, pane, anchoFrm, altoFrm, indice);
         BorderPane contenedor = ventana.getMiBorderPane(); 
         ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);
         return contenedor;
    }
}
