package com.unimagdalena.vista.genero;

import com.unimagdalena.controlador.genero.GeneroControladorEliminar;
import com.unimagdalena.controlador.genero.GeneroControladorListar;
import com.unimagdalena.controlador.genero.GeneroControladorVista;
import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class VistaGeneroAdm extends StackPane {

    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final VBox miCajaVertical;
    private final TableView<GeneroDto> miTabla;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_DERECHA = "-fx-alignment: CENTER-RIGHT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_DERECHA;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_DERECHA;

    private Text miTitulo;
    private HBox miCajaHorizontal;

    private final ObservableList<GeneroDto> datosTabla
            = FXCollections.observableArrayList();
    
    private BorderPane miPanelPrincipal;
    private Pane miPanelCuerpo;

    public VistaGeneroAdm(Stage esce, double ancho, double alto) {
        this(esce, null, null, ancho, alto); 
    }

    public VistaGeneroAdm(Stage esce, BorderPane princ, Pane pane, double ancho, double alto) {
        setAlignment(Pos.CENTER);
        miEscenario = esce;
        miPanelPrincipal = princ; 
        miPanelCuerpo = pane;
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE, Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        miTabla = new TableView<>();
        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        setBackground(fondo); 
        miCajaVertical = new VBox(20);
        getChildren().add(miMarco);
        todoResponsive20Puntos();
        armarTitulo();
        crearTabla();
        mostrarIconosAdministrar();
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
        miTitulo = new Text("Administrador de géneros - (" + canti + ") ");
        miTitulo.setFill(Color.web("#54e8b7"));
        miTitulo.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        miCajaVertical.getChildren().addAll(separadorTitulo, miTitulo);
    }

    private TableColumn<GeneroDto, Integer> crearColumnaCodigo() {
        TableColumn<GeneroDto, Integer> columna = new TableColumn<>("Codigo");
        columna.setCellValueFactory(new PropertyValueFactory<>("idGenero"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<GeneroDto, String> crearColumnaNombre() {
        TableColumn<GeneroDto, String> columna = new TableColumn<>("Nombre");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreGenero"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
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
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        return columna;
    }

    private TableColumn<GeneroDto, Integer> crearColumnaCantidadPeliculas() {
        TableColumn<GeneroDto, Integer> columna = new TableColumn<>("Pelis");
        columna.setCellValueFactory(new PropertyValueFactory<>("cantPeliculasGenero"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }
    
    private TableColumn<GeneroDto, String> crearColumnaImagen() {
        TableColumn<GeneroDto, String> columna = new TableColumn<>("Imagen");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPrivadoGenero"));
        
        columna.setCellFactory(col -> new TableCell<GeneroDto, String>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String nombreImagen, boolean empty) {
                super.updateItem(nombreImagen, empty);
                if (empty || nombreImagen == null || nombreImagen.isEmpty()) {
                    setGraphic(null);
                    setAlignment(Pos.CENTER);
                    setText(null);
                } else {
                    ImageView img = Icono.obtenerFotosExternas(nombreImagen, 50);
                    if (img != null) {
                        setGraphic(img);
                        setAlignment(Pos.CENTER);
                        setText(null);
                    } else {
                        setGraphic(null);
                        setAlignment(Pos.CENTER);
                        setText("Error");
                    }
                }
            }
        });
        
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.25));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }


    private void configurarColumnas() {
        miTabla.getColumns().addAll(
                List.of(
                        crearColumnaCodigo(), 
                        crearColumnaNombre(), 
                        crearColumnaEstado(), 
                        crearColumnaCantidadPeliculas(),
                        crearColumnaImagen()
                )
        );
    }

    private void crearTabla() {
        configurarColumnas();

        List<GeneroDto> arrGeneros = GeneroControladorListar.arregloGeneros();
        datosTabla.setAll(arrGeneros);

        miTabla.setItems(datosTabla);
        miTabla.setPlaceholder(new Text("No hay géneros registrados"));

        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        miTabla.maxWidthProperty().bind(miEscenario.widthProperty().multiply(0.8));
        miTabla.maxHeightProperty().bind(miEscenario.heightProperty().multiply(0.5));

        miEscenario.heightProperty().addListener((o, oldval, newval)
                -> miTabla.setPrefHeight(newval.doubleValue())
        );
        VBox.setVgrow(miTabla, Priority.ALWAYS);

        miCajaVertical.getChildren().add(miTabla);
        getChildren().add(miCajaVertical);
    }

    private void mostrarIconosAdministrar() {
        int anchoBoton = 40;
        int tamanioIcono = 18;
        
        Button btnEliminar = new Button();
        btnEliminar.setPrefWidth(anchoBoton);
        btnEliminar.setCursor(Cursor.HAND);
        btnEliminar.setGraphic(
                Icono.obtenerIcono(
                        Configuracion.ICONO_BORRAR,
                        tamanioIcono)
        );
        btnEliminar.setOnAction((e) -> {
            if (miTabla.getSelectionModel()
                    .getSelectedItem() == null) {
                Mensaje.mostrar(Alert.AlertType.WARNING,
                        miEscenario, "Te veo", "MAL ! agarra algo");
            } else {
                GeneroDto objGenero = miTabla
                        .getSelectionModel()
                        .getSelectedItem();
                if (objGenero.getCantPeliculasGenero() == 0) {
                    String msg1, msg2, msg3, msg4;
                    msg1 = "¿Estás seguro mi vale?";
                    msg2 = "\nCódigo: " + objGenero.getIdGenero();
                    msg3 = "\nGénero: " + objGenero.getNombreGenero();
                    msg4 = "\nSi se fue, se fue!";

                    Alert mensajito = new Alert(Alert.AlertType.CONFIRMATION);
                    mensajito.setTitle("Telo advierto");
                    mensajito.setHeaderText(null);
                    mensajito.setContentText(msg1 + msg2 + msg3 + msg4);
                    mensajito.initOwner(null);

                    if (mensajito.showAndWait().get() == ButtonType.OK) {
                        Integer idGenero = miTabla.getSelectionModel().getSelectedIndex();
                        if (GeneroControladorEliminar.borrar(idGenero)) {
                            int cant = GeneroControladorListar.cantidadGeneros();
                            miTitulo.setText("Administrador de géneros - (" + cant + ") ");
                            List<GeneroDto> quedaron =  GeneroControladorListar.arregloGeneros();
                            datosTabla.setAll(quedaron);
                            miTabla.refresh();
                            
                            Mensaje.mostrar(
                                    Alert.AlertType.INFORMATION,
                                    miEscenario, "EXITO",
                                    "Que buen inglés, lo borré");
                        } else {
                            Mensaje.mostrar(
                                    Alert.AlertType.ERROR,
                                    miEscenario, "Pailas",
                                    "No lo pude borrar!");
                        }

                    } else {
                        miTabla.getSelectionModel().clearSelection();
                    }

                } else {
                    Mensaje.mostrar(
                            Alert.AlertType.ERROR,
                            miEscenario, "Ey",
                            "Ya tiene peliculas");
                }
            }
        });
        
        Button btnActualizar = new Button();
        btnActualizar.setPrefWidth(anchoBoton);
        btnActualizar.setCursor(Cursor.HAND);
        btnActualizar.setGraphic(
                Icono.obtenerIcono(
                        Configuracion.ICONO_EDITAR,
                        tamanioIcono)
        );
        btnActualizar.setOnAction((e) -> {
            if (miTabla.getSelectionModel().getSelectedItem() == null) {
                Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Advertencia", "No ha seleccionado un género para editar");
            } else if (miPanelPrincipal == null) {
                 Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error Interno", "Falta la referencia al panel principal para navegar.");
            } else {
                GeneroDto objGenero = miTabla.getSelectionModel().getSelectedItem();
                int posicion = miTabla.getSelectionModel().getSelectedIndex();
                
                StackPane vistaEditar = GeneroControladorVista.abrirEditar(
                        miEscenario, 
                        miPanelPrincipal, 
                        miPanelCuerpo,
                        Configuracion.ANCHO_APP, 
                        Configuracion.ALTO_APP,
                        objGenero, 
                        posicion);
                        
                miPanelPrincipal.setCenter(vistaEditar);
            }
        });
        
        Button btnCancelar = new Button();
        btnCancelar.setPrefWidth(anchoBoton);
        btnCancelar.setCursor(Cursor.HAND);
        btnCancelar.setGraphic(
                Icono.obtenerIcono(
                        Configuracion.ICONO_CANCELAR,
                        tamanioIcono)
        );
        btnCancelar.setOnAction((e) -> {
            miTabla.getSelectionModel().clearSelection();
        });
        
        miCajaHorizontal = new HBox(6);
        miCajaHorizontal.setAlignment(Pos.CENTER);
        miCajaHorizontal.getChildren()
                .addAll(btnEliminar, btnActualizar, btnCancelar);
        miCajaVertical.getChildren().add(miCajaHorizontal);

    }

}