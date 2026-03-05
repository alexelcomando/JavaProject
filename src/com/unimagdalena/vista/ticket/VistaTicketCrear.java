package com.unimagdalena.vista.ticket;

import com.unimagdalena.controlador.ticket.TicketControladorGrabar;
import com.unimagdalena.controlador.cliente.ClienteControladorListar;
import com.unimagdalena.dto.TicketDto;
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class VistaTicketCrear extends StackPane {

    private static final int H_GAP = 10;
    private static final int V_GAP = 20;
    private static final int ALTO_FILA = 40;
    private static final int ALTO_CAJA = 35;
    private static final int TAMANYO_FUENTE = 18;

    private final GridPane miGrilla;
    private final Rectangle miMarco;
    private final Stage miEscenario;

    private CheckBox chkGeneral;
    private CheckBox chkVIP;
    private CheckBox chkPreferencial;
    private CheckBox chkEstudiante;
    private CheckBox chkTerceraEdad;

    private ComboBox<ClienteDto> cbmCliente;
    private RadioButton rbValido;
    private ToggleGroup grupoValido;
    private DatePicker dpFechaEmision;
    private TextField cajaPrecio;
    private TextField cajaImagen;
    private ImageView imgPorDefecto;
    private ImageView imgPrevisualizar;
    private StackPane contenedorImagen;
    private String rutaSeleccionada;

    public VistaTicketCrear(Stage esce, double ancho, double alto) {
        this.miEscenario = esce;
        rutaSeleccionada = "";

        setAlignment(Pos.CENTER);

        miGrilla = new GridPane();
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE,
                Configuracion.MARCO_ANCHO_PORCENTAJE, Configuracion.DEGRADEE_ARREGLO,
                Configuracion.COLOR_BORDE);

        getChildren().add(miMarco);

        configurarLaGrilla();
        pintarTitulo();
        pintarFormulario();
        getChildren().add(miGrilla);
    }

    private void configurarLaGrilla() {
        miGrilla.setHgap(H_GAP);
        miGrilla.setVgap(V_GAP);
        miGrilla.setAlignment(Pos.TOP_CENTER);
        miGrilla.setPadding(new Insets(20));

        miGrilla.prefWidthProperty().bind(miMarco.widthProperty().multiply(0.9));
        miGrilla.maxWidthProperty().bind(miMarco.widthProperty().multiply(0.9));

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
        Text titulo = new Text("Crear Ticket de Entrada");
        titulo.setFill(Color.web(Configuracion.COLOR_BORDE));
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        GridPane.setHalignment(titulo, HPos.CENTER);
        GridPane.setMargin(titulo, new Insets(10, 0, 0, 0));
        miGrilla.add(titulo, 0, 0, 3, 1);
    }

    private void pintarFormulario() {
        // Tipo de entrada con CheckBoxes
        Label lblTipoEntrada = new Label("Tipo de entrada:");
        lblTipoEntrada.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblTipoEntrada, 0, 1);

        chkGeneral = new CheckBox("General");
        chkVIP = new CheckBox("VIP");
        chkPreferencial = new CheckBox("Preferencial");
        chkEstudiante = new CheckBox("Estudiante");
        chkTerceraEdad = new CheckBox("Tercera Edad");

        String estiloCheckbox = "-fx-font-size: 14px;";
        chkGeneral.setStyle(estiloCheckbox);
        chkVIP.setStyle(estiloCheckbox);
        chkPreferencial.setStyle(estiloCheckbox);
        chkEstudiante.setStyle(estiloCheckbox);
        chkTerceraEdad.setStyle(estiloCheckbox);

        CheckBox[] todos = {chkGeneral, chkVIP, chkPreferencial, chkEstudiante, chkTerceraEdad};
        for (CheckBox chk : todos) {
            chk.setOnAction(e -> {
                if (chk.isSelected()) {
                    for (CheckBox otro : todos) {
                        if (otro != chk) {
                            otro.setSelected(false);
                        }
                    }
                }
            });
        }

        VBox columna1 = new VBox(8, chkGeneral, chkVIP, chkPreferencial);
        VBox columna2 = new VBox(8, chkEstudiante, chkTerceraEdad);
        HBox panelCheckboxes = new HBox(20, columna1, columna2);
        panelCheckboxes.setAlignment(Pos.CENTER_LEFT);

        miGrilla.add(panelCheckboxes, 1, 1, 1, 2);

        // Cliente
        Label lblCliente = new Label("Cliente:");
        lblCliente.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblCliente, 0, 3);

        cbmCliente = new ComboBox<>();
        List<ClienteDto> clientes = ClienteControladorListar.obtenerTodos();
        if (clientes.isEmpty()) {
            ClienteDto sinCliente = new ClienteDto();
            sinCliente.setIdCliente(0);
            sinCliente.setNombreCliente("No hay clientes registrados");
            cbmCliente.getItems().add(sinCliente);
        } else {
            cbmCliente.getItems().addAll(clientes);
        }
        cbmCliente.setPromptText("Seleccione un cliente");
        cbmCliente.setPrefHeight(ALTO_CAJA);
        cbmCliente.setPrefWidth(Double.MAX_VALUE);
        miGrilla.add(cbmCliente, 1, 3);

        // Es válido
        Label lblValido = new Label("Es válido:");
        lblValido.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblValido, 0, 4);

        grupoValido = new ToggleGroup();
        rbValido = new RadioButton("Sí");
        rbValido.setToggleGroup(grupoValido);
        rbValido.setSelected(true);
        RadioButton rbNoValido = new RadioButton("No");
        rbNoValido.setToggleGroup(grupoValido);

        HBox panelValido = new HBox(10, rbValido, rbNoValido);
        miGrilla.add(panelValido, 1, 4);

        // Fecha
        Label lblFechaEmision = new Label("Fecha de emisión:");
        lblFechaEmision.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblFechaEmision, 0, 5);

        dpFechaEmision = new DatePicker();
        dpFechaEmision.setValue(LocalDate.now());
        dpFechaEmision.setPrefHeight(ALTO_CAJA);
        dpFechaEmision.setMaxWidth(Double.MAX_VALUE);
        miGrilla.add(dpFechaEmision, 1, 5);

        // Precio
        Label lblPrecio = new Label("Precio del ticket:");
        lblPrecio.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblPrecio, 0, 6);

        cajaPrecio = new TextField();
        cajaPrecio.setPromptText("Ej: 15000");
        cajaPrecio.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaPrecio, 1, 6);

        // Imagen
        Label lblImagen = new Label("Imagen del ticket:");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblImagen, 0, 7);

        cajaImagen = new TextField();
        cajaImagen.setDisable(true);
        cajaImagen.setPrefHeight(ALTO_CAJA);

        Button btnEscogerImagen = new Button("+");
        btnEscogerImagen.setPrefHeight(ALTO_CAJA);
        btnEscogerImagen.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Selecciona una imagen");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
            File archivo = fc.showOpenDialog(miEscenario);

            if (archivo != null && archivo.exists()) {
                try {
                    File carpetaDestino = new File(Persistencia.RUTA_IMAGENES_EXTERNAS);
                    if (!carpetaDestino.exists()) {
                        carpetaDestino.mkdirs();
                    }

                    File destino = new File(carpetaDestino, archivo.getName());
                    java.nio.file.Files.copy(archivo.toPath(), destino.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                    rutaSeleccionada = destino.getAbsolutePath();
                    cajaImagen.setText(archivo.getName());
                    mostrarImagenPrevisualizada(rutaSeleccionada);
                } catch (Exception ex) {
                    Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error",
                            "No se pudo copiar la imagen: " + ex.getMessage());
                    rutaSeleccionada = "";
                }
            } else {
                rutaSeleccionada = "";
                mostrarImagenPorDefecto();
            }
        });

        HBox panelCajaImagen = new HBox(2, cajaImagen, btnEscogerImagen);
        HBox.setHgrow(cajaImagen, Priority.ALWAYS);
        panelCajaImagen.setAlignment(Pos.CENTER_LEFT);
        miGrilla.add(panelCajaImagen, 1, 7);

        // Contenedor para imagen con límites
        contenedorImagen = new StackPane();
        contenedorImagen.setMaxWidth(250);
        contenedorImagen.setMaxHeight(250);
        contenedorImagen.setMinWidth(190);
        contenedorImagen.setMinHeight(190);
        contenedorImagen.setAlignment(Pos.CENTER);
        contenedorImagen.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: transparent;");

        imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 190);
        contenedorImagen.getChildren().add(imgPorDefecto);

        GridPane.setHalignment(contenedorImagen, HPos.CENTER);
        miGrilla.add(contenedorImagen, 2, 1, 1, 6);

        Button btnGrabar = new Button("Guardar Ticket");
        btnGrabar.setMaxWidth(Double.MAX_VALUE);
        btnGrabar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnGrabar.setTextFill(Color.web(Configuracion.COLOR4));
        btnGrabar.setOnAction(e -> grabarTicket());
        miGrilla.add(btnGrabar, 1, 8);
    }

    private void mostrarImagenPrevisualizada(String ruta) {
        contenedorImagen.getChildren().clear();
        
        try {
            imgPrevisualizar = Icono.previsualizar(ruta, 250);
            
            imgPrevisualizar.setFitWidth(240);
            imgPrevisualizar.setFitHeight(240);
            imgPrevisualizar.setPreserveRatio(true);
            
            contenedorImagen.getChildren().add(imgPrevisualizar);
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
            mostrarImagenPorDefecto();
        }
    }

    private void mostrarImagenPorDefecto() {
        contenedorImagen.getChildren().clear();
        imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 190);
        contenedorImagen.getChildren().add(imgPorDefecto);
    }

    private String obtenerTiposSeleccionados() {
        if (chkGeneral.isSelected()) return "General";
        if (chkVIP.isSelected()) return "VIP";
        if (chkPreferencial.isSelected()) return "Preferencial";
        if (chkEstudiante.isSelected()) return "Estudiante";
        if (chkTerceraEdad.isSelected()) return "Tercera Edad";
        return "";
    }

    private boolean formularioValido() {
        if (!chkGeneral.isSelected() && !chkVIP.isSelected()
                && !chkPreferencial.isSelected() && !chkEstudiante.isSelected()
                && !chkTerceraEdad.isSelected()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Error",
                    "Selecciona un tipo de entrada.");
            chkGeneral.requestFocus();
            return false;
        }

        if (cbmCliente.getValue() == null || cbmCliente.getValue().getIdCliente() == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Error",
                    "Selecciona un cliente válido.");
            cbmCliente.requestFocus();
            return false;
        }

        if (dpFechaEmision.getValue() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Error",
                    "Selecciona la fecha de emisión.");
            dpFechaEmision.requestFocus();
            return false;
        }

        if (cajaPrecio.getText().isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Error",
                    "Ingresa el precio del ticket.");
            cajaPrecio.requestFocus();
            return false;
        }

        try {
            Double.parseDouble(cajaPrecio.getText());
        } catch (NumberFormatException e) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Error",
                    "El precio debe ser un número válido.");
            cajaPrecio.requestFocus();
            return false;
        }

        if (rutaSeleccionada.isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Error",
                    "Selecciona una imagen para el ticket.");
            return false;
        }

        return true;
    }

    private void grabarTicket() {
        if (formularioValido()) {
            TicketDto dto = new TicketDto();
            dto.setTipoEntradaTicket(obtenerTiposSeleccionados());
            dto.setIdClienteTicket(cbmCliente.getValue());
            dto.setEsValidoTicket(rbValido.isSelected());
            dto.setFechaEmisionTicket(dpFechaEmision.getValue());
            
            String precioPlano = new java.math.BigDecimal(cajaPrecio.getText()).toPlainString();
            dto.setPrecioTicket(Double.parseDouble(precioPlano));
            dto.setNombreImagenPublicoTicket(cajaImagen.getText());

            if (TicketControladorGrabar.crearTicket(dto, rutaSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario, "Éxito",
                        "Ticket guardado con éxito");
                limpiarFormulario();
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error",
                        "No se pudo guardar el ticket.");
            }
        }
    }

    private void limpiarFormulario() {
        chkGeneral.setSelected(false);
        chkVIP.setSelected(false);
        chkPreferencial.setSelected(false);
        chkEstudiante.setSelected(false);
        chkTerceraEdad.setSelected(false);

        cbmCliente.setValue(null);
        rbValido.setSelected(true);
        dpFechaEmision.setValue(LocalDate.now());
        cajaPrecio.setText("");

        rutaSeleccionada = "";
        cajaImagen.setText("");
        mostrarImagenPorDefecto();

        chkGeneral.requestFocus();
    }
}