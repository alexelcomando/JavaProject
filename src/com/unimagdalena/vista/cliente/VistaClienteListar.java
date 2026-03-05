package com.unimagdalena.vista.cliente;

import com.unimagdalena.controlador.cliente.ClienteControladorListar;
import com.unimagdalena.controlador.reserva.ReservaControladorListar; // Importado
import com.unimagdalena.controlador.ticket.TicketControladorListar; // Importado
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Marco;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty; // Importado
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class VistaClienteListar extends StackPane {
    private TableView<ClienteDto> tabla;
    // Estilo para centrar las nuevas columnas
    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";

    public VistaClienteListar(Stage stage, double w, double h) {
        setAlignment(Pos.CENTER);
        Rectangle bg = Marco.pintar(stage, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE, Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setMaxSize(w * 0.8, h * 0.7);

        Text titulo = new Text("Listado de Clientes (" + ClienteControladorListar.contar() + ")");
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        titulo.setFill(Color.WHITE);

        tabla = new TableView<>();
        configurarTabla();
        
        layout.getChildren().addAll(titulo, tabla);
        getChildren().addAll(bg, layout);
    }

    // --- MÉTODOS PARA LAS NUEVAS COLUMNAS ---
    private TableColumn<ClienteDto, Integer> crearColumnaCantReservas() {
        TableColumn<ClienteDto, Integer> columna = new TableColumn<>("Cant. Reservas");
        columna.setCellValueFactory(cellData -> {
            int idCliente = cellData.getValue().getIdCliente();
            long cantidad = ReservaControladorListar.arregloReservas().stream()
                    .filter(r -> r.getIdClienteReserva() != null && r.getIdClienteReserva().getIdCliente() == idCliente)
                    .count();
            return new SimpleObjectProperty<>((int) cantidad);
        });
        columna.prefWidthProperty().bind(tabla.widthProperty().multiply(0.08));
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
        columna.prefWidthProperty().bind(tabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }
    // ----------------------------------------

    private void configurarTabla() {
        TableColumn<ClienteDto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colId.prefWidthProperty().bind(tabla.widthProperty().multiply(0.05));
        
        TableColumn<ClienteDto, String> colNom = new TableColumn<>("Nombre");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colNom.prefWidthProperty().bind(tabla.widthProperty().multiply(0.20)); // Ajustado ancho

        TableColumn<ClienteDto, String> colTipoDoc = new TableColumn<>("Tipo Doc.");
        colTipoDoc.setCellValueFactory(new PropertyValueFactory<>("tipoDocumentoCliente"));
        colTipoDoc.prefWidthProperty().bind(tabla.widthProperty().multiply(0.10)); // Ajustado ancho
        
        TableColumn<ClienteDto, LocalDate> colFechaNac = new TableColumn<>("F. Nacimiento");
        colFechaNac.setCellValueFactory(new PropertyValueFactory<>("fechaDeNacimientoCliente"));
        colFechaNac.prefWidthProperty().bind(tabla.widthProperty().multiply(0.12)); // Ajustado ancho

        TableColumn<ClienteDto, String> colSexo = new TableColumn<>("Sexo");
        colSexo.setCellValueFactory(cell -> new SimpleStringProperty(
            cell.getValue().getSexoCliente() ? "M" : "F"
        ));
        colSexo.prefWidthProperty().bind(tabla.widthProperty().multiply(0.05));
        colSexo.setStyle(ESTILO_CENTRAR);

        TableColumn<ClienteDto, String> colVIP = new TableColumn<>("VIP");
        colVIP.setCellValueFactory(new PropertyValueFactory<>("esAccesoVipCliente"));
        colVIP.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String vip, boolean empty) {
                super.updateItem(vip, empty);
                if (empty || vip == null) {
                    setText(null);
                } else {
                    setText(vip);
                    setStyle("Si".equals(vip) ? "-fx-text-fill: green; -fx-alignment: CENTER;" : "-fx-text-fill: red; -fx-alignment: CENTER;");
                }
            }
        });
        colVIP.prefWidthProperty().bind(tabla.widthProperty().multiply(0.05));
        
        TableColumn<ClienteDto, String> columna = new TableColumn<>("Pelicula");
        columna.setCellValueFactory(cellData -> {
            if(cellData.getValue().getIdPeliculaCliente() != null)
                return new SimpleStringProperty(cellData.getValue().getIdPeliculaCliente().getNombrePelicula());
            return new SimpleStringProperty("N/A");
        });
        columna.prefWidthProperty().bind(tabla.widthProperty().multiply(0.15));
     
        TableColumn<ClienteDto, String> colNomImgPub = new TableColumn<>("Imagen");
        colNomImgPub.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPublicoCliente"));
        colNomImgPub.prefWidthProperty().bind(tabla.widthProperty().multiply(0.15));
        colNomImgPub.setStyle("-fx-alignment: CENTER-LEFT;");

        // Agregando las columnas existentes + las NUEVAS
        tabla.getColumns().addAll(List.of(
                colId, 
                colNom, 
                colTipoDoc, 
                colFechaNac, 
                colSexo, 
                colVIP, 
                crearColumnaCantReservas(), // Nueva
                crearColumnaCantTickets(),  // Nueva
                columna, // Pelicula
                colNomImgPub
        ));
        
        tabla.setItems(FXCollections.observableArrayList(ClienteControladorListar.obtenerTodos()));
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        VBox.setVgrow(tabla, Priority.ALWAYS);
    }
}