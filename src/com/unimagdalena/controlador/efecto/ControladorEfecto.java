
package com.unimagdalena.controlador;

import javafx.scene.layout.Pane;
import com.unimagdalena.helpers.utilidad.Aleatorio;
import com.unimagdalena.helpers.utilidad.Efecto;
public class ControladorEfecto {
    public static void aplicarEfecto(Pane contenedor,
            double anchoFrm, double altoFrm) {

        int opcion = Aleatorio.entero(1, 8);
        switch (opcion) {
            case 1 -> {
                contenedor.setTranslateX(anchoFrm - (anchoFrm * 0.2));
                Efecto.transicionX(contenedor, 2.0);
            }
            case 2 -> {
                contenedor.setTranslateY(altoFrm - (altoFrm * 0.2));
                Efecto.transicionY(contenedor, 2.0);
            }
            case 3 -> {
                Efecto.crecer(contenedor, 2.0);
            }
            case 4 -> {
                Efecto.rotar(contenedor, 2.0);
            }
            case 5 -> {
                Efecto.desvanecer(contenedor, 2.0);
            }
            case 6 -> {
                Efecto.rebote(contenedor, 2.0);
            }
            case 7 -> {
                Efecto.latido(contenedor, 2.0);
            }
           

        }
    }
}
