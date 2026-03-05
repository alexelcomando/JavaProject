package com.unimagdalena.vista.ticket;

import com.unimagdalena.controlador.ticket.TicketControladorEliminar;
import com.unimagdalena.controlador.ticket.TicketControladorListar;
import com.unimagdalena.controlador.ticket.TicketControladorVista;
import com.unimagdalena.dto.TicketDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class VistaTicketAdministrar extends StackPane {

    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final VBox miCajaVertical;
    private final TableView<TicketDto> miTabla;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_CENTRAR;

    private Text miTitulo;
    private HBox miCajaHorizontal;
    private final ObservableList<TicketDto> datosTabla = FXCollections.observableArrayList();

    public VistaTicketAdministrar(Stage esce, double ancho, double alto) {
        setAlignment(Pos.CENTER);
        miEscenario = esce;
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        miTabla = new TableView<>();
        miCajaVertical = new VBox(20);
        getChildren().add(miMarco);
        todoResponsive();
        armarTitulo();
        crearTabla();
        mostrarIconosAdministrar();
    }

    private void todoResponsive() {
        miCajaVertical.setAlignment(Pos.TOP_CENTER);
        miCajaVertical.prefWidthProperty().bind(miMarco.widthProperty());
        miCajaVertical.prefHeightProperty().bind(miMarco.heightProperty());
    }

    private void armarTitulo() {
        Region separadorTitulo = new Region();
        separadorTitulo.prefHeightProperty().bind(miEscenario.heightProperty().multiply(0.05));
        int cantidad = TicketControladorListar.cantidadTickets();
        miTitulo = new Text("Administrar Tickets - (" + cantidad + ")");
        miTitulo.setFill(Color.web("#54e8b7"));
        miTitulo.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        miCajaVertical.getChildren().addAll(separadorTitulo, miTitulo);
    }

    private TableColumn<TicketDto, Integer> crearColumnaCodigo() {
        TableColumn<TicketDto, Integer> columna = new TableColumn<>("Código");
        columna.setCellValueFactory(new PropertyValueFactory<>("idTicket"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<TicketDto, String> crearColumnaTipoEntrada() {
        TableColumn<TicketDto, String> columna = new TableColumn<>("Tipo");
        columna.setCellValueFactory(new PropertyValueFactory<>("tipoEntradaTicket"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.12));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<TicketDto, String> crearColumnaCliente() {
        TableColumn<TicketDto, String> columna = new TableColumn<>("Cliente");
        
        columna.setCellValueFactory(obj -> {
            if (obj.getValue().getIdClienteTicket() != null) {
                String nombreCliente = obj.getValue().getIdClienteTicket().getNombreCliente();
                return new SimpleStringProperty(nombreCliente != null ? nombreCliente : "Sin cliente");
            }
            return new SimpleStringProperty("Sin cliente");
        });
        
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.18));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<TicketDto, String> crearColumnaValido() {
        TableColumn<TicketDto, String> columna = new TableColumn<>("Válido");

        columna.setCellValueFactory(obj -> {
            Boolean valido = obj.getValue().getEsValidoTicket();
            String estado = (valido != null && valido) ? "Sí" : "No";
            return new SimpleStringProperty(estado);
        });

        columna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(estado);
                    setStyle(estado.equals("Sí") ? ESTILO_ELVERDE : ESTILO_ELROJO);
                }
            }
        });

        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        return columna;
    }

    private TableColumn<TicketDto, LocalDate> crearColumnaFechaEmision() {
        TableColumn<TicketDto, LocalDate> columna = new TableColumn<>("Fecha");
        columna.setCellValueFactory(new PropertyValueFactory<>("fechaEmisionTicket"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.12));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<TicketDto, Double> crearColumnaPrecio() {
        TableColumn<TicketDto, Double> columna = new TableColumn<>("Precio");
        columna.setCellValueFactory(new PropertyValueFactory<>("precioTicket"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        
        columna.setCellFactory(col -> new TableCell<TicketDto, Double>() {
            @Override
            protected void updateItem(Double precio, boolean empty) {
                super.updateItem(precio, empty);
                if (empty || precio == null) {
                    setText(null);
                } else {
                    setText("$" + String.format("%,.2f", precio));
                }
                setStyle(ESTILO_CENTRAR);
            }
        });
        
        return columna;
    }

    private TableColumn<TicketDto, String> crearColumnaImagen() {
        TableColumn<TicketDto, String> columna = new TableColumn<>("Imagen");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPrivadoTicket"));
        columna.setPrefWidth(100);
        columna.setStyle(ESTILO_CENTRAR);

        columna.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String nombreImagenPrivada, boolean empty) {
                super.updateItem(nombreImagenPrivada, empty);

                if (empty || nombreImagenPrivada == null || nombreImagenPrivada.isBlank()) {
                    setGraphic(null);
                } else {
                    Path ruta = Paths.get(Persistencia.RUTA_IMAGENES_EXTERNAS, nombreImagenPrivada);

                    if (Files.exists(ruta)) {
                        Image img = new Image(ruta.toUri().toString(), 50, 50, true, true);
                        imageView.setImage(img);
                    } else {
                        Image img = new Image(
                                getClass().getResourceAsStream(
                                        Persistencia.RUTA_IMAGENES_INTERNAS + Configuracion.ICONO_NO_DISPONIBLE
                                ),
                                50, 50, true, true
                        );
                        imageView.setImage(img);
                    }
                    setGraphic(imageView);
                }
            }
        });
        return columna;
    }

    private void configurarColumnas() {
        miTabla.getColumns().addAll(
                List.of(
                        crearColumnaCodigo(),
                        crearColumnaTipoEntrada(),
                        crearColumnaCliente(),
                        crearColumnaValido(),
                        crearColumnaFechaEmision(),
                        crearColumnaPrecio(),
                        crearColumnaImagen()
                )
        );
    }

    private void crearTabla() {
        configurarColumnas();

        List<TicketDto> arrTickets = TicketControladorListar.arregloTickets();
        datosTabla.setAll(arrTickets);
        miTabla.setItems(datosTabla);
        miTabla.setPlaceholder(new Text("No hay tickets registrados"));

        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        miTabla.maxWidthProperty().bind(miEscenario.widthProperty().multiply(0.8));
        miTabla.maxHeightProperty().bind(miEscenario.heightProperty().multiply(0.6));

        miEscenario.heightProperty().addListener((o, oldval, newval) -> miTabla.setPrefHeight(newval.doubleValue()));
        VBox.setVgrow(miTabla, Priority.ALWAYS);

        miCajaVertical.getChildren().add(miTabla);
        getChildren().add(miCajaVertical);
    }

    private void mostrarIconosAdministrar() {
        int anchoBoton = 40;
        int tamanioIcono = 18;

        // --- ELIMINAR ---
        Button btnEliminar = new Button();
        btnEliminar.setPrefWidth(anchoBoton);
        btnEliminar.setCursor(Cursor.HAND);
        btnEliminar.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_BORRAR, tamanioIcono));
        btnEliminar.setOnAction((e) -> {
            if (miTabla.getSelectionModel().getSelectedItem() == null) {
                Mensaje.mostrar(Alert.AlertType.WARNING,
                        miEscenario, "Advertencia", "Seleccione un ticket para eliminar");
            } else {
                TicketDto objTicket = miTabla.getSelectionModel().getSelectedItem();
                String msg = String.format(
                        "¿Estás seguro de eliminar este ticket?\nTipo: %s\nCliente: %s",
                        objTicket.getTipoEntradaTicket(),
                        objTicket.getIdClienteTicket() != null ? 
                            objTicket.getIdClienteTicket().getNombreCliente() : "Sin cliente"
                );

                Alert mensajito = new Alert(Alert.AlertType.CONFIRMATION);
                mensajito.setTitle("Confirmar eliminación");
                mensajito.setHeaderText(null);
                mensajito.setContentText(msg);
                mensajito.initOwner(null);

                if (mensajito.showAndWait().get() == ButtonType.OK) {
                    int indice = miTabla.getSelectionModel().getSelectedIndex();
                    if (TicketControladorEliminar.eliminar(indice)) {
                        refrescarTabla();
                        Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario, "Éxito", "Ticket eliminado correctamente");
                    } else {
                        Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error", "No se pudo eliminar el ticket");
                    }
                }
                miTabla.getSelectionModel().clearSelection();
            }
        });

        // --- ACTUALIZAR ---
        Button btnActualizar = new Button();
        btnActualizar.setPrefWidth(anchoBoton);
        btnActualizar.setCursor(Cursor.HAND);
        btnActualizar.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_EDITAR, tamanioIcono));
        btnActualizar.setOnAction((e) -> {
            TicketDto seleccionado = miTabla.getSelectionModel().getSelectedItem();

            if (seleccionado == null) {
                Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Advertencia",
                        "¡Seleccione un ticket para actualizar!");
            } else {
                BorderPane panelPrincipal = (BorderPane) miEscenario.getScene().getRoot();
                Pane panelAnterior = this;
                int indice = miTabla.getSelectionModel().getSelectedIndex();

                StackPane panelEditar = TicketControladorVista.mostrarEditar(
                        miEscenario,
                        seleccionado,
                        indice,
                        panelPrincipal,
                        panelAnterior
                );

                panelPrincipal.setCenter(panelEditar);
            }
        });

        // --- CANCELAR ---
        Button btnCancelar = new Button();
        btnCancelar.setPrefWidth(anchoBoton);
        btnCancelar.setCursor(Cursor.HAND);
        btnCancelar.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_CANCELAR, tamanioIcono));
        btnCancelar.setOnAction((e) -> miTabla.getSelectionModel().clearSelection());

        miCajaHorizontal = new HBox(6);
        miCajaHorizontal.setAlignment(Pos.CENTER);
        miCajaHorizontal.getChildren().addAll(btnEliminar, btnActualizar, btnCancelar);
        miCajaVertical.getChildren().add(miCajaHorizontal);
    }

    public void refrescarTabla() {
        int cant = TicketControladorListar.cantidadTickets();
        miTitulo.setText("Administrar Tickets - (" + cant + ")");
        
        List<TicketDto> arrTickets = TicketControladorListar.arregloTickets();
        datosTabla.setAll(arrTickets);
        miTabla.refresh();
    }
}