
package com.unimagdalena.helpers.utilidad;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Marco
{
   public static Rectangle pintar(Stage esce, double porcentajeAlto, double porcentajeAncho, Stop[] arregloColores, String colorBorder)
   {
       Rectangle miMarco = new Rectangle();
       miMarco.widthProperty().bind(esce.widthProperty().multiply(porcentajeAncho));
       miMarco.heightProperty().bind(esce.heightProperty().multiply(porcentajeAlto));
       
       miMarco.setArcHeight(30);
       miMarco.setArcWidth(30);
       
       miMarco.setFill(new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE, arregloColores));
       miMarco.setStroke(Color.web(colorBorder));
       
       return miMarco;
   }
}
