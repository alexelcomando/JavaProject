package com.unimagdalena.vista.funcion;

import com.unimagdalena.controlador.funcion.FuncionControladorGrabar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorListar;
import com.unimagdalena.controlador.sala.SalaControladorListar;
import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.helpers.constante.Configuracion;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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

public class VistaFuncionCrear extends StackPane {

    private static final int H_GAP = 10;
    private static final int V_GAP = 20;

    private static final int ALTO_FILA = 40;
    private static final int ALTO_CAJA = 35;

    private static final int TAMANYO_FUENTE = 18;
    private static final double AJUSTE_ARRIBA = 0.2;

    private final GridPane miGrilla;
    private final Rectangle miMarco;
    private final Stage miEscenario;

    private TextField cajaNomFuncion;
    private ComboBox<PeliculaDto> cbmPeliculaFuncion;
    private ComboBox<SalaDto> cbmSalaFuncion;
    private DatePicker dtpFechaFuncion;
    private Spinner<Integer> spinnerHora;
    private Spinner<Integer> spinnerMinuto;
    private RadioButton rbtMayorFuncion;
    private RadioButton rbtMenorFuncion;
    private ToggleGroup edades;

    // Estas propiedades son para las imagenes
    private TextField cajaImagen;
    private ImageView imgPorDefecto;
    private ImageView imgPrevisualizar;
    private String rutaSeleccionada;

    public VistaFuncionCrear(Stage esce, double ancho, double alto) {
        setAlignment(Pos.CENTER);
        miGrilla = new GridPane();
        miEscenario = esce;
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        getChildren().add(miMarco);
        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        setBackground(fondo); 
        configurarLaGrilla(ancho, alto);
        pintarTitulo();
        todoResponsive20Puntos();
        reUbicarFormulario();
        pintarFormulario();
        getChildren().add(miGrilla);
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

        int i;
        for (i = 0; i < 9; i++) {
            RowConstraints fila = new RowConstraints();
            fila.setMinHeight(ALTO_FILA);
            fila.setPrefHeight(ALTO_FILA);
            miGrilla.getRowConstraints().add(fila);
        }

    }

    private void pintarTitulo() {
        Text titulo = new Text("Formulario creación de función");
        titulo.setFill(Color.web(Configuracion.COLOR_BORDE));
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        GridPane.setHalignment(titulo, HPos.CENTER);
        GridPane.setMargin(titulo, new Insets(25, 0, 0, 0));
        // columna, fila, colspan, rowspan
        miGrilla.add(titulo, 0, 0, 3, 1);
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

    private void pintarFormulario() {
        Label lblNombre = new Label("Nombre de la función: ");
        lblNombre.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNombre, 0, 1);

        cajaNomFuncion = new TextField();
        cajaNomFuncion.setPromptText("Coloca el nombre de la función");
        GridPane.setHgrow(cajaNomFuncion, Priority.ALWAYS);
        cajaNomFuncion.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaNomFuncion, 1, 1);
        // *********************************************************************
        List<PeliculaDto> arrPeliculas = PeliculaControladorListar.arregloPeliculas();
        PeliculaDto opcionPorDefecto = new PeliculaDto(0, "Seleccione una pelicula", LocalDate.MIN, null, false, "", "", "");
        arrPeliculas.add(0, opcionPorDefecto);

        Label lblPelicula = new Label("Pelicula de la función: ");
        lblPelicula.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblPelicula, 0, 2);

        cbmPeliculaFuncion = new ComboBox<>();
        cbmPeliculaFuncion.setMaxWidth(Double.MAX_VALUE);
        cbmPeliculaFuncion.setPrefHeight(ALTO_CAJA);
        ObservableList<PeliculaDto> items = FXCollections.observableArrayList(arrPeliculas);
        cbmPeliculaFuncion.setItems(items);
        cbmPeliculaFuncion.getSelectionModel().selectFirst();
        miGrilla.add(cbmPeliculaFuncion, 1, 2);
        // *********************************************************************
        List<SalaDto> arrSalas = SalaControladorListar.arregloSalasDisponibles();
        SalaDto opcionPorDefecto2 = new SalaDto(0, "Seleccione una sala", null, false, 0, false, "", "");
        arrSalas.add(0, opcionPorDefecto2);

        Label lblSala = new Label("Sala de la función: ");
        lblSala.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblSala, 0, 3);

        cbmSalaFuncion = new ComboBox<>();
        cbmSalaFuncion.setMaxWidth(Double.MAX_VALUE);
        cbmSalaFuncion.setPrefHeight(ALTO_CAJA);
        ObservableList<SalaDto> items2 = FXCollections.observableArrayList(arrSalas);
        cbmSalaFuncion.setItems(items2);
        cbmSalaFuncion.getSelectionModel().selectFirst();
        miGrilla.add(cbmSalaFuncion, 1, 3);
        // *********************************************************************
        Label lblFecha = new Label("Fecha de la función: ");
        lblFecha.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblFecha, 0, 4);

        dtpFechaFuncion = new DatePicker();
        dtpFechaFuncion.setMaxWidth(Double.MAX_VALUE);
        dtpFechaFuncion.setPrefHeight(ALTO_CAJA);
        miGrilla.add(dtpFechaFuncion, 1, 4);
        // *********************************************************************
        Label lblHora = new Label("Hora de la función(Hora-Minuto): ");
        lblHora.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblHora, 0, 5);

        spinnerHora = new Spinner<>();
        SpinnerValueFactory<Integer> hourFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);
        hourFactory.setConverter(new javafx.util.StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                if (value == null) {
                    return "12"; 
                }
                return String.format("%02d", value);
            }

            @Override
            public Integer fromString(String string) {
                try {
                    int value = Integer.parseInt(string);
                    if (value < 0) {
                        return 0;
                    }
                    if (value > 23) {
                        return 23;
                    }
                    return value;
                } catch (NumberFormatException e) {
                    return 12;
                }
            }
        });
        spinnerHora.setValueFactory(hourFactory);
        spinnerHora.setPrefHeight(ALTO_CAJA);
        spinnerHora.setPrefWidth(70);

        spinnerMinuto = new Spinner<>();
        SpinnerValueFactory<Integer> minFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);

        minFactory.setConverter(new javafx.util.StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                if (value == null) {
                    return "00"; // Default de minutos
                }
                return String.format("%02d", value);
            }

            @Override
            public Integer fromString(String string) {
                try {
                    int value = Integer.parseInt(string);
                    // Validación de rango
                    if (value < 0) {
                        return 0;
                    }
                    if (value > 59) {
                        return 59;
                    }
                    return value;
                } catch (NumberFormatException e) {
                    // Si escribe mal, regresa a 0
                    return 0;
                }
            }
        });

        spinnerMinuto.setValueFactory(minFactory);
        spinnerMinuto.setPrefHeight(ALTO_CAJA);
        spinnerMinuto.setPrefWidth(70);
        
        HBox boxHora = new HBox(10);
        boxHora.getChildren().addAll(spinnerHora, spinnerMinuto);
        miGrilla.add(boxHora, 1, 5);
        // *********************************************************************
        Label lblEdad = new Label("Clasificacion de Edad");
        lblEdad.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblEdad, 0, 6);

        edades = new ToggleGroup();
        rbtMayorFuncion = new RadioButton("Mayores de edad");
        rbtMayorFuncion.setToggleGroup(edades);
        rbtMenorFuncion = new RadioButton("Menores de edad");
        rbtMenorFuncion.setToggleGroup(edades);
        HBox boxEdades = new HBox(15);
        boxEdades.getChildren().addAll(rbtMayorFuncion, rbtMenorFuncion);
        miGrilla.add(boxEdades, 1, 6);
        // *********************************************************************
        Label lblImagen = new Label("Imagen de la función: ");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblImagen, 0, 7);
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
        miGrilla.add(panelCajaBoton, 1, 7);

        imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 150);
        GridPane.setHalignment(imgPorDefecto, HPos.CENTER);
        GridPane.setValignment(imgPorDefecto, VPos.CENTER);
        miGrilla.add(imgPorDefecto, 2, 2, 1, 3);
        // *********************************************************************
        Button btnGrabar = new Button("¡Clic para crear la funcion!");
        btnGrabar.setMaxWidth(Double.MAX_VALUE);
        btnGrabar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnGrabar.setOnAction((e) -> {
            grabarFuncion();
        });
        miGrilla.add(btnGrabar, 1, 8);

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

    private Boolean obtenerRadioEdad() {
        if (rbtMayorFuncion.isSelected()) {
            return true;
        } else if (rbtMenorFuncion.isSelected()) {
            return false;
        }
        return null;
    }

    private void limpiarFormulario() {
        cajaNomFuncion.setText("");
        cajaNomFuncion.requestFocus();
        cajaImagen.setText("");
        cbmPeliculaFuncion.getSelectionModel().select(0);
        cbmSalaFuncion.getSelectionModel().select(0);
        dtpFechaFuncion.setValue(null);
        spinnerHora.getValueFactory().setValue(0);
        spinnerMinuto.getValueFactory().setValue(0);
        edades.selectToggle(null);
        miGrilla.getChildren().remove(imgPrevisualizar);
        GridPane.setHalignment(imgPorDefecto, HPos.CENTER);
        miGrilla.add(imgPorDefecto, 2, 2, 1, 3);
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

        Integer hora = spinnerHora.getValue();
        Integer minuto = spinnerMinuto.getValue();

        if (hora == null || minuto == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null,
                    "ERROR", "Debes seleccionar una hora válida para la función.");
            spinnerHora.requestFocus();
            return false;
        }
        
        if (edades.getSelectedToggle() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Debes seleccionar una clasificacion");
            return false;
        }
        
        if (rutaSeleccionada == null || rutaSeleccionada.isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null,
                "Ey", "Coloca una imagen");
            return false;
        }

        return true;
    }
    
    private void grabarFuncion() {
        if (formularioValido()) {
            FuncionDto dto = new FuncionDto();
            dto.setNombreFuncion(cajaNomFuncion.getText());
            dto.setIdPeliculaFuncion(obtenerComboPelicula());
            dto.setIdSalaFuncion(obtenerComboSala());
            dto.setFechaFuncion(dtpFechaFuncion.getValue());
            dto.setHoraFuncion(obtenerHoraFuncion());
            dto.setParaMayoresFuncion(obtenerRadioEdad());
            dto.setNombreImagenPublicoFuncion(cajaImagen.getText());
            
            if (FuncionControladorGrabar.crearFuncion(dto, rutaSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "EXITO", "FUNCION CREADA!!!!");
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "ERROR", "FUNCION NO CREADA!");
            }

            limpiarFormulario();
        }
    }

}
