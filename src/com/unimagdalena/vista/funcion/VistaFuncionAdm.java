package com.unimagdalena.vista.funcion;

import com.unimagdalena.controlador.funcion.FuncionControladorEliminar;
import com.unimagdalena.controlador.funcion.FuncionControladorListar;
import com.unimagdalena.controlador.funcion.FuncionControladorVista;
import com.unimagdalena.controlador.reserva.ReservaControladorListar; // Import necesario
import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.IconoNombre;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty; // Import necesario
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

public class VistaFuncionAdm extends SubScene {

    private final Stage laVentanaPrincipal;
    private final VBox organizadorVertical;
    private final TableView<FuncionDto> miTabla;
    private final StackPane miFormulario;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_DERECHA = "-fx-alignment: CENTER-RIGHT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_CENTRAR;

    private Text miTitulo;
    private HBox miCajaHorizontal;
    private final ObservableList<FuncionDto> datosTabla = FXCollections.observableArrayList();

    private StringProperty titulo;

    public VistaFuncionAdm(Stage ventanaPadre, BorderPane princ, Pane pane, double ancho, double alto) {
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

        // Configurar primero el organizador
        configurarOrganizadorVertical();

        // Luego crear y agregar todos los elementos en orden
        crearMarco();
        crearTitulo();
        configurarColumnas();
        configurarTabla();
        mostrarIconosAdministrar();

        // Finalmente agregar el organizador al formulario
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
        // Limpiar elementos existentes del título si los hay
        organizadorVertical.getChildren().removeIf(node
                -> node instanceof Label && ((Label) node).getText().contains("Administrar Funciones"));

        Region bloqueSeparador = new Region();
        bloqueSeparador.prefHeightProperty().bind(laVentanaPrincipal.heightProperty().multiply(0.05));

        int cantidadFunciones = FuncionControladorListar.cantidadFunciones();
        titulo = new SimpleStringProperty("Administrar Funciones (" + cantidadFunciones + ")");

        Label lblTitulo = new Label();
        lblTitulo.textProperty().bind(titulo);
        lblTitulo.setTextFill(Color.web("#E82E68"));
        lblTitulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));

        // Agregar al inicio del organizador vertical
        organizadorVertical.getChildren().add(0, bloqueSeparador);
        organizadorVertical.getChildren().add(1, lblTitulo);
    }

    private TableColumn<FuncionDto, Integer> crearColumnaCodigo() {
        TableColumn<FuncionDto, Integer> columna = new TableColumn<>("Codigo");
        columna.setCellValueFactory(new PropertyValueFactory<>("idFuncion"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<FuncionDto, String> crearColumnaNombre() {
        TableColumn<FuncionDto, String> columna = new TableColumn<>("Nombre");
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
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPrivadoFuncion"));
        columna.setCellFactory(column -> new TableCell<FuncionDto, String>() {
            @Override
            protected void updateItem(String nombreImagen, boolean bandera) {
                super.updateItem(nombreImagen, bandera);
                if (bandera || nombreImagen == null) {
                    setGraphic(null);
                } else {
                    setGraphic(Icono.obtenerFotosExternas(nombreImagen, 50));
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
                        crearColumnaPelicula(),
                        crearColumnaSala(),
                        crearColumnaFecha(),
                        crearColumnaHora(),
                        crearColumnaClasificacionEdad(),
                        crearColumnaCantReservas(), // <- Agregada aquí
                        crearColumnaImagen()
                ));
    }

    private void configurarTabla() {
        // Remover tabla existente si la hay
        organizadorVertical.getChildren().remove(miTabla);

        // Configurar la tabla
        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        miTabla.maxWidthProperty().bind(laVentanaPrincipal.widthProperty().multiply(0.75));
        miTabla.maxHeightProperty().bind(laVentanaPrincipal.heightProperty().multiply(0.5));
        miTabla.setPlaceholder(new Text("No hay funciones registradas"));

        // Cargar datos en la tabla
        List<FuncionDto> arrFunciones = FuncionControladorListar.arregloFunciones();
        datosTabla.setAll(arrFunciones);
        miTabla.setItems(datosTabla);

        // Agregar la tabla al organizador vertical (después del título)
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
                Mensaje.mostrar(Alert.AlertType.WARNING, null, "Advertencia", "Debes seleccionar una funcion");
            } else {
                FuncionDto objfuncion = miTabla.getSelectionModel().getSelectedItem();
                
                // --- VALIDACIÓN DE RESERVAS ---
                int id = objfuncion.getIdFuncion();
                long cantReservas = ReservaControladorListar.arregloReservas().stream()
                        .filter(r -> r.getIdFuncionReserva() != null && r.getIdFuncionReserva().getIdFuncion() == id)
                        .count();

                if (cantReservas > 0) {
                    Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error al eliminar",
                            "No se puede eliminar la función.\nTiene " + cantReservas + " reserva(s) asociada(s).");
                    return;
                }
                // --------------------------------

                String texto1, texto2, texto3, texto4;

                texto1 = "¿Seguro que quieres borrar la funcion?\n";
                texto2 = "\nCodigo: " + objfuncion.getIdFuncion();
                texto3 = "\nNombre: " + objfuncion.getNombreFuncion();
                texto4 = "\nEsto es irreversible!!!";

                Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
                msg.setTitle("Advertencia Borrar");
                msg.setHeaderText(null);
                msg.setContentText(texto1 + texto2 + texto3 + texto4);
                msg.initOwner(null);
                if (msg.showAndWait().get() == ButtonType.OK) {
                    int posicion = miTabla.getSelectionModel().getSelectedIndex();
                    if (FuncionControladorEliminar.borrar(posicion)) {

                        int cantidadFunciones = FuncionControladorListar.cantidadFunciones();
                        titulo.set("Administrar Funciones - (" + cantidadFunciones + ")");

                        // Actualiza la tabla
                        ObservableList<FuncionDto> listaActualizada = (ObservableList<FuncionDto>) FuncionControladorListar.arregloFunciones();
                        datosTabla.setAll(listaActualizada);
                        miTabla.refresh();

                        Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Exito", "Funcion eliminada");
                    } else {
                        Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo borrar la funcion");
                    }
                } else {
                    miTabla.getSelectionModel().clearSelection();
                }
            }
        });
        // *********************************************************************

        // Botón actualizar
        Button btnEditar = new Button();
        btnEditar.setPrefWidth(anchoBoton);
        btnEditar.setCursor(Cursor.HAND);
        btnEditar.setGraphic(Icono.obtenerIcono(IconoNombre.ICONO_EDITAR, tamanioIcono));

        btnEditar.setOnAction((ActionEvent t) -> {
            if (miTabla.getSelectionModel().getSelectedItem() == null) {
                Mensaje.mostrar(Alert.AlertType.WARNING, null, "Advertencia", "No ha seleccionado una funcion para editar");
            } else {
                FuncionDto objfuncion = miTabla.getSelectionModel().getSelectedItem();
                int posicion = miTabla.getSelectionModel().getSelectedIndex();

                panelCuerpo = FuncionControladorVista.editar(
                        laVentanaPrincipal, panelPrincipal, panelCuerpo,
                        Configuracion.ANCHO_APP, (Configuracion.ALTO_APP - Configuracion.ALTO_CABECERA),
                        objfuncion, posicion);
                panelPrincipal.setCenter(null);
                panelPrincipal.setCenter(panelCuerpo);
            }
        });
        // *********************************************************************

        // Desmarcar la categoría seleccionada
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