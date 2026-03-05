package com.unimagdalena.vista.reserva;

import com.unimagdalena.controlador.cliente.ClienteControladorListar;
import com.unimagdalena.controlador.funcion.FuncionControladorListar;
import com.unimagdalena.controlador.reserva.ReservaControladorGrabar;
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.dto.ReservaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Formulario;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import com.unimagdalena.helpers.utilidad.SlideSwitch;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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

public class VistaReservaCrear extends StackPane {

    private static final int H_GAP = 10;
    private static final int V_GAP = 20;
    private static final int ALTO_FILA = 40;
    private static final int ALTO_CAJA = 35;
    private static final int TAMANYO_FUENTE = 18;
    private static final double AJUSTE_ARRIBA = 0.1;

    private final GridPane miGrilla;
    private final Rectangle miMarco;
    private final Stage miEscenario;

    private TextField cajaNomReserva; 
    private DatePicker dtpFechaReserva;
    private ComboBox<ClienteDto> cbmCliente;
    private ComboBox<FuncionDto> cbmFuncion;
    private SlideSwitch slideSwitchBebidas;
    private ToggleGroup grupoPago;
    private RadioButton rbtEfectivo, rbtTarjeta;

    private TextField cajaImagen;
    private ImageView imgPorDefecto;
    private ImageView imgPrevisualizar;
    private String rutaSeleccionada;

    public VistaReservaCrear(Stage esce, double ancho, double alto) {
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
        Text titulo = new Text("Formulario de Registro de Reserva");
        titulo.setFill(Color.web(Configuracion.COLOR_BORDE));
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
        // Campo: Nombre de la Reserva (Fila 1)
        Label lblNombreReserva = new Label("Nombre/Descripción: ");
        lblNombreReserva.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNombreReserva, 0, 1);

        cajaNomReserva = new TextField();
        cajaNomReserva.setPromptText("Ej: Reserva de Cumpleaños");
        cajaNomReserva.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaNomReserva, 1, 1);
        
        // Campo: Cliente (Fila 2)
        List<ClienteDto> arrClientes = ClienteControladorListar.obtenerTodos();
        ClienteDto clienteDefecto = new ClienteDto(0, "Seleccione un Cliente", null, null, false, "", null, "", "");
        arrClientes.add(0, clienteDefecto);

        Label lblCliente = new Label("Cliente: ");
        lblCliente.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblCliente, 0, 2);

        cbmCliente = new ComboBox<>();
        cbmCliente.setMaxWidth(Double.MAX_VALUE);
        cbmCliente.setPrefHeight(ALTO_CAJA);
        ObservableList<ClienteDto> itemsClientes = FXCollections.observableArrayList(arrClientes);
        cbmCliente.setItems(itemsClientes);
        cbmCliente.getSelectionModel().selectFirst();
        miGrilla.add(cbmCliente, 1, 2);

        // Campo: Función (Fila 3)
        List<FuncionDto> arrFunciones = FuncionControladorListar.arregloFunciones();
        FuncionDto funcionDefecto = new FuncionDto(0, "Seleccione una Función", null, null, null, null, false, "", "");
        arrFunciones.add(0, funcionDefecto);

        Label lblFuncion = new Label("Función Asignada: ");
        lblFuncion.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblFuncion, 0, 3);

        cbmFuncion = new ComboBox<>();
        cbmFuncion.setMaxWidth(Double.MAX_VALUE);
        cbmFuncion.setPrefHeight(ALTO_CAJA);
        ObservableList<FuncionDto> itemsFunciones = FXCollections.observableArrayList(arrFunciones);
        cbmFuncion.setItems(itemsFunciones);
        cbmFuncion.getSelectionModel().selectFirst();
        miGrilla.add(cbmFuncion, 1, 3);

        // Campo: Fecha de Reserva (Fila 4)
        Label lblFecha = new Label("Fecha de Reserva: ");
        lblFecha.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblFecha, 0, 4);

        dtpFechaReserva = new DatePicker();
        dtpFechaReserva.setMaxWidth(Double.MAX_VALUE);
        dtpFechaReserva.setPrefHeight(ALTO_CAJA);
        miGrilla.add(dtpFechaReserva, 1, 4);
        
        // Campo: Método de Pago (Fila 5)
        Label lblMetodo = new Label("Método de Pago: ");
        lblMetodo.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblMetodo, 0, 5);

        grupoPago = new ToggleGroup();
        rbtEfectivo = new RadioButton("Efectivo");
        rbtEfectivo.setToggleGroup(grupoPago);
        rbtTarjeta = new RadioButton("Tarjeta");
        rbtTarjeta.setToggleGroup(grupoPago);
        HBox boxPago = new HBox(15);
        boxPago.getChildren().addAll(rbtEfectivo, rbtTarjeta);
        miGrilla.add(boxPago, 1, 5);

        // Campo: Incluye Bebidas (Fila 6)
        Label lblBebidas = new Label("Incluye Bebidas: ");
        lblBebidas.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblBebidas, 0, 6);

        slideSwitchBebidas = new SlideSwitch(150, 40);
        slideSwitchBebidas.setTextos("INCLUYE", "NO INCLUYE");
        miGrilla.add(slideSwitchBebidas, 1, 6);

        // Campo: Imagen de la Reserva (Fila 7)
        Label lblImagen = new Label("Imagen de la Reserva: ");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblImagen, 0, 7);

        cajaImagen = new TextField();
        cajaImagen.setDisable(true);
        cajaImagen.setPrefHeight(ALTO_CAJA);
        String[] extensionesPermitidas = {"*.png", "*.jpg", "*.jpeg"};
        FileChooser objSeleccionar = Formulario.selectorImagen(
                "Busca una imagen para la reserva", "La image", extensionesPermitidas);
        Button btnEscogerImagen = new Button("+");
        btnEscogerImagen.setPrefHeight(ALTO_CAJA);
        btnEscogerImagen.setOnAction((e) -> {
            rutaSeleccionada = GestorImagen.obtenerRutaImagen(cajaImagen, objSeleccionar);
            if (rutaSeleccionada.isEmpty()) {
                mostrarImagenPorDefecto();
            } else {
                mostrarImagenPrevisualizada(rutaSeleccionada);
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

        // Botón Grabar (Fila 8)
        Button btnGrabar = new Button("¡Clic para crear la Reserva!");
        btnGrabar.setMaxWidth(Double.MAX_VALUE);
        btnGrabar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnGrabar.setOnAction((e) -> {
            grabarReserva();
        });
        miGrilla.add(btnGrabar, 1, 8);
    }

    private void mostrarImagenPrevisualizada(String ruta) {
        miGrilla.getChildren().remove(imgPorDefecto);
        if (imgPrevisualizar != null) {
            miGrilla.getChildren().remove(imgPrevisualizar);
        }
        imgPrevisualizar = Icono.previsualizar(ruta, 150);
        GridPane.setHalignment(imgPrevisualizar, HPos.CENTER);
        miGrilla.add(imgPrevisualizar, 2, 2, 1, 3);
    }

    private void mostrarImagenPorDefecto() {
        if (imgPrevisualizar != null) {
            miGrilla.getChildren().remove(imgPrevisualizar);
        }
        if (!miGrilla.getChildren().contains(imgPorDefecto)) {
            imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 150);
            GridPane.setHalignment(imgPorDefecto, HPos.CENTER);
            miGrilla.add(imgPorDefecto, 2, 2, 1, 3);
        }
    }

    private ClienteDto obtenerClienteSeleccionado() {
        ClienteDto seleccionado = cbmCliente.getSelectionModel().getSelectedItem();
        if (seleccionado != null && seleccionado.getIdCliente() != 0) {
            return seleccionado;
        }
        return null;
    }

    private FuncionDto obtenerFuncionSeleccionada() {
        FuncionDto seleccionado = cbmFuncion.getSelectionModel().getSelectedItem();
        if (seleccionado != null && seleccionado.getIdFuncion() != 0) {
            return seleccionado;
        }
        return null;
    }
    
    private String obtenerMetodoPago() {
        if (rbtEfectivo.isSelected()) {
            return "Efectivo";
        } else if (rbtTarjeta.isSelected()) {
            return "Tarjeta";
        }
        return null;
    }

    private Boolean formularioValido() {
        if (cajaNomReserva.getText().isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Debes escribir un nombre/descripción para la reserva.");
            cajaNomReserva.requestFocus();
            return false;
        }
        if (obtenerClienteSeleccionado() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "ERROR", "Escoge un Cliente.");
            cbmCliente.requestFocus();
            return false;
        }
        if (obtenerFuncionSeleccionada() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "ERROR", "Escoge una Función.");
            cbmFuncion.requestFocus();
            return false;
        }
        if (dtpFechaReserva.getValue() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Debes escoger una fecha.");
            dtpFechaReserva.requestFocus();
            return false;
        }
        if (obtenerMetodoPago() == null) {
             Mensaje.mostrar(Alert.AlertType.WARNING, null, "EY", "Debes seleccionar un método de pago.");
            return false;
        }
        if (rutaSeleccionada == null || rutaSeleccionada.isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "Ey", "Coloca una imagen");
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        cajaNomReserva.setText("");
        cbmCliente.getSelectionModel().select(0);
        cbmFuncion.getSelectionModel().select(0);
        dtpFechaReserva.setValue(null);
        grupoPago.selectToggle(null);
        slideSwitchBebidas.setEstado(false);
        rutaSeleccionada = "";
        cajaImagen.setText("");
        mostrarImagenPorDefecto();
        cajaNomReserva.requestFocus();
    }

    private void grabarReserva() {
        if (formularioValido()) {
            ReservaDto dto = new ReservaDto();
            dto.setNombreReserva(cajaNomReserva.getText()); 
            dto.setMetodoPagoReserva(obtenerMetodoPago());
            dto.setFechaReserva(dtpFechaReserva.getValue());
            dto.setIdClienteReserva(obtenerClienteSeleccionado());
            dto.setIdFuncionReserva(obtenerFuncionSeleccionada());
            dto.setIncluyeBebidasReserva(slideSwitchBebidas.isOn());
            dto.setNombreImagenPublicoReserva(cajaImagen.getText());

            if (ReservaControladorGrabar.crearReserva(dto, rutaSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "EXITO", "RESERVA CREADA!!!!");
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "ERROR", "RESERVA NO CREADA!");
            }

            limpiarFormulario();
        }
    }
}