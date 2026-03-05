package com.unimagdalena.vista.empleado;

import com.unimagdalena.controlador.empleado.EmpleadoControladorGrabar;
import com.unimagdalena.controlador.sala.SalaControladorListar;
import com.unimagdalena.dto.EmpleadoDto;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.model.Empleado;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Formulario;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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

public class VistaEmpleadoCrear extends StackPane {

    private static final int H_GAP = 10;
    private static final int V_GAP = 20;

    private static final int ALTO_FILA = 40;
    private static final int ALTO_CAJA = 35;

    private static final int TAMANYO_FUENTE = 18;
    private static final double AJUSTE_ARRIBA = 0.2;

    private final GridPane miGrilla;
    private final Rectangle miMarco;
    private final Stage miEscenario;

    private TextField cajaNomEmpleado;
    private ToggleGroup esTiempoCompEmleado;
    private RadioButton rbtsiEsTCEmpleado;
    private RadioButton rbtnoEsTCEmpleado;
    private CheckBox cbkBajoEmpleado;
    private CheckBox cbkMedioEmpleado;
    private CheckBox cbkAltoEmpleado;
    private ComboBox<SalaDto> cbmSalaEmpleado;
    private Spinner<Integer> spinnerHoraEntradaEmp;
    private Spinner<Integer> spinnerMinutoEntradaEmp;
    private Spinner<Integer> spinnerHoraSalidaEmp;
    private Spinner<Integer> spinnerMinutoSalidaEmp;

    // Estas propiedades son para las imagenes
    private TextField cajaImagen;
    private ImageView imgPorDefecto;
    private ImageView imgPrevisualizar;
    private String rutaSeleccionada;

    public VistaEmpleadoCrear(Stage esce, double ancho, double alto) {
        setAlignment(Pos.CENTER);
        miGrilla = new GridPane();
        miEscenario = esce;
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);

        getChildren().add(miMarco);
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

        for (int j = 0; j < 9; j++) {
            RowConstraints fila = new RowConstraints();
            fila.setMinHeight(ALTO_FILA);
            fila.setPrefHeight(ALTO_FILA);
            miGrilla.getRowConstraints().add(fila);
        }
    }

    private void pintarTitulo() {
        Text titulo = new Text("Formulario registro de empleados");
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        GridPane.setHalignment(titulo, HPos.CENTER);
        GridPane.setMargin(titulo, new Insets(25, 0, 0, 0));
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
        Label lblNombre = new Label("Nombre del empleado: ");
        lblNombre.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNombre, 0, 1);

        cajaNomEmpleado = new TextField();
        cajaNomEmpleado.setPromptText("Coloca el nombre del empleado");
        GridPane.setHgrow(cajaNomEmpleado, Priority.ALWAYS);
        cajaNomEmpleado.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaNomEmpleado, 1, 1);
        // *********************************************************************
        Label lbltiempoC = new Label("¿Trabaja a tiempo completo?");
        lbltiempoC.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lbltiempoC, 0, 2);

        esTiempoCompEmleado = new ToggleGroup();
        rbtsiEsTCEmpleado = new RadioButton("Si");
        rbtsiEsTCEmpleado.setToggleGroup(esTiempoCompEmleado);
        rbtnoEsTCEmpleado = new RadioButton("No");
        rbtnoEsTCEmpleado.setToggleGroup(esTiempoCompEmleado);
        HBox box3D = new HBox(15);
        box3D.getChildren().addAll(rbtsiEsTCEmpleado, rbtnoEsTCEmpleado);
        miGrilla.add(box3D, 1, 2);
        // *********************************************************************
        Label lblNivelAcceso = new Label("Nivel de acceso del empleado: ");
        lblNivelAcceso.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNivelAcceso, 0, 3);

        cbkBajoEmpleado = new CheckBox("Bajo");
        cbkMedioEmpleado = new CheckBox("Medio");
        cbkAltoEmpleado = new CheckBox("Alto");
        HBox boxcheck = new HBox(15);
        boxcheck.getChildren().addAll(cbkBajoEmpleado, cbkMedioEmpleado, cbkAltoEmpleado);
        miGrilla.add(boxcheck, 1, 3);
        // *********************************************************************
        List<SalaDto> arrSalas = SalaControladorListar.arregloSalasDisponibles();
        SalaDto opcionPorDefecto = new SalaDto(0, "Seleccione una sala", null, false, 0, false, "", "");
        arrSalas.add(0, opcionPorDefecto);

        Label lblSala = new Label("Sala asignada al empleado: ");
        lblSala.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblSala, 0, 4);

        cbmSalaEmpleado = new ComboBox<>();
        cbmSalaEmpleado.setMaxWidth(Double.MAX_VALUE);
        cbmSalaEmpleado.setPrefHeight(ALTO_CAJA);
        ObservableList<SalaDto> items2 = FXCollections.observableArrayList(arrSalas);
        cbmSalaEmpleado.setItems(items2);
        cbmSalaEmpleado.getSelectionModel().selectFirst();
        miGrilla.add(cbmSalaEmpleado, 1, 4);
        // *********************************************************************
        Label lblHoraEnt = new Label("Hora de entrada del empleado(Hora-Minuto): ");
        lblHoraEnt.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblHoraEnt, 0, 5);

        spinnerHoraEntradaEmp = new Spinner<>();
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
        spinnerHoraEntradaEmp.setValueFactory(hourFactory);
        spinnerHoraEntradaEmp.setPrefHeight(ALTO_CAJA);
        spinnerHoraEntradaEmp.setPrefWidth(70);

        spinnerMinutoEntradaEmp = new Spinner<>();
        SpinnerValueFactory<Integer> minFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);

        minFactory.setConverter(new javafx.util.StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                if (value == null) {
                    return "00";
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
                    if (value > 59) {
                        return 59;
                    }
                    return value;
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        });

        spinnerMinutoEntradaEmp.setValueFactory(minFactory);
        spinnerMinutoEntradaEmp.setPrefHeight(ALTO_CAJA);
        spinnerMinutoEntradaEmp.setPrefWidth(70);

        HBox boxHoraEnt = new HBox(10);
        boxHoraEnt.getChildren().addAll(spinnerHoraEntradaEmp, spinnerMinutoEntradaEmp);
        miGrilla.add(boxHoraEnt, 1, 5);
        // *********************************************************************
        Label lblHoraSal = new Label("Hora de salida del empleado(Hora-Minuto): ");
        lblHoraSal.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblHoraSal, 0, 6);

        spinnerHoraSalidaEmp = new Spinner<>();
        SpinnerValueFactory<Integer> hourFactory2
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);
        hourFactory2.setConverter(new javafx.util.StringConverter<Integer>() {
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
        spinnerHoraSalidaEmp.setValueFactory(hourFactory2);
        spinnerHoraSalidaEmp.setPrefHeight(ALTO_CAJA);
        spinnerHoraSalidaEmp.setPrefWidth(70);

        spinnerMinutoSalidaEmp = new Spinner<>();
        SpinnerValueFactory<Integer> minFactory2
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);

        minFactory2.setConverter(new javafx.util.StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                if (value == null) {
                    return "00";
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
                    if (value > 59) {
                        return 59;
                    }
                    return value;
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        });

        spinnerMinutoSalidaEmp.setValueFactory(minFactory2);
        spinnerMinutoSalidaEmp.setPrefHeight(ALTO_CAJA);
        spinnerMinutoSalidaEmp.setPrefWidth(70);

        HBox boxHoraSalida = new HBox(10);
        boxHoraSalida.getChildren().addAll(spinnerHoraSalidaEmp, spinnerMinutoSalidaEmp);
        miGrilla.add(boxHoraSalida, 1, 6);
        // *********************************************************************
        Label lblImagen = new Label("Imagen del empleado: ");
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
        Button btnGrabar = new Button("¡Clic para registrar el empleado!");
        btnGrabar.setMaxWidth(Double.MAX_VALUE);
        btnGrabar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnGrabar.setOnAction((e) -> {
            grabarEmpleado();
        });
        miGrilla.add(btnGrabar, 1, 8);

    }

    private Boolean obtenerRadioTiempoCompleto() {
        if (rbtsiEsTCEmpleado.isSelected()) {
            return true;
        } else if (rbtnoEsTCEmpleado.isSelected()) {
            return false;
        }
        return null;
    }

    private String obtenerResultadoCheck() {
        if (cbkBajoEmpleado.isSelected()) {
            return "Bajo";
        }
        if (cbkMedioEmpleado.isSelected()) {
            return "Medio";
        }
        if (cbkAltoEmpleado.isSelected()) {
            return "Alto";
        }
        return null;
    }

    private SalaDto obtenerComboSala() {
        SalaDto seleccionado = cbmSalaEmpleado.getSelectionModel().getSelectedItem();
        if (seleccionado != null && seleccionado.getIdSala() != 0) {
            return seleccionado;
        }
        return null;
    }

    private LocalTime obtenerHoraEntradaEmpleado() {
        int hora = spinnerHoraEntradaEmp.getValue();
        int minuto = spinnerMinutoEntradaEmp.getValue();
        return LocalTime.of(hora, minuto);
    }

    private LocalTime obtenerHoraSalidaEmpleado() {
        int hora = spinnerHoraSalidaEmp.getValue();
        int minuto = spinnerMinutoSalidaEmp.getValue();
        return LocalTime.of(hora, minuto);
    }

    private void limpiarFormulario() {
        cajaNomEmpleado.setText("");
        cajaNomEmpleado.requestFocus();
        esTiempoCompEmleado.selectToggle(null);
        cbkBajoEmpleado.setSelected(false);
        cbkMedioEmpleado.setSelected(false);
        cbkAltoEmpleado.setSelected(false);
        cbmSalaEmpleado.getSelectionModel().select(0);
        spinnerHoraEntradaEmp.getValueFactory().setValue(0);
        spinnerMinutoEntradaEmp.getValueFactory().setValue(0);
        spinnerHoraSalidaEmp.getValueFactory().setValue(0);
        spinnerMinutoSalidaEmp.getValueFactory().setValue(0);
        cajaImagen.setText("");
        miGrilla.getChildren().remove(imgPrevisualizar);
        GridPane.setHalignment(imgPorDefecto, HPos.CENTER);
        miGrilla.add(imgPorDefecto, 2, 2, 1, 3);
    }

    private Boolean formularioValido() {
        if (cajaNomEmpleado.getText().isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Lease 100 años de seriedad.\n"
                    + "Debes escribir algo en la caja.");
            cajaNomEmpleado.requestFocus();
            return false;
        }

        if (esTiempoCompEmleado.getSelectedToggle() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Debes seleccionar si el trabajo del empleado es o no a tiempo completo");
            return false;
        }

        String resultadoCheck = obtenerResultadoCheck();
        if (resultadoCheck == null || resultadoCheck.isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY",
                    "Debes seleccionar un solo nivel de acceso");
            return false;
        }

        int totalChecks = 0;
        if (cbkBajoEmpleado.isSelected()) {
            totalChecks++;
        }
        if (cbkMedioEmpleado.isSelected()) {
            totalChecks++;
        }
        if (cbkAltoEmpleado.isSelected()) {
            totalChecks++;
        }

        if (totalChecks == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY",
                    "Debes seleccionar un nivel de acceso.");
            return false;
        }

        if (totalChecks > 1) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY",
                    "Solo debes seleccionar un nivel de acceso.");
            return false;
        }

        if (cbmSalaEmpleado.getSelectionModel().getSelectedIndex() == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null,
                    "ERROR", "Escoge una sala asignada al empleado");
            cbmSalaEmpleado.requestFocus();
            return false;
        }

        Integer hora = spinnerHoraEntradaEmp.getValue();
        Integer minuto = spinnerMinutoEntradaEmp.getValue();
        if (hora == null || minuto == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null,
                    "ERROR", "Debes seleccionar una hora válida para la entrada del empleado.");
            spinnerHoraEntradaEmp.requestFocus();
            spinnerMinutoEntradaEmp.requestFocus();
            return false;
        }

        Integer hora2 = spinnerHoraSalidaEmp.getValue();
        Integer minuto2 = spinnerMinutoSalidaEmp.getValue();
        if (hora2 == null || minuto2 == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null,
                    "ERROR", "Debes seleccionar una hora válida para la salida del empleado.");
            spinnerHoraSalidaEmp.requestFocus();
            spinnerMinutoSalidaEmp.requestFocus();
            return false;
        }

        if (rutaSeleccionada == null || rutaSeleccionada.isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null,
                    "Ey", "Coloca una imagen");
            return false;
        }

        return true;
    }

    private void grabarEmpleado() {
        if (formularioValido()) {
            EmpleadoDto dto = new EmpleadoDto();
            dto.setNombreEmpleado(cajaNomEmpleado.getText());
            dto.setEsTiempoCompletoEmpleado(obtenerRadioTiempoCompleto());
            dto.setNivelAccesoEmpleado(obtenerResultadoCheck());
            dto.setSalaAsignadaEmpleado(obtenerComboSala());
            dto.setHoraEntradaEmpleado(obtenerHoraEntradaEmpleado());
            dto.setHoraSalidaEmpleado(obtenerHoraSalidaEmpleado());
            dto.setNombreImagenPublicoEmpleado(cajaImagen.getText());

            if (EmpleadoControladorGrabar.crearEmpleado(dto, rutaSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "EXITO", "EMPLEADO REGISTRADO!!!!");
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "ERROR", "EMPLEADO NO REGISTRADO!");
            }

            limpiarFormulario();
        }
    }

}
