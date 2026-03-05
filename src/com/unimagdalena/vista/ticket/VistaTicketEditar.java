package com.unimagdalena.vista.ticket;

import com.unimagdalena.controlador.ticket.TicketControladorEditar;
import com.unimagdalena.controlador.cliente.ClienteControladorListar;
import com.unimagdalena.dto.TicketDto;
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
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

public class VistaTicketEditar extends StackPane {

    private static final int H_GAP = 10;
    private static final int V_GAP = 20;
    private static final int ALTO_FILA = 40;
    private static final int ALTO_CAJA = 35;
    private static final int TAMANYO_FUENTE = 18;

    private final GridPane miGrilla;
    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final BorderPane panelPrincipal;
    private final Pane panelAnterior;
    private VistaTicketAdministrar vistaAdministrar;
    private VistaTicketCarrusel vistaCarrusel;

    // Checkboxes (selección única)
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
    private String rutaSeleccionada;

    private final TicketDto ticket;
    private final int indice;

    public VistaTicketEditar(Stage esce, BorderPane panelPrincipal, Pane panelAnterior,
                             TicketDto ticket, int indice) {
        this.miEscenario = esce;
        this.panelPrincipal = panelPrincipal;
        this.panelAnterior = panelAnterior;
        this.ticket = ticket;
        this.indice = indice;
        this.rutaSeleccionada = "";

        if (panelAnterior instanceof VistaTicketAdministrar) {
            this.vistaAdministrar = (VistaTicketAdministrar) panelAnterior;
            this.vistaCarrusel = null;
        } else if (panelAnterior instanceof VistaTicketCarrusel) {
            this.vistaCarrusel = (VistaTicketCarrusel) panelAnterior;
            this.vistaAdministrar = null;
        }

        setAlignment(Pos.CENTER);

        miGrilla = new GridPane();
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        getChildren().add(miMarco);

        configurarLaGrilla();
        pintarTitulo();
        pintarFormulario();
        cargarDatos();
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
        Text titulo = new Text("Editar Ticket");
        titulo.setFill(Color.web(Configuracion.COLOR_BORDE));
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        GridPane.setHalignment(titulo, javafx.geometry.HPos.CENTER);
        GridPane.setMargin(titulo, new Insets(10, 0, 0, 0));
        miGrilla.add(titulo, 0, 0, 3, 1);
    }

    private void pintarFormulario() {

        // --- TIPO DE ENTRADA ---
        Label lblTipoEntrada = new Label("Tipo de entrada:");
        lblTipoEntrada.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblTipoEntrada, 0, 1);

        chkGeneral = new CheckBox("General");
        chkVIP = new CheckBox("VIP");
        chkPreferencial = new CheckBox("Preferencial");
        chkEstudiante = new CheckBox("Estudiante");
        chkTerceraEdad = new CheckBox("Tercera Edad");

        HBox fila1 = new HBox(15, chkGeneral, chkVIP, chkPreferencial);
        HBox fila2 = new HBox(15, chkEstudiante, chkTerceraEdad);
        fila1.setAlignment(Pos.CENTER_LEFT);
        fila2.setAlignment(Pos.CENTER_LEFT);

        VBox panelTipos = new VBox(8, fila1, fila2);
        miGrilla.add(panelTipos, 1, 1);

        // --- SOLO UNA OPCIÓN PERMITIDA ---
        CheckBox[] checks = { chkGeneral, chkVIP, chkPreferencial, chkEstudiante, chkTerceraEdad };

        for (CheckBox chk : checks) {
            chk.setOnAction(e -> {
                if (chk.isSelected()) {
                    for (CheckBox otro : checks) {
                        if (otro != chk) otro.setSelected(false);
                    }
                }
            });
        }

        // --- CLIENTE ---
        Label lblCliente = new Label("Cliente:");
        lblCliente.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblCliente, 0, 2);

        cbmCliente = new ComboBox<>();
        cbmCliente.getItems().addAll(ClienteControladorListar.obtenerTodos());
        cbmCliente.setPrefHeight(ALTO_CAJA);
        cbmCliente.setPrefWidth(Double.MAX_VALUE);
        miGrilla.add(cbmCliente, 1, 2);

        // --- ES VÁLIDO ---
        Label lblValido = new Label("Es válido:");
        lblValido.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblValido, 0, 3);

        grupoValido = new ToggleGroup();
        rbValido = new RadioButton("Sí");
        rbValido.setToggleGroup(grupoValido);
        RadioButton rbNoValido = new RadioButton("No");
        rbNoValido.setToggleGroup(grupoValido);

        HBox panelValido = new HBox(10, rbValido, rbNoValido);
        miGrilla.add(panelValido, 1, 3);

        // --- FECHA ---
        Label lblFechaEmision = new Label("Fecha de emisión:");
        lblFechaEmision.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblFechaEmision, 0, 4);

        dpFechaEmision = new DatePicker();
        dpFechaEmision.setPrefHeight(ALTO_CAJA);
        dpFechaEmision.setMaxWidth(Double.MAX_VALUE);
        miGrilla.add(dpFechaEmision, 1, 4);

        // --- PRECIO ---
        Label lblPrecio = new Label("Precio del ticket:");
        lblPrecio.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblPrecio, 0, 5);

        cajaPrecio = new TextField();
        cajaPrecio.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaPrecio, 1, 5);

        // --- IMAGEN ---
        Label lblImagen = new Label("Imagen del ticket:");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblImagen, 0, 6);

        cajaImagen = new TextField();
        cajaImagen.setDisable(true);
        cajaImagen.setPrefHeight(ALTO_CAJA);

        Button btnImagen = new Button("+");
        btnImagen.setPrefHeight(ALTO_CAJA);
        btnImagen.setOnAction(e -> elegirImagen());

        HBox hbImagen = new HBox(2, cajaImagen, btnImagen);
        HBox.setHgrow(cajaImagen, Priority.ALWAYS);
        hbImagen.setAlignment(Pos.CENTER_LEFT);
        miGrilla.add(hbImagen, 1, 6);

        imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 150);
        miGrilla.add(imgPorDefecto, 2, 1, 1, 5);

        // --- BOTÓN ACTUALIZAR ---
        Button btnActualizar = new Button("Actualizar Ticket");
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnActualizar.setOnAction(e -> actualizarTicket());
        miGrilla.add(btnActualizar, 1, 7);

        // --- REGRESAR ---
        Button btnRegresar = new Button("Regresar");
        btnRegresar.setMaxWidth(Double.MAX_VALUE);
        btnRegresar.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        btnRegresar.setTextFill(Color.web("#6C3483"));
        btnRegresar.setOnAction(e -> regresar());
        miGrilla.add(btnRegresar, 1, 8);
    }

    private void elegirImagen() {
        try {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            File archivo = fc.showOpenDialog(miEscenario);
            if (archivo != null && archivo.exists()) {

                File carpetaDestino = new File(Persistencia.RUTA_IMAGENES_EXTERNAS);
                if (!carpetaDestino.exists()) carpetaDestino.mkdirs();

                File destino = new File(carpetaDestino, archivo.getName());
                java.nio.file.Files.copy(archivo.toPath(), destino.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                rutaSeleccionada = archivo.getName();
                cajaImagen.setText(rutaSeleccionada);
                mostrarImagenPrevisualizada(destino.getAbsolutePath());
            }
        } catch (Exception ex) {
            Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error",
                    "No se pudo copiar la imagen: " + ex.getMessage());
        }
    }

    private void cargarDatos() {
        if (ticket == null) return;

        // --- Tipo entrada ---
        switch (ticket.getTipoEntradaTicket()) {
            case "General": chkGeneral.setSelected(true); break;
            case "VIP": chkVIP.setSelected(true); break;
            case "Preferencial": chkPreferencial.setSelected(true); break;
            case "Estudiante": chkEstudiante.setSelected(true); break;
            case "Tercera Edad": chkTerceraEdad.setSelected(true); break;
        }

        // Cliente
        if (ticket.getIdClienteTicket() != null) {
            cbmCliente.getItems().stream()
                    .filter(c -> c.getIdCliente() == ticket.getIdClienteTicket().getIdCliente())
                    .findFirst()
                    .ifPresent(cbmCliente.getSelectionModel()::select);
        }

        // Es válido
        if (ticket.getEsValidoTicket()) rbValido.setSelected(true);
        else grupoValido.selectToggle(grupoValido.getToggles().get(1));

        dpFechaEmision.setValue(ticket.getFechaEmisionTicket());
        cajaPrecio.setText(
    new java.math.BigDecimal(
        String.valueOf(ticket.getPrecioTicket())
    ).toPlainString()
);


      String nombreImagen = ticket.getNombreImagenPrivadoTicket();
    if (nombreImagen != null && !nombreImagen.isEmpty()) {
        rutaSeleccionada = nombreImagen;
        cajaImagen.setText(nombreImagen);

        ImageView imgCargada = Icono.obtenerFotosExternas(nombreImagen, 250);
        if (imgCargada != null) {
            miGrilla.getChildren().remove(imgPorDefecto);
            
            // Configurar límites para la imagen cargada
            imgCargada.setFitWidth(250);
            imgCargada.setFitHeight(250);
            imgCargada.setPreserveRatio(true);
            imgCargada.setSmooth(true);
            
            GridPane.setHalignment(imgCargada, javafx.geometry.HPos.CENTER);
            miGrilla.add(imgCargada, 2, 1, 1, 5);
            imgPrevisualizar = imgCargada;
        } else {
            mostrarImagenPorDefecto();
        }
    } else {
        mostrarImagenPorDefecto();
    }
    }

  private void mostrarImagenPrevisualizada(String ruta) {
    if (imgPrevisualizar != null) miGrilla.getChildren().remove(imgPrevisualizar);
    miGrilla.getChildren().remove(imgPorDefecto);

    try {
        imgPrevisualizar = Icono.previsualizar(ruta, 250);
        
        // Configurar límites para que no se salga del marco
        imgPrevisualizar.setFitWidth(250);
        imgPrevisualizar.setFitHeight(250);
        imgPrevisualizar.setPreserveRatio(true);
        imgPrevisualizar.setSmooth(true);
        
        GridPane.setHalignment(imgPrevisualizar, javafx.geometry.HPos.CENTER);
        miGrilla.add(imgPrevisualizar, 2, 1, 1, 5);
    } catch (Exception e) {
        mostrarImagenPorDefecto();
    }
}

  private void mostrarImagenPorDefecto() {
    if (imgPrevisualizar != null) miGrilla.getChildren().remove(imgPrevisualizar);
    if (!miGrilla.getChildren().contains(imgPorDefecto)) {
        imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 190);
        
        // Configurar límites
        imgPorDefecto.setFitWidth(190);
        imgPorDefecto.setFitHeight(190);
        imgPorDefecto.setPreserveRatio(true);
        
        GridPane.setHalignment(imgPorDefecto, javafx.geometry.HPos.CENTER);
        miGrilla.add(imgPorDefecto, 2, 1, 1, 5);
    }
}
    private void regresar() {
        if (vistaAdministrar != null) vistaAdministrar.refrescarTabla();
        else if (vistaCarrusel != null) vistaCarrusel.refrescarCarrusel();

        panelPrincipal.setCenter(panelAnterior);
    }

    // ---------------------------
    //   NO SALIR AUTOMÁTICAMENTE
    // ---------------------------
    private void actualizarTicket() {
        try {
            String tipoEntrada = "";
            if (chkGeneral.isSelected()) tipoEntrada = "General";
            else if (chkVIP.isSelected()) tipoEntrada = "VIP";
            else if (chkPreferencial.isSelected()) tipoEntrada = "Preferencial";
            else if (chkEstudiante.isSelected()) tipoEntrada = "Estudiante";
            else if (chkTerceraEdad.isSelected()) tipoEntrada = "Tercera Edad";

            ticket.setTipoEntradaTicket(tipoEntrada);
            ticket.setIdClienteTicket(cbmCliente.getValue());
            ticket.setEsValidoTicket(rbValido.isSelected());
            ticket.setFechaEmisionTicket(dpFechaEmision.getValue());
            ticket.setPrecioTicket(Double.parseDouble(cajaPrecio.getText()));

            String rutaParaActualizar = "";
            if (rutaSeleccionada != null && !rutaSeleccionada.isEmpty()
                    && !rutaSeleccionada.equals(ticket.getNombreImagenPrivadoTicket())) {

                ticket.setNombreImagenPublicoTicket(rutaSeleccionada);
                String rutaCompleta = Persistencia.RUTA_IMAGENES_EXTERNAS + File.separator + rutaSeleccionada;

                File archivoImagen = new File(rutaCompleta);
                if (archivoImagen.exists()) rutaParaActualizar = rutaCompleta;
            }

            boolean exito = TicketControladorEditar.actualizar(ticket, rutaParaActualizar, indice);

            if (exito) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario,
                        "Éxito", "Ticket actualizado correctamente");

                // REFRESCA PERO NO SALE DE LA VISTA
                if (vistaAdministrar != null) vistaAdministrar.refrescarTabla();
                if (vistaCarrusel != null) vistaCarrusel.refrescarCarrusel();

                return; // ⬅ QUEDARSE EN LA MISMA VISTA
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario,
                        "Error", "No se pudo actualizar el ticket");
            }

        } catch (Exception ex) {
            Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario,
                    "Error", "Error al actualizar el ticket: " + ex.getMessage());
        }
    }
}
