package com.unimagdalena.vista.genero;

import com.unimagdalena.controlador.genero.GeneroControladorEliminar;
import com.unimagdalena.controlador.genero.GeneroControladorListar;
import com.unimagdalena.controlador.genero.GeneroControladorUna;
import com.unimagdalena.controlador.genero.GeneroControladorVista;
import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Contenedor;
import com.unimagdalena.helpers.constante.IconoNombre;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.time.LocalDate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.SubScene;
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

public class VistaGeneroCarrusel extends SubScene {

    private final BorderPane miBorderPane;
    private final Stage laVentanaPrincipal;
    private final VBox organizadorVertical;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    private int indiceActual;
    private int totalGeneros;
    private GeneroDto objCargado;

    private StringProperty categoTitulo;
    private StringProperty categoNombre;
    private ObjectProperty<LocalDate> categofecha;
    private BooleanProperty categoEstado;
    private IntegerProperty categoCalificacion;
    private StringProperty categoPublico;
    private ObjectProperty<Image> categoImagen;

    private BooleanProperty carruselVacio;

    public VistaGeneroCarrusel(Stage ventanaPadre, BorderPane princ, Pane pane, double anchoPanel, double altoPanel, int indice) {
        super(new BorderPane(), anchoPanel, altoPanel);

        miBorderPane = (BorderPane) this.getRoot();

        laVentanaPrincipal = ventanaPadre;
        panelPrincipal = princ;
        panelCuerpo = pane;

        totalGeneros = GeneroControladorListar.cantidadGeneros();
        carruselVacio = new SimpleBooleanProperty(totalGeneros == 0);
        indiceActual = indice;

        if (carruselVacio.get()) {
            objCargado = null;
        } else {
            if (indiceActual >= totalGeneros) {
                indiceActual = 0;
            }
            objCargado = GeneroControladorUna.obtenerGenero(indiceActual);
        }
        

        organizadorVertical = new VBox();
        configurarOrganizadorVertical();
        crearTitulo();

        construirPanelIzquierdo(0.14);
        construirPanelDerecho(0.14);
        construirPanelCentro();
    }

    public BorderPane getMiBorderPane() {
        return miBorderPane;
    }

    private void actualizarDatosCarrusel() {
        categoTitulo.set("Detalle del Género (" + (indiceActual + 1) + " / " + totalGeneros + ")");
        categoNombre.set(objCargado.getNombreGenero());
        categofecha.set(objCargado.getFechaCreacionGenero());
        categoEstado.set(objCargado.getEstadoGenero());
        categoCalificacion.set(objCargado.getCalificacionGenero());
        categoPublico.set(objCargado.getPublicoObjetivoGenero());

        // Usar Icono para cargar la imagen sin bloquear el archivo
        ImageView imgCargada = Icono.obtenerFotosExternas(objCargado.getNombreImagenPrivadoGenero(), 250);
        categoImagen.set(imgCargada.getImage());
    }

    private void construirPanelIzquierdo(double porcentaje) {
        Button btnAnterior = new Button();
        btnAnterior.setGraphic(Icono.obtenerIcono("btnAtras.png", 80));
        btnAnterior.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        btnAnterior.setCursor(Cursor.HAND);
        
        btnAnterior.disableProperty().bind(carruselVacio);
        
        btnAnterior.setOnAction(e -> {
            indiceActual = obtenerIndice("Anterior", indiceActual, totalGeneros);
            objCargado = GeneroControladorUna.obtenerGenero(indiceActual);
            actualizarDatosCarrusel();
        });

        StackPane panelIzquierdo = new StackPane();
        panelIzquierdo.prefWidthProperty().bind(laVentanaPrincipal.widthProperty().multiply(porcentaje));
        panelIzquierdo.getChildren().add(btnAnterior);
        miBorderPane.setLeft(panelIzquierdo);
    }

    private void construirPanelDerecho(double porcentaje) {
        Button btnSiguiente = new Button();
        btnSiguiente.setGraphic(Icono.obtenerIcono("btnSiguiente.png", 80));
        btnSiguiente.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        btnSiguiente.setCursor(Cursor.HAND);
        
        btnSiguiente.disableProperty().bind(carruselVacio);
        
        btnSiguiente.setOnAction((ActionEvent t) -> {
            indiceActual = obtenerIndice("Siguiente", indiceActual, totalGeneros);
            objCargado = GeneroControladorUna.obtenerGenero(indiceActual);
            actualizarDatosCarrusel();
        });

        StackPane panelDerecho = new StackPane();
        panelDerecho.prefWidthProperty().bind(laVentanaPrincipal.widthProperty().multiply(porcentaje));
        panelDerecho.getChildren().add(btnSiguiente);
        miBorderPane.setRight(panelDerecho);
    }

    private void configurarOrganizadorVertical() {
        organizadorVertical.setSpacing(10);
        organizadorVertical.setAlignment(Pos.TOP_CENTER);
        organizadorVertical.prefWidthProperty().bind(laVentanaPrincipal.widthProperty());
        organizadorVertical.prefHeightProperty().bind(laVentanaPrincipal.heightProperty());
    }

    private void crearTitulo() {
        Region bloqueSeparador = new Region();
        bloqueSeparador.prefHeightProperty().bind(laVentanaPrincipal.heightProperty().multiply(0.10));
        organizadorVertical.getChildren().add(0, bloqueSeparador);

        totalGeneros = GeneroControladorListar.cantidadGeneros();
        if (carruselVacio.get()) {
            categoTitulo = new SimpleStringProperty("No hay géneros");
        } else {
            categoTitulo = new SimpleStringProperty("Detalle del Género (" + (indiceActual + 1) + " / " + totalGeneros + ")");
        }

        Label lblTitulo = new Label();
        lblTitulo.textProperty().bind(categoTitulo);
        lblTitulo.setTextFill(Color.web("#E82E68"));
        lblTitulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));
        organizadorVertical.getChildren().add(lblTitulo);
    }

    private void panelOpciones() {
        int anchoBoton = 40;
        int tamanioIcono = 16;

        Button btnEliminar = new Button();
        btnEliminar.setPrefWidth(anchoBoton);
        btnEliminar.setCursor(Cursor.HAND);
        btnEliminar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_BORRAR, tamanioIcono));
        btnEliminar.disableProperty().bind(carruselVacio);

        btnEliminar.setOnAction((t) -> {
            GeneroDto objGenero = objCargado;

            if (objGenero.getCantPeliculasGenero() > 0) {
                Mensaje.mostrar(Alert.AlertType.ERROR, laVentanaPrincipal, "Error", "No se puede borrar un género que tiene películas asociadas.");
                return;
            }

            String texto1, texto2, texto3, texto4;
            texto1 = "¿Seguro que quieres borrar el género?\n";
            texto2 = "\nCodigo: " + objGenero.getIdGenero();
            texto3 = "\nNombre: " + objGenero.getNombreGenero();
            texto4 = "\nEsto es irreversible!!!";

            Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
            msg.setTitle("Advertencia Borrar");
            msg.setHeaderText(null);
            msg.setContentText(texto1 + texto2 + texto3 + texto4);
            msg.initOwner(null);

            if (msg.showAndWait().get() == ButtonType.OK) {
                int posicion = indiceActual;
                if (GeneroControladorEliminar.borrar(posicion)) {
                    totalGeneros = GeneroControladorListar.cantidadGeneros();
                    carruselVacio.set(totalGeneros == 0);

                    if (carruselVacio.get()) {
                        objCargado = null;
                        categoTitulo.set("No hay géneros");
                        categoNombre.set("N/A");
                        categoImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
                        categofecha.set(null);
                        categoEstado.set(false);
                        categoCalificacion.set(0);
                        categoPublico.set("N/A");
                    } else {
                        if (indiceActual >= totalGeneros) {
                            indiceActual = 0;
                        }
                        objCargado = GeneroControladorUna.obtenerGenero(indiceActual);
                        actualizarDatosCarrusel();
                    }
                    Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Exito", "Género eliminado");
                } else {
                    Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo borrar el género");
                }
            }
        });

        Button btnActualizar = new Button();
        btnActualizar.setPrefWidth(anchoBoton);
        btnActualizar.setCursor(Cursor.HAND);
        btnActualizar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_EDITAR, tamanioIcono));
        btnActualizar.disableProperty().bind(carruselVacio);

        btnActualizar.setOnAction((ActionEvent t) -> {
            GeneroDto objGenero = objCargado;
            int posicion = indiceActual;

            panelCuerpo = GeneroControladorVista.abrirEditar(
                    laVentanaPrincipal, panelPrincipal, panelCuerpo,
                    Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(),
                    objGenero, posicion);
            panelPrincipal.setCenter(null);
            panelPrincipal.setCenter(panelCuerpo);
        });

        HBox panelHorizontalBotones = new HBox(4);
        panelHorizontalBotones.setAlignment(Pos.CENTER);
        panelHorizontalBotones.getChildren().addAll(btnEliminar, btnActualizar);

        organizadorVertical.getChildren().add(panelHorizontalBotones);
    }

    private void construirPanelCentro() {
        StackPane centerPane = new StackPane();
        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        centerPane.setBackground(fondo);

        Rectangle miMarco = Marco.pintar(laVentanaPrincipal, 0.65, 0.75,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        centerPane.getChildren().addAll(miMarco, organizadorVertical);

        panelOpciones();

        int tamanioFuente = 25;

        if (carruselVacio.get()) {
            categoNombre = new SimpleStringProperty("N/A");
            categoImagen = new SimpleObjectProperty<>(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
            categofecha = new SimpleObjectProperty<>(null);
            categoEstado = new SimpleBooleanProperty(false);
            categoCalificacion = new SimpleIntegerProperty(0);
            categoPublico = new SimpleStringProperty("N/A");
        } else {
            categoNombre = new SimpleStringProperty(objCargado.getNombreGenero());
            categoImagen = new SimpleObjectProperty<>();
            // USAR ICONO PARA CARGAR LA IMAGEN INICIALMENTE
            ImageView imgCargada = Icono.obtenerFotosExternas(objCargado.getNombreImagenPrivadoGenero(), 250);
            categoImagen.set(imgCargada.getImage());
            
            categofecha = new SimpleObjectProperty<>(objCargado.getFechaCreacionGenero());
            categoEstado = new SimpleBooleanProperty(objCargado.getEstadoGenero());
            categoCalificacion = new SimpleIntegerProperty(objCargado.getCalificacionGenero());
            categoPublico = new SimpleStringProperty(objCargado.getPublicoObjetivoGenero());
        }

        Label lblNombreCatego = new Label();
        lblNombreCatego.textProperty().bind(categoNombre);
        lblNombreCatego.setFont(Font.font("Verdana", tamanioFuente));
        lblNombreCatego.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblNombreCatego);

        ImageView imgMostrar = new ImageView();
        imgMostrar.setFitHeight(200);
        imgMostrar.setSmooth(true);
        imgMostrar.setPreserveRatio(true);
        imgMostrar.imageProperty().bind(categoImagen);
        organizadorVertical.getChildren().add(imgMostrar);

        tamanioFuente = 20;
        Label lblFecha = new Label();
        lblFecha.textProperty().bind(Bindings.createStringBinding(() ->
            categofecha.get() == null ? "" : "Fecha Creación: " + categofecha.get().toString(), categofecha));
        lblFecha.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblFecha.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblFecha);

        Label lblEstado = new Label();
        lblEstado.textProperty().bind(Bindings.when(carruselVacio).then("")
            .otherwise(Bindings.when(categoEstado).then("Activo").otherwise("Inactivo")));
        lblEstado.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblEstado.styleProperty().bind(
                categoEstado.map(dato -> dato.equals(true) ? "-fx-text-fill: green;" : "-fx-text-fill: red;")
        );
        organizadorVertical.getChildren().add(lblEstado);

        Label lblCalificacion = new Label();
        lblCalificacion.textProperty().bind(Bindings.createStringBinding(() ->
            categoCalificacion.get() == 0 ? "" : "Calificación: " + "★".repeat(categoCalificacion.get()), categoCalificacion));
        lblCalificacion.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblCalificacion.setTextFill(Color.GOLD);
        organizadorVertical.getChildren().add(lblCalificacion);

        Label lblPublico = new Label();
        lblPublico.textProperty().bind(categoPublico);
        lblPublico.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblPublico.styleProperty().bind(
                categoPublico.map(dato -> "MAYORES".equals(dato) ? "-fx-text-fill: green;" : "-fx-text-fill: red;")
        );
        organizadorVertical.getChildren().add(lblPublico);

        miBorderPane.setCenter(centerPane);
    }

    private static Integer obtenerIndice(String opcion, int indice, int numCarros) {
        Integer nuevoIndice, limite;

        nuevoIndice = indice;
        limite = numCarros - 1;
        switch (opcion.toLowerCase()) {
            case "anterior" -> {
                if (indice == 0) {
                    nuevoIndice = limite;
                } else {
                    nuevoIndice = indice - 1;
                }
            }
            case "siguiente" -> {
                if (indice == limite) {
                    nuevoIndice = 0;
                } else {
                    nuevoIndice = indice + 1;
                }
            }
        }
        return nuevoIndice;
    }
}