package com.unimagdalena.helpers.utilidad;

import com.unimagdalena.helpers.constante.Persistencia;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;

public class Fondo {

    public static Background asignar(String nombreFondo) {
        String rutaFondo = Persistencia.RUTA_IMAGENES_INTERNAS + nombreFondo;
        Image miImagen = new Image(rutaFondo, true);

        BackgroundSize ajustarTamanio = new BackgroundSize(1, 1, true, true, false, false);
        BackgroundImage fondoListo = new BackgroundImage(miImagen, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, ajustarTamanio);
        return new Background(fondoListo);
    }

    public static Background asignarAleatorio(String fondos[]) {
        String nombreFondo = fondos[Aleatorio.entero(0, fondos.length - 1)];
        String rutaFondo = Persistencia.RUTA_IMAGENES_INTERNAS + nombreFondo;
        Image miImagen = new Image(rutaFondo, true);

        BackgroundSize ajustarTamanio = new BackgroundSize(1, 1, true, true, false, false);
        BackgroundImage fondoListo = new BackgroundImage(miImagen, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, ajustarTamanio);
        return new Background(fondoListo);
    }
}
