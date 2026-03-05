package com.unimagdalena.vista.funcion;

import com.unimagdalena.controlador.funcion.FuncionControladorEditar;
import com.unimagdalena.controlador.funcion.FuncionControladorVista;
import com.unimagdalena.controlador.pelicula.PeliculaControladorListar;
import com.unimagdalena.controlador.sala.SalaControladorListar;
import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Contenedor;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Formulario;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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

public class VistaFuncionEditar extends SubScene {

    private final GridPane miGrilla;
    private final StackPane miFormulario;
    private final Stage laVentanaPrincipal;

    private static final int ALTO_CAJA = 35;
    private static final int TAMANYO_FUENTE = 18;

    private TextField cajaNomFuncion;
    private ComboBox<PeliculaDto> cbmPeliculaFuncion;
    private ComboBox<SalaDto> cbmSalaFuncion;
    private DatePicker dtpFechaFuncion;
    private Spinner<Integer> spinnerHora;
    private Spinner<Integer> spinnerMinuto;
    private RadioButton rbtMayorFuncion;
    private RadioButton rbtMenorFuncion;
    private ToggleGroup edades;

    private TextField cajaImagen;
    private ImageView imgPorDefecto;
    private ImageView imgPrevisualizar;

    private final int posicion;
    private final FuncionDto objFuncion;
    private String rutaImagenSeleccionada;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    public VistaFuncionEditar(Stage ventanaPadre, BorderPane princ, Pane pane, double ancho,
            double alto, FuncionDto objFuncionExterna, int posicionArchivo) {
        super(new StackPane(), ancho, alto);

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        miFormulario = (StackPane) getRoot();
        miFormulario.setBackground(fondo);
        miFormulario.setAlignment(Pos.CENTER);

        laVentanaPrincipal = ventanaPadre;

        posicion = posicionArchivo;
        objFuncion = objFuncionExterna;
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

        Text titulo = new Text("Formulario actualización de Funciones");
        titulo.setFill(Color.web("#E82E68"));
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));
        GridPane.setHalignment(titulo, HPos.CENTER);

        fila = 1;
        miGrilla.add(titulo, columna, fila, colSpan, rowSpan);
    }

    private void crearFormulario() {
        // Nombre
        Label lblNombre = new Label("Nombre de la función: ");
        lblNombre.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNombre, 0, 2);

        cajaNomFuncion = new TextField();
        cajaNomFuncion.setText(objFuncion.getNombreFuncion());
        GridPane.setHgrow(cajaNomFuncion, Priority.ALWAYS);
        cajaNomFuncion.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaNomFuncion, 1, 2);

        // Película
        List<PeliculaDto> arrPeliculas = PeliculaControladorListar.arregloPeliculas();
        PeliculaDto peliDefecto = new PeliculaDto(0, "Seleccione una pelicula", LocalDate.MIN, null, false, "", "", "");
        arrPeliculas.add(0, peliDefecto);

        Label lblPelicula = new Label("Pelicula de la función: ");
        lblPelicula.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblPelicula, 0, 3);

        cbmPeliculaFuncion = new ComboBox<>();
        cbmPeliculaFuncion.setMaxWidth(Double.MAX_VALUE);
        cbmPeliculaFuncion.setPrefHeight(ALTO_CAJA);
        cbmPeliculaFuncion.setItems(FXCollections.observableArrayList(arrPeliculas));

        PeliculaDto peliculaFunc = objFuncion.getIdPeliculaFuncion();
        if (peliculaFunc != null) {
            for (PeliculaDto item : cbmPeliculaFuncion.getItems()) {
                if (item.getIdPelicula() == peliculaFunc.getIdPelicula()) {
                    cbmPeliculaFuncion.setValue(item);
                    break;
                }
            }
        } else {
            cbmPeliculaFuncion.getSelectionModel().selectFirst();
        }
        miGrilla.add(cbmPeliculaFuncion, 1, 3);

        // Sala
        List<SalaDto> arrSalas = SalaControladorListar.arregloSalasDisponibles();
        SalaDto salaDefecto = new SalaDto(0, "Seleccione una sala", null, false, 0, false, "", "");
        arrSalas.add(0, salaDefecto);

        Label lblSala = new Label("Sala de la función: ");
        lblSala.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblSala, 0, 4);

        cbmSalaFuncion = new ComboBox<>();
        cbmSalaFuncion.setMaxWidth(Double.MAX_VALUE);
        cbmSalaFuncion.setPrefHeight(ALTO_CAJA);
        cbmSalaFuncion.setItems(FXCollections.observableArrayList(arrSalas));

        SalaDto salaFunc = objFuncion.getIdSalaFuncion();
        if (salaFunc != null) {
            for (SalaDto item : cbmSalaFuncion.getItems()) {
                if (item.getIdSala() == salaFunc.getIdSala()) {
                    cbmSalaFuncion.setValue(item);
                    break;
                }
            }
        } else {
            cbmSalaFuncion.getSelectionModel().selectFirst();
        }
        miGrilla.add(cbmSalaFuncion, 1, 4);

        // Fecha
        Label lblFecha = new Label("Fecha de la función: ");
        lblFecha.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblFecha, 0, 5);

        dtpFechaFuncion = new DatePicker();
        dtpFechaFuncion.setValue(objFuncion.getFechaFuncion());
        dtpFechaFuncion.setMaxWidth(Double.MAX_VALUE);
        dtpFechaFuncion.setPrefHeight(ALTO_CAJA);
        miGrilla.add(dtpFechaFuncion, 1, 5);

        // Hora
        Label lblHora = new Label("Hora de la función: ");
        lblHora.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblHora, 0, 6);

        spinnerHora = new Spinner<>();
        SpinnerValueFactory<Integer> hourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);
        spinnerHora.setValueFactory(hourFactory);
        spinnerHora.setPrefHeight(ALTO_CAJA);
        spinnerHora.setPrefWidth(70);

        spinnerMinuto = new Spinner<>();
        SpinnerValueFactory<Integer> minFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        spinnerMinuto.setValueFactory(minFactory);
        spinnerMinuto.setPrefHeight(ALTO_CAJA);
        spinnerMinuto.setPrefWidth(70);

        LocalTime hora = objFuncion.getHoraFuncion();
        if (hora != null) {
            hourFactory.setValue(hora.getHour());
            minFactory.setValue(hora.getMinute());
        }

        HBox boxHora = new HBox(10);
        boxHora.getChildren().addAll(spinnerHora, spinnerMinuto);
        miGrilla.add(boxHora, 1, 6);

        // Edad
        Label lblEdad = new Label("Clasificacion de Edad");
        lblEdad.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblEdad, 0, 7);

        edades = new ToggleGroup();
        rbtMayorFuncion = new RadioButton("Mayores de edad");
        rbtMayorFuncion.setToggleGroup(edades);
        rbtMenorFuncion = new RadioButton("Menores de edad");
        rbtMenorFuncion.setToggleGroup(edades);

        if (objFuncion.getParaMayoresFuncion() != null) {
            if (objFuncion.getParaMayoresFuncion()) {
                rbtMayorFuncion.setSelected(true);
            } else {
                rbtMenorFuncion.setSelected(true);
            }
        }

        HBox boxEdades = new HBox(15);
        boxEdades.getChildren().addAll(rbtMayorFuncion, rbtMenorFuncion);
        miGrilla.add(boxEdades, 1, 7);

        // Imagen
        Label lblImagen = new Label("Imagen de la Función: ");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblImagen, 0, 8);

        cajaImagen = new TextField();
        cajaImagen.setText(objFuncion.getNombreImagenPublicoFuncion());
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
            miGrilla.add(imgPrevisualizar, 1, 9); // Fila siguiente

        });
        HBox.setHgrow(cajaImagen, Priority.ALWAYS);
        HBox panelCajaBoton = new HBox(2);
        panelCajaBoton.setAlignment(Pos.BOTTOM_RIGHT);
        panelCajaBoton.getChildren().addAll(cajaImagen, btnEscogerImagen);
        miGrilla.add(panelCajaBoton, 1, 8);

        // Imagen Previsualizada
        imgPorDefecto = Icono.obtenerFotosExternas(objFuncion.getNombreImagenPrivadoFuncion(), 150);
        GridPane.setHalignment(imgPorDefecto, HPos.CENTER);
        miGrilla.add(imgPorDefecto, 1, 9);

        // Botón Actualizar
        Button btnActualizar = new Button("¡Clic para actualizar la funcion!");
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setTextFill(Color.web(Configuracion.COLOR4));
        btnActualizar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnActualizar.setOnAction((e) -> {
            actualizarFuncion();
        });
        miGrilla.add(btnActualizar, 1, 10);

        // Botón Regresar
        Button btnRegresar = new Button("Regresar");
        btnRegresar.setMaxWidth(Double.MAX_VALUE);
        btnRegresar.setTextFill(Color.web("#6C3483"));
        btnRegresar.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        miGrilla.add(btnRegresar, 1, 11);
        btnRegresar.setOnAction((ActionEvent e) -> {
            panelCuerpo = FuncionControladorVista.abrirAdministrar(
                    laVentanaPrincipal,
                    panelPrincipal, panelCuerpo,
                    Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor());
            panelPrincipal.setCenter(null);
            panelPrincipal.setCenter(panelCuerpo);
        });

    }

    private void actualizarFuncion() {
        if (formularioValido()) {
            String nombre = cajaNomFuncion.getText();
            PeliculaDto peliculaDto = obtenerComboPelicula();
            SalaDto salaDto = obtenerComboSala();
            LocalDate fecha = dtpFechaFuncion.getValue();
            LocalTime hora = obtenerHoraFuncion();
            Boolean paraMayores = obtenerRadioEdad();
            String nomIma = cajaImagen.getText();

            int codi = objFuncion.getIdFuncion();
            String nocu = objFuncion.getNombreImagenPrivadoFuncion();

            FuncionDto objFuncionEditado = new FuncionDto(codi, nombre, salaDto, peliculaDto, fecha, hora, paraMayores, nomIma, nocu);

            if (FuncionControladorEditar.actualizar(posicion, objFuncionEditado, rutaImagenSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "EXITO", "FUNCION ACTUALIZADA!!!!");
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "ERROR", "FUNCION NO ACTUALIZADA!");
            }
            
        }
    }

    private Boolean formularioValido() {
        if (cajaNomFuncion.getText().isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Lease 100 años de seriedad.\n"
                    + "Debes escribir algo en la caja.");
            cajaNomFuncion.requestFocus();
            return false;
        }

        if (cbmPeliculaFuncion.getSelectionModel().getSelectedIndex() == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null,
                    "ERROR", "Escoge una pelicula de la funcion");
            cbmPeliculaFuncion.requestFocus();
            return false;
        }

        if (cbmSalaFuncion.getSelectionModel().getSelectedIndex() == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null,
                    "ERROR", "Escoge una sala de la funcion");
            cbmSalaFuncion.requestFocus();
            return false;
        }

        if (dtpFechaFuncion.getValue() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Lease 100 años de seriedad.\n"
                    + "Debes escoger una fecha.");
            dtpFechaFuncion.requestFocus();
            return false;
        }

        if (edades.getSelectedToggle() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Debes seleccionar una clasificacion");
            return false;
        }

        return true;
    }

    private Boolean obtenerRadioEdad() {
        if (rbtMayorFuncion.isSelected()) {
            return true;
        } else if (rbtMenorFuncion.isSelected()) {
            return false;
        }
        return null;
    }

    private PeliculaDto obtenerComboPelicula() {
        PeliculaDto seleccionado = cbmPeliculaFuncion.getSelectionModel().getSelectedItem();
        if (seleccionado != null && seleccionado.getIdPelicula() != 0) {
            return seleccionado;
        }
        return null;
    }

    private SalaDto obtenerComboSala() {
        SalaDto seleccionado = cbmSalaFuncion.getSelectionModel().getSelectedItem();
        if (seleccionado != null && seleccionado.getIdSala() != 0) {
            return seleccionado;
        }
        return null;
    }

    private LocalTime obtenerHoraFuncion() {
        int hora = spinnerHora.getValue();
        int minuto = spinnerMinuto.getValue();
        return LocalTime.of(hora, minuto);
    }
}
