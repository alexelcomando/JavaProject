package com.unimagdalena.vista.ticket;

import com.unimagdalena.controlador.ticket.TicketControladorEliminar;
import com.unimagdalena.controlador.ticket.TicketControladorListar;
import com.unimagdalena.controlador.ticket.TicketControladorVista; // Necesario para el fallback
import com.unimagdalena.dto.TicketDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class VistaTicketCarrusel extends StackPane {

    private final Stage miEscenario;
    private final BorderPane panelPrincipal;
    private final Pane panelAnterior;
    private final VBox contenedorPrincipal;

    private int indiceActual;
    private List<TicketDto> tickets;

    private Label lblTitulo;
    private Label lblTipoEntrada;
    private Label lblCliente;
    private Label lblValido;
    private Label lblFechaEmision;
    private Label lblPrecio;
    private ImageView imgTicket;
    private Label lblContador;

    public VistaTicketCarrusel(Stage esce, BorderPane panelPrincipal, Pane panelAnterior, int indice) {
        this.miEscenario = esce;
        this.panelPrincipal = panelPrincipal;
        this.panelAnterior = panelAnterior;
        this.indiceActual = indice;

        setAlignment(Pos.CENTER);
        
        // Aplicar fondo
        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        setBackground(fondo);

        // Marco
        Rectangle miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE,
                Configuracion.MARCO_ANCHO_PORCENTAJE, Configuracion.DEGRADEE_ARREGLO,
                Configuracion.COLOR_BORDE);

        contenedorPrincipal = new VBox(15);
        contenedorPrincipal.setAlignment(Pos.TOP_CENTER);
        contenedorPrincipal.setPadding(new Insets(30, 40, 30, 40));

        getChildren().addAll(miMarco, contenedorPrincipal);

        cargarTickets();
        if (!tickets.isEmpty()) {
            construirVista();
            mostrarTicket(indiceActual);
        }
    }

    private void cargarTickets() {
        tickets = TicketControladorListar.arregloTickets();
        if (tickets.isEmpty()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Sin tickets",
                    "No hay tickets para mostrar");
            if (panelAnterior != null) {
                panelPrincipal.setCenter(panelAnterior);
            }
        }
    }

    private void construirVista() {
        // Título
        lblTitulo = new Label("Carrusel de Tickets");
        lblTitulo.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        lblTitulo.setTextFill(Color.web("#54e8b7"));
        contenedorPrincipal.getChildren().add(lblTitulo);

        // Botones de acción (Editar/Eliminar)
        HBox botonesAccion = crearBotonesAccion();
        contenedorPrincipal.getChildren().add(botonesAccion);

        // Contador
        lblContador = new Label("");
        lblContador.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        lblContador.setTextFill(Color.web("#2C3E50"));
        contenedorPrincipal.getChildren().add(lblContador);

        // Panel de imagen con navegación
        HBox panelImagenNavegacion = crearPanelImagenConNavegacion();
        contenedorPrincipal.getChildren().add(panelImagenNavegacion);

        // Información del ticket
        VBox infoBox = crearPanelInformacion();
        contenedorPrincipal.getChildren().add(infoBox);
    }

    private HBox crearBotonesAccion() {
        int anchoBoton = 45;
        int tamanioIcono = 20;

        // Botón Eliminar
        Button btnEliminar = new Button();
        btnEliminar.setPrefWidth(anchoBoton);
        btnEliminar.setPrefHeight(anchoBoton);
        btnEliminar.setCursor(Cursor.HAND);
        btnEliminar.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_BORRAR, tamanioIcono));
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5;");
        btnEliminar.setOnAction(e -> eliminarTicket());

        // Botón Editar
        Button btnEditar = new Button();
        btnEditar.setPrefWidth(anchoBoton);
        btnEditar.setPrefHeight(anchoBoton);
        btnEditar.setCursor(Cursor.HAND);
        btnEditar.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_EDITAR, tamanioIcono));
        btnEditar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5;");
        btnEditar.setOnAction(e -> editarTicket());

        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(btnEliminar, btnEditar);
        
        return box;
    }

    private HBox crearPanelImagenConNavegacion() {
        // Botón Anterior
        Button btnAnterior = new Button();
        btnAnterior.setGraphic(Icono.obtenerIcono("btnAtras.png", 60));
        btnAnterior.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        btnAnterior.setCursor(Cursor.HAND);
        btnAnterior.setOnAction(e -> navegarAnterior());

        // Imagen del ticket
        imgTicket = new ImageView();
        imgTicket.setFitWidth(300);
        imgTicket.setFitHeight(300);
        imgTicket.setPreserveRatio(true);
        imgTicket.setSmooth(true);

        // Botón Siguiente
        Button btnSiguiente = new Button();
        btnSiguiente.setGraphic(Icono.obtenerIcono("btnSiguiente.png", 60));
        btnSiguiente.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        btnSiguiente.setCursor(Cursor.HAND);
        btnSiguiente.setOnAction(e -> navegarSiguiente());

        // Panel horizontal: Anterior | Imagen | Siguiente
        HBox panelImagenNav = new HBox(20);
        panelImagenNav.setAlignment(Pos.CENTER);
        panelImagenNav.getChildren().addAll(btnAnterior, imgTicket, btnSiguiente);

        return panelImagenNav;
    }

    private VBox crearPanelInformacion() {
        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(10, 0, 0, 0));
        infoBox.setMaxWidth(600);

        lblTipoEntrada = crearLabel("");
        lblCliente = crearLabel("");
        lblValido = crearLabel("");
        lblFechaEmision = crearLabel("");
        lblPrecio = crearLabel("");

        infoBox.getChildren().addAll(lblTipoEntrada, lblCliente, lblValido,
                lblFechaEmision, lblPrecio);

        return infoBox;
    }

    private Label crearLabel(String texto) {
        Label label = new Label(texto);
        label.setFont(Font.font("Verdana", 18));
        label.setTextFill(Color.web("#34495E"));
        label.setWrapText(true);
        return label;
    }

    private void mostrarTicket(int indice) {
        if (tickets.isEmpty() || indice < 0 || indice >= tickets.size()) {
            return;
        }

        TicketDto ticket = tickets.get(indice);

        // Actualizar contador
        lblContador.setText("Ticket " + (indice + 1) + " de " + tickets.size());

        // Tipo de entrada
        lblTipoEntrada.setText("Tipo de entrada: " + ticket.getTipoEntradaTicket());
        lblTipoEntrada.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        // Cliente
        String nombreCliente = (ticket.getIdClienteTicket() != null)
                ? ticket.getIdClienteTicket().getNombreCliente()
                : "Sin cliente";
        lblCliente.setText("Cliente: " + nombreCliente);

        // Estado de validez
        String estado = (ticket.getEsValidoTicket() != null && ticket.getEsValidoTicket())
                ? "Válido" : "No válido";
        lblValido.setText("Estado: " + estado);
        lblValido.setTextFill((ticket.getEsValidoTicket() != null && ticket.getEsValidoTicket())
                ? Color.web("#27AE60") : Color.web("#E74C3C"));
        lblValido.setFont(Font.font("Verdana", FontWeight.BOLD, 18));

        // Fecha de emisión
        lblFechaEmision.setText("Fecha de emisión: " + ticket.getFechaEmisionTicket());

        // Precio
        lblPrecio.setText("Precio: $" + String.format("%.2f", ticket.getPrecioTicket()));
        lblPrecio.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        lblPrecio.setTextFill(Color.web("#27AE60"));

        // Cargar imagen
        String nombreImagen = ticket.getNombreImagenPrivadoTicket();
        if (nombreImagen != null && !nombreImagen.isEmpty()) {
            ImageView imgCargada = Icono.obtenerFotosExternas(nombreImagen, 300);
            if (imgCargada != null) {
                imgTicket.setImage(imgCargada.getImage());
            } else {
                imgTicket.setImage(Icono.obtenerIcono("imgNoDisponible.png", 300).getImage());
            }
        } else {
            imgTicket.setImage(Icono.obtenerIcono("imgNoDisponible.png", 300).getImage());
        }
    }

    private void navegarAnterior() {
        if (tickets.isEmpty()) return; // Evita error si se pulsa antes de tiempo
        if (indiceActual > 0) {
            indiceActual--;
            mostrarTicket(indiceActual);
        } else {
            // Circular: ir al último
            indiceActual = tickets.size() - 1;
            mostrarTicket(indiceActual);
        }
    }

    private void navegarSiguiente() {
        if (tickets.isEmpty()) return; // Evita error si se pulsa antes de tiempo
        if (indiceActual < tickets.size() - 1) {
            indiceActual++;
            mostrarTicket(indiceActual);
        } else {
            // Circular: volver al primero
            indiceActual = 0;
            mostrarTicket(indiceActual);
        }
    }

    private void editarTicket() {
        if (tickets.isEmpty()) return;
        TicketDto ticket = tickets.get(indiceActual);
        StackPane vistaEditar = TicketControladorVista.mostrarEditar(
                miEscenario, ticket, indiceActual, panelPrincipal, this);
        panelPrincipal.setCenter(vistaEditar);
    }

    private void eliminarTicket() {
        if (tickets.isEmpty()) {
            return;
        }
        
        TicketDto ticket = tickets.get(indiceActual);

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar este ticket?");
        confirmacion.setContentText("Ticket: " + ticket.getTipoEntradaTicket()
                + " - Cliente: "
                + (ticket.getIdClienteTicket() != null
                ? ticket.getIdClienteTicket().getNombreCliente() : "Sin cliente"));

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Eliminar usando el índice actual
            boolean eliminado = TicketControladorEliminar.eliminar(indiceActual);
            
            if (eliminado) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario, "Éxito",
                        "Ticket eliminado correctamente");

                // Recargar la lista actualizada desde la fuente de datos
                tickets = TicketControladorListar.arregloTickets();

                // Verificar si ya no hay tickets
                if (tickets.isEmpty()) {
                    // Limpiar la vista antes de regresar
                    contenedorPrincipal.getChildren().clear();
                    regresar(); // Llama a la lógica de retorno con fallback
                    return;
                }

                // Ajustar el índice si es necesario
                if (indiceActual >= tickets.size()) {
                    indiceActual = tickets.size() - 1;
                }

                // Mostrar el ticket actualizado
                mostrarTicket(indiceActual);
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error",
                        "No se pudo eliminar el ticket. Verifique la conexión.");
            }
        }
    }

    public void refrescarCarrusel() {
        tickets = TicketControladorListar.arregloTickets();
        
        // Verificar que aún hay tickets
        if (tickets.isEmpty()) {
            regresar();
            return;
        }
        
        // Ajustar índice si es necesario
        if (indiceActual >= tickets.size()) {
            indiceActual = tickets.size() - 1;
        }
        
        // Actualizar la vista con los nuevos datos
        mostrarTicket(indiceActual);
    }

    private void regresar() {
        // LÓGICA CORREGIDA: Incluye el fallback inspirado en VistaSalaCarrusel.java
        if (panelAnterior != null) {
            if (panelAnterior instanceof VistaTicketAdministrar) {
                // Si viene de Administrar, se refresca la tabla antes de volver a ella.
                ((VistaTicketAdministrar) panelAnterior).refrescarTabla();
            }
            panelPrincipal.setCenter(panelAnterior);
        } else {
            // CASO FALLBACK (Viene del menú, panelAnterior == null):
            // Redirigir a la vista de administración de tickets.
            
            // Usamos las dimensiones de la aplicación completa, ya que la vista de administración se diseñó para ese tamaño
            double ancho = miEscenario.getWidth();
            double alto = miEscenario.getHeight(); 
            
            // Usamos el controlador de vistas para cargar la vista de Administración como fallback
            StackPane vistaAdm = TicketControladorVista.abrirAdministrar(
                    miEscenario,
                    ancho,
                    alto
            );
            panelPrincipal.setCenter(vistaAdm);
        }
    }
}