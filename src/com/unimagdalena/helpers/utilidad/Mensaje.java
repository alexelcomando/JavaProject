package com.unimagdalena.helpers.utilidad;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Mensaje 
{
    private static Alert armarAlerta(Alert.AlertType tipo, Window ventana, String titulo, String texto)
    {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(texto);
        alerta.initOwner(ventana);
        return alerta;
    }
    
    public static void salir(Stage miEscenario)
    {
        Alert alertaSalir = armarAlerta(Alert.AlertType.CONFIRMATION, miEscenario, "Salir", "¿Desea salir del formulario de cine?");
        if(alertaSalir.showAndWait().get() == ButtonType.OK)
        {
            miEscenario.close();
        }
    }
    public static void mostrar(Alert.AlertType tipo, Window ventana, String titulo, String texto)
    {
        Alert alertaMostrar = armarAlerta(tipo, ventana, titulo, texto);
        alertaMostrar.show();
    }
}
