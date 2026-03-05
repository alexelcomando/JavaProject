package com.unimagdalena.vista.pelicula;

import com.unimagdalena.controlador.cliente.ClienteControladorListar;
import com.unimagdalena.controlador.funcion.FuncionControladorListar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorEliminar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorListar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorVista;
import com.unimagdalena.controlador.sala.SalaControladorListar; // Import necesario
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.IconoNombre;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

public class VistaPeliculaAdm extends SubScene {

    private final Stage laVentanaPrincipal;
    private final VBox organizadorVertical;
    private final TableView<PeliculaDto> miTabla;
    private final StackPane miFormulario;
    
    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_DERECHA = "-fx-alignment: CENTER-RIGHT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_DERECHA;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_DERECHA;

    private HBox miCajaHorizontal;
    private final ObservableList<PeliculaDto> datosTabla = FXCollections.observableArrayList();
    
    private StringProperty titulo;

    public VistaPeliculaAdm(Stage ventanaPadre, BorderPane princ, Pane pane, double ancho, double alto) {
        super(new StackPane(), ancho, alto);

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        miFormulario = (StackPane) getRoot();
        miFormulario.setBackground(fondo);
        miFormulario.setAlignment(Pos.CENTER);

        laVentanaPrincipal = ventanaPadre;
        panelPrincipal = princ;
        panelCuerpo = pane;

        miTabla = new TableView<>();
        organizadorVertical = new VBox();

        configurarOrganizadorVertical();
        crearMarco();
        crearTitulo();
        configurarColumnas();
        configurarTabla();
        mostrarIconosAdministrar();
        
        miFormulario.getChildren().add(organizadorVertical);
    }

    public StackPane getMiFormulario() {
        return miFormulario;
    }

    private void configurarOrganizadorVertical() {
        organizadorVertical.setSpacing(20);
        organizadorVertical.setAlignment(Pos.TOP_CENTER);
        organizadorVertical.prefWidthProperty().bind(laVentanaPrincipal.widthProperty());
        organizadorVertical.prefHeightProperty().bind(laVentanaPrincipal.heightProperty());
    }

    private void crearMarco() {
        double alto = Configuracion.MARCO_ALTO_PORCENTAJE;
        double ancho = Configuracion.MARCO_ANCHO_PORCENTAJE;
        Rectangle miMarco = Marco.pintar(laVentanaPrincipal, alto, ancho,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        miFormulario.getChildren().add(miMarco);
    }

    private void crearTitulo() {
        organizadorVertical.getChildren().removeIf(node -> 
            node instanceof Label && ((Label) node).getText().contains("Administrar Peliculas"));
        
        Region bloqueSeparador = new Region();
        bloqueSeparador.prefHeightProperty().bind(laVentanaPrincipal.heightProperty().multiply(0.05));
        
        int cantidadPeliculas = PeliculaControladorListar.cantidadPeliculas();
        titulo = new SimpleStringProperty("Administrar Peliculas (" + cantidadPeliculas + ")");
        
        Label lblTitulo = new Label();
        lblTitulo.textProperty().bind(titulo);
        lblTitulo.setTextFill(Color.web("#E82E68"));
        lblTitulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));
        
        organizadorVertical.getChildren().add(0, bloqueSeparador);
        organizadorVertical.getChildren().add(1, lblTitulo);
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
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.12));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<PeliculaDto, String> crearColumnaGenero() {
        TableColumn<PeliculaDto, String> columna = new TableColumn<>("Genero");

        columna.setCellValueFactory(cellData -> {
            String nombreGenero = cellData.getValue().getIdGeneroPelicula().getNombreGenero();
            return new SimpleStringProperty(nombreGenero);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.12));
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
    
    // --- NUEVAS COLUMNAS ---
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
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPrivadoPelicula"));
        columna.setCellFactory(column -> new TableCell<PeliculaDto, String>() {
            @Override
            protected void updateItem(String nombreImagen, boolean bandera) {
                super.updateItem(nombreImagen, bandera);
                if (bandera || nombreImagen == null) {
                    setGraphic(null);
                } else {
                    setGraphic(Icono.obtenerFotosExternas(nombreImagen, 40));
                }
            }
        });
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

    private void configurarTabla() {
        organizadorVertical.getChildren().remove(miTabla);
        
        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        miTabla.maxWidthProperty().bind(laVentanaPrincipal.widthProperty().multiply(0.75)); 
        miTabla.maxHeightProperty().bind(laVentanaPrincipal.heightProperty().multiply(0.5));
        miTabla.setPlaceholder(new Text("No hay películas registradas"));

        List<PeliculaDto> arrPeliculas = PeliculaControladorListar.arregloPeliculas();
        datosTabla.setAll(arrPeliculas);
        miTabla.setItems(datosTabla);

        organizadorVertical.getChildren().add(miTabla);
        VBox.setVgrow(miTabla, Priority.ALWAYS);
    }
    
    private void mostrarIconosAdministrar() {
        int anchoBoton = 40;
        int tamanioIcono = 16;

        Button btnEliminar = new Button();
        btnEliminar.setPrefWidth(anchoBoton);
        btnEliminar.setCursor(Cursor.HAND);
        btnEliminar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_BORRAR, tamanioIcono));

        btnEliminar.setOnAction((e) -> {
            if (miTabla.getSelectionModel().getSelectedItem() == null) {
                Mensaje.mostrar(Alert.AlertType.WARNING, null, "Advertencia", "Debes seleccionar una pelicula");
            } else {
                PeliculaDto objpeli = miTabla.getSelectionModel().getSelectedItem();
                
                // --- VALIDACIÓN ---
                int id = objpeli.getIdPelicula();
                long cantFunciones = FuncionControladorListar.arregloFunciones().stream()
                        .filter(f -> f.getIdPeliculaFuncion().getIdPelicula() == id).count();
                
                long cantClientes = ClienteControladorListar.obtenerTodos().stream()
                        .filter(c -> c.getIdPeliculaCliente() != null && c.getIdPeliculaCliente().getIdPelicula() == id).count();
                
                // Validación Nueva: Cantidad de Salas
                long cantSalas = SalaControladorListar.arregloSalas().stream()
                        .filter(s -> s.getIdPeliculaSala() != null && s.getIdPeliculaSala().getIdPelicula() == id)
                        .count();

                if (cantFunciones > 0 || cantClientes > 0 || cantSalas > 0) {
                    String errorMsg = "No se puede eliminar la película.\n";
                    if(cantFunciones > 0) errorMsg += "- Tiene " + cantFunciones + " funciones asociadas.\n";
                    if(cantClientes > 0) errorMsg += "- Hay " + cantClientes + " clientes viéndola.\n";
                    if(cantSalas > 0) errorMsg += "- Se está reproduciendo en " + cantSalas + " sala(s)."; // Mensaje nuevo
                    
                    Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error al eliminar", errorMsg);
                    return;
                }
                // ------------------

                String texto1 = "¿Seguro que quieres borrar la película?\n";
                String texto2 = "\nCodigo: " + objpeli.getIdPelicula();
                String texto3 = "\nNombre: " + objpeli.getNombrePelicula();
                String texto4 = "\nEsto es irreversible!!!";

                Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
                msg.setTitle("Advertencia Borrar");
                msg.setHeaderText(null);
                msg.setContentText(texto1 + texto2 + texto3 + texto4);
                msg.initOwner(null);
                if (msg.showAndWait().get() == ButtonType.OK) {
                    int posicion = miTabla.getSelectionModel().getSelectedIndex();
                    if (PeliculaControladorEliminar.borrar(posicion)) {
                        int cantidadPeliculas = PeliculaControladorListar.cantidadPeliculas();
                        titulo.set("Administrar Peliculas - (" + cantidadPeliculas + ")");

                        ObservableList<PeliculaDto> listaActualizada = (ObservableList<PeliculaDto>) PeliculaControladorListar.arregloPeliculas();
                        datosTabla.setAll(listaActualizada);
                        miTabla.refresh();

                        Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Exito", "Pelicula eliminada");
                    } else {
                        Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo borrar la pelicula");
                    }
                } else {
                    miTabla.getSelectionModel().clearSelection();
                }
            }
        });

        Button btnEditar = new Button();
        btnEditar.setPrefWidth(anchoBoton);
        btnEditar.setCursor(Cursor.HAND);
        btnEditar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_EDITAR, tamanioIcono));

        btnEditar.setOnAction((ActionEvent t) -> {
            if (miTabla.getSelectionModel().getSelectedItem() == null) {
                Mensaje.mostrar(Alert.AlertType.WARNING, null, "Advertencia", "No ha seleccionado una pelicula para editar");
            } else {
                PeliculaDto objpeli = miTabla.getSelectionModel().getSelectedItem();
                int posicion = miTabla.getSelectionModel().getSelectedIndex();

                panelCuerpo = PeliculaControladorVista.editar(
                        laVentanaPrincipal, panelPrincipal, panelCuerpo,
                        Configuracion.ANCHO_APP, (Configuracion.ALTO_APP - Configuracion.ALTO_CABECERA),
                        objpeli, posicion);
                panelPrincipal.setCenter(null);
                panelPrincipal.setCenter(panelCuerpo);
            }
        });

        Button btnCancelar = new Button();
        btnCancelar.setPrefWidth(anchoBoton);
        btnCancelar.setCursor(Cursor.HAND);
        btnCancelar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_CANCELAR, tamanioIcono));

        btnCancelar.setOnAction((e) -> {
            miTabla.getSelectionModel().clearSelection();
        });
        miCajaHorizontal = new HBox(4);
        miCajaHorizontal.setAlignment(Pos.CENTER);
        miCajaHorizontal.getChildren().addAll(btnEliminar, btnEditar, btnCancelar);

        organizadorVertical.getChildren().add(miCajaHorizontal);
    }
}