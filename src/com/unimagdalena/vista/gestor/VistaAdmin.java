
package com.unimagdalena.vista.gestor;

import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Contenedor;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class VistaAdmin 
{
    private final Scene miEscena;
    private final Stage miEscenario;
    
    private final Pane miPanelCuerpo;
    private final HBox miPanelCabecera;
    private final BorderPane miPanelPrincipal;

    public VistaAdmin() 
    {
        miEscenario = new Stage();
        miPanelCuerpo = new Pane();
        miPanelPrincipal = new BorderPane();
        
        VistaCabecera cabeceraVista = new VistaCabecera(miEscenario, miPanelPrincipal, miPanelCuerpo,
                Configuracion.ANCHO_APP, Contenedor.ALTO_CABECERA.getValor());

        miPanelCabecera = cabeceraVista.getMiPanelCabecera();
        miPanelCuerpo.setPrefHeight(Contenedor.ALTO_CABECERA.getValor());

        miPanelPrincipal.setTop(miPanelCabecera);
        miPanelPrincipal.setCenter(miPanelCuerpo);

        miEscena = new Scene(miPanelPrincipal,
                Configuracion.ANCHO_APP, Configuracion.ALTO_APP);
        miEscenario.setScene(miEscena);
    }
    
    public Stage getMiEscenario() {
        return miEscenario;
    }
    
}
