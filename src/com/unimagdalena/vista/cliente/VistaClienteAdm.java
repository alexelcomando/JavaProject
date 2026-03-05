package com.unimagdalena.vista.cliente;

import com.unimagdalena.controlador.cliente.ClienteControladorEliminar;
import com.unimagdalena.controlador.cliente.ClienteControladorListar;
import com.unimagdalena.controlador.cliente.ClienteControladorVista;
import com.unimagdalena.controlador.reserva.ReservaControladorListar; // Importado
import com.unimagdalena.controlador.ticket.TicketControladorListar; // Importado
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Contenedor;
import com.unimagdalena.helpers.constante.IconoNombre;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty; // Importado
import javafx.beans.property.SimpleStringProperty;
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

public class VistaClienteAdm extends SubScene {

    private final Stage laVentanaPrincipal;
    private final VBox organizadorVertical;
    private final TableView<ClienteDto> miTabla;
    private final StackPane miFormulario;
    
    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_CENTRAR;

    private Text miTitulo;
    private HBox miCajaHorizontal;
    private final ObservableList<ClienteDto> datosTabla = FXCollections.observableArrayList();
    
    public VistaClienteAdm(Stage ventanaPadre, BorderPane princ, Pane pane, double ancho, double alto) {
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

    private void configurarOrganizadorVertical() {
        organizadorVertical.setAlignment(Pos.TOP_CENTER);
        organizadorVertical.prefWidthProperty().bind(laVentanaPrincipal.widthProperty());
        organizadorVertical.prefHeightProperty().bind(laVentanaPrincipal.heightProperty());
    }

    private void crearMarco() {
        double ancho = Configuracion.MARCO_ANCHO_PORCENTAJE;
        double alto = Configuracion.MARCO_ALTO_PORCENTAJE;
        Rectangle miMarco = Marco.pintar(laVentanaPrincipal, alto, ancho,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        miFormulario.getChildren().add(miMarco);
    }

    private void crearTitulo() {
        Region bloqueSeparador = new Region();
        bloqueSeparador.prefHeightProperty().bind(laVentanaPrincipal.heightProperty().multiply(0.05));
        organizadorVertical.getChildren().add(bloqueSeparador);
        
        int cantidadClientes = ClienteControladorListar.contar();
        miTitulo = new Text("Administrar Clientes (" + cantidadClientes + ")");
        miTitulo.setFill(Color.web("#E82E68"));
        miTitulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));
        organizadorVertical.getChildren().add(miTitulo);
    }
    
    private TableColumn<ClienteDto, String> crearColumnaNombre() {
        TableColumn<ClienteDto, String> columna = new TableColumn<>("Nombre");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ClienteDto, String> crearColumnaDocumento() {
        TableColumn<ClienteDto, String> columna = new TableColumn<>("Documento");
        columna.setCellValueFactory(new PropertyValueFactory<>("tipoDocumentoCliente"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.12));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }
    
    private TableColumn<ClienteDto, String> crearColumnaSexo() {
        TableColumn<ClienteDto, String> columna = new TableColumn<>("Sexo");
        columna.setCellValueFactory(cell -> new SimpleStringProperty(
            cell.getValue().getSexoCliente() ? "M" : "F"
        ));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.05));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }
    
    private TableColumn<ClienteDto, String> crearColumnaVIP() {
        TableColumn<ClienteDto, String> columna = new TableColumn<>("VIP");
        columna.setCellValueFactory(new PropertyValueFactory<>("esAccesoVipCliente"));

        columna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String vip, boolean empty) {
                super.updateItem(vip, empty);
                if (empty || vip == null) {
                    setText(null);
                    setStyle(ESTILO_CENTRAR);
                } else {
                    setText(vip);
                    setStyle("Si".equals(vip) ? ESTILO_ELVERDE : ESTILO_ELROJO);
                }
            }
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        return columna;
    }
    
    private TableColumn<ClienteDto, String> crearColumnaPelicula() {
        TableColumn<ClienteDto, String> columna = new TableColumn<>("Pelicula");

        columna.setCellValueFactory(cellData -> {
            if (cellData.getValue().getIdPeliculaCliente() != null)
                return new SimpleStringProperty(cellData.getValue().getIdPeliculaCliente().getNombrePelicula());
            return new SimpleStringProperty("N/A");
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    // --- NUEVAS COLUMNAS ---
    private TableColumn<ClienteDto, Integer> crearColumnaCantReservas() {
        TableColumn<ClienteDto, Integer> columna = new TableColumn<>("Cant. Reservas");
        columna.setCellValueFactory(cellData -> {
            int idCliente = cellData.getValue().getIdCliente();
            long cantidad = ReservaControladorListar.arregloReservas().stream()
                    .filter(r -> r.getIdClienteReserva() != null && r.getIdClienteReserva().getIdCliente() == idCliente)
                    .count();
            return new SimpleObjectProperty<>((int) cantidad);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ClienteDto, Integer> crearColumnaCantTickets() {
        TableColumn<ClienteDto, Integer> columna = new TableColumn<>("Cant. Tickets");
        columna.setCellValueFactory(cellData -> {
            int idCliente = cellData.getValue().getIdCliente();
            long cantidad = TicketControladorListar.arregloTickets().stream()
                    .filter(t -> t.getIdClienteTicket() != null && t.getIdClienteTicket().getIdCliente() == idCliente)
                    .count();
            return new SimpleObjectProperty<>((int) cantidad);
        });
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }
    // ----------------------

    private TableColumn<ClienteDto, String> crearColumnaImagen() {
        TableColumn<ClienteDto, String> columna = new TableColumn<>("Foto");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPrivadoCliente"));
        
        columna.setCellFactory(col -> new TableCell<ClienteDto, String>() {
            @Override
            protected void updateItem(String nombreImagen, boolean empty) {
                super.updateItem(nombreImagen, empty);
                if (empty || nombreImagen == null || nombreImagen.isEmpty()) {
                    setGraphic(null);
                    setAlignment(Pos.CENTER);
                } else {
                    setGraphic(Icono.obtenerFotosExternas(nombreImagen, 40));
                    setAlignment(Pos.CENTER);
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
                        crearColumnaNombre(),
                        crearColumnaDocumento(),
                        crearColumnaSexo(),
                        crearColumnaVIP(),
                        crearColumnaCantReservas(), // Nueva
                        crearColumnaCantTickets(),  // Nueva
                        crearColumnaPelicula(),
                        crearColumnaImagen()
                ));
    }

    private void configurarTabla() {
        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        miTabla.maxWidthProperty().bind(laVentanaPrincipal.widthProperty().multiply(0.8));
        miTabla.maxHeightProperty().bind(laVentanaPrincipal.heightProperty().multiply(0.5));
        miTabla.setPlaceholder(new Text("No hay clientes registrados"));

        List<ClienteDto> arrClientes = ClienteControladorListar.obtenerTodos();
        datosTabla.setAll(arrClientes);
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
                Mensaje.mostrar(Alert.AlertType.WARNING, null, "Advertencia", "Debes seleccionar un cliente");
            } else {
                ClienteDto objCliente = miTabla.getSelectionModel().getSelectedItem();

                // --- VALIDACIÓN DE ELIMINACIÓN ---
                int idCliente = objCliente.getIdCliente();
                long cantReservas = ReservaControladorListar.arregloReservas().stream()
                        .filter(r -> r.getIdClienteReserva() != null && r.getIdClienteReserva().getIdCliente() == idCliente)
                        .count();
                long cantTickets = TicketControladorListar.arregloTickets().stream()
                        .filter(t -> t.getIdClienteTicket() != null && t.getIdClienteTicket().getIdCliente() == idCliente)
                        .count();

                if (cantReservas > 0 || cantTickets > 0) {
                    String mensaje = "No se puede eliminar el cliente.\n";
                    if(cantReservas > 0) mensaje += "- Tiene " + cantReservas + " reserva(s) activa(s).\n";
                    if(cantTickets > 0) mensaje += "- Ha comprado " + cantTickets + " ticket(s).";
                    
                    Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error al eliminar", mensaje);
                    return;
                }
                // ---------------------------------

                String texto1, texto2, texto3;
                texto1 = "¿Seguro que quieres borrar el cliente?\n";
                texto2 = "\nID: " + objCliente.getIdCliente();
                texto3 = "\nNombre: " + objCliente.getNombreCliente();

                Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
                msg.setTitle("Advertencia Borrar");
                msg.setHeaderText(null);
                msg.setContentText(texto1 + texto2 + texto3);
                msg.initOwner(null);
                if (msg.showAndWait().get() == ButtonType.OK) {
                    int posicion = miTabla.getSelectionModel().getSelectedIndex();
                    if (ClienteControladorEliminar.borrar(posicion)) {
                        int cantidadClientes = ClienteControladorListar.contar();
                        miTitulo.setText("Administrar Clientes (" + cantidadClientes + ")");

                        ObservableList<ClienteDto> listaActualizada = FXCollections.observableArrayList(ClienteControladorListar.obtenerTodos());
                        datosTabla.setAll(listaActualizada);
                        miTabla.refresh();

                        Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Exito", "Cliente eliminado");
                    } else {
                        Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo borrar el cliente");
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
                Mensaje.mostrar(Alert.AlertType.WARNING, null, "Advertencia", "No ha seleccionado un cliente para editar");
            } else {
                ClienteDto objCliente = miTabla.getSelectionModel().getSelectedItem();
                int posicion = miTabla.getSelectionModel().getSelectedIndex();

                panelCuerpo = ClienteControladorVista.abrirEditar(
                        laVentanaPrincipal, panelPrincipal, panelCuerpo,
                        Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(),
                        objCliente, posicion);
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