package com.unimagdalena.helpers.constante;

import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;

public class Configuracion {

    public static final int ALTO_APP = (int) (Screen.getPrimary().getBounds().getHeight() * 0.85);
    public static final int ANCHO_APP = (int) (Screen.getPrimary().getBounds().getWidth() * 0.7);

    public static final double ALTO_CABECERA = ALTO_APP * 0.1;

    public final static double CABECERA_ALTO_PORCENTAJE = 0.1;

    public static final String COLOR1 = "#FF5733"; // Naranja cine (vibrante)
    public static final String COLOR2 = "#C70039"; // Rojo película (intenso)
    public static final String COLOR3 = "#900C3F"; // Vino elegante (profundo)
    public static final String COLOR4 = "#581845"; // Morado cinematográfico (sofisticado)

    //ESTILO CSS
    public static final String CABECERA_COLOR_FONDO
            = String.format("-fx-background-color: linear-gradient( to right, %s, %s, %s, %s);", COLOR1, COLOR2, COLOR3, COLOR4);

    public static final String COLOR_BORDE = "#29e6c3";
    public static final Stop[] DEGRADEE_ARREGLO = new Stop[]{
        new Stop(0.0, Color.web("#FF5733", 0.8)), // COLOR1
        new Stop(0.25, Color.web("#C70039", 0.7)), // COLOR2
        new Stop(0.5, Color.web("#900C3F", 0.6)), // COLOR3
        new Stop(0.75, Color.web("#581845", 0.5)) // COLOR4
    };
    //Porcentajes del marco
    public static final double MARCO_ALTO_PORCENTAJE = 0.75;
    public static final double MARCO_ANCHO_PORCENTAJE = 0.8;

    // Iconos
    public static final String ICONO_BORRAR = "iconoBorrar.png";
    public static final String ICONO_CANCELAR = "iconoCancelar.png";
    public static final String ICONO_EDITAR = "iconoEditar.png";
    public static final String ICONO_NO_DISPONIBLE = "imgNoDisponible.png";

    // Fondos
    public final static String FONDOS[] = {
        "fondo01.png",
        "fondo02.png",
        "fondo03.png",
        "fondo04.png",
        "fondo05.png",
        "fondo06.png",
        "fondo07.png"
    };

}
