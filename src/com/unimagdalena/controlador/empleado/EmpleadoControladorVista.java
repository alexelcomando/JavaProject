package com.unimagdalena.controlador.empleado;

import com.unimagdalena.controlador.ControladorEfecto;
import com.unimagdalena.dto.EmpleadoDto;
import com.unimagdalena.vista.empleado.VistaEmpleadoAdm;
import com.unimagdalena.vista.empleado.VistaEmpleadoCarrusel;
import com.unimagdalena.vista.empleado.VistaEmpleadoCrear;
import com.unimagdalena.vista.empleado.VistaEmpleadoEditar;
import com.unimagdalena.vista.empleado.VistaEmpleadoListar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class EmpleadoControladorVista {
    public static StackPane abrirCrear(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaEmpleadoCrear(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane abrirListar(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaEmpleadoListar(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane abrirAdministrar(Stage esce, BorderPane princ, Pane pane, double ancho, double alto) {
        VistaEmpleadoAdm ventana = new VistaEmpleadoAdm(esce, princ, pane, ancho, alto);
        StackPane contenedor = ventana.getMiFormulario();
        
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane editar(Stage miEscenario, BorderPane princ, Pane pane,
            double anchoFrm, double altoFrm, EmpleadoDto objPeli, int posi) {

        VistaEmpleadoEditar ventana = new VistaEmpleadoEditar(miEscenario, princ, pane, anchoFrm, altoFrm, objPeli, posi);
        StackPane contenedor = ventana.getMiFormulario();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);
        return contenedor;
    }
    
    public static BorderPane carrusel(Stage miEscenario, BorderPane princ, Pane pane,
            double anchoFrm, double altoFrm, int indice) {

        VistaEmpleadoCarrusel ventana = new VistaEmpleadoCarrusel(miEscenario, princ, pane, anchoFrm, altoFrm, indice);
        BorderPane contenedor = ventana.getMiBorderPane();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);
        return contenedor;
    }
}
