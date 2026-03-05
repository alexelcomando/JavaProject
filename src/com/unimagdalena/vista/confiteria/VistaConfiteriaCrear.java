package com.unimagdalena.vista.confiteria;

import com.unimagdalena.controlador.confiteria.ConfiteriaControladorGrabar;
import com.unimagdalena.dto.ConfiteriaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Formulario;
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
import com.unimagdalena.helpers.utilidad.GestorImagen;

import java.time.LocalDate;

public class VistaConfiteriaCrear extends StackPane {

    private static final int H_GAP = 10;
    private static final int V_GAP = 20;
    private static final int ALTO_FILA = 40;
    private static final int ALTO_CAJA = 35;
    private static final int TAMANYO_FUENTE = 18;
    private static final double AJUSTE_ARRIBA = 0.05;

    private final GridPane miGrilla;
    private final Rectangle miMarco;

    private TextField cajaNombre;
    private TextField cajaPrecio;
    private ComboBox<String> cbmTipo;
    private RadioButton rbDisponible;
    private ToggleGroup grupoDisponible;
    private DatePicker pickerFechaCompra;
    private CheckBox chkEfectivo, chkTarjeta;

    private TextField cajaImagen;
    private ImageView imgPorDefecto;
    private ImageView imgPrevisualizar;
    private StackPane contenedorImagen;
    private String rutaSeleccionada;

    public VistaConfiteriaCrear(Stage esce, double ancho, double alto) {
        rutaSeleccionada = "";
        setAlignment(Pos.CENTER);
        miGrilla = new GridPane();
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE, Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        getChildren().add(miMarco);

        configurarLaGrilla(ancho, alto);
        pintarTitulo();
        pintarFormulario();
        reUbicarFormulario();
        todoResponsive20Puntos();
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

    private void todoResponsive20Puntos() {
        miGrilla.setAlignment(Pos.TOP_CENTER);
        miGrilla.prefWidthProperty().bind(miMarco.widthProperty());
        miGrilla.prefHeightProperty().bind(miMarco.heightProperty());
    }

    private void configurarLaGrilla(double anchito, double altito) {
        miGrilla.setHgap(H_GAP);
        miGrilla.setVgap(V_GAP);
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

        for (int j = 0; j < 9; j++) {
            RowConstraints fila = new RowConstraints();
            fila.setMinHeight(ALTO_FILA);
            fila.setPrefHeight(ALTO_FILA);
            miGrilla.getRowConstraints().add(fila);
        }
    }

    private void pintarTitulo() {
        Text titulo = new Text("Crear Producto de Confitería");
        titulo.setFill(Color.web(Configuracion.COLOR_BORDE));
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        GridPane.setHalignment(titulo, HPos.CENTER);
        GridPane.setMargin(titulo, new Insets(10, 0, 0, 0));

        miGrilla.add(titulo, 0, 0, 3, 1);
    }

    private void pintarFormulario() {
        // Nombre producto
        Label lblNombre = new Label("Nombre del producto:");
        lblNombre.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNombre, 0, 1);

        cajaNombre = new TextField();
        cajaNombre.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaNombre, 1, 1);

        // Precio
        Label lblPrecio = new Label("Precio del producto:");
        lblPrecio.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblPrecio, 0, 2);

        cajaPrecio = new TextField();
        cajaPrecio.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaPrecio, 1, 2);

        // Tipo producto
        Label lblTipo = new Label("Tipo de producto:");
        lblTipo.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblTipo, 0, 3);

        cbmTipo = new ComboBox<>();
        cbmTipo.getItems().addAll("Seleccione...", "Dulce", "Bebida", "Snack");
        cbmTipo.getSelectionModel().select(0);
        cbmTipo.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cbmTipo, 1, 3);

        // Disponible
        Label lblDispon = new Label("Disponible:");
        lblDispon.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblDispon, 0, 4);

        grupoDisponible = new ToggleGroup();
        rbDisponible = new RadioButton("Sí");
        rbDisponible.setToggleGroup(grupoDisponible);
        RadioButton rbNoDisponible = new RadioButton("No");
        rbNoDisponible.setToggleGroup(grupoDisponible);

        HBox panelDisponible = new HBox(10, rbDisponible, rbNoDisponible);
        miGrilla.add(panelDisponible, 1, 4);

        // Fecha compra
        Label lblFecha = new Label("Fecha de compra:");
        lblFecha.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblFecha, 0, 5);

        pickerFechaCompra = new DatePicker(LocalDate.now());
        pickerFechaCompra.setPrefHeight(ALTO_CAJA);
        miGrilla.add(pickerFechaCompra, 1, 5);

        // Método de pago
        Label lblPago = new Label("Método de pago:");
        lblPago.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblPago, 0, 6);

        chkEfectivo = new CheckBox("Efectivo");
        chkTarjeta = new CheckBox("Tarjeta");
        HBox panelPago = new HBox(10, chkEfectivo, chkTarjeta);
        miGrilla.add(panelPago, 1, 6);

        // Imagen producto
        Label lblImagen = new Label("Imagen del producto:");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblImagen, 0, 7);

        cajaImagen = new TextField();
        cajaImagen.setDisable(true);
        String[] extensionesPermitidas = {"*.png", "*.jpg", "*.jpeg"};

        FileChooser objSeleccionar = Formulario.selectorImagen("Selecciona una imagen", "Imagen", extensionesPermitidas);
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

        HBox panelCajaImagen = new HBox(2, cajaImagen, btnEscogerImagen);
        panelCajaImagen.setAlignment(Pos.CENTER_LEFT);
        miGrilla.add(panelCajaImagen, 1, 7);

        // Contenedor para imagen con fondo transparente
        contenedorImagen = new StackPane();
        contenedorImagen.setMaxWidth(250);
        contenedorImagen.setMaxHeight(250);
        contenedorImagen.setMinWidth(190);
        contenedorImagen.setMinHeight(190);
        contenedorImagen.setAlignment(Pos.CENTER);
        // FONDO TRANSPARENTE - elimina el color de fondo
        contenedorImagen.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: transparent;");

        imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 190);
        contenedorImagen.getChildren().add(imgPorDefecto);

        GridPane.setHalignment(contenedorImagen, HPos.CENTER);
        miGrilla.add(contenedorImagen, 2, 1, 1, 6);

        // Botón grabar
        Button btnGrabar = new Button("Guardar Producto");
        btnGrabar.setMaxWidth(Double.MAX_VALUE);
        btnGrabar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnGrabar.setOnAction((e) -> grabarProducto());
        miGrilla.add(btnGrabar, 1, 8);
    }

    private void mostrarImagenPrevisualizada(String ruta) {
        contenedorImagen.getChildren().clear();
        
        try {
            imgPrevisualizar = Icono.previsualizar(ruta, 250);
            
            if (imgPrevisualizar != null) {
                imgPrevisualizar.setFitWidth(240);
                imgPrevisualizar.setFitHeight(240);
                imgPrevisualizar.setPreserveRatio(true);
                
                contenedorImagen.getChildren().add(imgPrevisualizar);
            } else {
                mostrarImagenPorDefecto();
            }
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

    private boolean formularioValido() {
        if (cajaNombre.getText().isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "Error", "Debes ingresar un nombre.");
            return false;
        }
        if (cajaPrecio.getText().isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "Error", "El precio es obligatorio.");
            return false;
        }
        if (cbmTipo.getSelectionModel().getSelectedIndex() == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "Error", "Selecciona un tipo.");
            return false;
        }
        if (grupoDisponible.getSelectedToggle() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "Error", "Debes especificar si el producto está disponible.");
            return false;
        }
        if (!chkEfectivo.isSelected() && !chkTarjeta.isSelected()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "Error", "Debes seleccionar al menos un método de pago.");
            return false;
        }
        if (rutaSeleccionada.isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "Error", "Selecciona una imagen.");
            return false;
        }
        return true;
    }

    private void grabarProducto() {
        if (formularioValido()) {
            ConfiteriaDto dto = new ConfiteriaDto();
            dto.setNombreProductoConfiteria(cajaNombre.getText());
            dto.setPrecioProductoConfiteria(Double.parseDouble(cajaPrecio.getText()));
            dto.setTipoProductoConfiteria(cbmTipo.getValue());
            dto.setProductoDisponibleConfiteria(rbDisponible.isSelected());

            LocalDate fecha = pickerFechaCompra.getValue();
            if (fecha == null) {
                Mensaje.mostrar(Alert.AlertType.WARNING, null, "Error", "Por favor, selecciona una fecha de compra.");
                return;
            }
            dto.setFechaCompraProducto(fecha);

            dto.setMetodoPagoProducto(obtenerMetodoPago());
            dto.setNombreImagenPublicoConfiteria(cajaImagen.getText());

            if (ConfiteriaControladorGrabar.crearConfiteria(dto, rutaSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Éxito", "Producto guardado con éxito");
                limpiarFormulario();
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo guardar el producto.");
            }
        }
    }

    private String obtenerMetodoPago() {
        if (chkEfectivo.isSelected() && chkTarjeta.isSelected()) {
            return "Efectivo y Tarjeta";
        } else if (chkEfectivo.isSelected()) {
            return "Efectivo";
        } else if (chkTarjeta.isSelected()) {
            return "Tarjeta";
        } else {
            return "Sin especificar";
        }
    }

    private void limpiarFormulario() {
        cajaNombre.setText("");
        cajaPrecio.setText("");
        cbmTipo.getSelectionModel().select(0);
        grupoDisponible.selectToggle(null);
        chkEfectivo.setSelected(false);
        chkTarjeta.setSelected(false);
        pickerFechaCompra.setValue(LocalDate.now());

        rutaSeleccionada = "";
        cajaImagen.setText("");
        mostrarImagenPorDefecto();

        cajaNombre.requestFocus();
    }
}