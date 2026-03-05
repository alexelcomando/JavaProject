package com.unimagdalena.vista.reserva;

import com.unimagdalena.controlador.reserva.ReservaControladorListar;
import com.unimagdalena.dto.ReservaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Marco;
import java.time.LocalDate;
import java.util.List;
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

public class VistaReservaListar extends StackPane {

    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final VBox miCajaVertical;
    private final TableView<ReservaDto> miTabla;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_CENTRAR;

    public VistaReservaListar(Stage esce, double ancho, double alto) {
        setAlignment(Pos.CENTER);
        miEscenario = esce;
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        miTabla = new TableView<>();
        miCajaVertical = new VBox(20);
        getChildren().add(miMarco);
        todoResponsive20Puntos();
        armarTitulo();
        crearTabla();
    }

    private void todoResponsive20Puntos() {
        miCajaVertical.setAlignment(Pos.TOP_CENTER);
        miCajaVertical.prefWidthProperty().bind(miMarco.widthProperty());
        miCajaVertical.prefHeightProperty().bind(miMarco.heightProperty());
    }

    private void armarTitulo() {
        Region separadorTitulo = new Region();
        separadorTitulo.prefHeightProperty().bind(miEscenario.heightProperty().multiply(0.05));
        int canti = ReservaControladorListar.cantidadReservas();
        Text miTitulo = new Text("Listado de Reservas - (" + canti + ") ");
        miTitulo.setFill(Color.web("#54e8b7"));
        miTitulo.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        miCajaVertical.getChildren().addAll(separadorTitulo, miTitulo);
    }

    private TableColumn<ReservaDto, Integer> crearColumnaCodigo() {
        TableColumn<ReservaDto, Integer> columna = new TableColumn<>("Código");
        columna.setCellValueFactory(new PropertyValueFactory<>("idReserva"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }
    
    private TableColumn<ReservaDto, String> crearColumnaNombre() {
        TableColumn<ReservaDto, String> columna = new TableColumn<>("Nombre/Desc.");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreReserva"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.18));
        columna.setStyle(ESTILO_IZQUIERDA);
        return columna;
    }

    private TableColumn<ReservaDto, String> crearColumnaCliente() {
        TableColumn<ReservaDto, String> columna = new TableColumn<>("Cliente");
        columna.setCellValueFactory(cellData -> {
            String nombreCliente = cellData.getValue().getIdClienteReserva().getNombreCliente();
            return new SimpleStringProperty(nombreCliente);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.18));
        columna.setStyle(ESTILO_IZQUIERDA);
        return columna;
    }

    private TableColumn<ReservaDto, String> crearColumnaFuncion() {
        TableColumn<ReservaDto, String> columna = new TableColumn<>("Función");
        columna.setCellValueFactory(cellData -> {
            String nombreFuncion = cellData.getValue().getIdFuncionReserva().getNombreFuncion();
            return new SimpleStringProperty(nombreFuncion);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ReservaDto, LocalDate> crearColumnaFecha() {
        TableColumn<ReservaDto, LocalDate> columna = new TableColumn<>("Fecha");
        columna.setCellValueFactory(new PropertyValueFactory<>("fechaReserva"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ReservaDto, String> crearColumnaMetodoPago() {
        TableColumn<ReservaDto, String> columna = new TableColumn<>("Método Pago");
        columna.setCellValueFactory(new PropertyValueFactory<>("metodoPagoReserva"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ReservaDto, String> crearColumnaBebidas() {
        TableColumn<ReservaDto, String> columna = new TableColumn<>("Bebidas");

        columna.setCellValueFactory(obj -> {
            Boolean incluye = obj.getValue().getIncluyeBebidasReserva();
            String estado = (incluye != null && incluye) ? "Sí" : "No";
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

    private TableColumn<ReservaDto, String> crearColumnaImagen() {
        TableColumn<ReservaDto, String> columna = new TableColumn<>("Imagen");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPublicoReserva"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.13));
        columna.setStyle(ESTILO_IZQUIERDA);
        return columna;
    }

    private void configurarColumnas() {
        miTabla.getColumns().addAll(
                List.of(
                        crearColumnaCodigo(),
                        crearColumnaNombre(),
                        crearColumnaCliente(),
                        crearColumnaFuncion(),
                        crearColumnaFecha(),
                        crearColumnaMetodoPago(),
                        crearColumnaBebidas(),
                        crearColumnaImagen()
                )
        );
    }

    private void crearTabla() {
        configurarColumnas();

        List<ReservaDto> arrReserva = ReservaControladorListar.arregloReservas();
        ObservableList<ReservaDto> datosTabla = FXCollections.observableArrayList(arrReserva);
        miTabla.setItems(datosTabla);
        miTabla.setPlaceholder(new Text("No hay reservas registradas"));

        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        miTabla.maxWidthProperty().bind(miEscenario.widthProperty().multiply(0.8));
        miTabla.maxHeightProperty().bind(miEscenario.heightProperty().multiply(0.6));

        miEscenario.heightProperty().addListener((o, oldval, newval) -> miTabla.setPrefHeight(newval.doubleValue()));
        VBox.setVgrow(miTabla, Priority.ALWAYS);

        miCajaVertical.getChildren().add(miTabla);
        getChildren().add(miCajaVertical);
    }
}