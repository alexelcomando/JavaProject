package com.unimagdalena.vista.empleado;

import com.unimagdalena.controlador.empleado.EmpleadoControladorListar;
import com.unimagdalena.dto.EmpleadoDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Marco;
import java.time.LocalTime;
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

public class VistaEmpleadoListar extends StackPane {
    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final VBox miCajaVertical;
    private final TableView<EmpleadoDto> miTabla;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_DERECHA = "-fx-alignment: CENTER-RIGHT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_DERECHA;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_DERECHA;

    public VistaEmpleadoListar(Stage esce, double ancho, double alto) {
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
        separadorTitulo.prefHeightProperty().bind(miEscenario.heightProperty().multiply(0.10));
        int canti = EmpleadoControladorListar.cantidadEmpleados();
        Text miTitulo = new Text("Listado de empleados - (" + canti + ") ");
        miTitulo.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        miCajaVertical.getChildren().addAll(separadorTitulo, miTitulo);
    }

    private TableColumn<EmpleadoDto, Integer> crearColumnaCodigo() {
        TableColumn<EmpleadoDto, Integer> columna = new TableColumn<>("Codigo");
        columna.setCellValueFactory(new PropertyValueFactory<>("idEmpleado"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<EmpleadoDto, String> crearColumnaNombre() {
        TableColumn<EmpleadoDto, String> columna = new TableColumn<>("Nombre");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<EmpleadoDto, String> crearColumnaTiempoCompleto() {
        TableColumn<EmpleadoDto, String> columna = new TableColumn<>("Tiempo Completo");
        columna.setCellValueFactory(obj -> {
            boolean esTC = obj.getValue().getEsTiempoCompletoEmpleado();
            String estado = esTC ? "Si" : "No";
            return new SimpleStringProperty(estado);
        });

        columna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String estadoTxT, boolean empty) {
                super.updateItem(estadoTxT, empty);
                if (empty || estadoTxT == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(estadoTxT);
                    setStyle("Si".equals(estadoTxT) ? ESTILO_ELVERDE : ESTILO_ELROJO);
                }
            }
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.12));
        return columna;
    }

    private TableColumn<EmpleadoDto, String> crearColumnaNivelAcceso() {
        TableColumn<EmpleadoDto, String> columna = new TableColumn<>("Nivel Acceso");
        columna.setCellValueFactory(new PropertyValueFactory<>("nivelAccesoEmpleado"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<EmpleadoDto, String> crearColumnaSala() {
        TableColumn<EmpleadoDto, String> columna = new TableColumn<>("Sala Asignada");
        columna.setCellValueFactory(cellData -> {
            String nombreSala = "Sin Asignar";
            if (cellData.getValue().getSalaAsignadaEmpleado() != null) {
                nombreSala = cellData.getValue().getSalaAsignadaEmpleado().getNombreSala();
            }
            return new SimpleStringProperty(nombreSala);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<EmpleadoDto, LocalTime> crearColumnaHoraEntrada() {
        TableColumn<EmpleadoDto, LocalTime> columna = new TableColumn<>("Hora Entrada");
        columna.setCellValueFactory(new PropertyValueFactory<>("horaEntradaEmpleado"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<EmpleadoDto, LocalTime> crearColumnaHoraSalida() {
        TableColumn<EmpleadoDto, LocalTime> columna = new TableColumn<>("Hora Salida");
        columna.setCellValueFactory(new PropertyValueFactory<>("horaSalidaEmpleado"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<EmpleadoDto, String> crearColumnaImagenEmpleado() {
        TableColumn<EmpleadoDto, String> columna = new TableColumn<>("Imagen");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPublicoEmpleado"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private void configurarColumnas() {
        miTabla.getColumns().addAll(
                List.of(
                        crearColumnaCodigo(),
                        crearColumnaNombre(),
                        crearColumnaTiempoCompleto(),
                        crearColumnaNivelAcceso(),
                        crearColumnaSala(),
                        crearColumnaHoraEntrada(),
                        crearColumnaHoraSalida(),
                        crearColumnaImagenEmpleado()
                ));
    }

    private void crearTabla() {
        configurarColumnas();
        List<EmpleadoDto> arrEmpleados = EmpleadoControladorListar.arregloEmpleados();
        ObservableList<EmpleadoDto> datosTabla = FXCollections.observableArrayList(arrEmpleados);
        miTabla.setItems(datosTabla);
        miTabla.setPlaceholder(new Text("No hay empleados registrados"));

        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        miTabla.maxWidthProperty().bind(miEscenario.widthProperty().multiply(0.8)); // Ajustado un poco más ancho para las columnas extra
        miTabla.maxHeightProperty().bind(miEscenario.heightProperty().multiply(0.5));

        miEscenario.heightProperty().addListener((o, oldVal, newVal) -> miTabla.setPrefHeight(newVal.doubleValue()));

        VBox.setVgrow(miTabla, Priority.ALWAYS);

        miCajaVertical.getChildren().add(miTabla);
        getChildren().add(miCajaVertical);
    }

}