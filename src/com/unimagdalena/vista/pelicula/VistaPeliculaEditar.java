package com.unimagdalena.vista.pelicula;

import com.unimagdalena.controlador.genero.GeneroControladorListar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorEditar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorVista;
import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Contenedor;
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
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class VistaPeliculaEditar extends SubScene {

    private final GridPane miGrilla;
    private final StackPane miFormulario;
    private final Stage laVentanaPrincipal;

    private static final int ALTO_CAJA = 35;
    private static final int TAMANYO_FUENTE = 18;

    private TextField cajaNomPelicula;
    private CheckBox cbkMayorPelicula;
    private CheckBox cbkMenorPelicula;
    private ComboBox<GeneroDto> cbmGeneroPelicula;
    private DatePicker dtpFechaPelicula;
    private RadioButton rbtSi3Dpelicula;
    private RadioButton rbtNo3Dpelicula;

    private TextField cajaImagen;
    private ImageView imgPorDefecto;
    private ImageView imgPrevisualizar;

    private final int posicion;
    private final PeliculaDto objPelicula;
    private String rutaImagenSeleccionada;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    public VistaPeliculaEditar(Stage ventanaPadre, BorderPane princ, Pane pane, double ancho,
            double alto, PeliculaDto objPeliExterna, int posicionArchivo) {
        super(new StackPane(), ancho, alto);

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        miFormulario = (StackPane) getRoot();
        miFormulario.setBackground(fondo);
        miFormulario.setAlignment(Pos.CENTER);

        laVentanaPrincipal = ventanaPadre;

        posicion = posicionArchivo;
        objPelicula = objPeliExterna;
        panelPrincipal = princ;
        panelCuerpo = pane;

        miGrilla = new GridPane();

        rutaImagenSeleccionada = "";

        configurarGrilla(ancho, alto);
        crearMarco();
        crearTitulo();
        crearFormulario();

        miFormulario.getChildren().add(miGrilla);
    }

    public StackPane getMiFormulario() {
        return miFormulario;
    }

    private void configurarGrilla(double ancho, double alto) {
        double anchoMarco = ancho * 0.5;

        miGrilla.setHgap(10);
        miGrilla.setVgap(20);
        miGrilla.setAlignment(Pos.TOP_CENTER);

        miGrilla.setPrefSize(anchoMarco, alto);
        miGrilla.setMinSize(anchoMarco, alto);
        miGrilla.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        miGrilla.prefWidthProperty().bind(laVentanaPrincipal.widthProperty().multiply(0.7));

        ColumnConstraints anchoColumna1 = new ColumnConstraints();
        anchoColumna1.setPercentWidth(40);

        ColumnConstraints anchoColumna2 = new ColumnConstraints();
        anchoColumna2.setPercentWidth(60);
        anchoColumna2.setHgrow(Priority.ALWAYS);

        miGrilla.getColumnConstraints().addAll(anchoColumna1, anchoColumna2);
    }

    private void crearMarco() {
        double ancho = Configuracion.MARCO_ANCHO_PORCENTAJE;
        double alto = Configuracion.MARCO_ALTO_PORCENTAJE;
        Rectangle miMarco = Marco.pintar(laVentanaPrincipal, alto, ancho, Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        miFormulario.getChildren().add(miMarco);
    }

    private void crearTitulo() {
        int columna, fila, colSpan, rowSpan;

        Region bloqueSeparador = new Region();
        bloqueSeparador.prefHeightProperty().bind(laVentanaPrincipal.heightProperty().multiply(0.05));
        columna = 0;
        fila = 0;
        colSpan = 3;
        rowSpan = 1;
        miGrilla.add(bloqueSeparador, columna, fila, colSpan, rowSpan);

        Text titulo = new Text("Formulario actualización de Peliculas");
        titulo.setFill(Color.web("#E82E68"));
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));
        GridPane.setHalignment(titulo, HPos.CENTER);

        fila = 1;
        miGrilla.add(titulo, columna, fila, colSpan, rowSpan);
    }

    private void crearFormulario() {
        Label lblNombre = new Label("Nombre de la película: ");
        lblNombre.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNombre, 0, 2);

        cajaNomPelicula = new TextField();
        cajaNomPelicula.setText(objPelicula.getNombrePelicula());
        GridPane.setHgrow(cajaNomPelicula, Priority.ALWAYS);
        cajaNomPelicula.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaNomPelicula, 1, 2);

        Label lblFecha = new Label("Fecha de Estreno: ");
        lblFecha.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblFecha, 0, 3);

        dtpFechaPelicula = new DatePicker();
        dtpFechaPelicula.setValue(objPelicula.getFechaEstrenoPelicula());
        dtpFechaPelicula.setMaxWidth(Double.MAX_VALUE);
        dtpFechaPelicula.setPrefHeight(ALTO_CAJA);
        miGrilla.add(dtpFechaPelicula, 1, 3);

        List<GeneroDto> arrGeneros = GeneroControladorListar.arregloGenerosActivos();
        GeneroDto opcionPorDefecto = new GeneroDto(0, "Seleccione un Genero", true, 0, 0, LocalDate.MIN, "", "", "");
        arrGeneros.add(0, opcionPorDefecto);

        Label lblGenero = new Label("Género de la película: ");
        lblGenero.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblGenero, 0, 4);

        cbmGeneroPelicula = new ComboBox<>();
        cbmGeneroPelicula.setMaxWidth(Double.MAX_VALUE);
        cbmGeneroPelicula.setPrefHeight(ALTO_CAJA);
        ObservableList<GeneroDto> items = FXCollections.observableArrayList(arrGeneros);
        cbmGeneroPelicula.setItems(items);

        GeneroDto generoPelicula = objPelicula.getIdGeneroPelicula();
        if (generoPelicula != null) {
            boolean encontrado = false;
            for (GeneroDto item : cbmGeneroPelicula.getItems()) {
                if (item.getIdGenero() == generoPelicula.getIdGenero()) {
                    cbmGeneroPelicula.setValue(item);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                cbmGeneroPelicula.getSelectionModel().selectFirst();
            }
        } else {
            cbmGeneroPelicula.getSelectionModel().selectFirst();
        }

        miGrilla.add(cbmGeneroPelicula, 1, 4);

        Label lbl3D = new Label("¿Es 3D?");
        lbl3D.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lbl3D, 0, 5);

        ToggleGroup es3DoNo = new ToggleGroup();
        rbtSi3Dpelicula = new RadioButton("Si");
        rbtSi3Dpelicula.setToggleGroup(es3DoNo);
        rbtNo3Dpelicula = new RadioButton("No");
        rbtNo3Dpelicula.setToggleGroup(es3DoNo);

        if (objPelicula.getEs3dPelicula()) {
            rbtSi3Dpelicula.setSelected(true);
        } else {
            rbtNo3Dpelicula.setSelected(true);
        }

        HBox box3D = new HBox(15);
        box3D.getChildren().addAll(rbtSi3Dpelicula, rbtNo3Dpelicula);
        miGrilla.add(box3D, 1, 5);

        Label lblEdad = new Label("Clasificacion de Edad: ");
        lblEdad.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblEdad, 0, 6);

        cbkMayorPelicula = new CheckBox("Mayores de Edad");
        cbkMenorPelicula = new CheckBox("Menores de Edad");

        String clasificacion = objPelicula.getClasificacionEdadPelicula();
        if ("Mayor de Edad".equals(clasificacion)) {
            cbkMayorPelicula.setSelected(true);
        } else if ("Menor de Edad".equals(clasificacion)) {
            cbkMenorPelicula.setSelected(true);
        }

        HBox boxcheck = new HBox(15);
        boxcheck.getChildren().addAll(cbkMayorPelicula, cbkMenorPelicula);
        miGrilla.add(boxcheck, 1, 6);

        Label lblImagen = new Label("Imagen de la Pelicula: ");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblImagen, 0, 7);

        cajaImagen = new TextField();
        cajaImagen.setText(objPelicula.getNombreImagenPublicoPelicula());
        cajaImagen.setDisable(true);
        cajaImagen.setPrefHeight(ALTO_CAJA);
        String[] extensionesPermitidas = {"*.png", "*.jpg", "*.jpeg"};
        FileChooser objSeleccionar = Formulario.selectorImagen(
                "Erda, busca una imagen", "La image", extensionesPermitidas);
        Button btnEscogerImagen = new Button("+");
        btnEscogerImagen.setPrefHeight(ALTO_CAJA);
        btnEscogerImagen.setOnAction((e) -> {
            rutaImagenSeleccionada = GestorImagen.obtenerRutaImagen(cajaImagen, objSeleccionar);
            miGrilla.getChildren().remove(imgPorDefecto);
            miGrilla.getChildren().remove(imgPrevisualizar);
            imgPrevisualizar = Icono.previsualizar(rutaImagenSeleccionada, 150);

            GridPane.setHalignment(imgPrevisualizar, HPos.CENTER);
            miGrilla.add(imgPrevisualizar, 1, 8); // Fila siguiente
        });
        HBox.setHgrow(cajaImagen, Priority.ALWAYS);
        HBox panelCajaBoton = new HBox(2);
        panelCajaBoton.setAlignment(Pos.BOTTOM_RIGHT);
        panelCajaBoton.getChildren().addAll(cajaImagen, btnEscogerImagen);
        miGrilla.add(panelCajaBoton, 1, 7);

        imgPorDefecto = Icono.obtenerFotosExternas(objPelicula.getNombreImagenPrivadoPelicula(), 150);
        GridPane.setHalignment(imgPorDefecto, HPos.CENTER);
        miGrilla.add(imgPorDefecto, 1, 8);

        Button btnActualizar = new Button("¡Clic para actualizar la pelicula!");
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setTextFill(Color.web(Configuracion.COLOR4));
        btnActualizar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnActualizar.setOnAction((e) -> {
            actualizarPelicula();
        });
        miGrilla.add(btnActualizar, 1, 9);

        Button btnRegresar = new Button("Regresar");
        btnRegresar.setMaxWidth(Double.MAX_VALUE);
        btnRegresar.setTextFill(Color.web("#6C3483"));
        btnRegresar.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        miGrilla.add(btnRegresar, 1, 10);
        btnRegresar.setOnAction((ActionEvent e) -> {
            panelCuerpo = PeliculaControladorVista.abrirAdministrar(
                    laVentanaPrincipal,
                    panelPrincipal, panelCuerpo,
                    Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor());
            panelPrincipal.setCenter(null);
            panelPrincipal.setCenter(panelCuerpo);
        });

    }

    private void actualizarPelicula() {
        if (formularioValido()) {
            String nombre = cajaNomPelicula.getText();
            LocalDate fechaEst = dtpFechaPelicula.getValue();
            GeneroDto gendto = obtenerResultadoComboGenero();
            Boolean es3d = obtenerResultadoRadio();
            String edad = obtenerResultadoCheck();
            String nomIma = cajaImagen.getText();

            int codi = objPelicula.getIdPelicula();
            String nocu = objPelicula.getNombreImagenPrivadoPelicula();
            PeliculaDto objPeliEditado = new PeliculaDto(codi, nombre, fechaEst, gendto, es3d, edad, nomIma, nocu);

            if (PeliculaControladorEditar.actualizar(posicion, objPeliEditado, rutaImagenSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "EXITO", "PELICULA ACTUALIZADA!!!!");
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "ERROR", "PELICULA NO ACTUALIZADA!");
            }

        }
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

        return true;

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
