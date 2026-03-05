package com.unimagdalena.vista.pelicula;

import com.unimagdalena.controlador.cliente.ClienteControladorListar;
import com.unimagdalena.controlador.funcion.FuncionControladorListar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorListar;
import com.unimagdalena.controlador.sala.SalaControladorListar; // Import necesario
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Marco;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
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

public class VistaPeliculaListar extends StackPane {

    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final VBox miCajaVertical;
    private final TableView<PeliculaDto> miTabla;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_DERECHA = "-fx-alignment: CENTER-RIGHT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_DERECHA;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_DERECHA;

    public VistaPeliculaListar(Stage esce, double ancho, double alto) {
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
        int canti = PeliculaControladorListar.cantidadPeliculas();
        Text miTitulo = new Text("Listado de peliculas - (" + canti + ") ");
        miTitulo.setFill(Color.web("#54e8b7"));
        miTitulo.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        miCajaVertical.getChildren().addAll(separadorTitulo, miTitulo);
    }

    private TableColumn<PeliculaDto, Integer> crearColumnaCodigo() {
        TableColumn<PeliculaDto, Integer> columna = new TableColumn<>("Codigo");
        columna.setCellValueFactory(new PropertyValueFactory<>("idPelicula"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<PeliculaDto, String> crearColumnaNombre() {
        TableColumn<PeliculaDto, String> columna = new TableColumn<>("Nombre");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombrePelicula"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.18));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<PeliculaDto, LocalDate> crearColumnaFechaEstreno() {
        TableColumn<PeliculaDto, LocalDate> columna = new TableColumn<>("Fecha Estreno");
        columna.setCellValueFactory(new PropertyValueFactory<>("fechaEstrenoPelicula"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<PeliculaDto, String> crearColumnaGenero() {
        TableColumn<PeliculaDto, String> columna = new TableColumn<>("Genero");

        columna.setCellValueFactory(cellData -> {
            String nombreGenero = cellData.getValue().getIdGeneroPelicula().getNombreGenero();
            return new SimpleStringProperty(nombreGenero);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<PeliculaDto, String> crearColumnaClasificacionEdad() {
        TableColumn<PeliculaDto, String> columna = new TableColumn<>("Clasificacion");
        columna.setCellValueFactory(new PropertyValueFactory<>("clasificacionEdadPelicula"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<PeliculaDto, String> crearColumna3D() {
        TableColumn<PeliculaDto, String> columna = new TableColumn<>("¿Es 3D?");
        columna.setCellValueFactory(obj -> {

            boolean es3D = obj.getValue().getEs3dPelicula();

            String estado = es3D ? "Si" : "No";
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

    // --- COLUMNAS EXISTENTES ---
    private TableColumn<PeliculaDto, Integer> crearColumnaCantFunciones() {
        TableColumn<PeliculaDto, Integer> columna = new TableColumn<>("Cant. Funciones");
        columna.setCellValueFactory(cellData -> {
            int idPelicula = cellData.getValue().getIdPelicula();
            long cantidad = FuncionControladorListar.arregloFunciones().stream()
                    .filter(f -> f.getIdPeliculaFuncion().getIdPelicula() == idPelicula)
                    .count();
            return new SimpleObjectProperty<>((int) cantidad);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<PeliculaDto, Integer> crearColumnaCantClientes() {
        TableColumn<PeliculaDto, Integer> columna = new TableColumn<>("Cant. Clientes");
        columna.setCellValueFactory(cellData -> {
            int idPelicula = cellData.getValue().getIdPelicula();
            long cantidad = ClienteControladorListar.obtenerTodos().stream()
                    .filter(c -> c.getIdPeliculaCliente() != null && c.getIdPeliculaCliente().getIdPelicula() == idPelicula)
                    .count();
            return new SimpleObjectProperty<>((int) cantidad);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }
    
    // --- NUEVA COLUMNA: Cantidad de Salas ---
    private TableColumn<PeliculaDto, Integer> crearColumnaCantSalas() {
        TableColumn<PeliculaDto, Integer> columna = new TableColumn<>("Cant. Salas");
        columna.setCellValueFactory(cellData -> {
            int idPelicula = cellData.getValue().getIdPelicula();
            long cantidad = SalaControladorListar.arregloSalas().stream()
                    .filter(s -> s.getIdPeliculaSala() != null && s.getIdPeliculaSala().getIdPelicula() == idPelicula)
                    .count();
            return new SimpleObjectProperty<>((int) cantidad);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<PeliculaDto, String> crearColumnaImagenPelicula() {
        TableColumn<PeliculaDto, String> columna = new TableColumn<>("Imagen");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPublicoPelicula"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private void configurarColumnas() {
        miTabla.getColumns().addAll(
                List.of(
                        crearColumnaCodigo(),
                        crearColumnaNombre(),
                        crearColumnaGenero(),
                        crearColumnaFechaEstreno(),
                        crearColumnaClasificacionEdad(),
                        crearColumna3D(),
                        crearColumnaCantFunciones(),
                        crearColumnaCantClientes(),
                        crearColumnaCantSalas(), // <- Agregada aquí
                        crearColumnaImagenPelicula()
                ));

    }

    private void crearTabla() {
        configurarColumnas();
        List<PeliculaDto> arrPeliculas = PeliculaControladorListar.arregloPeliculas();
        ObservableList<PeliculaDto> datosTabla = FXCollections.observableArrayList(arrPeliculas);
        miTabla.setItems(datosTabla);
        miTabla.setPlaceholder(new Text("No hay peliculas registradas"));

        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        miTabla.maxWidthProperty().bind(miEscenario.widthProperty().multiply(0.75));
        miTabla.maxHeightProperty().bind(miEscenario.heightProperty().multiply(0.5));

        miEscenario.heightProperty().addListener((o, oldVal, newVal) -> miTabla.setPrefHeight(newVal.doubleValue()));

        VBox.setVgrow(miTabla, Priority.ALWAYS);

        miCajaVertical.getChildren().add(miTabla);
        getChildren().add(miCajaVertical);
    }
}