package com.unimagdalena.vista.reserva;

import com.unimagdalena.controlador.reserva.ReservaControladorEliminar;
import com.unimagdalena.controlador.reserva.ReservaControladorListar;
import com.unimagdalena.controlador.reserva.ReservaControladorUna;
import com.unimagdalena.controlador.reserva.ReservaControladorVista;
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.dto.ReservaDto;
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

public class VistaReservaCarrusel extends SubScene {

    private final BorderPane miBorderPane;
    private final Stage laVentanaPrincipal;
    private final VBox organizadorVertical;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    private int indiceActual;
    private int totalReservas;
    private ReservaDto objCargado;

    private StringProperty categoTitulo;
    private StringProperty categoNombreReserva;
    private ObjectProperty<ClienteDto> categoCliente;
    private ObjectProperty<FuncionDto> categoFuncion;
    private ObjectProperty<LocalDate> categoFecha;
    private StringProperty categoMetodoPago;
    private BooleanProperty categoBebidas;
    private ObjectProperty<Image> categoImagen;

    private BooleanProperty carruselVacio;

    public VistaReservaCarrusel(Stage ventanaPadre, BorderPane princ, Pane pane, double anchoPanel, double altoPanel, int indice) {
        super(new BorderPane(), anchoPanel, altoPanel);

        indiceActual = indice;
        totalReservas = ReservaControladorListar.cantidadReservas();
        carruselVacio = new SimpleBooleanProperty(totalReservas == 0);

        miBorderPane = (BorderPane) this.getRoot();

        laVentanaPrincipal = ventanaPadre;
        panelPrincipal = princ;
        panelCuerpo = pane;

        if (carruselVacio.get()) {
            objCargado = null;
        } else {
            if (indiceActual >= totalReservas) {
                indiceActual = 0;
            }
            objCargado = ReservaControladorUna.obtenerReserva(indiceActual);
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
        // SOLUCIÓN: Comprobación de nulidad para manejar el caso de lista vacía/última eliminación.
        if (objCargado == null) {
            categoTitulo.set("No hay reservas");
            categoNombreReserva.set("N/A");
            categoCliente.set(null);
            categoFuncion.set(null);
            categoFecha.set(null);
            categoMetodoPago.set("N/A");
            categoBebidas.set(false);
            categoImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
            return;
        }

        categoTitulo.set("Detalle de la Reserva (" + (indiceActual + 1) + " / " + totalReservas + ")");
        categoNombreReserva.set(objCargado.getNombreReserva());
        categoCliente.set(objCargado.getIdClienteReserva());
        categoFuncion.set(objCargado.getIdFuncionReserva());
        categoFecha.set(objCargado.getFechaReserva());
        categoMetodoPago.set(objCargado.getMetodoPagoReserva());
        categoBebidas.set(objCargado.getIncluyeBebidasReserva());

        try (FileInputStream imgArchivo = new FileInputStream(
                Persistencia.RUTA_IMAGENES_EXTERNAS
                + Persistencia.SEPARADOR_CARPETAS
                + objCargado.getNombreImagenPrivadoReserva()
        )) {
            Image imgnueva = new Image(imgArchivo);
            categoImagen.set(imgnueva);
        } catch (IOException ex) {
            Logger.getLogger(VistaReservaCarrusel.class.getName()).log(Level.SEVERE, null, ex);
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
            indiceActual = obtenerIndice("Anterior", indiceActual, totalReservas);
            objCargado = ReservaControladorUna.obtenerReserva(indiceActual);
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
            indiceActual = obtenerIndice("Siguiente", indiceActual, totalReservas);
            objCargado = ReservaControladorUna.obtenerReserva(indiceActual);
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

        totalReservas = ReservaControladorListar.cantidadReservas();
        if (carruselVacio.get()) {
            categoTitulo = new SimpleStringProperty("No hay reservas");
        } else {
            categoTitulo = new SimpleStringProperty("Detalle de la Reserva (" + (indiceActual + 1) + " / " + totalReservas + ")");
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
            ReservaDto objReserva = objCargado;

            String texto1, texto2, texto3, texto4;
            texto1 = "¿Seguro que quieres borrar la reserva?\n";
            texto2 = "\nCodigo: " + objReserva.getIdReserva();
            texto3 = "\nNombre: " + objReserva.getNombreReserva();
            texto4 = "\nEsto es irreversible!!!";

            Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
            msg.setTitle("Advertencia Borrar");
            msg.setHeaderText(null);
            msg.setContentText(texto1 + texto2 + texto3 + texto4);
            msg.initOwner(null);

            if (msg.showAndWait().get() == ButtonType.OK) {
                int posicion = indiceActual;
                if (ReservaControladorEliminar.borrar(posicion)) {
                    totalReservas = ReservaControladorListar.cantidadReservas();
                    carruselVacio.set(totalReservas == 0);

                    if (carruselVacio.get()) {
                        objCargado = null;
                        actualizarDatosCarrusel();
                    } else {
                        if (indiceActual >= totalReservas) {
                            indiceActual = 0;
                        }
                        objCargado = ReservaControladorUna.obtenerReserva(indiceActual);
                        actualizarDatosCarrusel();
                    }
                    Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Exito", "Reserva eliminada");
                } else {
                    Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo borrar la reserva");
                }
            }
        });

        Button btnActualizar = new Button();
        btnActualizar.setPrefWidth(anchoBoton);
        btnActualizar.setCursor(Cursor.HAND);
        btnActualizar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_EDITAR, tamanioIcono));
        btnActualizar.disableProperty().bind(carruselVacio);

        btnActualizar.setOnAction((ActionEvent t) -> {
            ReservaDto objReserva = objCargado;
            int posicion = indiceActual;

            panelCuerpo = ReservaControladorVista.editar(
                    laVentanaPrincipal, panelPrincipal, panelCuerpo,
                    Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(),
                    objReserva, posicion);
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
            categoNombreReserva = new SimpleStringProperty("N/A");
            categoCliente = new SimpleObjectProperty<>(null);
            categoFuncion = new SimpleObjectProperty<>(null);
            categoFecha = new SimpleObjectProperty<>(null);
            categoMetodoPago = new SimpleStringProperty("N/A");
            categoBebidas = new SimpleBooleanProperty(false);
            categoImagen = new SimpleObjectProperty<>(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
        } else {
            categoNombreReserva = new SimpleStringProperty(objCargado.getNombreReserva());
            categoCliente = new SimpleObjectProperty<>(objCargado.getIdClienteReserva());
            categoFuncion = new SimpleObjectProperty<>(objCargado.getIdFuncionReserva());
            categoFecha = new SimpleObjectProperty<>(objCargado.getFechaReserva());
            categoMetodoPago = new SimpleStringProperty(objCargado.getMetodoPagoReserva());
            categoBebidas = new SimpleBooleanProperty(objCargado.getIncluyeBebidasReserva());
            categoImagen = new SimpleObjectProperty<>();

            try (FileInputStream imgArchivo = new FileInputStream(
                    Persistencia.RUTA_IMAGENES_EXTERNAS
                    + Persistencia.SEPARADOR_CARPETAS
                    + objCargado.getNombreImagenPrivadoReserva()
            )) {
                Image imgnueva = new Image(imgArchivo);
                categoImagen.set(imgnueva);
            } catch (IOException ex) {
                Logger.getLogger(VistaReservaCarrusel.class.getName()).log(Level.SEVERE, null, ex);
                categoImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
            }
        }
        
        Label lblNombreReserva = new Label();
        lblNombreReserva.textProperty().bind(categoNombreReserva);
        lblNombreReserva.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblNombreReserva.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblNombreReserva);


        Label lblCliente = new Label();
        lblCliente.textProperty().bind(Bindings.createStringBinding(()
                -> categoCliente.get() == null ? "Cliente: N/A" : "Cliente: " + categoCliente.get().getNombreCliente(), categoCliente));
        lblCliente.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblCliente.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblCliente);

        Label lblFuncion = new Label();
        lblFuncion.textProperty().bind(Bindings.createStringBinding(()
                -> categoFuncion.get() == null ? "Función: N/A" : "Función: " + categoFuncion.get().getNombreFuncion(), categoFuncion));
        lblFuncion.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblFuncion.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblFuncion);

        tamanioFuente = 20;
        Label lblFecha = new Label();
        lblFecha.textProperty().bind(Bindings.createStringBinding(()
                -> categoFecha.get() == null ? "" : "Fecha: " + categoFecha.get().toString(), categoFecha));
        lblFecha.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblFecha.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblFecha);

        Label lblMetodo = new Label();
        lblMetodo.textProperty().bind(Bindings.concat("Pago: ", categoMetodoPago));
        lblMetodo.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblMetodo.setTextFill(Color.web("#29e6c3"));
        organizadorVertical.getChildren().add(lblMetodo);

        Label lblBebidas = new Label();
        lblBebidas.textProperty().bind(Bindings.when(categoBebidas).then("Incluye Bebidas").otherwise("No Incluye Bebidas"));
        lblBebidas.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblBebidas.styleProperty().bind(
                categoBebidas
                        .map(dato -> dato.equals(true) ? "-fx-text-fill: green;" : "-fx-text-fill: red;")
        );
        organizadorVertical.getChildren().add(lblBebidas);

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