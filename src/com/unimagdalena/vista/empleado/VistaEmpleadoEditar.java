package com.unimagdalena.vista.empleado;

import com.unimagdalena.controlador.empleado.EmpleadoControladorEditar;
import com.unimagdalena.controlador.empleado.EmpleadoControladorVista;
import com.unimagdalena.controlador.sala.SalaControladorListar;
import com.unimagdalena.dto.EmpleadoDto;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Contenedor;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Formulario;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.time.LocalTime;
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

public class VistaEmpleadoEditar extends SubScene {

    private final GridPane miGrilla;
    private final StackPane miFormulario;
    private final Stage laVentanaPrincipal;

    private static final int ALTO_CAJA = 35;
    private static final int TAMANYO_FUENTE = 18;

    private TextField cajaNomEmpleado;
    private ToggleGroup esTiempoCompEmleado;
    private RadioButton rbtsiEsTCEmpleado;
    private RadioButton rbtnoEsTCEmpleado;
    private CheckBox cbkBajoEmpleado;
    private CheckBox cbkMedioEmpleado;
    private CheckBox cbkAltoEmpleado;
    private ComboBox<SalaDto> cbmSalaEmpleado;
    
    // Spinners para manejar las horas
    private Spinner<Integer> spinnerHoraEntradaEmp;
    private Spinner<Integer> spinnerMinutoEntradaEmp;
    private Spinner<Integer> spinnerHoraSalidaEmp;
    private Spinner<Integer> spinnerMinutoSalidaEmp;

    private TextField cajaImagen;
    private ImageView imgPorDefecto;
    private ImageView imgPrevisualizar;

    private final int posicion;
    private final EmpleadoDto objEmpleado;
    private String rutaImagenSeleccionada;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    public VistaEmpleadoEditar(Stage ventanaPadre, BorderPane princ, Pane pane, double ancho,
            double alto, EmpleadoDto objEmpExterno, int posicionArchivo) {
        super(new StackPane(), ancho, alto);

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        miFormulario = (StackPane) getRoot();
        miFormulario.setBackground(fondo);
        miFormulario.setAlignment(Pos.CENTER);

        laVentanaPrincipal = ventanaPadre;

        posicion = posicionArchivo;
        objEmpleado = objEmpExterno;
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

        Text titulo = new Text("Formulario actualización de Empleados");
        titulo.setFill(Color.web("#E82E68"));
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));
        GridPane.setHalignment(titulo, HPos.CENTER);

        fila = 1;
        miGrilla.add(titulo, columna, fila, colSpan, rowSpan);
    }

    private void crearFormulario() {
        // --- Nombre ---
        Label lblNombre = new Label("Nombre del empleado: ");
        lblNombre.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNombre, 0, 2);

        cajaNomEmpleado = new TextField();
        cajaNomEmpleado.setText(objEmpleado.getNombreEmpleado());
        GridPane.setHgrow(cajaNomEmpleado, Priority.ALWAYS);
        cajaNomEmpleado.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaNomEmpleado, 1, 2);

        // --- Tiempo Completo ---
        Label lbltiempoC = new Label("¿Trabaja a tiempo completo?");
        lbltiempoC.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lbltiempoC, 0, 3);
        
        esTiempoCompEmleado = new ToggleGroup();
        rbtsiEsTCEmpleado = new RadioButton("Si");
        rbtsiEsTCEmpleado.setToggleGroup(esTiempoCompEmleado);
        rbtnoEsTCEmpleado = new RadioButton("No");
        rbtnoEsTCEmpleado.setToggleGroup(esTiempoCompEmleado);
        
        if (objEmpleado.getEsTiempoCompletoEmpleado()) {
            rbtsiEsTCEmpleado.setSelected(true);
        } else {
            rbtnoEsTCEmpleado.setSelected(true);
        }
        
        HBox box3D = new HBox(15); 
        box3D.getChildren().addAll(rbtsiEsTCEmpleado, rbtnoEsTCEmpleado);
        miGrilla.add(box3D, 1, 3);

        // --- Nivel Acceso ---
        Label lblNivelAcceso = new Label("Nivel de acceso: ");
        lblNivelAcceso.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNivelAcceso, 0, 4);
        
        cbkBajoEmpleado = new CheckBox("Bajo");
        cbkMedioEmpleado = new CheckBox("Medio");
        cbkAltoEmpleado = new CheckBox("Alto");
        
        String nivelActual = objEmpleado.getNivelAccesoEmpleado();
        if ("Bajo".equals(nivelActual)) cbkBajoEmpleado.setSelected(true);
        if ("Medio".equals(nivelActual)) cbkMedioEmpleado.setSelected(true);
        if ("Alto".equals(nivelActual)) cbkAltoEmpleado.setSelected(true);

        HBox boxcheck = new HBox(15); 
        boxcheck.getChildren().addAll(cbkBajoEmpleado, cbkMedioEmpleado, cbkAltoEmpleado);
        miGrilla.add(boxcheck, 1, 4);

        // --- Sala Asignada ---
        List<SalaDto> arrSalas = SalaControladorListar.arregloSalasDisponibles();
        SalaDto opcionPorDefecto = new SalaDto(0, "Seleccione una sala", null, false, 0, false, "", "");
        arrSalas.add(0, opcionPorDefecto);

        Label lblSala = new Label("Sala asignada: ");
        lblSala.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblSala, 0, 5);

        cbmSalaEmpleado = new ComboBox<>();
        cbmSalaEmpleado.setMaxWidth(Double.MAX_VALUE);
        cbmSalaEmpleado.setPrefHeight(ALTO_CAJA);
        ObservableList<SalaDto> items2 = FXCollections.observableArrayList(arrSalas);
        cbmSalaEmpleado.setItems(items2);
        
        SalaDto salaActual = objEmpleado.getSalaAsignadaEmpleado();
        if (salaActual != null) {
            boolean encontrado = false;
            for (SalaDto item : cbmSalaEmpleado.getItems()) {
                if (item.getIdSala() == salaActual.getIdSala()) {
                    cbmSalaEmpleado.setValue(item);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) cbmSalaEmpleado.getSelectionModel().selectFirst();
        } else {
            cbmSalaEmpleado.getSelectionModel().selectFirst();
        }
        miGrilla.add(cbmSalaEmpleado, 1, 5);

        // --- Hora Entrada ---
        Label lblHoraEnt = new Label("Hora Entrada (H-M): ");
        lblHoraEnt.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblHoraEnt, 0, 6);

        spinnerHoraEntradaEmp = crearSpinnerHora(objEmpleado.getHoraEntradaEmpleado().getHour());
        spinnerMinutoEntradaEmp = crearSpinnerMinuto(objEmpleado.getHoraEntradaEmpleado().getMinute());
        
        HBox boxHoraEnt = new HBox(10);
        boxHoraEnt.getChildren().addAll(spinnerHoraEntradaEmp, spinnerMinutoEntradaEmp);
        miGrilla.add(boxHoraEnt, 1, 6);

        // --- Hora Salida ---
        Label lblHoraSal = new Label("Hora Salida (H-M): ");
        lblHoraSal.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblHoraSal, 0, 7);

        spinnerHoraSalidaEmp = crearSpinnerHora(objEmpleado.getHoraSalidaEmpleado().getHour());
        spinnerMinutoSalidaEmp = crearSpinnerMinuto(objEmpleado.getHoraSalidaEmpleado().getMinute());
        
        HBox boxHoraSalida = new HBox(10);
        boxHoraSalida.getChildren().addAll(spinnerHoraSalidaEmp, spinnerMinutoSalidaEmp);
        miGrilla.add(boxHoraSalida, 1, 7);

        // --- Imagen ---
        Label lblImagen = new Label("Imagen del empleado: ");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblImagen, 0, 8);

        cajaImagen = new TextField();
        cajaImagen.setText(objEmpleado.getNombreImagenPublicoEmpleado());
        cajaImagen.setDisable(true);
        cajaImagen.setPrefHeight(ALTO_CAJA);
        
        String[] extensionesPermitidas = {"*.png", "*.jpg", "*.jpeg"};
        FileChooser objSeleccionar = Formulario.selectorImagen(
                "Busca una imagen", "La imagen", extensionesPermitidas);
        Button btnEscogerImagen = new Button("+");
        btnEscogerImagen.setPrefHeight(ALTO_CAJA);
        
        btnEscogerImagen.setOnAction((e) -> {
            rutaImagenSeleccionada = GestorImagen.obtenerRutaImagen(cajaImagen, objSeleccionar);
            miGrilla.getChildren().remove(imgPorDefecto);
            miGrilla.getChildren().remove(imgPrevisualizar);
            imgPrevisualizar = Icono.previsualizar(rutaImagenSeleccionada, 150);

            GridPane.setHalignment(imgPrevisualizar, HPos.CENTER);
            miGrilla.add(imgPrevisualizar, 1, 9);
        });
        HBox.setHgrow(cajaImagen, Priority.ALWAYS);
        HBox panelCajaBoton = new HBox(2);
        panelCajaBoton.setAlignment(Pos.BOTTOM_RIGHT);
        panelCajaBoton.getChildren().addAll(cajaImagen, btnEscogerImagen);
        miGrilla.add(panelCajaBoton, 1, 8);

        imgPorDefecto = Icono.obtenerFotosExternas(objEmpleado.getNombreImagenPrivadoEmpleado(), 150);
        GridPane.setHalignment(imgPorDefecto, HPos.CENTER);
        miGrilla.add(imgPorDefecto, 1, 9);

        // --- Botones ---
        Button btnActualizar = new Button("¡Clic para actualizar!");
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setTextFill(Color.web(Configuracion.COLOR4));
        btnActualizar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnActualizar.setOnAction((e) -> {
            actualizarEmpleado();
        });
        miGrilla.add(btnActualizar, 1, 10);

        Button btnRegresar = new Button("Regresar");
        btnRegresar.setMaxWidth(Double.MAX_VALUE);
        btnRegresar.setTextFill(Color.web("#6C3483"));
        btnRegresar.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        miGrilla.add(btnRegresar, 1, 11);
        
        btnRegresar.setOnAction((ActionEvent e) -> {
            panelCuerpo = EmpleadoControladorVista.abrirAdministrar(
                    laVentanaPrincipal,
                    panelPrincipal, panelCuerpo,
                    Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor());
            panelPrincipal.setCenter(null);
            panelPrincipal.setCenter(panelCuerpo);
        });
    }

    // Metodos auxiliares para crear spinners
    private Spinner<Integer> crearSpinnerHora(int valorInicial) {
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, valorInicial);
        // Formatear para mostrar 2 dígitos (ej: 09 en vez de 9)
        factory.setConverter(new javafx.util.StringConverter<Integer>() {
            @Override public String toString(Integer value) { return String.format("%02d", value); }
            @Override public Integer fromString(String string) { return Integer.valueOf(string); }
        });
        spinner.setValueFactory(factory);
        spinner.setPrefHeight(ALTO_CAJA);
        spinner.setPrefWidth(70);
        return spinner;
    }

    private Spinner<Integer> crearSpinnerMinuto(int valorInicial) {
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, valorInicial);
        factory.setConverter(new javafx.util.StringConverter<Integer>() {
            @Override public String toString(Integer value) { return String.format("%02d", value); }
            @Override public Integer fromString(String string) { return Integer.valueOf(string); }
        });
        spinner.setValueFactory(factory);
        spinner.setPrefHeight(ALTO_CAJA);
        spinner.setPrefWidth(70);
        return spinner;
    }

    private void actualizarEmpleado() {
        if (formularioValido()) {
            EmpleadoDto dto = new EmpleadoDto();
            dto.setIdEmpleado(objEmpleado.getIdEmpleado());
            dto.setNombreEmpleado(cajaNomEmpleado.getText());
            dto.setEsTiempoCompletoEmpleado(obtenerRadioTiempoCompleto());
            dto.setNivelAccesoEmpleado(obtenerResultadoCheck());
            dto.setSalaAsignadaEmpleado(obtenerComboSala());
            dto.setHoraEntradaEmpleado(LocalTime.of(spinnerHoraEntradaEmp.getValue(), spinnerMinutoEntradaEmp.getValue()));
            dto.setHoraSalidaEmpleado(LocalTime.of(spinnerHoraSalidaEmp.getValue(), spinnerMinutoSalidaEmp.getValue()));
            
            dto.setNombreImagenPublicoEmpleado(cajaImagen.getText());
            dto.setNombreImagenPrivadoEmpleado(objEmpleado.getNombreImagenPrivadoEmpleado());

            if (EmpleadoControladorEditar.actualizar(posicion, dto, rutaImagenSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "EXITO", "EMPLEADO ACTUALIZADO!!!!");
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "ERROR", "EMPLEADO NO ACTUALIZADO!");
            }
        }
    }

    private Boolean formularioValido() {
        if (cajaNomEmpleado.getText().isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Debes escribir el nombre.");
            cajaNomEmpleado.requestFocus();
            return false;
        }
        if (esTiempoCompEmleado.getSelectedToggle() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Selecciona si es tiempo completo.");
            return false;
        }
        String check = obtenerResultadoCheck();
        if (check == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Selecciona un nivel de acceso.");
            return false;
        }
        // Validar que solo haya un check seleccionado
        int seleccionados = 0;
        if(cbkBajoEmpleado.isSelected()) seleccionados++;
        if(cbkMedioEmpleado.isSelected()) seleccionados++;
        if(cbkAltoEmpleado.isSelected()) seleccionados++;
        if(seleccionados > 1){
             Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Selecciona SOLO UN nivel de acceso.");
             return false;
        }

        if (cbmSalaEmpleado.getSelectionModel().getSelectedIndex() == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "ERROR", "Escoge una sala válida.");
            cbmSalaEmpleado.requestFocus();
            return false;
        }
        return true;
    }

    private Boolean obtenerRadioTiempoCompleto() {
        if (rbtsiEsTCEmpleado.isSelected()) return true;
        if (rbtnoEsTCEmpleado.isSelected()) return false;
        return null;
    }

    private String obtenerResultadoCheck() {
        if (cbkBajoEmpleado.isSelected()) return "Bajo";
        if (cbkMedioEmpleado.isSelected()) return "Medio";
        if (cbkAltoEmpleado.isSelected()) return "Alto";
        return null;
    }

    private SalaDto obtenerComboSala() {
        SalaDto seleccionado = cbmSalaEmpleado.getSelectionModel().getSelectedItem();
        if (seleccionado != null && seleccionado.getIdSala() != 0) {
            return seleccionado;
        }
        return null;
    }
}