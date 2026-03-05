package com.unimagdalena.vista.confiteria;

import com.unimagdalena.controlador.confiteria.ConfiteriaControladorListar;
import com.unimagdalena.dto.ConfiteriaDto;
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

import java.util.List;

public class VistaConfiteriaListar extends StackPane {

    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final VBox miCajaVertical;
    private final TableView<ConfiteriaDto> miTabla;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_CENTRAR;

    public VistaConfiteriaListar(Stage esce, double ancho, double alto) {
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
        int canti = ConfiteriaControladorListar.cantidadConfiteria(); // ¡ya lo tienes en el controlador!
        Text miTitulo = new Text("Listado de Confitería - (" + canti + ") ");
        miTitulo.setFill(Color.web("#54e8b7"));
        miTitulo.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        miCajaVertical.getChildren().addAll(separadorTitulo, miTitulo);
    }

    private TableColumn<ConfiteriaDto, Integer> crearColumnaCodigo() {
        TableColumn<ConfiteriaDto, Integer> columna = new TableColumn<>("Código");
        columna.setCellValueFactory(new PropertyValueFactory<>("idConfiteria"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ConfiteriaDto, String> crearColumnaNombre() {
        TableColumn<ConfiteriaDto, String> columna = new TableColumn<>("Nombre");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreProductoConfiteria"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ConfiteriaDto, Double> crearColumnaPrecio() {
        TableColumn<ConfiteriaDto, Double> columna = new TableColumn<>("Precio");
        columna.setCellValueFactory(new PropertyValueFactory<>("precioProductoConfiteria"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        
        // Formatear precio con símbolo de peso
        columna.setCellFactory(col -> new TableCell<ConfiteriaDto, Double>() {
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

    private TableColumn<ConfiteriaDto, String> crearColumnaTipo() {
        TableColumn<ConfiteriaDto, String> columna = new TableColumn<>("Tipo");
        columna.setCellValueFactory(new PropertyValueFactory<>("tipoProductoConfiteria"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ConfiteriaDto, String> crearColumnaEstado() {
        TableColumn<ConfiteriaDto, String> columna = new TableColumn<>("Estado");

        columna.setCellValueFactory(obj -> {
            Boolean disponible = obj.getValue().getProductoDisponibleConfiteria();
            String estado = (disponible != null && disponible) ? "Disponible" : "Agotado";
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
                    setStyle(estado.equals("Disponible") ? ESTILO_ELVERDE : ESTILO_ELROJO);
                }
            }
        });

        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        return columna;
    }

    private TableColumn<ConfiteriaDto, String> crearColumnaMetPago() {
        TableColumn<ConfiteriaDto, String> columna = new TableColumn<>("Método Pago");
        columna.setCellValueFactory(new PropertyValueFactory<>("metodoPagoProducto"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ConfiteriaDto, String> crearColumnaFechaCompra() {
        TableColumn<ConfiteriaDto, String> columna = new TableColumn<>("Fecha Compra");
        columna.setCellValueFactory(new PropertyValueFactory<>("fechaCompraProducto"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ConfiteriaDto, String> crearColumnaImagen() {
        TableColumn<ConfiteriaDto, String> columna = new TableColumn<>("Imagen");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPublicoConfiteria"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_IZQUIERDA);
        return columna;
    }

    private void configurarColumnas() {
        miTabla.getColumns().addAll(
                List.of(
                        crearColumnaCodigo(),   
                        crearColumnaNombre(),
                        crearColumnaPrecio(),
                        crearColumnaTipo(),
                        crearColumnaEstado(),
                        crearColumnaMetPago(),
                        crearColumnaFechaCompra(),
                        crearColumnaImagen()
                )
        );
    }

    private void crearTabla() {
        configurarColumnas();

        List<ConfiteriaDto> arrConf = ConfiteriaControladorListar.arregloConfiteria();
        ObservableList<ConfiteriaDto> datosTabla = FXCollections.observableArrayList(arrConf);
        miTabla.setItems(datosTabla);
        miTabla.setPlaceholder(new Text("No hay productos registrados"));

        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        miTabla.maxWidthProperty().bind(miEscenario.widthProperty().multiply(0.8));
        miTabla.maxHeightProperty().bind(miEscenario.heightProperty().multiply(0.6));

        miEscenario.heightProperty().addListener((o, oldval, newval) -> miTabla.setPrefHeight(newval.doubleValue()));
        VBox.setVgrow(miTabla, Priority.ALWAYS);

        miCajaVertical.getChildren().add(miTabla);
        getChildren().add(miCajaVertical);
    }
}
