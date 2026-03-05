package com.unimagdalena.vista.gestor;

import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Icono;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.FontWeight;
import javafx.scene.image.ImageView;

public class VistaAcerca {

    public final static String LBL_TEXTO = "#FFFFFF";
    public final static String ACERCA_FOTO = "developers.png";

    public final static String ACERCA_NOMBREA = "Alejandro Ferreira Florez";
    public final static String ACERCA_CODIGOA = "2025114077";
    public final static String ACERCA_CORREOA = "aeferreira@unimagdalena.edu.co";

    public final static String ACERCA_NOMBRET = "Tomas Alejandro Polanco Siado";
    public final static String ACERCA_CODIGOT = "2025114081";
    public final static String ACERCA_CORREOT = "tpolanco@unimagdalena.edu.co";

    public final static String ACERCA_NOMBREJ = "Jesus Miguel Murillo Ferradanez";
    public final static String ACERCA_CODIGOJ = "2025114071";
    public final static String ACERCA_CORREOJ = "jmmurillof@unimagdalena.edu.co";

    public static void mostrar(Stage escenarioPadre, double anchoPanel, double altoPanel) {
        Stage escenarioModal = new Stage();

        VBox miPanel = new VBox(6);
        miPanel.setAlignment(Pos.CENTER);
        miPanel.setPadding(new Insets(10, 0, 0, 0));
        miPanel.setStyle(Configuracion.CABECERA_COLOR_FONDO);

        ImageView foto = Icono.obtenerIcono(ACERCA_FOTO, 300);
        foto.setPreserveRatio(true);

        Label lblNombreA = new Label(ACERCA_NOMBREA);
        lblNombreA.setAlignment(Pos.CENTER);
        lblNombreA.setTextFill(Color.web(LBL_TEXTO));
        lblNombreA.setFont(Font.font("Verdana", FontWeight.BOLD, 18));

        Label lblCorreoA = new Label(ACERCA_CORREOA);
        lblCorreoA.setAlignment(Pos.CENTER);
        lblCorreoA.setTextFill(Color.web(LBL_TEXTO));
        lblCorreoA.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));

        Label lblCodigoA = new Label(ACERCA_CODIGOA);
        lblCodigoA.setAlignment(Pos.CENTER);
        lblCodigoA.setTextFill(Color.web(LBL_TEXTO));
        lblCodigoA.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));

        Label lblNombreT = new Label(ACERCA_NOMBRET);
        lblNombreT.setAlignment(Pos.CENTER);
        lblNombreT.setTextFill(Color.web(LBL_TEXTO));
        lblNombreT.setFont(Font.font("Verdana", FontWeight.BOLD, 18));

        Label lblCorreoT = new Label(ACERCA_CORREOT);
        lblCorreoT.setAlignment(Pos.CENTER);
        lblCorreoT.setTextFill(Color.web(LBL_TEXTO));
        lblCorreoT.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));

        Label lblCodigoT = new Label(ACERCA_CODIGOT);
        lblCodigoT.setAlignment(Pos.CENTER);
        lblCodigoT.setTextFill(Color.web(LBL_TEXTO));
        lblCodigoT.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));

        Label lblNombreJ = new Label(ACERCA_NOMBREJ);
        lblNombreJ.setAlignment(Pos.CENTER);
        lblNombreJ.setTextFill(Color.web(LBL_TEXTO));
        lblNombreJ.setFont(Font.font("Verdana", FontWeight.BOLD, 18));

        Label lblCorreoJ = new Label(ACERCA_CORREOJ);
        lblCorreoJ.setAlignment(Pos.CENTER);
        lblCorreoJ.setTextFill(Color.web(LBL_TEXTO));
        lblCorreoJ.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));

        Label lblCodigoJ = new Label(ACERCA_CODIGOJ);
        lblCodigoJ.setAlignment(Pos.CENTER);
        lblCodigoJ.setTextFill(Color.web(LBL_TEXTO));
        lblCodigoJ.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        
        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setPrefWidth(160);
        btnCerrar.setTextFill(Color.web("#E82E68"));
        btnCerrar.setOnAction(event -> escenarioModal.close());

        miPanel.getChildren().addAll(
                foto,
                lblNombreA, lblCorreoA, lblCodigoA,
                lblNombreT, lblCorreoT, lblCodigoT,
                lblNombreJ, lblCorreoJ, lblCodigoJ,
                btnCerrar
        );

        Scene nuevaEscena = new Scene(miPanel, anchoPanel, altoPanel);
        escenarioModal.setScene(nuevaEscena);
        escenarioModal.initModality(Modality.APPLICATION_MODAL);
        escenarioModal.initStyle(StageStyle.UTILITY);
        escenarioModal.setTitle("Acerca de ...");
        escenarioModal.show();

        escenarioPadre.getScene().getRoot().setOpacity(0.2);
        escenarioModal.setOnHidden(event -> escenarioPadre.getScene().getRoot().setOpacity(1.0));
    }
}
