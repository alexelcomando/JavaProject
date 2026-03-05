package com.unimagdalena.vista.genero;

import com.unimagdalena.controlador.genero.GeneroControladorListar;
import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Fondo;
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

public class VistaGeneroListar extends StackPane {

    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final VBox miCajaVertical;
    private final TableView<GeneroDto> miTabla;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_DERECHA = "-fx-alignment: CENTER-RIGHT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_CENTRAR;
    private static final String ESTILO_AMARILLO = "-fx-text-fill: gold; " + ESTILO_CENTRAR;

    public VistaGeneroListar(Stage esce, double ancho, double alto) {
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
        separadorTitulo.prefHeightProperty().bind(miEscenario.heightProperty().multiply(0.05));
        int canti = GeneroControladorListar.cantidadGeneros();
        Text miTitulo = new Text("Listado de géneros - (" + canti + ") ");
        miTitulo.setFill(Color.web("#54e8b7"));
        miTitulo.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        miCajaVertical.getChildren().addAll(separadorTitulo, miTitulo);
    }

    private TableColumn<GeneroDto, Integer> crearColumnaCodigo() {
        TableColumn<GeneroDto, Integer> columna = new TableColumn<>("Codigo");
        columna.setCellValueFactory(new PropertyValueFactory<>("idGenero"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<GeneroDto, String> crearColumnaNombre() {
        TableColumn<GeneroDto, String> columna = new TableColumn<>("Nombre");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreGenero"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<GeneroDto, String> crearColumnaEstado() {
        TableColumn<GeneroDto, String> columna = new TableColumn<>("Estado");
        columna.setCellValueFactory(obj -> {
            String estado = obj.getValue().getEstadoGenero() ? "Activo" : "Inactivo";
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
                    setStyle("Activo".equals(estadoTxT) ? ESTILO_ELVERDE : ESTILO_ELROJO);
                }
            }
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.12));
        return columna;
    }
    
    private TableColumn<GeneroDto, LocalDate> crearColumnaFechaCreacion() {
        TableColumn<GeneroDto, LocalDate> columna = new TableColumn<>("Fecha Creación");
        columna.setCellValueFactory(new PropertyValueFactory<>("fechaCreacionGenero"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<GeneroDto, Integer> crearColumnaCalificacion() {
        TableColumn<GeneroDto, Integer> columna = new TableColumn<>("Calificación");
        columna.setCellValueFactory(new PropertyValueFactory<>("calificacionGenero"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer calificacion, boolean empty) {
                super.updateItem(calificacion, empty);
                if (empty || calificacion == null || calificacion == 0) {
                    setText(null);
                    setStyle(ESTILO_CENTRAR);
                } else {
                    String estrellas = "★".repeat(calificacion);
                    setText(estrellas);
                    setStyle(ESTILO_AMARILLO);
                }
            }
        });
        return columna;
    }
    
    private TableColumn<GeneroDto, String> crearColumnaPublicoObjetivo() {
        TableColumn<GeneroDto, String> columna = new TableColumn<>("Público Objetivo");
        columna.setCellValueFactory(new PropertyValueFactory<>("publicoObjetivoGenero"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String publico, boolean empty) {
                super.updateItem(publico, empty);
                if (empty || publico == null) {
                    setText(null);
                    setStyle(ESTILO_CENTRAR);
                } else {
                    setText(publico);
                    setStyle("MAYORES".equals(publico) ? ESTILO_ELVERDE : ESTILO_ELROJO);
                }
            }
        });
        return columna;
    }

    private TableColumn<GeneroDto, Integer> crearColumnaCantidadPeliculas() {
        TableColumn<GeneroDto, Integer> columna = new TableColumn<>("Pelis");
        columna.setCellValueFactory(new PropertyValueFactory<>("cantPeliculasGenero"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<GeneroDto, String> crearColumnaNombreImagen() {
        TableColumn<GeneroDto, String> columna = new TableColumn<>("Nombre Imagen");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPublicoGenero"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.17));
        columna.setStyle(ESTILO_IZQUIERDA);
        return columna;
    }


    private void configurarColumnas() {
        miTabla.getColumns().addAll(
                List.of(
                        crearColumnaCodigo(), 
                        crearColumnaNombre(), 
                        crearColumnaEstado(),
                        crearColumnaFechaCreacion(), 
                        crearColumnaCalificacion(), 
                        crearColumnaPublicoObjetivo(),
                        crearColumnaCantidadPeliculas(), // COLUMNA DE PELÍCULAS MOVÍA ANTES DE IMAGEN
                        crearColumnaNombreImagen()));
    }

    private void crearTabla() {
        configurarColumnas();

        List<GeneroDto> arrGeneros = GeneroControladorListar.arregloGeneros();
        ObservableList<GeneroDto> datosTabla = FXCollections.observableArrayList(arrGeneros);
        miTabla.setItems(datosTabla);
        miTabla.setPlaceholder(new Text("No hay géneros registrados"));

        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        miTabla.maxWidthProperty().bind(miEscenario.widthProperty().multiply(0.8));
        miTabla.maxHeightProperty().bind(miEscenario.heightProperty().multiply(0.6));

        miEscenario.heightProperty().addListener((o, oldval, newval) -> miTabla.setPrefHeight(newval.doubleValue()));
        VBox.setVgrow(miTabla, Priority.ALWAYS);

        miCajaVertical.getChildren().add(miTabla);
        getChildren().add(miCajaVertical);
    }

}