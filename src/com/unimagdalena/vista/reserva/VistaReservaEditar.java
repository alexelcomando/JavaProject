package com.unimagdalena.vista.reserva;

import com.unimagdalena.controlador.cliente.ClienteControladorListar;
import com.unimagdalena.controlador.funcion.FuncionControladorListar;
import com.unimagdalena.controlador.reserva.ReservaControladorEditar;
import com.unimagdalena.controlador.reserva.ReservaControladorVista;
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.dto.ReservaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Contenedor;
import com.unimagdalena.helpers.utilidad.Fondo;
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

public class VistaReservaEditar extends SubScene {

    private final GridPane miGrilla;
    private final StackPane miFormulario;
    private final Stage laVentanaPrincipal;

    private static final int ALTO_CAJA = 35;
    private static final int TAMANYO_FUENTE = 18;

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

    private final int posicion;
    private final ReservaDto objReserva;
    private String rutaImagenSeleccionada;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    public VistaReservaEditar(Stage ventanaPadre, BorderPane princ, Pane pane, double ancho,
            double alto, ReservaDto objReservaExterna, int posicionArchivo) {
        super(new StackPane(), ancho, alto);

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        miFormulario = (StackPane) getRoot();
        miFormulario.setBackground(fondo);
        miFormulario.setAlignment(Pos.CENTER);

        laVentanaPrincipal = ventanaPadre;

        posicion = posicionArchivo;
        objReserva = objReservaExterna;
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

        Text titulo = new Text("Formulario actualización de Reserva");
        titulo.setFill(Color.web("#E82E68"));
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));
        GridPane.setHalignment(titulo, HPos.CENTER);

        fila = 1;
        miGrilla.add(titulo, columna, fila, colSpan, rowSpan);
    }

    private void crearFormulario() {
        // Campo: Nombre de la Reserva (Fila 2)
        Label lblNombreReserva = new Label("Nombre/Descripción: ");
        lblNombreReserva.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNombreReserva, 0, 2);

        cajaNomReserva = new TextField(objReserva.getNombreReserva());
        cajaNomReserva.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaNomReserva, 1, 2);
        
        // Campo: Cliente (Fila 3)
        List<ClienteDto> arrClientes = ClienteControladorListar.obtenerTodos();
        ClienteDto clienteDefecto = new ClienteDto(0, "Seleccione un Cliente", null, null, false, "", null, "", "");
        arrClientes.add(0, clienteDefecto);

        Label lblCliente = new Label("Cliente: ");
        lblCliente.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblCliente, 0, 3);

        cbmCliente = new ComboBox<>();
        cbmCliente.setMaxWidth(Double.MAX_VALUE);
        cbmCliente.setPrefHeight(ALTO_CAJA);
        cbmCliente.setItems(FXCollections.observableArrayList(arrClientes));
        seleccionarClienteFuncion(cbmCliente, objReserva.getIdClienteReserva().getIdCliente(), clienteDefecto);
        miGrilla.add(cbmCliente, 1, 3);

        // Campo: Función (Fila 4)
        List<FuncionDto> arrFunciones = FuncionControladorListar.arregloFunciones();
        FuncionDto funcionDefecto = new FuncionDto(0, "Seleccione una Función", null, null, null, null, false, "", "");
        arrFunciones.add(0, funcionDefecto);

        Label lblFuncion = new Label("Función Asignada: ");
        lblFuncion.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblFuncion, 0, 4);

        cbmFuncion = new ComboBox<>();
        cbmFuncion.setMaxWidth(Double.MAX_VALUE);
        cbmFuncion.setPrefHeight(ALTO_CAJA);
        cbmFuncion.setItems(FXCollections.observableArrayList(arrFunciones));
        seleccionarClienteFuncion(cbmFuncion, objReserva.getIdFuncionReserva().getIdFuncion(), funcionDefecto);
        miGrilla.add(cbmFuncion, 1, 4);

        // Campo: Fecha de Reserva (Fila 5)
        Label lblFecha = new Label("Fecha de Reserva: ");
        lblFecha.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblFecha, 0, 5);

        dtpFechaReserva = new DatePicker(objReserva.getFechaReserva());
        dtpFechaReserva.setMaxWidth(Double.MAX_VALUE);
        dtpFechaReserva.setPrefHeight(ALTO_CAJA);
        miGrilla.add(dtpFechaReserva, 1, 5);

        // Campo: Método de Pago (Fila 6)
        Label lblMetodo = new Label("Método de Pago: ");
        lblMetodo.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblMetodo, 0, 6);

        grupoPago = new ToggleGroup();
        rbtEfectivo = new RadioButton("Efectivo");
        rbtEfectivo.setToggleGroup(grupoPago);
        rbtTarjeta = new RadioButton("Tarjeta");
        rbtTarjeta.setToggleGroup(grupoPago);

        if ("Efectivo".equals(objReserva.getMetodoPagoReserva())) {
            rbtEfectivo.setSelected(true);
        } else if ("Tarjeta".equals(objReserva.getMetodoPagoReserva())) {
            rbtTarjeta.setSelected(true);
        }

        HBox boxPago = new HBox(15);
        boxPago.getChildren().addAll(rbtEfectivo, rbtTarjeta);
        miGrilla.add(boxPago, 1, 6);

        // Campo: Incluye Bebidas (Fila 7)
        Label lblBebidas = new Label("Incluye Bebidas: ");
        lblBebidas.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblBebidas, 0, 7);

        slideSwitchBebidas = new SlideSwitch(150, 40);
        slideSwitchBebidas.setTextos("INCLUYE", "NO INCLUYE");
        slideSwitchBebidas.setEstado(objReserva.getIncluyeBebidasReserva());
        miGrilla.add(slideSwitchBebidas, 1, 7);

        // Campo: Imagen de la Reserva (Fila 8)
        Label lblImagen = new Label("Imagen de la Reserva: ");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblImagen, 0, 8);

        cajaImagen = new TextField(objReserva.getNombreImagenPublicoReserva());
        cajaImagen.setDisable(true);
        cajaImagen.setPrefHeight(ALTO_CAJA);

        String[] extensionesPermitidas = {"*.png", "*.jpg", "*.jpeg"};
        FileChooser objSeleccionar = Formulario.selectorImagen(
                "Busca una imagen para la reserva", "La image", extensionesPermitidas);

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

        imgPorDefecto = Icono.obtenerFotosExternas(objReserva.getNombreImagenPrivadoReserva(), 150);
        GridPane.setHalignment(imgPorDefecto, HPos.CENTER);
        miGrilla.add(imgPorDefecto, 1, 9);

        // Botón Actualizar (Fila 10)
        Button btnActualizar = new Button("¡Clic para actualizar la Reserva!");
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setTextFill(Color.web(Configuracion.COLOR4));
        btnActualizar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnActualizar.setOnAction((e) -> {
            actualizarReserva();
        });
        miGrilla.add(btnActualizar, 1, 10);

        // Botón Regresar (Fila 11)
        Button btnRegresar = new Button("Regresar");
        btnRegresar.setMaxWidth(Double.MAX_VALUE);
        btnRegresar.setTextFill(Color.web("#6C3483"));
        btnRegresar.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        miGrilla.add(btnRegresar, 1, 11);
        btnRegresar.setOnAction((ActionEvent e) -> {
            regresarAAdministrar();
        });
    }

    private <T> void seleccionarClienteFuncion(ComboBox<T> cbm, int idDeseado, T defecto) {
        for (T item : cbm.getItems()) {
            if (item instanceof ClienteDto) {
                if (((ClienteDto) item).getIdCliente() == idDeseado) {
                    cbm.setValue(item);
                    return;
                }
            }
            if (item instanceof FuncionDto) {
                if (((FuncionDto) item).getIdFuncion() == idDeseado) {
                    cbm.setValue(item);
                    return;
                }
            }
        }
        cbm.setValue(defecto);
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
        return true;
    }

    private void actualizarReserva() {
        if (formularioValido()) {
            ReservaDto dto = new ReservaDto();
            dto.setIdReserva(objReserva.getIdReserva());
            dto.setNombreReserva(cajaNomReserva.getText());
            dto.setMetodoPagoReserva(obtenerMetodoPago());
            dto.setFechaReserva(dtpFechaReserva.getValue());
            dto.setIdClienteReserva(obtenerClienteSeleccionado());
            dto.setIdFuncionReserva(obtenerFuncionSeleccionada());
            dto.setIncluyeBebidasReserva(slideSwitchBebidas.isOn());
            dto.setNombreImagenPublicoReserva(cajaImagen.getText());
            dto.setNombreImagenPrivadoReserva(objReserva.getNombreImagenPrivadoReserva());

            if (ReservaControladorEditar.actualizar(posicion, dto, rutaImagenSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "EXITO", "RESERVA ACTUALIZADA!!!!");
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "ERROR", "RESERVA NO ACTUALIZADA!");
            }
            regresarAAdministrar();
        }
    }

    private void regresarAAdministrar() {
        panelCuerpo = ReservaControladorVista.abrirAdministrar(
                laVentanaPrincipal,
                panelPrincipal, panelCuerpo,
                Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor());
        panelPrincipal.setCenter(null);
        panelPrincipal.setCenter(panelCuerpo);
    }
}