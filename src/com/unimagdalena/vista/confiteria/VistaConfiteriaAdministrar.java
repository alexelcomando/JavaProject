package com.unimagdalena.vista.confiteria;

import com.unimagdalena.controlador.confiteria.ConfiteriaControladorEliminar;
import com.unimagdalena.controlador.confiteria.ConfiteriaControladorListar;
import com.unimagdalena.controlador.confiteria.ConfiteriaControladorEditar;
import com.unimagdalena.dto.ConfiteriaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class VistaConfiteriaAdministrar extends StackPane {

    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final VBox miCajaVertical;
    private final TableView<ConfiteriaDto> miTabla;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_CENTRAR;

    private Text miTitulo;
    private HBox miCajaHorizontal;

    private final ObservableList<ConfiteriaDto> datosTabla = FXCollections.observableArrayList();

    public VistaConfiteriaAdministrar(Stage esce, double ancho, double alto) {
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
        int canti = ConfiteriaControladorListar.cantidadConfiteria();
        miTitulo = new Text("Listado de Confitería - (" + canti + ") ");
        miTitulo.setFill(Color.web("#54e8b7"));
        miTitulo.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        miCajaVertical.getChildren().addAll(separadorTitulo, miTitulo);
    }

    // --- CREACIÓN DE COLUMNAS ---
    private TableColumn<ConfiteriaDto, Integer> crearColumnaCodigo() {
        TableColumn<ConfiteriaDto, Integer> columna = new TableColumn<>("Código");
        columna.setCellValueFactory(new PropertyValueFactory<>("idConfiteria"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ConfiteriaDto, String> crearColumnaNombre() {
        TableColumn<ConfiteriaDto, String> columna = new TableColumn<>("Nombre");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreProductoConfiteria"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ConfiteriaDto, Double> crearColumnaPrecio() {
        TableColumn<ConfiteriaDto, Double> columna = new TableColumn<>("Precio");
        columna.setCellValueFactory(new PropertyValueFactory<>("precioProductoConfiteria"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        
        // Formatear precio con símbolo de peso
        columna.setCellFactory(col -> new TableCell<ConfiteriaDto, Double>() {
            @Override
            protected void updateItem(Double precio, boolean empty) {
                super.updateItem(precio, empty);
                if (empty || precio == null) {
                    setText(null);
                } else {
                    setText("$" + String.format("%,.2f", precio));
                }
                setStyle(ESTILO_CENTRAR);
            }
        });
        
        return columna;
    }

    private TableColumn<ConfiteriaDto, String> crearColumnaTipo() {
        TableColumn<ConfiteriaDto, String> columna = new TableColumn<>("Tipo");
        columna.setCellValueFactory(new PropertyValueFactory<>("tipoProductoConfiteria"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ConfiteriaDto, String> crearColumnaEstado() {
        TableColumn<ConfiteriaDto, String> columna = new TableColumn<>("Estado");

        columna.setCellValueFactory(obj -> {
            Boolean disponible = obj.getValue().getProductoDisponibleConfiteria();
            String estado = (disponible != null && disponible) ? "Disponible" : "Agotado";
            return new SimpleStringProperty(estado);
        });

        columna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(estado);
                    setStyle(estado.equals("Disponible") ? ESTILO_ELVERDE : ESTILO_ELROJO);
                }
            }
        });

        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        return columna;
    }

    private TableColumn<ConfiteriaDto, String> crearColumnaMetPago() {
        TableColumn<ConfiteriaDto, String> columna = new TableColumn<>("Método Pago");
        columna.setCellValueFactory(new PropertyValueFactory<>("metodoPagoProducto"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ConfiteriaDto, String> crearColumnaFechaCompra() {
        TableColumn<ConfiteriaDto, String> columna = new TableColumn<>("Fecha Compra");
        columna.setCellValueFactory(new PropertyValueFactory<>("fechaCompraProducto"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<ConfiteriaDto, String> crearColumnaImagen() {
        TableColumn<ConfiteriaDto, String> columna = new TableColumn<>("Imagen");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPrivadoConfiteria"));
        columna.setPrefWidth(100);
        columna.setStyle(ESTILO_CENTRAR);

        columna.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String nombreImagenPrivada, boolean empty) {
                super.updateItem(nombreImagenPrivada, empty);

                if (empty || nombreImagenPrivada == null || nombreImagenPrivada.isBlank()) {
                    setGraphic(null);
                } else {
                    Path ruta = Paths.get(Persistencia.RUTA_IMAGENES_EXTERNAS, nombreImagenPrivada);

                    if (Files.exists(ruta)) {
                        Image img = new Image(ruta.toUri().toString(), 50, 50, true, true);
                        imageView.setImage(img);
                    } else {
                        Image img = new Image(
                                getClass().getResourceAsStream(
                                        Persistencia.RUTA_IMAGENES_INTERNAS + Configuracion.ICONO_NO_DISPONIBLE
                                ),
                                50, 50, true, true
                        );
                        imageView.setImage(img);
                    }
                    setGraphic(imageView);
                }
            }
        });
        return columna;
    }

    private void configurarColumnas() {
        miTabla.getColumns().addAll(
                List.of(
                        crearColumnaCodigo(),
                        crearColumnaNombre(),
                        crearColumnaPrecio(),
                        crearColumnaTipo(),
                        crearColumnaEstado(),
                        crearColumnaMetPago(),
                        crearColumnaFechaCompra(),
                        crearColumnaImagen()
                )
        );
    }

    private void crearTabla() {
        configurarColumnas();

        List<ConfiteriaDto> arrConf = ConfiteriaControladorListar.arregloConfiteria();
        datosTabla.setAll(arrConf);
        miTabla.setItems(datosTabla);
        miTabla.setPlaceholder(new Text("No hay productos registrados"));

        miTabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        miTabla.maxWidthProperty().bind(miEscenario.widthProperty().multiply(0.8));
        miTabla.maxHeightProperty().bind(miEscenario.heightProperty().multiply(0.6));

        miEscenario.heightProperty().addListener((o, oldval, newval) -> miTabla.setPrefHeight(newval.doubleValue()));
        VBox.setVgrow(miTabla, Priority.ALWAYS);

        miCajaVertical.getChildren().add(miTabla);
        getChildren().add(miCajaVertical);
    }

private void mostrarIconosAdministrar() {
    int anchoBoton = 40;
    int tamanioIcono = 18;

    // --- ELIMINAR ---
    Button btnEliminar = new Button();
    btnEliminar.setPrefWidth(anchoBoton);
    btnEliminar.setCursor(Cursor.HAND);
    btnEliminar.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_BORRAR, tamanioIcono));
    btnEliminar.setOnAction((e) -> {
        if (miTabla.getSelectionModel().getSelectedItem() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING,
                    miEscenario, "Te veo", "MAL ! agarra algo");
        } else {
            ConfiteriaDto objConfiteria = miTabla.getSelectionModel().getSelectedItem();
            String msg = String.format(
                    "¿Estás seguro mi vale?\nCódigo: %d\nNombre: %s\nSi se fue, se fue!",
                    objConfiteria.getIdConfiteria(),
                    objConfiteria.getNombreProductoConfiteria()
            );

            Alert mensajito = new Alert(Alert.AlertType.CONFIRMATION);
            mensajito.setTitle("Te lo advierto");
            mensajito.setHeaderText(null);
            mensajito.setContentText(msg);
            mensajito.initOwner(null);

            if (mensajito.showAndWait().get() == ButtonType.OK) {
                int codigo = objConfiteria.getIdConfiteria();
                if (ConfiteriaControladorEliminar.borrar(codigo)) {
                    refrescarTabla();
                    Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario, "ÉXITO", "Que buen inglés, lo borré");
                } else {
                    Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Pailas", "No lo pude borrar!");
                }
            }
            miTabla.getSelectionModel().clearSelection();
        }
    });

    // --- ACTUALIZAR ---
    Button btnActualizar = new Button();
    btnActualizar.setPrefWidth(anchoBoton);
    btnActualizar.setCursor(Cursor.HAND);
    btnActualizar.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_EDITAR, tamanioIcono));
    btnActualizar.setOnAction((e) -> {
        ConfiteriaDto seleccionado = miTabla.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Advertencia",
                    "¡Seleccione un producto para actualizar!");
        } else {
            BorderPane panelPrincipal = (BorderPane) miEscenario.getScene().getRoot();
            Pane panelAnterior = this;

            StackPane panelEditar = ConfiteriaControladorEditar.abrirEditar(
                    miEscenario,
                    panelPrincipal,
                    panelAnterior,
                    seleccionado
            );

            panelPrincipal.setCenter(panelEditar);
        }
    });

    // --- CANCELAR ---
    Button btnCancelar = new Button();
    btnCancelar.setPrefWidth(anchoBoton);
    btnCancelar.setCursor(Cursor.HAND);
    btnCancelar.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_CANCELAR, tamanioIcono));
    btnCancelar.setOnAction((e) -> miTabla.getSelectionModel().clearSelection());

    miCajaHorizontal = new HBox(6);
    miCajaHorizontal.setAlignment(Pos.CENTER);
    // Solo 3 botones: Eliminar, Actualizar, Cancelar
    miCajaHorizontal.getChildren().addAll(btnEliminar, btnActualizar, btnCancelar);
    miCajaVertical.getChildren().add(miCajaHorizontal);
}
    
    /**
 * Método público para refrescar la tabla después de actualizar
 */
public void refrescarTabla() {
    // Actualizar el título con la cantidad
    int cant = ConfiteriaControladorListar.cantidadConfiteria();
    miTitulo.setText("Listado de Confitería - (" + cant + ") ");
    
    // Recargar los datos de la tabla
    List<ConfiteriaDto> arrConf = ConfiteriaControladorListar.arregloConfiteria();
    datosTabla.setAll(arrConf);
    miTabla.refresh();
}
}
