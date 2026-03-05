package com.unimagdalena.vista.cliente;

import com.unimagdalena.helpers.utilidad.Mensaje;
import com.unimagdalena.helpers.utilidad.Formulario;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import com.unimagdalena.controlador.cliente.ClienteControladorGrabar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorListar;
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;

public class VistaClienteCrear extends StackPane {
    private static final int H_GAP = 10;
    private static final int V_GAP = 20;

    private static final int ALTO_FILA = 40;
    private static final int ALTO_CAJA = 35;

    private static final int TAMANYO_FUENTE = 18;
    private static final double AJUSTE_ARRIBA = 0.2;

    private final GridPane miGrilla;
    private final Rectangle miMarco;
    private final Stage miEscenario;
    
    private TextField txtNombre, txtImagen;
    private ComboBox<String> cmbTipoDoc;
    private ComboBox<PeliculaDto> cmbPelicula;
    private DatePicker dtpFecha;
    private RadioButton rbMasc, rbFem;
    private CheckBox chkVIP;
    private ImageView imgPreview;
    private ImageView imgDefault; 
    private String rutaImagen = "";

    public VistaClienteCrear(Stage stage, double w, double h) {
        miEscenario = stage;
        setAlignment(Pos.CENTER);
        miGrilla = new GridPane();
        miMarco = Marco.pintar(stage, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE, Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        getChildren().add(miMarco);
        
        configurarLaGrilla(w, h);
        pintarTitulo();
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

        for (int j = 0; j < 10; j++) {
            RowConstraints fila = new RowConstraints();
            fila.setMinHeight(ALTO_FILA);
            fila.setPrefHeight(ALTO_FILA);
            miGrilla.getRowConstraints().add(fila);
        }
    }

    private void pintarTitulo() {
        Text titulo = new Text("Registro de Cliente");
        titulo.setFill(Color.BLACK); 
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        GridPane.setHalignment(titulo, HPos.CENTER);
        GridPane.setMargin(titulo, new Insets(10, 0, 0, 0));
        miGrilla.add(titulo, 0, 0, 3, 1);
    }
    
    private Label crearLabel(String texto) {
        Label l = new Label(texto);
        l.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        l.setTextFill(Color.BLACK);
        return l;
    }

    private void PintarFormulario() {
        // Nombre (Fila 1)
        miGrilla.add(crearLabel("Nombre Completo:"), 0, 1);
        txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del cliente");
        txtNombre.setStyle("-fx-text-fill: black;");
        txtNombre.setPrefHeight(ALTO_CAJA);
        miGrilla.add(txtNombre, 1, 1);

        // Tipo Doc (Fila 2)
        miGrilla.add(crearLabel("Tipo Documento:"), 0, 2);
        cmbTipoDoc = new ComboBox<>();
        cmbTipoDoc.getItems().addAll("Cédula", "Tarjeta Identidad", "Pasaporte");
        cmbTipoDoc.getSelectionModel().selectFirst();
        cmbTipoDoc.setStyle("-fx-text-fill: black;");
        cmbTipoDoc.setMaxWidth(Double.MAX_VALUE);
        cmbTipoDoc.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cmbTipoDoc, 1, 2);
        
        // Pelicula de Interés (Fila 3)
        List<PeliculaDto> arrPeliculas = PeliculaControladorListar.arregloPeliculas();
        PeliculaDto peliculaDefecto = new PeliculaDto(0, "Seleccione película", LocalDate.MIN, null, false, "", "", "");
        arrPeliculas.add(0, peliculaDefecto);
        
        Label lblPelicula = new Label("Película de Interés:");
        lblPelicula.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblPelicula, 0, 3);
        
        cmbPelicula = new ComboBox<>();
        cmbPelicula.setItems(FXCollections.observableArrayList(arrPeliculas));
        cmbPelicula.getSelectionModel().selectFirst();
        cmbPelicula.setMaxWidth(Double.MAX_VALUE);
        cmbPelicula.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cmbPelicula, 1, 3);

        // Fecha Nacimiento (Fila 4)
        miGrilla.add(crearLabel("Fecha Nacimiento:"), 0, 4);
        dtpFecha = new DatePicker();
        dtpFecha.setStyle("-fx-text-fill: black;");
        dtpFecha.setMaxWidth(Double.MAX_VALUE);
        dtpFecha.setPrefHeight(ALTO_CAJA);
        miGrilla.add(dtpFecha, 1, 4);

        // Sexo (Fila 5)
        miGrilla.add(crearLabel("Sexo:"), 0, 5);
        ToggleGroup tg = new ToggleGroup();
        rbMasc = new RadioButton("Masculino"); rbMasc.setToggleGroup(tg); rbMasc.setSelected(true);
        rbFem = new RadioButton("Femenino"); rbFem.setToggleGroup(tg);
        HBox boxSexo = new HBox(15, rbMasc, rbFem);
        boxSexo.setAlignment(Pos.CENTER_LEFT);
        miGrilla.add(boxSexo, 1, 5);

        // VIP (Fila 6)
        miGrilla.add(crearLabel("Acceso VIP:"), 0, 6);
        chkVIP = new CheckBox("Sí, quiero acceso VIP");
        miGrilla.add(chkVIP, 1, 6);

        // Imagen (Fila 7)
        miGrilla.add(crearLabel("Foto Cliente:"), 0, 7);
        txtImagen = new TextField(); 
        txtImagen.setDisable(true);
        txtImagen.setStyle("-fx-text-fill: black;");
        txtImagen.setPrefHeight(ALTO_CAJA);

        String[] extensionesPermitidas = {"*.png", "*.jpg", "*.jpeg"};
        FileChooser fc = Formulario.selectorImagen("Foto Cliente", "Imágenes", extensionesPermitidas);
        
        Button btnImg = new Button("+");
        btnImg.setTextFill(Color.BLACK);
        btnImg.setPrefHeight(ALTO_CAJA);
        btnImg.setOnAction(e -> seleccionarImagen());
        
        HBox boxImg = new HBox(2);
        HBox.setHgrow(txtImagen, Priority.ALWAYS);
        boxImg.setAlignment(Pos.BOTTOM_RIGHT);
        boxImg.getChildren().addAll(txtImagen, btnImg);
        miGrilla.add(boxImg, 1, 7);

        // Imagen por defecto/Previsualización (Columna 2, Filas 1-7)
        imgDefault = Icono.obtenerIcono(Configuracion.ICONO_NO_DISPONIBLE, 150);
        GridPane.setHalignment(imgDefault, HPos.CENTER);
        GridPane.setValignment(imgDefault, VPos.CENTER);
        miGrilla.add(imgDefault, 2, 1, 1, 7); 

        // Boton Guardar (Fila 8)
        Button btnGuardar = new Button("GUARDAR CLIENTE");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnGuardar.setTextFill(Color.BLACK);
        btnGuardar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnGuardar.setOnAction(e -> guardar());
        miGrilla.add(btnGuardar, 1, 8);
    }
    
    private void seleccionarImagen() {
        FileChooser fc = Formulario.selectorImagen("Foto Cliente", "Imágenes", new String[]{"*.png", "*.jpg"});
        rutaImagen = GestorImagen.obtenerRutaImagen(txtImagen, fc);
        
        if (rutaImagen.isEmpty()) {
            miGrilla.getChildren().remove(imgPreview);
            miGrilla.add(imgDefault, 2, 1, 1, 7);
        } else {
            miGrilla.getChildren().remove(imgDefault);
            if(imgPreview != null) miGrilla.getChildren().remove(imgPreview);
            imgPreview = Icono.previsualizar(rutaImagen, 150);
            GridPane.setHalignment(imgPreview, HPos.CENTER);
            miGrilla.add(imgPreview, 2, 1, 1, 7);
        }
    }

    private Boolean formularioValido() {
        if(txtNombre.getText().isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Lease 100 años de seriedad.\nDebes escribir algo en el nombre.");
            txtNombre.requestFocus();
            return false;
        }
        if(cmbPelicula.getSelectionModel().getSelectedIndex() == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Debes seleccionar una película de interés.");
            cmbPelicula.requestFocus();
            return false;
        }
        if(dtpFecha.getValue() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Necesitas una fecha de nacimiento.");
            dtpFecha.requestFocus();
            return false;
        }
        if(rutaImagen.isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "Paila", "Y la imagen?");
            return false;
        }
        return true;
    }

    private void guardar() {
        if(formularioValido()) {
            ClienteDto dto = new ClienteDto();
            dto.setNombreCliente(txtNombre.getText());
            dto.setTipoDocumentoCliente(cmbTipoDoc.getValue());
            dto.setIdPeliculaCliente(cmbPelicula.getValue());
            dto.setFechaDeNacimientoCliente(dtpFecha.getValue());
            dto.setSexoCliente(rbMasc.isSelected()); 
            dto.setEsAccesoVipCliente(chkVIP.isSelected() ? "Si" : "No");
            dto.setNombreImagenPublicoCliente(txtImagen.getText());

            if(ClienteControladorGrabar.crear(dto, rutaImagen)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Éxito", "Cliente guardado con éxito!");
                limpiar();
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo guardar el cliente.");
            }
        }
    }

    private void limpiar() {
        txtNombre.clear();
        cmbPelicula.getSelectionModel().selectFirst();
        dtpFecha.setValue(null);
        cmbTipoDoc.getSelectionModel().selectFirst();
        rbMasc.setSelected(true);
        chkVIP.setSelected(false);
        
        rutaImagen = "";
        txtImagen.clear();
        
        if(imgPreview != null) miGrilla.getChildren().remove(imgPreview);
        GridPane.setHalignment(imgDefault, HPos.CENTER);
        miGrilla.add(imgDefault, 2, 1, 1, 7);
        txtNombre.requestFocus();
    }
}