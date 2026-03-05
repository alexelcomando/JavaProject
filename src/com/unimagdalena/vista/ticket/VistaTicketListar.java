package com.unimagdalena.vista.ticket;

import com.unimagdalena.controlador.ticket.TicketControladorListar;
import com.unimagdalena.dto.TicketDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Marco;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class VistaTicketListar extends StackPane {

    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final VBox miCajaVertical;
    private final TableView<TicketDto> miTabla;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_CENTRAR;

    public VistaTicketListar(Stage esce, double ancho, double alto) {
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
        Text miTitulo = new Text("Listado de Tickets - (" + cantidad + ")");
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
        TableColumn<TicketDto, String> columna = new TableColumn<>("Tipo Entrada");
        columna.setCellValueFactory(new PropertyValueFactory<>("tipoEntradaTicket"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
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
        
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
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
        TableColumn<TicketDto, LocalDate> columna = new TableColumn<>("Fecha Emisión");
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
        
        // Formatear precio con símbolo de peso
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
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPublicoTicket"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.27));
        columna.setStyle(ESTILO_IZQUIERDA);
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
        ObservableList<TicketDto> datosTabla = FXCollections.observableArrayList(arrTickets);
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
}