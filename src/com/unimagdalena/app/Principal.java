
package com.unimagdalena.app;

import com.unimagdalena.controlador.gestor.SalidaControlador;
import com.unimagdalena.helpers.constante.IconoNombre;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.vista.gestor.VistaAdmin;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Principal extends Application 
{
    private VistaAdmin adminVista;

    public Principal() {
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        String iconoApp = Persistencia.RUTA_IMAGENES_INTERNAS + IconoNombre.ICONO_APP;
        Image miImagen = new Image(getClass().getResourceAsStream(iconoApp));

        adminVista = new VistaAdmin();
        stage = adminVista.getMiEscenario();
        stage.setTitle("Formulario de Cine");
        stage.show();
        stage.getIcons().add(miImagen);
        xBotonCerrar(stage);

    }

    private void xBotonCerrar(Stage miEscenario) {
        miEscenario.setOnCloseRequest((event)
                -> {
            event.consume();
            SalidaControlador.verificar(miEscenario);
        });

    }
}
