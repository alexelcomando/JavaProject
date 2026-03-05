package com.unimagdalena.vista.funcion;

import com.unimagdalena.controlador.funcion.FuncionControladorListar; 
import com.unimagdalena.controlador.reserva.ReservaControladorListar; // Import necesario
import com.unimagdalena.dto.FuncionDto; 
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Marco;
import java.time.LocalDate;
import java.time.LocalTime; 
import java.util.List;
import javafx.beans.property.SimpleObjectProperty; // Import necesario
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
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

public class VistaFuncionListar extends StackPane { 

    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final VBox miCajaVertical;
    private final TableView<FuncionDto> miTabla; 

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_DERECHA = "-fx-alignment: CENTER-RIGHT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_DERECHA;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_DERECHA;

    public VistaFuncionListar(Stage esce, double ancho, double alto) {
        setAlignment(Pos.CENTER);
        miEscenario = esce;
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        miTabla = new TableView<>();
        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        setBackground(fondo); 
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
        int canti = FuncionControladorListar.cantidadFunciones(); 
        Text miTitulo = new Text("Listado de Funciones - (" + canti + ") "); 
        miTitulo.setFill(Color.web("#54e8b7"));
        miTitulo.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        miCajaVertical.getChildren().addAll(separadorTitulo, miTitulo);
    }

    private TableColumn<FuncionDto, Integer> crearColumnaCodigo() {
        TableColumn<FuncionDto, Integer> columna = new TableColumn<>("Codigo");
        columna.setCellValueFactory(new PropertyValueFactory<>("idFuncion")); 
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<FuncionDto, String> crearColumnaNombre() {
        TableColumn<FuncionDto, String> columna = new TableColumn<>("Nombre Función");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreFuncion")); 
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.18));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<FuncionDto, String> crearColumnaPelicula() {
        TableColumn<FuncionDto, String> columna = new TableColumn<>("Película");
        columna.setCellValueFactory(cellData -> {
            String nombrePelicula = cellData.getValue().getIdPeliculaFuncion().getNombrePelicula(); 
            return new javafx.beans.property.SimpleStringProperty(nombrePelicula);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<FuncionDto, String> crearColumnaSala() {
        TableColumn<FuncionDto, String> columna = new TableColumn<>("Sala");
        columna.setCellValueFactory(cellData -> {
            String nombreSala = cellData.getValue().getIdSalaFuncion().getNombreSala();
            return new javafx.beans.property.SimpleStringProperty(nombreSala);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.12));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<FuncionDto, LocalDate> crearColumnaFecha() {
        TableColumn<FuncionDto, LocalDate> columna = new TableColumn<>("Fecha");
        columna.setCellValueFactory(new PropertyValueFactory<>("fechaFuncion")); 
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<FuncionDto, LocalTime> crearColumnaHora() {
        TableColumn<FuncionDto, LocalTime> columna = new TableColumn<>("Hora");
        columna.setCellValueFactory(new PropertyValueFactory<>("horaFuncion")); 
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<FuncionDto, String> crearColumnaClasificacionEdad() {
        TableColumn<FuncionDto, String> columna = new TableColumn<>("Clasificación");
        columna.setCellValueFactory(obj -> {
            boolean paraMayores = obj.getValue().getParaMayoresFuncion(); 
            String estado = paraMayores ? "Mayores de edad" : "Menores de edad";
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
                    setStyle("Mayores de edad".equals(estadoTxT) ? ESTILO_ELVERDE : ESTILO_ELROJO);
                }
            }
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.12));
        return columna;
    }
    
    // --- NUEVA COLUMNA: Cantidad de Reservas ---
    private TableColumn<FuncionDto, Integer> crearColumnaCantReservas() {
        TableColumn<FuncionDto, Integer> columna = new TableColumn<>("Cant. Reservas");
        columna.setCellValueFactory(cellData -> {
            int idFuncion = cellData.getValue().getIdFuncion();
            long cantidad = ReservaControladorListar.arregloReservas().stream()
                    .filter(r -> r.getIdFuncionReserva() != null && r.getIdFuncionReserva().getIdFuncion() == idFuncion)
                    .count();
            return new SimpleObjectProperty<>((int) cantidad);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<FuncionDto, String> crearColumnaImagen() {
        TableColumn<FuncionDto, String> columna = new TableColumn<>("Imagen");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPublicoFuncion")); 
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_IZQUIERDA); 
        return columna;
    }

    private void configurarColumnas() {
        miTabla.getColumns().addAll(
                List.of(
                        crearColumnaCodigo(),
                        crearColumnaNombre(),
                        crearColumnaPelicula(),
                        crearColumnaSala(),
                        crearColumnaFecha(),
                        crearColumnaHora(),
                        crearColumnaClasificacionEdad(),
                        crearColumnaCantReservas(), // <- Agregada aquí
                        crearColumnaImagen()
                ));
    }

    private void crearTabla() {
        configurarColumnas();
        List<FuncionDto> arrFunciones = FuncionControladorListar.arregloFunciones(); 
        
        ObservableList<FuncionDto> datosTabla = FXCollections.observableArrayList(arrFunciones);
        miTabla.setItems(datosTabla);
        miTabla.setPlaceholder(new Text("No hay funciones registradas")); 

        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        
        miTabla.maxWidthProperty().bind(miEscenario.widthProperty().multiply(0.75)); 
        miTabla.maxHeightProperty().bind(miEscenario.heightProperty().multiply(0.5));

        miEscenario.heightProperty().addListener((o, oldVal, newVal) -> miTabla.setPrefHeight(newVal.doubleValue()));

        VBox.setVgrow(miTabla, Priority.ALWAYS);

        miCajaVertical.getChildren().add(miTabla);
        getChildren().add(miCajaVertical);
    }
}