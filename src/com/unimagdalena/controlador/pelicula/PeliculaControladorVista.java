
package com.unimagdalena.controlador.pelicula;

import com.unimagdalena.controlador.ControladorEfecto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.vista.pelicula.VistaPeliculaAdm;
import com.unimagdalena.vista.pelicula.VistaPeliculaCarrusel;
import com.unimagdalena.vista.pelicula.VistaPeliculaCrear;
import com.unimagdalena.vista.pelicula.VistaPeliculaEditar;
import com.unimagdalena.vista.pelicula.VistaPeliculaListar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PeliculaControladorVista {
    public static StackPane abrirCrear(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaPeliculaCrear(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }

    public static StackPane abrirListar(Stage esce, double ancho, double alto) {
        StackPane contenedor = new VistaPeliculaListar(esce, ancho, alto);
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane abrirAdministrar(Stage esce, BorderPane princ, Pane pane, double ancho, double alto) {
        VistaPeliculaAdm ventana = new VistaPeliculaAdm(esce, princ, pane, ancho, alto);
        StackPane contenedor = ventana.getMiFormulario();
        
        ControladorEfecto.aplicarEfecto(contenedor, ancho, alto);
        return contenedor;
    }
    
    public static StackPane editar(Stage miEscenario, BorderPane princ, Pane pane,
            double anchoFrm, double altoFrm, PeliculaDto objPeli, int posi) {

        VistaPeliculaEditar ventana = new VistaPeliculaEditar(miEscenario, princ, pane, anchoFrm, altoFrm, objPeli, posi);
        StackPane contenedor = ventana.getMiFormulario();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);
        return contenedor;
    }
    
    public static BorderPane carrusel(Stage miEscenario, BorderPane princ, Pane pane,
            double anchoFrm, double altoFrm, int indice) {

        VistaPeliculaCarrusel ventana = new VistaPeliculaCarrusel(miEscenario, princ, pane, anchoFrm, altoFrm, indice);
        BorderPane contenedor = ventana.getMiBorderPane();

        ControladorEfecto.aplicarEfecto(contenedor, anchoFrm, altoFrm);
        return contenedor;
    }
}
