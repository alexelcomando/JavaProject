package com.unimagdalena.vista.reserva;

import com.unimagdalena.controlador.reserva.ReservaControladorEliminar;
import com.unimagdalena.controlador.reserva.ReservaControladorListar;
import com.unimagdalena.controlador.reserva.ReservaControladorVista;
import com.unimagdalena.dto.ReservaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Contenedor;
import com.unimagdalena.helpers.constante.IconoNombre;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.time.LocalDate;
import java.util.List;
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

public class VistaReservaAdm extends SubScene {

    private final Stage laVentanaPrincipal;
    private final VBox organizadorVertical;
    private final TableView<ReservaDto> miTabla;
    private final StackPane miFormulario;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_CENTRAR;

    private HBox miCajaHorizontal;
    private final ObservableList<ReservaDto> datosTabla = FXCollections.observableArrayList();

    private StringProperty titulo;

    public VistaReservaAdm(Stage ventanaPadre, BorderPane princ, Pane pane, double ancho, double alto) {
        super(new StackPane(), ancho, alto);

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        miFormulario = (StackPane) getRoot();
        miFormulario.setBackground(fondo);
        miFormulario.setAlignment(Pos.CENTER);

        laVentanaPrincipal = ventanaPadre;
        panelPrincipal = princ;
        panelCuerpo = pane;

        miTabla = new TableView<>();
        organizadorVertical = new VBox(20);

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
        organizadorVertical.getChildren().removeIf(node
                -> node instanceof Label && ((Label) node).getText().contains("Administrar Reservas"));

        Region bloqueSeparador = new Region();
        bloqueSeparador.prefHeightProperty().bind(laVentanaPrincipal.heightProperty().multiply(0.05));
        organizadorVertical.getChildren().add(0, bloqueSeparador);

        int cantidadReservas = ReservaControladorListar.cantidadReservas();
        titulo = new SimpleStringProperty("Administrar Reservas (" + cantidadReservas + ")");

        Label lblTitulo = new Label();
        lblTitulo.textProperty().bind(titulo);
        lblTitulo.setTextFill(Color.web("#E82E68"));
        lblTitulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));
        organizadorVertical.getChildren().add(1, lblTitulo);
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
            return new SimpleStringProperty(cellData.getValue().getIdClienteReserva().getNombreCliente());
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.18));
        columna.setStyle(ESTILO_IZQUIERDA);
        return columna;
    }

    private TableColumn<ReservaDto, String> crearColumnaFuncion() {
        TableColumn<ReservaDto, String> columna = new TableColumn<>("Función");
        columna.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getIdFuncionReserva().getNombreFuncion());
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
        TableColumn<ReservaDto, String> columna = new TableColumn<>("Pago");
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
                    setStyle(ESTILO_CENTRAR);
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
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPrivadoReserva"));

        columna.setCellFactory(column -> new TableCell<ReservaDto, String>() {
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
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.13));
        columna.setStyle(ESTILO_CENTRAR);
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
                ));
    }

    private void configurarTabla() {
        organizadorVertical.getChildren().remove(miTabla);

        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        miTabla.maxWidthProperty().bind(laVentanaPrincipal.widthProperty().multiply(0.8));
        miTabla.maxHeightProperty().bind(laVentanaPrincipal.heightProperty().multiply(0.5));
        miTabla.setPlaceholder(new Text("No hay reservas registradas"));

        List<ReservaDto> arrReservas = ReservaControladorListar.arregloReservas();
        datosTabla.setAll(arrReservas);
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
                Mensaje.mostrar(Alert.AlertType.WARNING, null, "Advertencia", "Debes seleccionar una reserva");
            } else {
                ReservaDto objReserva = miTabla.getSelectionModel().getSelectedItem();

                String texto1, texto2, texto3, texto4;
                texto1 = "¿Seguro que quieres borrar la reserva?\n";
                texto2 = "\nCodigo: " + objReserva.getIdReserva();
                texto3 = "\nNombre: " + objReserva.getNombreReserva();
                texto4 = "\nEsto es irreversible!!!";

                Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
                msg.setTitle("Advertencia Borrar");
                msg.setHeaderText(null);
                msg.setContentText(texto1 + texto2 + texto3 + texto4);
                msg.initOwner(null);
                if (msg.showAndWait().get() == ButtonType.OK) {
                    int posicion = miTabla.getSelectionModel().getSelectedIndex();
                    if (ReservaControladorEliminar.borrar(posicion)) {

                        int cantidadReservas = ReservaControladorListar.cantidadReservas();
                        titulo.set("Administrar Reservas (" + cantidadReservas + ")");

                        List<ReservaDto> listaActualizada = ReservaControladorListar.arregloReservas();
                        datosTabla.setAll(listaActualizada);
                        miTabla.refresh();

                        Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Exito", "Reserva eliminada");
                    } else {
                        Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo borrar la reserva");
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
                Mensaje.mostrar(Alert.AlertType.WARNING, null, "Advertencia", "No ha seleccionado una reserva para editar");
            } else {
                ReservaDto objReserva = miTabla.getSelectionModel().getSelectedItem();
                int posicion = miTabla.getSelectionModel().getSelectedIndex();

                panelCuerpo = ReservaControladorVista.editar(
                        laVentanaPrincipal, panelPrincipal, panelCuerpo,
                        Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(),
                        objReserva, posicion);

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
        miCajaHorizontal = new HBox(6);
        miCajaHorizontal.setAlignment(Pos.CENTER);
        miCajaHorizontal.getChildren().addAll(btnEliminar, btnEditar, btnCancelar);

        organizadorVertical.getChildren().add(miCajaHorizontal);
    }
}