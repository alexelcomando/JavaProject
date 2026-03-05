
package com.unimagdalena.helpers.utilidad;
import javafx.util.Duration;
import javafx.scene.layout.Pane;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Efecto {
    
       public static void transicionX(Pane contenedor, double segundos) {
        TranslateTransition cambio = new TranslateTransition();
        cambio.setDuration(Duration.seconds(segundos));
        cambio.setNode(contenedor);
        cambio.setToX(0);
        cambio.play();
        System.out.println("TransicionX");
    }

    public static void transicionY(Pane contenedor, double segundos) {
        TranslateTransition cambio = new TranslateTransition();
        cambio.setDuration(Duration.seconds(segundos));
        cambio.setNode(contenedor);
        cambio.setToY(0);
        cambio.play();
        System.out.println("TransicionY");
    }

    public static void crecer(Pane contenedor, double segundos) {
        ScaleTransition cambio = new ScaleTransition();
        cambio.setDuration(Duration.seconds(segundos));
        cambio.setNode(contenedor);
        cambio.setFromX(0.1);
        cambio.setFromY(0.1);
        cambio.setToX(1);
        cambio.setToY(1);
        cambio.play();
        System.out.println("Crecer");
    }

    public static void rotar(Pane contenedor, double segundos) {
        RotateTransition cambio = new RotateTransition();
        cambio.setDuration(Duration.seconds(segundos));
        cambio.setNode(contenedor);
        cambio.setFromAngle(0);
        cambio.setToAngle(360);
        cambio.setCycleCount(1);
        cambio.play();
        System.out.println("Rotar");
    }

    public static void desvanecer(Pane contenedor, double segundos) {
        FadeTransition cambio = new FadeTransition();
        cambio.setDuration(Duration.seconds(segundos));
        cambio.setNode(contenedor);
        cambio.setFromValue(0);
        cambio.setToValue(1);
        cambio.play();
        System.out.println("Desvanecer");
    }

    public static void rebote(Pane contenedor, double segundos) {
        TranslateTransition cambio = new TranslateTransition();
        cambio.setDuration(Duration.seconds(segundos));
        cambio.setNode(contenedor);
        cambio.setByY(-150);
        cambio.setCycleCount(4);
        cambio.setAutoReverse(true);
        cambio.play();
        System.out.println("Rebote");
    }

    // Nuevo efecto
    public static void latido(Pane contenedor, double segundos) {
        ScaleTransition latido = new ScaleTransition(Duration.seconds(segundos), contenedor);
        latido.setFromX(1.0);
        latido.setFromY(1.0);
        latido.setToX(1.2);
        latido.setToY(1.2);
        latido.setCycleCount(2); // Sube y baja
        latido.setAutoReverse(true);
        latido.play();
        System.out.println("Latido");
    }
    
}
