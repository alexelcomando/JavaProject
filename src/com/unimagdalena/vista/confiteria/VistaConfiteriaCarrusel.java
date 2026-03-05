package com.unimagdalena.vista.confiteria;

import com.unimagdalena.controlador.confiteria.ConfiteriaControladorEditar;
import com.unimagdalena.controlador.confiteria.ConfiteriaControladorEliminar;
import com.unimagdalena.controlador.confiteria.ConfiteriaControladorListar;
import com.unimagdalena.dto.ConfiteriaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VistaConfiteriaCarrusel extends StackPane {

    private final BorderPane miBorderPane;
    private final Stage miEscenario;
    private final VBox organizadorVertical;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    private int indiceActual;
    private int totalProductos;
    private ConfiteriaDto objCargado;

    private StringProperty propTitulo;
    private StringProperty propNombre;
    private StringProperty propPrecio;
    private StringProperty propTipo;
    private BooleanProperty propDisponible;
    private ObjectProperty<LocalDate> propFecha;
    private StringProperty propMetodoPago;
    private ObjectProperty<Image> propImagen;

    public VistaConfiteriaCarrusel(Stage esce, BorderPane panelPrinc, Pane panelAnt, int indice) {
        this.miEscenario = esce;
        this.panelPrincipal = panelPrinc;
        this.panelCuerpo = panelAnt;
        
        // Inicializar siempre el BorderPane y VBox
        miBorderPane = new BorderPane();
        organizadorVertical = new VBox();
        
        indiceActual = indice;
        totalProductos = ConfiteriaControladorListar.cantidadConfiteria();

        // Validar que hay productos
        if (totalProductos == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, esce, "Sin productos",
                    "No hay productos registrados para mostrar en el carrusel");
            setAlignment(Pos.CENTER);
            getChildren().add(miBorderPane);
            return;
        }

        objCargado = ConfiteriaControladorListar.obtenerUno(indice);

        setAlignment(Pos.CENTER);

        configurarOrganizadorVertical();
        crearTitulo();

        construirPanelIzquierdo(0.14);
        construirPanelDerecho(0.14);
        construirPanelCentro();

        getChildren().add(miBorderPane);
    }

    private void construirPanelIzquierdo(double porcentaje) {
        Button btnAnterior = new Button();
        btnAnterior.setGraphic(Icono.obtenerIcono("btnAtras.png", 80));
        btnAnterior.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        btnAnterior.setCursor(Cursor.HAND);
        btnAnterior.setOnAction(e -> navegarCarrusel("Anterior"));

        StackPane panelIzquierdo = new StackPane();
        panelIzquierdo.prefWidthProperty().bind(miEscenario.widthProperty().multiply(porcentaje));
        panelIzquierdo.getChildren().add(btnAnterior);
        miBorderPane.setLeft(panelIzquierdo);
    }

    private void construirPanelDerecho(double porcentaje) {
        Button btnSiguiente = new Button();
        btnSiguiente.setGraphic(Icono.obtenerIcono("btnSiguiente.png", 80));
        btnSiguiente.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        btnSiguiente.setCursor(Cursor.HAND);
        btnSiguiente.setOnAction(e -> navegarCarrusel("Siguiente"));

        StackPane panelDerecho = new StackPane();
        panelDerecho.prefWidthProperty().bind(miEscenario.widthProperty().multiply(porcentaje));
        panelDerecho.getChildren().add(btnSiguiente);
        miBorderPane.setRight(panelDerecho);
    }

    private void navegarCarrusel(String direccion) {
        indiceActual = obtenerIndice(direccion, indiceActual, totalProductos);
        objCargado = ConfiteriaControladorListar.obtenerUno(indiceActual);

        actualizarDatosCarrusel();
    }

    private void actualizarDatosCarrusel() {
        propTitulo.set("Detalle del Producto (" + (indiceActual + 1) + " / " + totalProductos + ")");
        propNombre.set(objCargado.getNombreProductoConfiteria());
        propPrecio.set("$" + String.format(java.util.Locale.US, "%.2f", objCargado.getPrecioProductoConfiteria()));
        propTipo.set(objCargado.getTipoProductoConfiteria());
        propDisponible.set(objCargado.getProductoDisponibleConfiteria());
        propFecha.set(objCargado.getFechaCompraProducto());
        propMetodoPago.set(objCargado.getMetodoPagoProducto());

        // Actualizar imagen
        try {
            String rutaNuevaImagen = Persistencia.RUTA_IMAGENES_EXTERNAS
                    + Persistencia.SEPARADOR_CARPETAS
                    + objCargado.getNombreImagenPrivadoConfiteria();
            FileInputStream imgArchivo = new FileInputStream(rutaNuevaImagen);
            Image imgnueva = new Image(imgArchivo);
            propImagen.set(imgnueva);
        } catch (FileNotFoundException ex) {
            System.out.println("No se encontró la imagen: " + ex.getMessage());
            // Cargar imagen por defecto
            propImagen.set(new Image(getClass().getResourceAsStream(
                    Persistencia.RUTA_IMAGENES_INTERNAS + "imgNoDisponible.png")));
        }
    }

    private void configurarOrganizadorVertical() {
        organizadorVertical.setSpacing(15);
        organizadorVertical.setAlignment(Pos.TOP_CENTER);
        organizadorVertical.setPadding(new Insets(20));
        organizadorVertical.prefWidthProperty().bind(miEscenario.widthProperty());
        organizadorVertical.prefHeightProperty().bind(miEscenario.heightProperty());
    }

    private void crearTitulo() {
        Region bloqueSeparador = new Region();
        bloqueSeparador.prefHeightProperty().bind(miEscenario.heightProperty().multiply(0.05));
        organizadorVertical.getChildren().add(0, bloqueSeparador);

        // DEBUG - Imprime estos valores en consola
        System.out.println("=== DEBUG CARRUSEL ===");
        System.out.println("Índice actual: " + indiceActual);
        System.out.println("Total productos: " + totalProductos);
        System.out.println("Cantidad real: " + ConfiteriaControladorListar.cantidadConfiteria());
        
        propTitulo = new SimpleStringProperty("Detalle del Producto (" + (indiceActual + 1) + " / " + totalProductos + ")");

        Label lblTitulo = new Label();
        lblTitulo.textProperty().bind(propTitulo);
        lblTitulo.setTextFill(Color.web("#54e8b7"));
        lblTitulo.setFont(Font.font("verdana", FontWeight.BOLD, 28));
        organizadorVertical.getChildren().add(lblTitulo);
    }

    private void panelOpciones() {
        int anchoBoton = 45;
        int tamanioIcono = 20;

        // Botón Eliminar
        Button btnEliminar = new Button();
        btnEliminar.setPrefWidth(anchoBoton);
        btnEliminar.setPrefHeight(anchoBoton);
        btnEliminar.setCursor(Cursor.HAND);
        btnEliminar.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_BORRAR, tamanioIcono));
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5;");
        btnEliminar.setOnAction(e -> eliminarProductoActual());

        // Botón Editar
        Button btnEditar = new Button();
        btnEditar.setPrefWidth(anchoBoton);
        btnEditar.setPrefHeight(anchoBoton);
        btnEditar.setCursor(Cursor.HAND);
        btnEditar.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_EDITAR, tamanioIcono));
        btnEditar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5;");
        btnEditar.setOnAction(e -> editarProductoActual());

        HBox panelHorizontalBotones = new HBox(10);
        panelHorizontalBotones.setAlignment(Pos.CENTER);
        panelHorizontalBotones.getChildren().addAll(btnEliminar, btnEditar);

        organizadorVertical.getChildren().add(panelHorizontalBotones);
    }

    private void eliminarProductoActual() {
        String texto = String.format(
                "¿Estás seguro de eliminar este producto?\n\n"
                + "Código: %d\n"
                + "Nombre: %s\n\n"
                + "¡Esta acción es irreversible!",
                objCargado.getIdConfiteria(),
                objCargado.getNombreProductoConfiteria()
        );

        Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
        msg.setTitle("Confirmar Eliminación");
        msg.setHeaderText(null);
        msg.setContentText(texto);
        msg.initOwner(miEscenario);

        if (msg.showAndWait().get() == ButtonType.OK) {
            if (ConfiteriaControladorEliminar.borrar(objCargado.getIdConfiteria())) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario,
                        "Éxito", "Producto eliminado correctamente");

                // Actualizar cantidad total
                totalProductos = ConfiteriaControladorListar.cantidadConfiteria();

                // Si ya no hay productos, regresar
                if (totalProductos == 0) {
                    Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario,
                            "Sin productos", "No hay más productos para mostrar");
                    regresarAAdministrar();
                    return;
                }

                // Ajustar índice si es necesario
                if (indiceActual >= totalProductos) {
                    indiceActual = totalProductos - 1;
                }

                // Cargar nuevo producto
                objCargado = ConfiteriaControladorListar.obtenerUno(indiceActual);
                actualizarDatosCarrusel();

            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario,
                        "Error", "No se pudo eliminar el producto");
            }
        }
    }

    private void editarProductoActual() {
        StackPane panelEditar = ConfiteriaControladorEditar.abrirEditar(
                miEscenario,
                panelPrincipal,
                this, // Pasar el carrusel como panel anterior
                objCargado
        );
        panelPrincipal.setCenter(panelEditar);
    }

    private void regresarAAdministrar() {
        VistaConfiteriaAdministrar vistaAdmin = new VistaConfiteriaAdministrar(
                miEscenario,
                miEscenario.getWidth(),
                miEscenario.getHeight()
        );
        panelPrincipal.setCenter(vistaAdmin);
    }

    private void construirPanelCentro() {
        StackPane centerPane = new StackPane();

        // Fondo
        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        centerPane.setBackground(fondo);

        // Marco
        Rectangle miMarco = Marco.pintar(miEscenario, 0.65, 0.75,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        centerPane.getChildren().addAll(miMarco, organizadorVertical);

        panelOpciones();

        // Nombre del producto
        propNombre = new SimpleStringProperty(objCargado.getNombreProductoConfiteria());
        Label lblNombre = new Label();
        lblNombre.textProperty().bind(propNombre);
        lblNombre.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        lblNombre.setTextFill(Color.web("#2C3E50"));
        organizadorVertical.getChildren().add(lblNombre);

        // Imagen del producto
        propImagen = new SimpleObjectProperty<>();

        try {
            String rutaNuevaImagen = Persistencia.RUTA_IMAGENES_EXTERNAS
                    + Persistencia.SEPARADOR_CARPETAS
                    + objCargado.getNombreImagenPrivadoConfiteria();
            FileInputStream imgArchivo = new FileInputStream(rutaNuevaImagen);
            Image imgNueva = new Image(imgArchivo);
            propImagen.set(imgNueva);
        } catch (FileNotFoundException ex) {
            System.out.println("Imagen no encontrada: " + ex.getMessage());
            propImagen.set(new Image(getClass().getResourceAsStream(
                    Persistencia.RUTA_IMAGENES_INTERNAS + "imgNoDisponible.png")));
        }

        ImageView imgProducto = new ImageView();
        imgProducto.imageProperty().bind(propImagen);
        imgProducto.setFitHeight(280);
        imgProducto.setPreserveRatio(true);
        imgProducto.setSmooth(true);
        organizadorVertical.getChildren().add(imgProducto);

        // Precio - CORREGIDO para mostrar con punto decimal
        propPrecio = new SimpleStringProperty("$" + String.format(java.util.Locale.US, "%.2f", objCargado.getPrecioProductoConfiteria()));
        Label lblPrecio = new Label();
        lblPrecio.textProperty().bind(Bindings.concat("Precio: ", propPrecio));
        lblPrecio.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        lblPrecio.setTextFill(Color.web("#27AE60"));
        organizadorVertical.getChildren().add(lblPrecio);

        // Tipo de producto
        propTipo = new SimpleStringProperty(objCargado.getTipoProductoConfiteria());
        Label lblTipo = new Label();
        lblTipo.textProperty().bind(Bindings.concat("Tipo: ", propTipo));
        lblTipo.setFont(Font.font("Verdana", FontWeight.NORMAL, 18));
        lblTipo.setTextFill(Color.web("#34495E"));
        organizadorVertical.getChildren().add(lblTipo);

        // Disponibilidad
        propDisponible = new SimpleBooleanProperty(objCargado.getProductoDisponibleConfiteria());
        Label lblDisponible = new Label();
        lblDisponible.textProperty().bind(Bindings.createStringBinding(
                () -> "Estado: " + (propDisponible.get() ? "Disponible" : "Agotado"),
                propDisponible
        ));
        lblDisponible.setFont(Font.font("Verdana", FontWeight.NORMAL, 18));
        lblDisponible.textFillProperty().bind(
                propDisponible.map(dato -> dato ? Color.web("#27AE60") : Color.web("#E74C3C"))
        );
        organizadorVertical.getChildren().add(lblDisponible);

        // Fecha de compra
        propFecha = new SimpleObjectProperty<>(objCargado.getFechaCompraProducto());
        Label lblFecha = new Label();
        lblFecha.textProperty().bind(Bindings.createStringBinding(
                () -> "Fecha de compra: " + (propFecha.get() != null ? propFecha.get().toString() : "N/A"),
                propFecha
        ));
        lblFecha.setFont(Font.font("Verdana", FontWeight.NORMAL, 18));
        lblFecha.setTextFill(Color.web("#34495E"));
        organizadorVertical.getChildren().add(lblFecha);

        // Método de pago
        propMetodoPago = new SimpleStringProperty(objCargado.getMetodoPagoProducto());
        Label lblMetodoPago = new Label();
        lblMetodoPago.textProperty().bind(Bindings.concat("Método de pago: ", propMetodoPago));
        lblMetodoPago.setFont(Font.font("Verdana", FontWeight.NORMAL, 18));
        lblMetodoPago.setTextFill(Color.web("#34495E"));
        organizadorVertical.getChildren().add(lblMetodoPago);

        miBorderPane.setCenter(centerPane);
    }

    private static Integer obtenerIndice(String opcion, int indice, int total) {
        int nuevoIndice = indice;
        int limite = total - 1;

        switch (opcion.toLowerCase()) {
            case "anterior" ->
                nuevoIndice = (indice == 0) ? limite : indice - 1;
            case "siguiente" ->
                nuevoIndice = (indice == limite) ? 0 : indice + 1;
        }
        return nuevoIndice;
    }

    /**
     * Método público para refrescar el carrusel después de editar
     */
    public void refrescarCarrusel() {
        totalProductos = ConfiteriaControladorListar.cantidadConfiteria();

        if (totalProductos == 0) {
            regresarAAdministrar();
            return;
        }

        if (indiceActual >= totalProductos) {
            indiceActual = totalProductos - 1;
        }

        objCargado = ConfiteriaControladorListar.obtenerUno(indiceActual);
        actualizarDatosCarrusel();
    }
}