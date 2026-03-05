package com.unimagdalena.vista.funcion;

import com.unimagdalena.controlador.funcion.FuncionControladorCant;
import com.unimagdalena.controlador.funcion.FuncionControladorEliminar;
import com.unimagdalena.controlador.funcion.FuncionControladorListar;
import com.unimagdalena.controlador.funcion.FuncionControladorUna;
import com.unimagdalena.controlador.funcion.FuncionControladorVista;
import com.unimagdalena.controlador.reserva.ReservaControladorListar; // Import necesario
import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.dto.SalaDto;
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
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

public class VistaFuncionCarrusel extends SubScene {

    private final BorderPane miBorderPane;
    private final Stage laVentanaPrincipal;
    private final VBox organizadorVertical;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    private int indiceActual;
    private int totalFunciones;
    private FuncionDto objCargado; 

    private StringProperty categoTitulo;
    private StringProperty categoNombre;
    private ObjectProperty<PeliculaDto> categoPelicula; 
    private ObjectProperty<SalaDto> categoSala; 
    private ObjectProperty<LocalDate> categoFecha; 
    private ObjectProperty<LocalTime> categoHora; 
    private BooleanProperty categoEdad;
    private ObjectProperty<Image> categoImagen;

    private BooleanProperty carruselVacio;

    public VistaFuncionCarrusel(Stage ventanaPadre, BorderPane princ, Pane pane, double anchoPanel, double altoPanel, int indice) {
        super(new BorderPane(), anchoPanel, altoPanel);

        indiceActual = indice;
        totalFunciones = FuncionControladorListar.cantidadFunciones(); 
        carruselVacio = new SimpleBooleanProperty(totalFunciones == 0);

        miBorderPane = (BorderPane) this.getRoot();

        laVentanaPrincipal = ventanaPadre;
        panelPrincipal = princ;
        panelCuerpo = pane;

        if (carruselVacio.get()) {
            objCargado = null;
        } else {
            if (indiceActual >= totalFunciones) {
                indiceActual = 0;
            }
            objCargado = FuncionControladorUna.obtenerFuncion(indiceActual);
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
        categoTitulo.set("Detalle de la Función (" + (indiceActual + 1) + " / " + totalFunciones + ")"); 
        categoNombre.set(objCargado.getNombreFuncion());
        categoPelicula.set(objCargado.getIdPeliculaFuncion());
        categoSala.set(objCargado.getIdSalaFuncion()); 
        categoFecha.set(objCargado.getFechaFuncion());
        categoHora.set(objCargado.getHoraFuncion()); 
        categoEdad.set(objCargado.getParaMayoresFuncion());

        try (FileInputStream imgArchivo = new FileInputStream(
                Persistencia.RUTA_IMAGENES_EXTERNAS + 
                Persistencia.SEPARADOR_CARPETAS + 
                objCargado.getNombreImagenPrivadoFuncion()
        )) {
            Image imgnueva = new Image(imgArchivo);
            categoImagen.set(imgnueva);
        } catch (IOException ex) { 
            Logger.getLogger(VistaFuncionCarrusel.class.getName()).log(Level.SEVERE, null, ex); 
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
            indiceActual = obtenerIndice("Anterior", indiceActual, totalFunciones);
            objCargado = FuncionControladorUna.obtenerFuncion(indiceActual); 
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
            indiceActual = obtenerIndice("Siguiente", indiceActual, totalFunciones);
            objCargado = FuncionControladorUna.obtenerFuncion(indiceActual); 
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

        totalFunciones = FuncionControladorCant.obtener(); 
        if (carruselVacio.get()) {
            categoTitulo = new SimpleStringProperty("No hay funciones"); 
        } else {
            categoTitulo = new SimpleStringProperty("Detalle de la función (" + (indiceActual + 1) + " / " + totalFunciones + ")"); 
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
            FuncionDto objFuncion = objCargado; 
            
            // --- VALIDACIÓN DE RESERVAS ---
            int id = objFuncion.getIdFuncion();
            long cantReservas = ReservaControladorListar.arregloReservas().stream()
                    .filter(r -> r.getIdFuncionReserva() != null && r.getIdFuncionReserva().getIdFuncion() == id)
                    .count();

            if (cantReservas > 0) {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error al eliminar",
                        "No se puede eliminar la función.\nTiene " + cantReservas + " reserva(s) asociada(s).");
                return;
            }
            // --------------------------------

            String texto1, texto2, texto3, texto4;
            texto1 = "¿Seguro que quieres borrar la funcion?\n"; 
            texto2 = "\nCodigo: " + objFuncion.getIdFuncion(); 
            texto3 = "\nNombre: " + objFuncion.getNombreFuncion();
            texto4 = "\nEsto es irreversible!!!";

            Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
            msg.setTitle("Advertencia Borrar");
            msg.setHeaderText(null);
            msg.setContentText(texto1 + texto2 + texto3 + texto4);
            msg.initOwner(null);

            if (msg.showAndWait().get() == ButtonType.OK) {
                int posicion = indiceActual;
                if (FuncionControladorEliminar.borrar(posicion)) { 
                    totalFunciones = FuncionControladorListar.cantidadFunciones(); 
                    carruselVacio.set(totalFunciones == 0);

                    if (carruselVacio.get()) {
                        objCargado = null;
                        categoTitulo.set("No hay Funciones"); 
                        categoNombre.set("N/A");
                        categoPelicula.set(null); 
                        categoSala.set(null); 
                        categoFecha.set(null); 
                        categoHora.set(null); 
                        categoEdad.set(false); 
                        categoImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
                    } else {
                        if (indiceActual >= totalFunciones) {
                            indiceActual = 0;
                        }
                        objCargado = FuncionControladorUna.obtenerFuncion(indiceActual); 
                        actualizarDatosCarrusel();
                    }
                    Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Exito", "Función eliminada");
                } else {
                    Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo borrar la función"); 
                }
            }
        });

        Button btnActualizar = new Button();
        btnActualizar.setPrefWidth(anchoBoton);
        btnActualizar.setCursor(Cursor.HAND);
        btnActualizar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_EDITAR, tamanioIcono));
        btnActualizar.disableProperty().bind(carruselVacio);

        btnActualizar.setOnAction((ActionEvent t) -> {
            FuncionDto objFuncion = objCargado;
            int posicion = indiceActual;

            panelCuerpo = FuncionControladorVista.editar( 
                    laVentanaPrincipal, panelPrincipal, panelCuerpo,
                    Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(),
                    objFuncion, posicion);
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

        Rectangle miMarco = Marco.pintar(laVentanaPrincipal, 0.75, 0.75, 
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        centerPane.getChildren().addAll(miMarco, organizadorVertical);

        panelOpciones();
        int tamanioFuente = 25;

        if (carruselVacio.get()) {
            categoNombre = new SimpleStringProperty("N/A");
            categoPelicula = new SimpleObjectProperty<>(null);
            categoSala = new SimpleObjectProperty<>(null);
            categoFecha = new SimpleObjectProperty<>(null);
            categoHora = new SimpleObjectProperty<>(null);
            categoEdad = new SimpleBooleanProperty(false);
            categoImagen = new SimpleObjectProperty<>(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
        } else {
            categoNombre = new SimpleStringProperty(objCargado.getNombreFuncion());
            categoPelicula = new SimpleObjectProperty<>(objCargado.getIdPeliculaFuncion());
            categoSala = new SimpleObjectProperty<>(objCargado.getIdSalaFuncion());
            categoFecha = new SimpleObjectProperty<>(objCargado.getFechaFuncion());
            categoHora = new SimpleObjectProperty<>(objCargado.getHoraFuncion());
            categoEdad = new SimpleBooleanProperty(objCargado.getParaMayoresFuncion());
            categoImagen = new SimpleObjectProperty<>();
            
            try (FileInputStream imgArchivo = new FileInputStream(
                    Persistencia.RUTA_IMAGENES_EXTERNAS + 
                    Persistencia.SEPARADOR_CARPETAS + 
                    objCargado.getNombreImagenPrivadoFuncion()
            )) {
                Image imgnueva = new Image(imgArchivo);
                categoImagen.set(imgnueva);
            } catch (IOException ex) { 
                Logger.getLogger(VistaFuncionCarrusel.class.getName()).log(Level.SEVERE, null, ex);
                categoImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
            }
 
        }

        // Nombre de la funcion
        Label lblNombreCatego = new Label();
        lblNombreCatego.textProperty().bind(categoNombre);
        lblNombreCatego.setFont(Font.font("Verdana", tamanioFuente));
        lblNombreCatego.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblNombreCatego);
        
        // Pelicula de la funcion
        tamanioFuente = 20;
        Label lblPelicula = new Label();
        lblPelicula.textProperty().bind(
                Bindings.createStringBinding(()
                        -> categoPelicula.get() == null ? "" : "Película: " + categoPelicula.get().getNombrePelicula(),
                        categoPelicula
                )
        );
        lblPelicula.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblPelicula.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblPelicula);

        // Sala de la funcion
        Label lblSala = new Label();
        lblSala.textProperty().bind(
                Bindings.createStringBinding(()
                        -> categoSala.get() == null ? "" : "Sala: " + categoSala.get().getNombreSala(),
                        categoSala
                )
        );
        lblSala.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblSala.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblSala);

        // Fecha de la funcion
        Label lblFecha = new Label();
        lblFecha.textProperty().bind(Bindings.createStringBinding(()
                -> categoFecha.get() == null ? "" : "Fecha: " + categoFecha.get().toString(), categoFecha));
        lblFecha.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblFecha.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblFecha);

        // Hora de la funcion
        Label lblHora = new Label();
        lblHora.textProperty().bind(Bindings.createStringBinding(()
                -> categoHora.get() == null ? "" : "Hora: " + categoHora.get().toString(), categoHora));
        lblHora.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblHora.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblHora);

        // Clasificacion de edad
        Label lblEdad = new Label();
        lblEdad.textProperty().bind(Bindings.when(categoEdad).then("Para Mayores de Edad").otherwise("Para Menores de Edad"));
        lblEdad.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblEdad.styleProperty().bind(
                categoEdad
                        .map(dato -> dato.equals(true) ? "-fx-text-fill: green;" : "-fx-text-fill: red;")
        );
        organizadorVertical.getChildren().add(lblEdad);

        // Imagen de la funcion
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