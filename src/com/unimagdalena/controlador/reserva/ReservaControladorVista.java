package com.unimagdalena.controlador.reserva;

import com.unimagdalena.controlador.ControladorEfecto;
import com.unimagdalena.dto.ReservaDto;
import com.unimagdalena.vista.reserva.VistaReservaAdm;
import com.unimagdalena.vista.reserva.VistaReservaCarrusel;
import com.unimagdalena.vista.reserva.VistaReservaCrear;
import com.unimagdalena.vista.reserva.VistaReservaEditar;
import com.unimagdalena.vista.reserva.VistaReservaListar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ReservaControladorVista {
    public static StackPane abrirCrear(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaReservaCrear(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }

    public static StackPane abrirListar(Stage esce, double ancho, double alto) {
         StackPane contenedor = new VistaReservaListar(esce, ancho, alto);
         ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
         return contenedor;
    }

    public static StackPane abrirAdministrar(Stage esce, BorderPane princ, Pane pane, double ancho, double alto) {
         VistaReservaAdm ventana = new VistaReservaAdm(esce, princ, pane, ancho, alto);
         StackPane contenedor = ventana.getMiFormulario();
         ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
         return contenedor;
    }

    public static StackPane editar(Stage miEscenario, BorderPane princ, Pane pane,
            double anchoFrm, double altoFrm, ReservaDto objReserva, int posi) {
         VistaReservaEditar ventana = new VistaReservaEditar(miEscenario, princ, pane, anchoFrm, altoFrm, objReserva, posi);
         StackPane contenedor = ventana.getMiFormulario();
         ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);
         return contenedor;

    }

    public static BorderPane carrusel(Stage miEscenario, BorderPane princ, Pane pane,
            double anchoFrm, double altoFrm, int indice) {
         VistaReservaCarrusel ventana = new VistaReservaCarrusel(miEscenario, princ, pane, anchoFrm, altoFrm, indice);
         BorderPane contenedor = ventana.getMiBorderPane();
         ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);
         return contenedor;
    }
}