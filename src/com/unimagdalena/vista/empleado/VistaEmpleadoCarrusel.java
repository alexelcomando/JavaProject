package com.unimagdalena.vista.empleado;

import com.unimagdalena.controlador.empleado.EmpleadoControladorCant;
import com.unimagdalena.controlador.empleado.EmpleadoControladorEliminar;
import com.unimagdalena.controlador.empleado.EmpleadoControladorListar;
import com.unimagdalena.controlador.empleado.EmpleadoControladorUna;
import com.unimagdalena.controlador.empleado.EmpleadoControladorVista;
import com.unimagdalena.dto.EmpleadoDto;
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

public class VistaEmpleadoCarrusel extends SubScene {

    private final BorderPane miBorderPane;
    private final Stage laVentanaPrincipal;
    private final VBox organizadorVertical;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    private int indiceActual;
    private int totalEmpleados;
    private EmpleadoDto objCargado;

    // Propiedades para Binding en la Interfaz
    private StringProperty propTitulo;
    private StringProperty propNombre;
    private BooleanProperty propTiempoCompleto;
    private StringProperty propNivelAcceso;
    private ObjectProperty<SalaDto> propSala;
    private ObjectProperty<LocalTime> propHoraEntrada;
    private ObjectProperty<LocalTime> propHoraSalida;
    private ObjectProperty<Image> propImagen;

    private BooleanProperty carruselVacio;

    public VistaEmpleadoCarrusel(Stage ventanaPadre, BorderPane princ, Pane pane, double anchoPanel, double altoPanel, int indice) {
        super(new BorderPane(), anchoPanel, altoPanel);

        indiceActual = indice;
        totalEmpleados = EmpleadoControladorListar.cantidadEmpleados();
        carruselVacio = new SimpleBooleanProperty(totalEmpleados == 0);

        miBorderPane = (BorderPane) this.getRoot();

        laVentanaPrincipal = ventanaPadre;
        panelPrincipal = princ;
        panelCuerpo = pane;

        if (carruselVacio.get()) {
            objCargado = null;
        } else {
            if (indiceActual >= totalEmpleados) {
                indiceActual = 0;
            }
            objCargado = EmpleadoControladorUna.obtenerEmpleado(indiceActual);
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
        propTitulo.set("Detalle del Empleado (" + (indiceActual + 1) + " / " + totalEmpleados + ")");
        propNombre.set(objCargado.getNombreEmpleado());
        propTiempoCompleto.set(objCargado.getEsTiempoCompletoEmpleado());
        propNivelAcceso.set(objCargado.getNivelAccesoEmpleado());
        propSala.set(objCargado.getSalaAsignadaEmpleado());
        propHoraEntrada.set(objCargado.getHoraEntradaEmpleado());
        propHoraSalida.set(objCargado.getHoraSalidaEmpleado());

        try (FileInputStream imgArchivo = new FileInputStream(
                Persistencia.RUTA_IMAGENES_EXTERNAS
                + Persistencia.SEPARADOR_CARPETAS
                + objCargado.getNombreImagenPrivadoEmpleado()
        )) {
            Image imgnueva = new Image(imgArchivo);
            propImagen.set(imgnueva);
        } catch (IOException ex) {
            Logger.getLogger(VistaEmpleadoCarrusel.class.getName()).log(Level.SEVERE, null, ex);
            propImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
        }
    }

    private void construirPanelIzquierdo(double porcentaje) {
        Button btnAnterior = new Button();
        btnAnterior.setGraphic(Icono.obtenerIcono("btnAtras.png", 80));
        btnAnterior.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        btnAnterior.setCursor(Cursor.HAND);

        btnAnterior.disableProperty().bind(carruselVacio);

        btnAnterior.setOnAction(e -> {
            indiceActual = obtenerIndice("Anterior", indiceActual, totalEmpleados);
            objCargado = EmpleadoControladorUna.obtenerEmpleado(indiceActual);
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
            indiceActual = obtenerIndice("Siguiente", indiceActual, totalEmpleados);
            objCargado = EmpleadoControladorUna.obtenerEmpleado(indiceActual);
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

        totalEmpleados = EmpleadoControladorCant.obtener();
        if (carruselVacio.get()) {
            propTitulo = new SimpleStringProperty("No hay empleados");
        } else {
            propTitulo = new SimpleStringProperty("Detalle del Empleado (" + (indiceActual + 1) + " / " + totalEmpleados + ")");
        }

        Label lblTitulo = new Label();
        lblTitulo.textProperty().bind(propTitulo);
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
            EmpleadoDto objEmpleado = objCargado;

            String texto1, texto2, texto3, texto4;
            texto1 = "¿Seguro que quieres borrar al empleado?\n";
            texto2 = "\nCodigo: " + objEmpleado.getIdEmpleado();
            texto3 = "\nNombre: " + objEmpleado.getNombreEmpleado();
            texto4 = "\nEsto es irreversible!!!";

            Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
            msg.setTitle("Advertencia Borrar");
            msg.setHeaderText(null);
            msg.setContentText(texto1 + texto2 + texto3 + texto4);
            msg.initOwner(null);

            if (msg.showAndWait().get() == ButtonType.OK) {
                int posicion = indiceActual;
                if (EmpleadoControladorEliminar.borrar(posicion)) {
                    totalEmpleados = EmpleadoControladorListar.cantidadEmpleados();
                    carruselVacio.set(totalEmpleados == 0);

                    if (carruselVacio.get()) {
                        objCargado = null;
                        propTitulo.set("No hay Empleados");
                        propNombre.set("N/A");
                        propTiempoCompleto.set(false);
                        propNivelAcceso.set("N/A");
                        propSala.set(null);
                        propHoraEntrada.set(null);
                        propHoraSalida.set(null);
                        propImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
                    } else {
                        if (indiceActual >= totalEmpleados) {
                            indiceActual = 0;
                        }
                        objCargado = EmpleadoControladorUna.obtenerEmpleado(indiceActual);
                        actualizarDatosCarrusel();
                    }
                    Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Exito", "Empleado eliminado");
                } else {
                    Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo borrar el empleado");
                }
            }
        });

        Button btnActualizar = new Button();
        btnActualizar.setPrefWidth(anchoBoton);
        btnActualizar.setCursor(Cursor.HAND);
        btnActualizar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_EDITAR, tamanioIcono));
        btnActualizar.disableProperty().bind(carruselVacio);

        btnActualizar.setOnAction((ActionEvent t) -> {
            EmpleadoDto objEmpleado = objCargado;
            int posicion = indiceActual;

            panelCuerpo = EmpleadoControladorVista.editar(
                    laVentanaPrincipal, panelPrincipal, panelCuerpo,
                    Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(),
                    objEmpleado, posicion);
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

        // Marco
        Rectangle miMarco = Marco.pintar(laVentanaPrincipal, 0.60, 0.75,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        centerPane.getChildren().addAll(miMarco, organizadorVertical);

        panelOpciones();
        int tamanioFuente = 25;

        if (carruselVacio.get()) {
            propNombre = new SimpleStringProperty("N/A");
            propTiempoCompleto = new SimpleBooleanProperty(false);
            propNivelAcceso = new SimpleStringProperty("N/A");
            propSala = new SimpleObjectProperty<>(null);
            propHoraEntrada = new SimpleObjectProperty<>(null);
            propHoraSalida = new SimpleObjectProperty<>(null);
            propImagen = new SimpleObjectProperty<>(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
        } else {
            propNombre = new SimpleStringProperty(objCargado.getNombreEmpleado());
            propTiempoCompleto = new SimpleBooleanProperty(objCargado.getEsTiempoCompletoEmpleado());
            propNivelAcceso = new SimpleStringProperty(objCargado.getNivelAccesoEmpleado());
            propSala = new SimpleObjectProperty<>(objCargado.getSalaAsignadaEmpleado());
            propHoraEntrada = new SimpleObjectProperty<>(objCargado.getHoraEntradaEmpleado());
            propHoraSalida = new SimpleObjectProperty<>(objCargado.getHoraSalidaEmpleado());
            propImagen = new SimpleObjectProperty<>();
            try (FileInputStream imgArchivo = new FileInputStream(
                    Persistencia.RUTA_IMAGENES_EXTERNAS
                    + Persistencia.SEPARADOR_CARPETAS
                    + objCargado.getNombreImagenPrivadoEmpleado()
            )) {
                Image imgnueva = new Image(imgArchivo);
                propImagen.set(imgnueva);
            } catch (IOException ex) {
                Logger.getLogger(VistaEmpleadoCarrusel.class.getName()).log(Level.SEVERE, null, ex);
                propImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
            }
        }

        // --- Nombre Empleado ---
        Label lblNombre = new Label();
        lblNombre.textProperty().bind(propNombre);
        lblNombre.setFont(Font.font("Verdana", tamanioFuente));
        lblNombre.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblNombre);
        
        // --- Sala Asignada ---
        tamanioFuente = 20;
        Label lblSala = new Label();
        lblSala.textProperty().bind(
                Bindings.createStringBinding(()
                        -> propSala.get() == null ? "Sin Asignación" : "Sala: " + propSala.get().getNombreSala(),
                        propSala
                )
        );
        lblSala.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblSala.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblSala);

        // --- Horario ---
        Label lblHorario = new Label();
        lblHorario.textProperty().bind(Bindings.createStringBinding(()
                -> (propHoraEntrada.get() == null || propHoraSalida.get() == null) 
                        ? "" 
                        : "Horario: " + propHoraEntrada.get() + " - " + propHoraSalida.get(), 
                propHoraEntrada, propHoraSalida));
        lblHorario.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblHorario.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblHorario);

        // --- Tiempo Completo ---
        Label lblTiempoC = new Label();
        lblTiempoC.textProperty().bind(Bindings.when(propTiempoCompleto).then("Tiempo Completo").otherwise("Medio Tiempo"));
        lblTiempoC.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblTiempoC.styleProperty().bind(
                propTiempoCompleto
                        .map(dato -> dato.equals(true) ? "-fx-text-fill: #6C3483;" : "-fx-text-fill: orange;")
        );
        organizadorVertical.getChildren().add(lblTiempoC);
        
        // --- Nivel Acceso ---
        Label lblNivel = new Label();
        lblNivel.textProperty().bind(Bindings.concat("Nivel: ", propNivelAcceso));
        lblNivel.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblNivel.setTextFill(Color.web("#29e6c3")); // Un color distintivo del borde
        organizadorVertical.getChildren().add(lblNivel);

        // --- Imagen ---
        ImageView imgMostrar = new ImageView();
        imgMostrar.setFitHeight(200);
        imgMostrar.setSmooth(true);
        imgMostrar.setPreserveRatio(true);
        imgMostrar.imageProperty().bind(propImagen);
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