package com.unimagdalena.vista.pelicula;

import com.unimagdalena.controlador.genero.GeneroControladorListar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorGrabar;
import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Formulario;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class VistaPeliculaCrear extends StackPane {

    private static final int H_GAP = 10;
    private static final int V_GAP = 20;

    private static final int ALTO_FILA = 40;
    private static final int ALTO_CAJA = 35;

    private static final int TAMANYO_FUENTE = 18;
    private static final double AJUSTE_ARRIBA = 0.2;

    private final GridPane miGrilla;
    private final Rectangle miMarco;
    private final Stage miEscenario;

    private TextField cajaNomPelicula;
    private CheckBox cbkMayorPelicula;
    private CheckBox cbkMenorPelicula;
    private ComboBox<GeneroDto> cbmGeneroPelicula;
    private DatePicker dtpFechaPelicula;
    private RadioButton rbtSi3Dpelicula;
    private RadioButton rbtNo3Dpelicula;
    private ToggleGroup es3DoNo;

    // Estas propiedades son para las imagenes
    private TextField cajaImagen;
    private ImageView imgPorDefecto;
    private ImageView imgPrevisualizar;
    private String rutaSeleccionada;

    public VistaPeliculaCrear(Stage esce, double ancho, double alto) {
        setAlignment(Pos.CENTER);
        miGrilla = new GridPane();
        miEscenario = esce;
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        setBackground(fondo); 

        getChildren().add(miMarco);
        configurarLaGrilla(ancho, alto);
        pintarTitulo();
        todoResponsive20Puntos();
        reUbicarFormulario();
        PintarFormulario();
        getChildren().add(miGrilla);
    }

    private void todoResponsive20Puntos() {
        miGrilla.setAlignment(Pos.TOP_CENTER);
        miGrilla.prefWidthProperty().bind(miMarco.widthProperty());
        miGrilla.prefHeightProperty().bind(miMarco.heightProperty());
    }

    private void reUbicarFormulario() {
        Runnable organizar = () -> {
            double altoDelFormulario = miMarco.getHeight() * AJUSTE_ARRIBA;
            if (altoDelFormulario > 0) {
                miGrilla.setTranslateY(altoDelFormulario / 2 + altoDelFormulario);
            }
        };
        organizar.run();
        miMarco.heightProperty().addListener(((obs, antes, despues) -> {
            organizar.run();
        }));
    }

    private void pintarTitulo() {
        Text titulo = new Text("Formulario creación de película");
        titulo.setFill(Color.web(Configuracion.COLOR_BORDE));
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        GridPane.setHalignment(titulo, HPos.CENTER);
        GridPane.setMargin(titulo, new Insets(25, 0, 0, 0));
        // columna, fila, colspan, rowspan
        miGrilla.add(titulo, 0, 0, 3, 1);
    }

    private void PintarFormulario() {
        Label lblNombre = new Label("Nombre de la película: ");
        lblNombre.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNombre, 0, 1);

        cajaNomPelicula = new TextField();
        cajaNomPelicula.setPromptText("Coloca el nombre de la pelicula");
        GridPane.setHgrow(cajaNomPelicula, Priority.ALWAYS);
        cajaNomPelicula.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaNomPelicula, 1, 1);
        // *********************************************************************
        Label lblFecha = new Label("Fecha de Estreno: ");
        lblFecha.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblFecha, 0, 2);

        dtpFechaPelicula = new DatePicker();
        dtpFechaPelicula.setMaxWidth(Double.MAX_VALUE);
        dtpFechaPelicula.setPrefHeight(ALTO_CAJA);
        miGrilla.add(dtpFechaPelicula, 1, 2);
        // *********************************************************************
        List<GeneroDto> arrGeneros = GeneroControladorListar.arregloGenerosActivos();
        GeneroDto opcionPorDefecto = new GeneroDto(0, "Seleccione un Genero", true, 0, 0, LocalDate.MIN, "", "", "");
        arrGeneros.add(0, opcionPorDefecto);

        Label lblGenero = new Label("Género de la película: ");
        lblGenero.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblGenero, 0, 3);

        cbmGeneroPelicula = new ComboBox<>();
        cbmGeneroPelicula.setMaxWidth(Double.MAX_VALUE);
        cbmGeneroPelicula.setPrefHeight(ALTO_CAJA);
        ObservableList<GeneroDto> items = FXCollections.observableArrayList(arrGeneros);
        cbmGeneroPelicula.setItems(items);
        cbmGeneroPelicula.getSelectionModel().selectFirst();
        miGrilla.add(cbmGeneroPelicula, 1, 3);
        // ******************************************************ma***************
        Label lbl3D = new Label("¿Es 3D?");
        lbl3D.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lbl3D, 0, 4);

        es3DoNo = new ToggleGroup();
        rbtSi3Dpelicula = new RadioButton("Si");
        rbtSi3Dpelicula.setToggleGroup(es3DoNo);
        rbtNo3Dpelicula = new RadioButton("No");
        rbtNo3Dpelicula.setToggleGroup(es3DoNo);
        HBox box3D = new HBox(15);
        box3D.getChildren().addAll(rbtSi3Dpelicula, rbtNo3Dpelicula);
        miGrilla.add(box3D, 1, 4);
        // *********************************************************************
        Label lblEdad = new Label("Clasificacion de Edad: ");
        lblEdad.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblEdad, 0, 5);

        cbkMayorPelicula = new CheckBox("Mayores de Edad");
        cbkMenorPelicula = new CheckBox("Menores de Edad");
        HBox boxcheck = new HBox(15);
        boxcheck.getChildren().addAll(cbkMayorPelicula, cbkMenorPelicula);
        miGrilla.add(boxcheck, 1, 5);
        // *********************************************************************
        Label lblImagen = new Label("Imagen de la Pelicula: ");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblImagen, 0, 6);

        cajaImagen = new TextField();
        cajaImagen.setDisable(true);
        cajaImagen.setPrefHeight(ALTO_CAJA);
        String[] extensionesPermitidas = {"*.png", "*.jpg", "*.jpeg"};
        FileChooser objSeleccionar = Formulario.selectorImagen(
                "Erda, busca una imagen", "La image", extensionesPermitidas);
        Button btnEscogerImagen = new Button("+");
        btnEscogerImagen.setPrefHeight(ALTO_CAJA);
        btnEscogerImagen.setOnAction((e) -> {
            rutaSeleccionada = GestorImagen.obtenerRutaImagen(cajaImagen, objSeleccionar);
            if (rutaSeleccionada.isEmpty()) {
                miGrilla.getChildren().remove(imgPorDefecto);
                miGrilla.getChildren().remove(imgPrevisualizar);
                miGrilla.add(imgPorDefecto, 2, 2, 1, 3);
            } else {
                miGrilla.getChildren().remove(imgPorDefecto);
                miGrilla.getChildren().remove(imgPrevisualizar);
                imgPrevisualizar = Icono.previsualizar(rutaSeleccionada, 150);
                GridPane.setHalignment(this, HPos.CENTER);
                miGrilla.add(imgPrevisualizar, 2, 2, 1, 3);
            }
        });
        HBox.setHgrow(cajaImagen, Priority.ALWAYS);
        HBox panelCajaBoton = new HBox(2);
        panelCajaBoton.setAlignment(Pos.BOTTOM_RIGHT);
        panelCajaBoton.getChildren().addAll(cajaImagen, btnEscogerImagen);
        miGrilla.add(panelCajaBoton, 1, 6);

        imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 150);
        GridPane.setHalignment(imgPorDefecto, HPos.CENTER);
        GridPane.setValignment(imgPorDefecto, VPos.CENTER);
        miGrilla.add(imgPorDefecto, 2, 2, 1, 3);
        // *********************************************************************
        Button btnGrabar = new Button("¡Clic para crear la pelicula!");
        btnGrabar.setMaxWidth(Double.MAX_VALUE);
        btnGrabar.setTextFill(Color.web(Configuracion.COLOR4));
        btnGrabar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnGrabar.setOnAction((e) -> {
            grabarPelicula();
        });
        miGrilla.add(btnGrabar, 1, 7);

    }

    private void limpiarFormulario() {
        cajaNomPelicula.setText("");
        cajaImagen.setText("");
        cbmGeneroPelicula.getSelectionModel().select(0);
        cajaNomPelicula.requestFocus();
        dtpFechaPelicula.setValue(null);
        es3DoNo.selectToggle(null);
        cbkMayorPelicula.setSelected(false);
        cbkMenorPelicula.setSelected(false);
        miGrilla.getChildren().remove(imgPrevisualizar);
        GridPane.setHalignment(imgPorDefecto, HPos.CENTER);
        miGrilla.add(imgPorDefecto, 2, 2, 1, 3);
    }

    private Boolean formularioValido() {
        String resultadoCheck = obtenerResultadoCheck();

        if (cajaNomPelicula.getText().isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Lease 100 años de seriedad.\n"
                    + "Debes escribir algo en la caja.");
            cajaNomPelicula.requestFocus();
            return false;
        }

        if (dtpFechaPelicula.getValue() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Lease 100 años de seriedad.\n"
                    + "Debes escoger una fecha.");
            dtpFechaPelicula.requestFocus();
            return false;
        }

        if (cbmGeneroPelicula.getSelectionModel().getSelectedIndex() == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null,
                    "ERROR", "Escoge un Genero de la Pelicula");
            cbmGeneroPelicula.requestFocus();
            return false;
        }

        if (resultadoCheck == null || resultadoCheck.isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Debes seleccionar una clasificacion");
            return false;
        }

        if (cbkMayorPelicula.isSelected() && cbkMenorPelicula.isSelected()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Debe marcar solo una casilla de clasificacion de edad");
            return false;
        }
        
        if (es3DoNo.getSelectedToggle() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null,
                    "EY", "Debes seleccionar si la película es 3D o no.");
            return false;
        }

        if (rutaSeleccionada == null || rutaSeleccionada.isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null,
                    "Ey", "Coloca una imagen");
            return false;
        }

        return true;

    }

    private void grabarPelicula() {
        if (formularioValido()) {
            PeliculaDto dto = new PeliculaDto();
            dto.setNombrePelicula(cajaNomPelicula.getText());
            dto.setFechaEstrenoPelicula(dtpFechaPelicula.getValue());
            dto.setIdGeneroPelicula(obtenerResultadoComboGenero());
            dto.setEs3dPelicula(obtenerResultadoRadio());
            dto.setClasificacionEdadPelicula(obtenerResultadoCheck());
            dto.setNombreImagenPublicoPelicula(cajaImagen.getText());

            if (PeliculaControladorGrabar.crearPelicula(dto, rutaSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "EXITO", "PELICULA CREADA!!!!");
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "ERROR", "PELICULA NO CREADA!");
            }

            limpiarFormulario();
        }

    }

    private void configurarLaGrilla(double anchito, double altito) {
        double porcentaje_ancho = 0.6;
        double anchoMarco = anchito * porcentaje_ancho;

        miGrilla.setHgap(H_GAP);
        miGrilla.setVgap(V_GAP);
        miGrilla.setPrefSize(anchoMarco, altito);
        miGrilla.setMinSize(anchoMarco, altito);
        miGrilla.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(40);
        col2.setHgrow(Priority.ALWAYS);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(30);

        miGrilla.getColumnConstraints().addAll(col1, col2, col3);

        for (int j = 0; j < 8; j++) {
            RowConstraints fila = new RowConstraints();
            fila.setMinHeight(ALTO_FILA);
            fila.setPrefHeight(ALTO_FILA);
            miGrilla.getRowConstraints().add(fila);
        }
    }

    private Boolean obtenerResultadoRadio() {
        if (rbtSi3Dpelicula.isSelected()) {
            return true;
        } else if (rbtNo3Dpelicula.isSelected()) {
            return false;
        }
        return null;
    }

    private GeneroDto obtenerResultadoComboGenero() {
        GeneroDto seleccionado = cbmGeneroPelicula.getSelectionModel().getSelectedItem();

        if (seleccionado != null && seleccionado.getIdGenero() != 0) {
            return seleccionado;
        }

        return null;
    }

    private String obtenerResultadoCheck() {
        if (cbkMayorPelicula.isSelected()) {
            return "Mayor de Edad";
        }
        if (cbkMenorPelicula.isSelected()) {
            return "Menor de Edad";
        }
        return null;
    }

}
