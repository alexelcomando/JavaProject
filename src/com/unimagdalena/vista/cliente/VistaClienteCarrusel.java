package com.unimagdalena.vista.cliente;

import com.unimagdalena.controlador.cliente.ClienteControladorEliminar;
import com.unimagdalena.controlador.cliente.ClienteControladorListar;
import com.unimagdalena.controlador.cliente.ClienteControladorUna;
import com.unimagdalena.controlador.cliente.ClienteControladorVista;
import com.unimagdalena.controlador.reserva.ReservaControladorListar; // Importado
import com.unimagdalena.controlador.ticket.TicketControladorListar; // Importado
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Contenedor;
import com.unimagdalena.helpers.constante.IconoNombre;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.time.LocalDate;
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

public class VistaClienteCarrusel extends SubScene {

    private final BorderPane miBorderPane;
    private final Stage laVentanaPrincipal;
    private final VBox organizadorVertical;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    private int indiceActual;
    private int totalClientes;
    private ClienteDto objCargado;

    private StringProperty clienteTitulo;
    private StringProperty clienteNombre;
    private StringProperty clienteTipoDoc;
    private ObjectProperty<LocalDate> clienteFechaNac;
    private BooleanProperty clienteSexo;
    private StringProperty clienteVip;
    private StringProperty clientePelicula;
    private ObjectProperty<Image> clienteImagen;

    private BooleanProperty carruselVacio;

    public VistaClienteCarrusel(Stage ventanaPadre, BorderPane princ, Pane pane, double anchoPanel, double altoPanel, int indice) {
        super(new BorderPane(), anchoPanel, altoPanel);

        miBorderPane = (BorderPane) this.getRoot();

        laVentanaPrincipal = ventanaPadre;
        panelPrincipal = princ;
        panelCuerpo = pane;

        totalClientes = ClienteControladorListar.contar();
        carruselVacio = new SimpleBooleanProperty(totalClientes == 0);
        indiceActual = indice;

        if (carruselVacio.get()) {
            objCargado = null;
        } else {
            if (indiceActual >= totalClientes) {
                indiceActual = 0;
            }
            objCargado = ClienteControladorUna.obtenerCliente(indiceActual);
        }

        clienteNombre = new SimpleStringProperty();
        clienteTipoDoc = new SimpleStringProperty();
        clienteFechaNac = new SimpleObjectProperty<>();
        clienteSexo = new SimpleBooleanProperty();
        clienteVip = new SimpleStringProperty();
        clientePelicula = new SimpleStringProperty();
        clienteImagen = new SimpleObjectProperty<>();
        
        if (carruselVacio.get()) {
            clienteTitulo = new SimpleStringProperty("No hay clientes registrados");
        } else {
            clienteTitulo = new SimpleStringProperty("Detalle del Cliente (" + (indiceActual + 1) + " / " + totalClientes + ")");
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
        if (objCargado == null) {
            clienteTitulo.set("No hay clientes registrados");
            clienteNombre.set("N/A");
            clienteTipoDoc.set("N/A");
            clienteFechaNac.set(null);
            clienteSexo.set(false);
            clienteVip.set("N/A");
            clientePelicula.set("N/A");
            clienteImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
            return;
        }
        
        clienteTitulo.set("Detalle del Cliente (" + (indiceActual + 1) + " / " + totalClientes + ")");
        clienteNombre.set(objCargado.getNombreCliente());
        clienteTipoDoc.set(objCargado.getTipoDocumentoCliente());
        clienteFechaNac.set(objCargado.getFechaDeNacimientoCliente());
        clienteSexo.set(objCargado.getSexoCliente());
        clienteVip.set(objCargado.getEsAccesoVipCliente());

        PeliculaDto pelicula = objCargado.getIdPeliculaCliente();
        if (pelicula != null) {
            clientePelicula.set(pelicula.getNombrePelicula());
        } else {
            clientePelicula.set("Sin asignar");
        }

        ImageView imgCargada = Icono.obtenerFotosExternas(objCargado.getNombreImagenPrivadoCliente(), 250);
        clienteImagen.set(imgCargada.getImage());
    }

    private void construirPanelIzquierdo(double porcentaje) {
        Button btnAnterior = new Button();
        btnAnterior.setGraphic(Icono.obtenerIcono("btnAtras.png", 80));
        btnAnterior.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        btnAnterior.setCursor(Cursor.HAND);
        btnAnterior.setOnAction(e -> {
            if (carruselVacio.get()) return;
            indiceActual = obtenerIndice("Anterior", indiceActual, totalClientes);
            objCargado = ClienteControladorUna.obtenerCliente(indiceActual);
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
        btnSiguiente.setOnAction((ActionEvent t) -> {
            if (carruselVacio.get()) return;
            indiceActual = obtenerIndice("Siguiente", indiceActual, totalClientes);
            objCargado = ClienteControladorUna.obtenerCliente(indiceActual);
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

        Label lblTitulo = new Label();
        lblTitulo.textProperty().bind(clienteTitulo);
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
            ClienteDto objCliente = objCargado;

            // --- VALIDACIÓN DE ELIMINACIÓN ---
            int idCliente = objCliente.getIdCliente();
            long cantReservas = ReservaControladorListar.arregloReservas().stream()
                    .filter(r -> r.getIdClienteReserva() != null && r.getIdClienteReserva().getIdCliente() == idCliente)
                    .count();
            long cantTickets = TicketControladorListar.arregloTickets().stream()
                    .filter(tk -> tk.getIdClienteTicket() != null && tk.getIdClienteTicket().getIdCliente() == idCliente)
                    .count();

            if (cantReservas > 0 || cantTickets > 0) {
                String mensaje = "No se puede eliminar el cliente.\n";
                if (cantReservas > 0) mensaje += "- Tiene " + cantReservas + " reserva(s) activa(s).\n";
                if (cantTickets > 0) mensaje += "- Ha comprado " + cantTickets + " ticket(s).";

                Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error al eliminar", mensaje);
                return;
            }
            // ---------------------------------

            String texto1 = "¿Seguro que quieres borrar el cliente?\n";
            String texto2 = "\nID: " + objCliente.getIdCliente();
            String texto3 = "\nNombre: " + objCliente.getNombreCliente();
            String texto4 = "\nEsto es irreversible!!!";

            Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
            msg.setTitle("Advertencia Borrar");
            msg.setHeaderText(null);
            msg.setContentText(texto1 + texto2 + texto3 + texto4);
            msg.initOwner(null);

            if (msg.showAndWait().get() == ButtonType.OK) {
                int posicion = indiceActual;
                if (ClienteControladorEliminar.borrar(posicion)) {
                    totalClientes = ClienteControladorListar.contar();
                    carruselVacio.set(totalClientes == 0);

                    if (carruselVacio.get()) {
                        objCargado = null;
                        actualizarDatosCarrusel();
                    } else {
                        if (indiceActual >= totalClientes) {
                            indiceActual = 0;
                        }
                        objCargado = ClienteControladorUna.obtenerCliente(indiceActual);
                        actualizarDatosCarrusel();
                    }
                    Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Exito", "Cliente eliminado");
                } else {
                    Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo borrar el cliente");
                }
            }
        });

        Button btnActualizar = new Button();
        btnActualizar.setPrefWidth(anchoBoton);
        btnActualizar.setCursor(Cursor.HAND);
        btnActualizar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_EDITAR, tamanioIcono));
        btnActualizar.disableProperty().bind(carruselVacio);

        btnActualizar.setOnAction((ActionEvent t) -> {
            ClienteDto objCliente = objCargado;
            int posicion = indiceActual;

            panelCuerpo = ClienteControladorVista.abrirEditar(
                    laVentanaPrincipal, panelPrincipal, panelCuerpo,
                    Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(),
                    objCliente, posicion);
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

        if (!carruselVacio.get()) {
             actualizarDatosCarrusel();
        } else {
             clienteNombre.set("N/A");
             clienteImagen.set(Icono.obtenerIcono("imgNoDisponible.png", 250).getImage());
             clienteTipoDoc.set("N/A");
             clienteFechaNac.set(null);
             clienteSexo.set(false);
             clienteVip.set("N/A");
             clientePelicula.set("N/A");
        }

        Label lblNombreCliente = new Label();
        lblNombreCliente.textProperty().bind(clienteNombre);
        lblNombreCliente.setFont(Font.font("Verdana", tamanioFuente));
        lblNombreCliente.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblNombreCliente);

        ImageView imgMostrar = new ImageView();
        imgMostrar.setFitHeight(200);
        imgMostrar.setSmooth(true);
        imgMostrar.setPreserveRatio(true);
        imgMostrar.imageProperty().bind(clienteImagen);
        organizadorVertical.getChildren().add(imgMostrar);

        tamanioFuente = 20;
        Label lblTipoDoc = new Label();
        lblTipoDoc.textProperty().bind(Bindings.createStringBinding(() ->
            carruselVacio.get() ? "" : "Doc: " + clienteTipoDoc.get(), clienteTipoDoc, carruselVacio));
        lblTipoDoc.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblTipoDoc.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblTipoDoc);

        Label lblFechaNac = new Label();
        lblFechaNac.textProperty().bind(Bindings.createStringBinding(() ->
            clienteFechaNac.get() == null ? "" : "Nacimiento: " + clienteFechaNac.get().toString(), clienteFechaNac));
        lblFechaNac.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblFechaNac.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblFechaNac);

        Label lblSexo = new Label();
        lblSexo.textProperty().bind(Bindings.when(carruselVacio).then("")
            .otherwise(Bindings.when(clienteSexo).then("Sexo: Masculino").otherwise("Sexo: Femenino")));
        lblSexo.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblSexo.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblSexo);
        
        Label lblPelicula = new Label();
        lblPelicula.textProperty().bind(Bindings.createStringBinding(() -> 
            carruselVacio.get() ? "" : "Película: " + clientePelicula.get(), clientePelicula, carruselVacio));
        lblPelicula.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblPelicula.setTextFill(Color.web("#6C3483"));
        organizadorVertical.getChildren().add(lblPelicula);

        Label lblVip = new Label();
        lblVip.textProperty().bind(Bindings.createStringBinding(() -> "Acceso VIP: " + clienteVip.get(), clienteVip));
        lblVip.setFont(Font.font("Verdana", FontWeight.BOLD, tamanioFuente));
        lblVip.styleProperty().bind(
                clienteVip.map(dato -> "Si".equals(dato) ? "-fx-text-fill: green;" : "-fx-text-fill: red;")
        );
        organizadorVertical.getChildren().add(lblVip);

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