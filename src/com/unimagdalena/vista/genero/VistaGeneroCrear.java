package com.unimagdalena.vista.genero;

import com.unimagdalena.controlador.genero.GeneroControladorGrabar;
import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Formulario;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import com.unimagdalena.helpers.utilidad.SlideSwitch;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
import com.unimagdalena.helpers.utilidad.GestorImagen;
import javafx.scene.layout.Background;

public class VistaGeneroCrear extends StackPane {

    private static final int H_GAP = 10;
    private static final int V_GAP = 20;

    private static final int ALTO_FILA = 40;
    private static final int AlTO_CAJA = 35;

    private static final int TAMANYO_FUENTE = 18;
    private static final double AJUSTE_ARRIBA = 0.2;

    private final GridPane miGrilla;
    private final Rectangle miMarco;

    private TextField cajaNombre;
    private ComboBox<String> cbmEstado;
    private DatePicker datePickerFechaCreacion;
    private SlideSwitch slideSwitchPublico;
    private List<CheckBox> checkBoxesCalificacion;

    private TextField cajaImagen;
    private ImageView imgPorDefecto;
    private ImageView imgPrevisualizar;
    private String rutaSeleccionada;

    public VistaGeneroCrear(Stage esce, double ancho, double alto) {
        rutaSeleccionada = "";
        setAlignment(Pos.CENTER);
        miGrilla = new GridPane();
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE, Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        getChildren().add(miMarco);
        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        setBackground(fondo); 

        configurarLaGrilla(ancho, alto);
        pintarTitulo();
        todoResponsive20Puntos();
        PintarFormulario();
        reUbicarFormulario();
        getChildren().add(miGrilla);
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

    private void todoResponsive20Puntos() {
        miGrilla.setAlignment(Pos.TOP_CENTER);
        miGrilla.prefWidthProperty().bind(miMarco.widthProperty());
        miGrilla.prefHeightProperty().bind(miMarco.heightProperty());
    }

    private void configurarLaGrilla(double anchito, double altito) {
        double porcentaje_ancho = 0.7;
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

        for (int j = 0; j < 9; j++) {
            RowConstraints fila = new RowConstraints();
            fila.setMinHeight(ALTO_FILA);
            fila.setPrefHeight(ALTO_FILA);
            miGrilla.getRowConstraints().add(fila);
        }
    }

    private void pintarTitulo() {
        Text titulo = new Text("Formulario creación de género");
        titulo.setFill(Color.BLACK); 
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        GridPane.setHalignment(titulo, HPos.CENTER);
        GridPane.setMargin(titulo, new Insets(10, 0, 0, 0));
        miGrilla.add(titulo, 0, 0, 3, 1);
    }

    private void PintarFormulario() {
        Label lblNombre = new Label("Nombre del género: ");
        lblNombre.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        lblNombre.setTextFill(Color.BLACK); // ★ TEXTO NEGRO
        miGrilla.add(lblNombre, 0, 1);

        cajaNombre = new TextField();
        cajaNombre.setPromptText("Coloca el nombre mi vale");
        cajaNombre.setStyle("-fx-text-fill: black;");
        GridPane.setHgrow(cajaNombre, Priority.ALWAYS);
        cajaNombre.setPrefHeight(AlTO_CAJA);
        miGrilla.add(cajaNombre, 1, 1);

        Label lblEstado = new Label("Estado del género");
        lblEstado.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        lblEstado.setTextFill(Color.BLACK); 
        miGrilla.add(lblEstado, 0, 2);

        cbmEstado = new ComboBox<>();
        cbmEstado.setStyle("-fx-text-fill: black;"); // ★ TEXTO NEGRO
        cbmEstado.setMaxWidth(Double.MAX_VALUE);
        cbmEstado.setPrefHeight(AlTO_CAJA);
        cbmEstado.getItems().addAll("Seleccione el estado", "Activo", "Inactivo");
        cbmEstado.getSelectionModel().select(0);
        miGrilla.add(cbmEstado, 1, 2);

        Label lblFecha = new Label("Fecha de Creación: ");
        lblFecha.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        lblFecha.setTextFill(Color.BLACK); 
        miGrilla.add(lblFecha, 0, 3);

        datePickerFechaCreacion = new DatePicker();
        datePickerFechaCreacion.setStyle("-fx-text-fill: black;"); // ★ TEXTO NEGRO
        datePickerFechaCreacion.setMaxWidth(Double.MAX_VALUE);
        datePickerFechaCreacion.setPrefHeight(AlTO_CAJA);
        miGrilla.add(datePickerFechaCreacion, 1, 3);

        Label lblCalificacion = new Label("Calificación (1-5): ");
        lblCalificacion.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        lblCalificacion.setTextFill(Color.BLACK); 
        miGrilla.add(lblCalificacion, 0, 4);

        checkBoxesCalificacion = new ArrayList<>();
        HBox calificacionBox = new HBox(15);
        calificacionBox.setAlignment(Pos.CENTER_LEFT);

        for (int i = 1; i <= 5; i++) {
            CheckBox cb = new CheckBox();
            cb.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_EDITAR, 20));
            cb.setTextFill(Color.BLACK);
            cb.setAllowIndeterminate(false);

            final int index = i;
            cb.setOnAction(e -> {
                if (cb.isSelected()) {
                    for (int j = 0; j < index; j++) {
                        checkBoxesCalificacion.get(j).setSelected(true);
                    }
                } else {
                    for (int j = index; j < 5; j++) {
                        checkBoxesCalificacion.get(j).setSelected(false);
                    }
                }
            });

            checkBoxesCalificacion.add(cb);
            calificacionBox.getChildren().add(cb);
        }

        miGrilla.add(calificacionBox, 1, 4);

        Label lblPublicoObjetivo = new Label("Público Objetivo: ");
        lblPublicoObjetivo.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        lblPublicoObjetivo.setTextFill(Color.BLACK); 
        miGrilla.add(lblPublicoObjetivo, 0, 5);

        slideSwitchPublico = new SlideSwitch();
        slideSwitchPublico.setPrefHeight(AlTO_CAJA);
        slideSwitchPublico.setTextos("MAYORES", "MENORES");
        slideSwitchPublico.setMaxWidth(Double.MAX_VALUE);
        HBox publicoBox = new HBox();
        publicoBox.getChildren().add(slideSwitchPublico);
        miGrilla.add(publicoBox, 1, 5);

        Label lblImagen = new Label("Imagen del género");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        lblImagen.setTextFill(Color.BLACK); // ★ TEXTO NEGRO
        miGrilla.add(lblImagen, 0, 6);

        cajaImagen = new TextField();
        cajaImagen.setDisable(true);
        cajaImagen.setStyle("-fx-text-fill: black;"); // ★ TEXTO NEGRO
        cajaImagen.setPrefHeight(AlTO_CAJA);

        String[] extensionesPermitidas = {"*.png", "*.jpg", "*.jpeg"};
        FileChooser objSeleccionar = Formulario.selectorImagen(
                "Ey, busca una imagen", "La image", extensionesPermitidas);

        Button btnEscogerImagen = new Button("+");
        btnEscogerImagen.setTextFill(Color.BLACK); // ★ TEXTO NEGRO
        btnEscogerImagen.setPrefHeight(AlTO_CAJA);
        btnEscogerImagen.setOnAction((e) -> {
            rutaSeleccionada = GestorImagen.obtenerRutaImagen(cajaImagen, objSeleccionar);
            if (rutaSeleccionada.isEmpty()) {
                miGrilla.getChildren().remove(imgPorDefecto);
                miGrilla.getChildren().remove(imgPrevisualizar);
                miGrilla.add(imgPorDefecto, 2, 1, 1, 5);
            } else {
                miGrilla.getChildren().remove(imgPorDefecto);
                miGrilla.getChildren().remove(imgPrevisualizar);
                imgPrevisualizar = Icono.previsualizar(rutaSeleccionada, 150);
                GridPane.setHalignment(this, HPos.CENTER);
                miGrilla.add(imgPrevisualizar, 2, 1, 1, 5);
            }
        });

        HBox.setHgrow(cajaImagen, Priority.ALWAYS);

        HBox panelCajaBoton = new HBox(2);
        panelCajaBoton.setAlignment(Pos.BOTTOM_RIGHT);
        panelCajaBoton.getChildren().addAll(cajaImagen, btnEscogerImagen);
        miGrilla.add(panelCajaBoton, 1, 6);

        imgPorDefecto = Icono.obtenerIcono(Configuracion.ICONO_NO_DISPONIBLE, 150);
        GridPane.setHalignment(imgPorDefecto, HPos.CENTER);
        GridPane.setValignment(imgPorDefecto, VPos.CENTER);
        miGrilla.add(imgPorDefecto, 2, 1, 1, 5);

        Button btnGrabar = new Button("Clic para GRABAR YA!!!!");
        btnGrabar.setMaxWidth(Double.MAX_VALUE);
        btnGrabar.setTextFill(Color.BLACK); // ★ TEXTO NEGRO
        btnGrabar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnGrabar.setOnAction((e) -> {
            grabarGenero();
        });
        miGrilla.add(btnGrabar, 1, 7);
    }

    private Boolean obtenerEstadoCombo() {
        String seleccion = cbmEstado.getValue();
        if ("Activo".equals(seleccion)) {
            return true;
        }
        if ("Inactivo".equals(seleccion)) {
            return false;
        }
        return null;
    }

    private Integer obtenerCalificacion() {
        int calificacion = 0;
        for (CheckBox cb : checkBoxesCalificacion) {
            if (cb.isSelected()) {
                calificacion++;
            }
        }
        return calificacion;
    }

    private String obtenerPublicoObjetivo() {
        return slideSwitchPublico.isOn() ? "MAYORES" : "MENORES";
    }

    private void limpiarFormulario() {
        cajaNombre.setText("");
        cbmEstado.getSelectionModel().select(0);
        datePickerFechaCreacion.setValue(null);
        slideSwitchPublico.reset();

        for (CheckBox cb : checkBoxesCalificacion) {
            cb.setSelected(false);
        }

        cajaNombre.requestFocus();

        rutaSeleccionada = "";
        cajaImagen.setText("");
        miGrilla.getChildren().remove(imgPrevisualizar);
        GridPane.setHalignment(imgPorDefecto, HPos.CENTER);
        miGrilla.add(imgPorDefecto, 2, 1, 1, 5);
    }

    private Boolean formularioValido() {
        if (cajaNombre.getText().isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Lease 100 años de seriedad.\nDebes escribir algo en la caja.");
            cajaNombre.requestFocus();
            return false;
        }
        if (cbmEstado.getSelectionModel().getSelectedIndex() == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Agarra una de las Ver*** del combo de estado");
            cbmEstado.requestFocus();
            return false;
        }
        if (datePickerFechaCreacion.getValue() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Necesitas una fecha de creación.");
            datePickerFechaCreacion.requestFocus();
            return false;
        }
        if (obtenerCalificacion() == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Debes seleccionar al menos una estrella de calificación.");
            return false;
        }
        if (rutaSeleccionada.isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "Paila", "Y la imagen?");
            return false;
        }
        return true;
    }

    private void grabarGenero() {
        if (formularioValido()) {
            GeneroDto dto = new GeneroDto();
            dto.setNombreGenero(cajaNombre.getText());
            dto.setEstadoGenero(obtenerEstadoCombo());
            dto.setFechaCreacionGenero(datePickerFechaCreacion.getValue());
            dto.setCalificacionGenero(obtenerCalificacion());
            dto.setPublicoObjetivoGenero(obtenerPublicoObjetivo());
            dto.setNombreImagenPublicoGenero(cajaImagen.getText());

            if (GeneroControladorGrabar.crearGenero(dto, rutaSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "EXITO", "Listo me fui !!!!!!!!!!!!!!");
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "ERROR", "No me fui!!!!");
            }

            limpiarFormulario();
        }
    }
}
