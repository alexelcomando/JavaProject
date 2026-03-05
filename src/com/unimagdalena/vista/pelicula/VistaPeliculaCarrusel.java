package com.unimagdalena.vista.pelicula;

import com.unimagdalena.controlador.cliente.ClienteControladorListar;
import com.unimagdalena.controlador.funcion.FuncionControladorListar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorCant;
import com.unimagdalena.controlador.pelicula.PeliculaControladorEliminar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorListar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorUna;
import com.unimagdalena.controlador.pelicula.PeliculaControladorVista;
import com.unimagdalena.controlador.sala.SalaControladorListar; // Import necesario
import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Contenedor;
import com.unimagdalena.helpers.constante.IconoNombre;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
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

public class VistaPeliculaCarrusel extends SubScene {

    private final BorderPane miBorderPane;
    private final Stage laVentanaPrincipal;
    private final VBox organizadorVertical;
    private final TableView<PeliculaDto> miTabla;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    private int indiceActual;
    private int totalPeliculas;
    private PeliculaDto objCargado;

    private StringProperty categoTitulo;
    private StringProperty categoNombre;
    private ObjectProperty<LocalDate> categofecha;
    private ObjectProperty<GeneroDto> categogenero;
    private BooleanProperty categoEs3d;
    private StringProperty categoedad;
    private ObjectProperty<Image> categoImagen;

    private BooleanProperty carruselVacio;

    private final ObservableList<PeliculaDto> datosTabla = FXCollections.observableArrayList();

    public VistaPeliculaCarrusel(Stage ventanaPadre, BorderPane princ, Pane pane, double anchoPanel, double altoPanel, int indice) {
        super(new BorderPane(), anchoPanel, altoPanel);

        indiceActual = indice;
        totalPeliculas = PeliculaControladorListar.cantidadPeliculas();
        carruselVacio = new SimpleBooleanProperty(totalPeliculas == 0);

        miBorderPane = (BorderPane) this.getRoot();

        laVentanaPrincipal = ventanaPadre;
        panelPrincipal = princ;
        panelCuerpo = pane;
        miTabla = new TableView<>();

        if (carruselVacio.get()) {
            objCargado = null;
        } else {
            if (indiceActual >= totalPeliculas) {
                indiceActual = 0;
            }
            objCargado = PeliculaControladorUna.obtenerPelicula(indiceActual);
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
        categoTitulo.set("Detalle de la Pelicula (" + (indiceActual + 1) + " / " + totalPeliculas + ")");
        categoNombre.set(objCargado.getNombrePelicula());
        categofecha.set(objCargado.getFechaEstrenoPelicula());
        categogenero.set(objCargado.getIdGeneroPelicula());
        categoEs3d.set(objCargado.getEs3dPelicula());
        categoedad.set(objCargado.getClasificacionEdadPelicula());

        try (FileInputStream imgArchivo = new FileInputStream(
                Persistencia.RUTA_IMAGENES_EXTERNAS
                + Persistencia.SEPARADOR_CARPETAS
                + objCargado.getNombreImagenPrivadoPelicula()
        )) {
            Image imgnueva = new Image(imgArchivo);
            categoImagen.set(imgnueva);
        } catch (IOException ex) { 
            Logger.getLogger(VistaPeliculaCarrusel.class.getName()).log(Level.SEVERE, null, ex);
            categoImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage()); 
        }
    }

    private void construirPanelIzquierdo(double porcentaje) {
        Button btnAnterior = new Button();
        btnAnterior.setGraphic(Icono.obtenerIcono("btnAtras.png", 80));
        btnAnterior.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        btnAnterior.setCursor(Cursor.HAND);

        btnAnterior.disableProperty().bind(carruselVacio);

        btnAnterior.setOnAction(e -> {
            indiceActual = obtenerIndice("Anterior", indiceActual, totalPeliculas);
            objCargado = PeliculaControladorUna.obtenerPelicula(indiceActual);
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
            indiceActual = obtenerIndice("Siguiente", indiceActual, totalPeliculas);
            objCargado = PeliculaControladorUna.obtenerPelicula(indiceActual);
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

        totalPeliculas = PeliculaControladorCant.obtener();
        if (carruselVacio.get()) {
            categoTitulo = new SimpleStringProperty("No hay peliculas");
        } else {
            categoTitulo = new SimpleStringProperty("Detalle de la pelicula (" + (indiceActual + 1) + " / " + totalPeliculas + ")");
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
            PeliculaDto objPelicula = objCargado;
            
            // --- VALIDACIÓN ---
            int id = objPelicula.getIdPelicula();
            long cantFunciones = FuncionControladorListar.arregloFunciones().stream()
                    .filter(f -> f.getIdPeliculaFuncion().getIdPelicula() == id).count();
            
            long cantClientes = ClienteControladorListar.obtenerTodos().stream()
                    .filter(c -> c.getIdPeliculaCliente() != null && c.getIdPeliculaCliente().getIdPelicula() == id).count();

            // Validación Nueva: Cantidad de Salas
            long cantSalas = SalaControladorListar.arregloSalas().stream()
                    .filter(s -> s.getIdPeliculaSala() != null && s.getIdPeliculaSala().getIdPelicula() == id)
                    .count();
            
            if (cantFunciones > 0 || cantClientes > 0 || cantSalas > 0) {
                String errorMsg = "No se puede eliminar la película.\n";
                if(cantFunciones > 0) errorMsg += "- Tiene " + cantFunciones + " funciones programadas.\n";
                if(cantClientes > 0) errorMsg += "- Hay " + cantClientes + " clientes viéndola.\n";
                if(cantSalas > 0) errorMsg += "- Se está reproduciendo en " + cantSalas + " sala(s)."; // Mensaje nuevo
                
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error al eliminar", errorMsg);
                return;
            }
            // ------------------

            String texto1, texto2, texto3, texto4;
            texto1 = "¿Seguro que quieres borrar la pelicula?\n";
            texto2 = "\nCodigo: " + objPelicula.getIdPelicula();
            texto3 = "\nNombre: " + objPelicula.getNombrePelicula();
            texto4 = "\nEsto es irreversible!!!";

            Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
            msg.setTitle("Advertencia Borrar");
            msg.setHeaderText(null);
            msg.setContentText(texto1 + texto2 + texto3 + texto4);
            msg.initOwner(null);

            if (msg.showAndWait().get() == ButtonType.OK) {
                int posicion = indiceActual;
                if (PeliculaControladorEliminar.borrar(posicion)) {
                    totalPeliculas = PeliculaControladorListar.cantidadPeliculas();
                    carruselVacio.set(totalPeliculas == 0);

                    if (carruselVacio.get()) {
                        objCargado = null;
                        categoTitulo.set("No hay Peliculas");
                        categoNombre.set("N/A");
                        categofecha.set(null);
                        categogenero.set(null);
                        categoEs3d.set(false);
                        categoedad.set("N/A");
                        categoImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
                    } else {
                        if (indiceActual >= totalPeliculas) {
                            indiceActual = 0;
                        }
                        objCargado = PeliculaControladorUna.obtenerPelicula(indiceActual);
                        actualizarDatosCarrusel();
                    }
                    Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Exito", "Pelicula eliminada");
                } else {
                    Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo borrar la pelicula");
                }
            }
        });

        Button btnActualizar = new Button();
        btnActualizar.setPrefWidth(anchoBoton);
        btnActualizar.setCursor(Cursor.HAND);
        btnActualizar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_EDITAR, tamanioIcono));
        btnActualizar.disableProperty().bind(carruselVacio);

        btnActualizar.setOnAction((ActionEvent t) -> {
            PeliculaDto objPelicula = objCargado;
            int posicion = indiceActual;

            panelCuerpo = PeliculaControladorVista.editar(
                    laVentanaPrincipal, panelPrincipal, panelCuerpo,
                    Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(),
                    objPelicula, posicion);
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

        // Fondo
        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        centerPane.setBackground(fondo);
        // *********************************************************************

        // Marco
        Rectangle miMarco = Marco.pintar(laVentanaPrincipal, 0.55, 0.75,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        centerPane.getChildren().addAll(miMarco, organizadorVertical);
        // *********************************************************************

        panelOpciones();
        int tamanioFuente = 25;

        if (carruselVacio.get()) {
            categoNombre = new SimpleStringProperty("N/A");
            categofecha = new SimpleObjectProperty<>(null);
            categogenero = new SimpleObjectProperty<>(null);
            categoEs3d = new SimpleBooleanProperty(false);
            categoedad = new SimpleStringProperty("N/A");
            categoImagen = new SimpleObjectProperty<>(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
        } else {
            categoNombre = new SimpleStringProperty(objCargado.getNombrePelicula());
            categofecha = new SimpleObjectProperty<>(objCargado.getFechaEstrenoPelicula());
            categogenero = new SimpleObjectProperty<>(objCargado.getIdGeneroPelicula());
            categoEs3d = new SimpleBooleanProperty(objCargado.getEs3dPelicula());
            categoedad = new SimpleStringProperty(objCargado.getClasificacionEdadPelicula());
            categoImagen = new SimpleObjectProperty<>();
            try (FileInputStream imgArchivo = new FileInputStream(
                    Persistencia.RUTA_IMAGENES_EXTERNAS
                    + Persistencia.SEPARADOR_CARPETAS
                    + objCargado.getNombreImagenPrivadoPelicula()
            )) {
                Image imgnueva = new Image(imgArchivo);
                categoImagen.set(imgnueva);
            } catch (IOException ex) { 
                Logger.getLogger(VistaPeliculaCarrusel.class.getName()).log(Level.SEVERE, null, ex);
                categoImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
            }
        }

        // Nombre de la pelicula
        Label lblNombreCatego = new Label();
        lblNombreCatego.textProperty().bind(categoNombre);
        lblNombreCatego.setFont(Font.font("Verdana", tamanioFuente));
        lblNombreCatego.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblNombreCatego);
        // *********************************************************************
        // fecha de la pelicula
        tamanioFuente = 20;
        Label lblFecha = new Label();
        lblFecha.textProperty().bind(Bindings.createStringBinding(()
                -> categofecha.get() == null ? "" : "Fecha Creación: " + categofecha.get().toString(), categofecha));
        lblFecha.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblFecha.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblFecha);
        // *********************************************************************
        // Genero de la pelicula
        tamanioFuente = 20;
        Label lblGenero = new Label();
        lblGenero.textProperty().bind(
                Bindings.createStringBinding(()
                        -> categogenero.get() == null ? "" : "Género: " + categogenero.get(),
                        categogenero
                )
        );
        lblGenero.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblGenero.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblGenero);
        // *********************************************************************
        // 3d pelicula
        tamanioFuente = 20;
        Label lblEs3d = new Label();
        lblEs3d.textProperty().bind(Bindings.when(categoEs3d).then("Si").otherwise("No"));
        lblEs3d.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblEs3d.styleProperty().bind(
                categoEs3d
                        .map(dato -> dato.equals(true) ? "-fx-text-fill: #6C3483;" : "-fx-text-fill: red;")
        );
        organizadorVertical.getChildren().add(lblEs3d);
        // *********************************************************************
        // edad de la pelicula
        tamanioFuente = 25;
        Label lblEdad = new Label();
        lblEdad.textProperty().bind(categoedad);
        lblEdad.setFont(Font.font("Verdana", tamanioFuente));
        lblEdad.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblEdad);
        // *********************************************************************
        // Imagen de la pelicula
        ImageView imgMostrar = new ImageView();
        imgMostrar.setFitHeight(200);
        imgMostrar.setSmooth(true);
        imgMostrar.setPreserveRatio(true);
        imgMostrar.imageProperty().bind(categoImagen);
        organizadorVertical.getChildren().add(imgMostrar);

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