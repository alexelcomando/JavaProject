package com.unimagdalena.vista.confiteria;

import com.unimagdalena.controlador.confiteria.ConfiteriaControladorEditar;
import com.unimagdalena.dto.ConfiteriaDto;
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

public class VistaConfiteriaEditar extends StackPane {

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
    private VistaConfiteriaAdministrar vistaAdministrar;
    private VistaConfiteriaCarrusel vistaCarrusel;

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
    private String rutaSeleccionada;

    private final ConfiteriaDto confiteria;

    public VistaConfiteriaEditar(Stage esce, BorderPane panelPrincipal, Pane panelAnterior, ConfiteriaDto confiteria) {
        this.miEscenario = esce;
        this.panelPrincipal = panelPrincipal;
        this.panelAnterior = panelAnterior;
        this.confiteria = confiteria;
        this.rutaSeleccionada = "";
        imgPrevisualizar = null;


        // Guardar referencia a la vista anterior (puede ser administrar o carrusel)
        if (panelAnterior instanceof VistaConfiteriaAdministrar) {
            this.vistaAdministrar = (VistaConfiteriaAdministrar) panelAnterior;
            this.vistaCarrusel = null;
        } else if (panelAnterior instanceof VistaConfiteriaCarrusel) {
            this.vistaCarrusel = (VistaConfiteriaCarrusel) panelAnterior;
            this.vistaAdministrar = null;
        } else {
            this.vistaAdministrar = null;
            this.vistaCarrusel = null;
        }

        setAlignment(Pos.CENTER);

        miGrilla = new GridPane();
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        getChildren().add(miMarco);

        configurarLaGrilla();
        pintarTitulo();
        pintarFormulario();
        cargarDatos(); // Cargar datos DESPUÉS de crear el formulario
        getChildren().add(miGrilla);
    }

    private void configurarLaGrilla() {
        miGrilla.setHgap(H_GAP);
        miGrilla.setVgap(V_GAP);
        miGrilla.setAlignment(Pos.TOP_CENTER);
        miGrilla.setPadding(new Insets(20));

        // Hacer responsivo con el ancho del marco
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
        Text titulo = new Text("Editar Producto de Confitería");
        titulo.setFill(Color.web(Configuracion.COLOR_BORDE));
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        GridPane.setHalignment(titulo, javafx.geometry.HPos.CENTER);
        GridPane.setMargin(titulo, new Insets(10, 0, 0, 0));
        miGrilla.add(titulo, 0, 0, 3, 1);
    }

    private void pintarFormulario() {
        // Nombre
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

        // Tipo
        Label lblTipo = new Label("Tipo de producto:");
        lblTipo.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        cbmTipo = new ComboBox<>();
        cbmTipo.getItems().addAll("Seleccione...", "Dulce", "Bebida", "Snack");
        cbmTipo.setPrefHeight(ALTO_CAJA);
        miGrilla.add(lblTipo, 0, 3);
        miGrilla.add(cbmTipo, 1, 3);

        // Disponible
        Label lblDisponible = new Label("Disponible:");
        lblDisponible.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        grupoDisponible = new ToggleGroup();
        rbDisponible = new RadioButton("Sí");
        rbDisponible.setToggleGroup(grupoDisponible);
        RadioButton rbNo = new RadioButton("No");
        rbNo.setToggleGroup(grupoDisponible);
        HBox hbDisponible = new HBox(10, rbDisponible, rbNo);
        miGrilla.add(lblDisponible, 0, 4);
        miGrilla.add(hbDisponible, 1, 4);

        // Fecha
        Label lblFecha = new Label("Fecha de compra:");
        lblFecha.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        pickerFechaCompra = new DatePicker();
        pickerFechaCompra.setPrefHeight(ALTO_CAJA);
        miGrilla.add(lblFecha, 0, 5);
        miGrilla.add(pickerFechaCompra, 1, 5);

        // Métodos de pago
        Label lblPago = new Label("Método de pago:");
        lblPago.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        chkEfectivo = new CheckBox("Efectivo");
        chkTarjeta = new CheckBox("Tarjeta");
        HBox hbPago = new HBox(10, chkEfectivo, chkTarjeta);
        miGrilla.add(lblPago, 0, 6);
        miGrilla.add(hbPago, 1, 6);

        // Imagen
        Label lblImagen = new Label("Imagen del producto:");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblImagen, 0, 7);

        cajaImagen = new TextField();
        cajaImagen.setDisable(true);
        cajaImagen.setPrefHeight(ALTO_CAJA);

        Button btnImagen = new Button("+");
        btnImagen.setPrefHeight(ALTO_CAJA);

        btnImagen.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            File archivo = fc.showOpenDialog(miEscenario);
            if (archivo != null && archivo.exists()) {
                try {
                    File carpetaDestino = new File(Persistencia.RUTA_IMAGENES_EXTERNAS);
                    if (!carpetaDestino.exists()) {
                        carpetaDestino.mkdirs();
                    }

                    File destino = new File(carpetaDestino, archivo.getName());
                    java.nio.file.Files.copy(archivo.toPath(), destino.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                    rutaSeleccionada = archivo.getName();
                    cajaImagen.setText(rutaSeleccionada);

                    mostrarImagenPrevisualizada(destino.getAbsolutePath());
                } catch (Exception ex) {
                    Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error", "No se pudo copiar la imagen: " + ex.getMessage());
                }
            }
        });

        HBox hbImagen = new HBox(2, cajaImagen, btnImagen);
        hbImagen.setAlignment(Pos.CENTER_LEFT);
        miGrilla.add(hbImagen, 1, 7);

        // Imagen por defecto (se mostrará inicialmente)
        imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 150);
        miGrilla.add(imgPorDefecto, 1, 7);

        Button btnActualizar = new Button("Actualizar Producto");
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnActualizar.setOnAction(e -> actualizarProducto());
        miGrilla.add(btnActualizar, 1, 8);

        // Botón Regresar
        Button btnRegresar = new Button("Regresar");
        btnRegresar.setMaxWidth(Double.MAX_VALUE);
        btnRegresar.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        btnRegresar.setTextFill(Color.web("#6C3483"));
        btnRegresar.setOnAction(e -> {
            // Refrescar la vista anterior antes de regresar
            if (vistaAdministrar != null) {
                vistaAdministrar.refrescarTabla();
            } else if (vistaCarrusel != null) {
                vistaCarrusel.refrescarCarrusel();
            }
            // Regresar a la vista anterior
            panelPrincipal.setCenter(panelAnterior);
        });
        miGrilla.add(btnRegresar, 1, 9);
    }

    private void cargarDatos() {
        if (confiteria == null) {
            return;
        }

        // Cargar datos del formulario
       cajaNombre.setText(confiteria.getNombreProductoConfiteria());
        cajaPrecio.setText(String.format(java.util.Locale.US, "%.2f", confiteria.getPrecioProductoConfiteria()));

         
        // Tipo
        String tipo = confiteria.getTipoProductoConfiteria();
        if (tipo != null && cbmTipo.getItems().contains(tipo)) {
            cbmTipo.getSelectionModel().select(tipo);
        } else {
            cbmTipo.getSelectionModel().select(0);
        }

        // Disponible
        if (confiteria.getProductoDisponibleConfiteria()) {
            rbDisponible.setSelected(true);
        } else {
            grupoDisponible.selectToggle(grupoDisponible.getToggles().get(1));
        }

        // Fecha
        pickerFechaCompra.setValue(confiteria.getFechaCompraProducto());

        // Métodos de pago
        String metodo = confiteria.getMetodoPagoProducto();
        if (metodo != null) {
            chkEfectivo.setSelected(metodo.contains("Efectivo"));
            chkTarjeta.setSelected(metodo.contains("Tarjeta"));
        }

        // Cargar imagen - CORREGIDO
        String nombreImagen = confiteria.getNombreImagenPrivadoConfiteria(); // Usar el nombre privado

        System.out.println("=== DEBUG CARGA IMAGEN ===");
        System.out.println("Nombre imagen privado: " + nombreImagen);
        System.out.println("Nombre imagen público: " + confiteria.getNombreImagenPublicoConfiteria());

        if (nombreImagen != null && !nombreImagen.isEmpty()) {
            rutaSeleccionada = nombreImagen;
            cajaImagen.setText(nombreImagen);

            // Usar obtenerFotosExternas que ya maneja la ruta completa internamente
            ImageView imgCargada = Icono.obtenerFotosExternas(nombreImagen, 250);

            if (imgCargada != null) {
                // Remover imagen por defecto
                if (miGrilla.getChildren().contains(imgPorDefecto)) {
                    miGrilla.getChildren().remove(imgPorDefecto);
                }
                // Agregar imagen cargada
                imgPrevisualizar = imgCargada;
                imgPrevisualizar.setPreserveRatio(true);
                imgPrevisualizar.setFitWidth(250);
                imgPrevisualizar.setFitHeight(250);
                miGrilla.add(imgPrevisualizar, 2, 1, 1, 6);

            } else {
                System.out.println("obtenerFotosExternas devolvió null");
                mostrarImagenPorDefecto();
            }
        } else {
            System.out.println("No hay nombre de imagen");
            rutaSeleccionada = "";
            cajaImagen.setText("");
            mostrarImagenPorDefecto();
        }
    }

    private void mostrarImagenPrevisualizada(String ruta) {
        // Eliminar imágenes previas
        if (imgPrevisualizar != null && miGrilla.getChildren().contains(imgPrevisualizar)) {
            miGrilla.getChildren().remove(imgPrevisualizar);
        }
        if (miGrilla.getChildren().contains(imgPorDefecto)) {
            miGrilla.getChildren().remove(imgPorDefecto);
        }

        try {
            imgPrevisualizar = Icono.previsualizar(ruta, 250);
            imgPrevisualizar.setPreserveRatio(true);
            imgPrevisualizar.setFitWidth(250);
            imgPrevisualizar.setFitHeight(250);

            miGrilla.add(imgPrevisualizar, 2, 1, 1, 6);
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
            mostrarImagenPorDefecto();
        }
    }

    private void mostrarImagenPorDefecto() {
        if (imgPrevisualizar != null && miGrilla.getChildren().contains(imgPrevisualizar)) {
            miGrilla.getChildren().remove(imgPrevisualizar);
        }
        if (!miGrilla.getChildren().contains(imgPorDefecto)) {
            imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 190);
            miGrilla.add(imgPorDefecto, 2, 1, 1, 6);
        }
    }

    private void actualizarProducto() {
        try {
            confiteria.setNombreProductoConfiteria(cajaNombre.getText());

            // Validar precio
            try {
                confiteria.setPrecioProductoConfiteria(Double.parseDouble(cajaPrecio.getText()));
            } catch (NumberFormatException ex) {
                Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error", "Precio inválido");
                return;
            }

            confiteria.setTipoProductoConfiteria(cbmTipo.getValue());
            confiteria.setProductoDisponibleConfiteria(rbDisponible.isSelected());
            confiteria.setFechaCompraProducto(pickerFechaCompra.getValue());
            confiteria.setMetodoPagoProducto(obtenerMetodoPago());

            // IMPORTANTE: Solo actualizar imagen si se seleccionó una nueva
            String rutaParaActualizar = "";

            // Si rutaSeleccionada es diferente al nombre original, significa que eligió una nueva
            if (rutaSeleccionada != null && !rutaSeleccionada.isEmpty()
                    && !rutaSeleccionada.equals(confiteria.getNombreImagenPrivadoConfiteria())) {
                // Usuario seleccionó una imagen NUEVA
                confiteria.setNombreImagenPublicoConfiteria(rutaSeleccionada);

                // Construir ruta completa para pasar al controlador
                String rutaCompleta = Persistencia.RUTA_IMAGENES_EXTERNAS + File.separator + rutaSeleccionada;
                File archivoImagen = new File(rutaCompleta);

                if (archivoImagen.exists()) {
                    rutaParaActualizar = rutaCompleta;
                } else {
                    System.out.println("ADVERTENCIA: Archivo no encontrado: " + rutaCompleta);
                }
            } else {
                // Usuario NO seleccionó una nueva imagen, mantener la actual
                // No enviar ruta al controlador (cadena vacía significa "no actualizar imagen")
                rutaParaActualizar = "";
            }

            System.out.println("=== DEBUG ACTUALIZAR ===");
            System.out.println("Ruta seleccionada: " + rutaSeleccionada);
            System.out.println("Ruta para actualizar: " + rutaParaActualizar);
            System.out.println("Nombre privado original: " + confiteria.getNombreImagenPrivadoConfiteria());

            boolean exito = ConfiteriaControladorEditar.actualizar(confiteria, rutaParaActualizar);
            if (exito) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario, "Éxito", "Producto actualizado correctamente");

                // Refrescar la vista anterior (tabla o carrusel)
                if (vistaAdministrar != null) {
                    vistaAdministrar.refrescarTabla();
                } else if (vistaCarrusel != null) {
                    vistaCarrusel.refrescarCarrusel();
                }
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error", "No se pudo actualizar el producto");
            }
        } catch (Exception ex) {
            Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error", "Error al actualizar el producto: " + ex.getMessage());
            ex.printStackTrace();
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

}