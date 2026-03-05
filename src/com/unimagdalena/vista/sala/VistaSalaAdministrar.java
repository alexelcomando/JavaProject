package com.unimagdalena.vista.sala;

import com.unimagdalena.controlador.empleado.EmpleadoControladorListar;
import com.unimagdalena.controlador.funcion.FuncionControladorListar;
import com.unimagdalena.dto.EmpleadoDto;
import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.controlador.empleado.EmpleadoControladorListar;
import com.unimagdalena.dto.EmpleadoDto;
import com.unimagdalena.controlador.sala.SalaControladorEliminar;
import com.unimagdalena.controlador.sala.SalaControladorListar;
import com.unimagdalena.controlador.sala.SalaControladorEditar;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.Fondo;
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

public class VistaSalaAdministrar extends StackPane {

    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final VBox miCajaVertical;
    private final TableView<SalaDto> miTabla;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELAMARILLO = "-fx-text-fill: orange; " + ESTILO_CENTRAR;

    private Text miTitulo;
    private HBox miCajaHorizontal;

    private final ObservableList<SalaDto> datosTabla = FXCollections.observableArrayList();

    public VistaSalaAdministrar(Stage esce, double ancho, double alto) {
        setAlignment(Pos.CENTER);
        miEscenario = esce;
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        miTabla = new TableView<>();
        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        setBackground(fondo);
        miCajaVertical = new VBox(20);
        getChildren().add(miMarco);
        todoResponsive();
        armarTitulo();
        crearTabla();
        mostrarIconosAdministrar();
    }

    private void todoResponsive() {
        miCajaVertical.setAlignment(Pos.TOP_CENTER);
        miCajaVertical.prefWidthProperty().bind(miMarco.widthProperty());
        miCajaVertical.prefHeightProperty().bind(miMarco.heightProperty());
    }

    private boolean salaTieneEmpleadosAsignados(int idSala) {
        List<EmpleadoDto> empleados = EmpleadoControladorListar.arregloEmpleados();

        for (EmpleadoDto empleado : empleados) {
            if (empleado.getSalaAsignadaEmpleado() != null
                    && empleado.getSalaAsignadaEmpleado().getIdSala() == idSala) {
                return true;
            }
        }

        return false;
    }

    private int contarEmpleadosEnSala(int idSala) {
        List<EmpleadoDto> empleados = EmpleadoControladorListar.arregloEmpleados();
        int contador = 0;

        for (EmpleadoDto empleado : empleados) {
            if (empleado.getSalaAsignadaEmpleado() != null
                    && empleado.getSalaAsignadaEmpleado().getIdSala() == idSala) {
                contador++;
            }
        }

        return contador;
    }

    private void armarTitulo() {
        Region separadorTitulo = new Region();
        separadorTitulo.prefHeightProperty().bind(miEscenario.heightProperty().multiply(0.05));
        int canti = SalaControladorListar.cantidadSalas();
        miTitulo = new Text("Administrar Salas - (" + canti + ") ");
        miTitulo.setFill(Color.web("#54e8b7"));
        miTitulo.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        miCajaVertical.getChildren().addAll(separadorTitulo, miTitulo);
    }

    // --- CREACIÓN DE COLUMNAS ---
    private TableColumn<SalaDto, Integer> crearColumnaCodigo() {
        TableColumn<SalaDto, Integer> columna = new TableColumn<>("Código");
        columna.setCellValueFactory(new PropertyValueFactory<>("idSala"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<SalaDto, String> crearColumnaNombre() {
        TableColumn<SalaDto, String> columna = new TableColumn<>("Nombre Sala");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreSala"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.15));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<SalaDto, String> crearColumnaPelicula() {
        TableColumn<SalaDto, String> columna = new TableColumn<>("Película");

        columna.setCellValueFactory(obj -> {
            SalaDto sala = obj.getValue();
            String nombrePelicula = sala.getIdPeliculaSala() != null
                    ? sala.getIdPeliculaSala().getNombrePelicula()
                    : "Sin asignar";
            return new SimpleStringProperty(nombrePelicula);
        });

        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<SalaDto, String> crearColumnaEstado() {
        TableColumn<SalaDto, String> columna = new TableColumn<>("Estado");

        columna.setCellValueFactory(obj -> {
            Boolean disponible = obj.getValue().getEstaDisponibleSala();
            String estado = (disponible != null && disponible) ? "Disponible" : "No disponible";
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

        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.12));
        return columna;
    }

    private TableColumn<SalaDto, Integer> crearColumnaAsientos() {
        TableColumn<SalaDto, Integer> columna = new TableColumn<>("Asientos");
        columna.setCellValueFactory(new PropertyValueFactory<>("numeroAsientosSala"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<SalaDto, String> crearColumnaVIP() {
        TableColumn<SalaDto, String> columna = new TableColumn<>("VIP");

        columna.setCellValueFactory(obj -> {
            Boolean vip = obj.getValue().getServicioVipSala();
            String textoVip = (vip != null && vip) ? "SÍ" : "NO";
            return new SimpleStringProperty(textoVip);
        });

        columna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String vip, boolean empty) {
                super.updateItem(vip, empty);
                if (empty || vip == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(vip);
                    setStyle(vip.equals("SÍ") ? ESTILO_ELAMARILLO : ESTILO_CENTRAR);
                }
            }
        });

        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        return columna;
    }

    private TableColumn<SalaDto, String> crearColumnaEmpleados() {
        TableColumn<SalaDto, String> columna = new TableColumn<>("Empleados");

        columna.setCellValueFactory(obj -> {
            int idSala = obj.getValue().getIdSala();
            int cantidad = contarEmpleadosEnSala(idSala);
            return new SimpleStringProperty(String.valueOf(cantidad));
        });

        columna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String cantidad, boolean empty) {
                super.updateItem(cantidad, empty);
                if (empty || cantidad == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(cantidad);
                    int cant = Integer.parseInt(cantidad);
                    // Colorear según cantidad: 0=rojo, 1-2=naranja, 3+=verde
                    if (cant == 0) {
                        setStyle(ESTILO_ELROJO);
                    } else if (cant <= 2) {
                        setStyle(ESTILO_ELAMARILLO);
                    } else {
                        setStyle(ESTILO_ELVERDE);
                    }
                }
            }
        });

        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.09));
        return columna;
    }

    private TableColumn<SalaDto, String> crearColumnaFunciones() {
        TableColumn<SalaDto, String> columna = new TableColumn<>("Funciones");

        columna.setCellValueFactory(obj -> {
            int idSala = obj.getValue().getIdSala();
            int cantidad = contarFuncionesEnSala(idSala);
            return new SimpleStringProperty(String.valueOf(cantidad));
        });

        columna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String cantidad, boolean empty) {
                super.updateItem(cantidad, empty);
                if (empty || cantidad == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(cantidad);
                    int cant = Integer.parseInt(cantidad);
                    // Colorear según cantidad: 0=rojo, 1-2=naranja, 3+=verde
                    if (cant == 0) {
                        setStyle(ESTILO_ELROJO);
                    } else if (cant <= 2) {
                        setStyle(ESTILO_ELAMARILLO);
                    } else {
                        setStyle(ESTILO_ELVERDE);
                    }
                }
            }
        });

        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.09));
        return columna;
    }

    private TableColumn<SalaDto, String> crearColumnaImagen() {
        TableColumn<SalaDto, String> columna = new TableColumn<>("Imagen");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPrivadoSala"));
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
                        crearColumnaPelicula(),
                        crearColumnaEstado(),
                        crearColumnaAsientos(),
                        crearColumnaVIP(),
                        crearColumnaEmpleados(),
                        crearColumnaFunciones(), // NUEVA COLUMNA
                        crearColumnaImagen()
                )
        );
    }

    private void crearTabla() {
        configurarColumnas();

        List<SalaDto> arrSalas = SalaControladorListar.arregloSalas();
        datosTabla.setAll(arrSalas);
        miTabla.setItems(datosTabla);
        miTabla.setPlaceholder(new Text("No hay salas registradas"));

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
                        miEscenario, "Advertencia", "Selecciona una sala");
            } else {
                SalaDto objSala = miTabla.getSelectionModel().getSelectedItem();

                // VALIDACIÓN 1: Verificar si tiene empleados
                if (salaTieneEmpleadosAsignados(objSala.getIdSala())) {
                    int cantidadEmpleados = contarEmpleadosEnSala(objSala.getIdSala());

                    String mensaje = String.format(
                            "No se puede eliminar esta sala.\n\n"
                            + "La sala '%s' tiene %d empleado%s asignado%s.\n\n"
                            + "Debe reasignar o eliminar los empleados antes de eliminar la sala.",
                            objSala.getNombreSala(),
                            cantidadEmpleados,
                            cantidadEmpleados == 1 ? "" : "s",
                            cantidadEmpleados == 1 ? "" : "s"
                    );

                    Alert alertaAdvertencia = new Alert(Alert.AlertType.WARNING);
                    alertaAdvertencia.setTitle("Sala con empleados asignados");
                    alertaAdvertencia.setHeaderText("No se puede eliminar");
                    alertaAdvertencia.setContentText(mensaje);
                    alertaAdvertencia.initOwner(miEscenario);
                    alertaAdvertencia.showAndWait();

                    miTabla.getSelectionModel().clearSelection();
                    return;
                }

                // VALIDACIÓN 2: Verificar si tiene funciones
                if (salaTieneFuncionesAsignadas(objSala.getIdSala())) {
                    int cantidadFunciones = contarFuncionesEnSala(objSala.getIdSala());

                    String mensaje = String.format(
                            "No se puede eliminar esta sala.\n\n"
                            + "La sala '%s' tiene %d función%s programada%s.\n\n"
                            + "Debe eliminar o reprogramar las funciones antes de eliminar la sala.",
                            objSala.getNombreSala(),
                            cantidadFunciones,
                            cantidadFunciones == 1 ? "" : "es",
                            cantidadFunciones == 1 ? "" : "s"
                    );

                    Alert alertaAdvertencia = new Alert(Alert.AlertType.WARNING);
                    alertaAdvertencia.setTitle("Sala con funciones programadas");
                    alertaAdvertencia.setHeaderText("No se puede eliminar");
                    alertaAdvertencia.setContentText(mensaje);
                    alertaAdvertencia.initOwner(miEscenario);
                    alertaAdvertencia.showAndWait();

                    miTabla.getSelectionModel().clearSelection();
                    return;
                }

                // Si no tiene ni empleados ni funciones, proceder con la eliminación
                String msg = String.format(
                        "¿Estás seguro de eliminar esta sala?\nCódigo: %d\nNombre: %s\n¡Esta acción es irreversible!",
                        objSala.getIdSala(),
                        objSala.getNombreSala()
                );

                Alert mensajito = new Alert(Alert.AlertType.CONFIRMATION);
                mensajito.setTitle("Confirmar Eliminación");
                mensajito.setHeaderText(null);
                mensajito.setContentText(msg);
                mensajito.initOwner(miEscenario);

                if (mensajito.showAndWait().get() == ButtonType.OK) {
                    int codigo = objSala.getIdSala();
                    if (SalaControladorEliminar.borrar(codigo)) {
                        refrescarTabla();
                        Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario, "ÉXITO", "Sala eliminada correctamente");
                    } else {
                        Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error", "No se pudo eliminar la sala");
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
            SalaDto seleccionado = miTabla.getSelectionModel().getSelectedItem();

            if (seleccionado == null) {
                Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Advertencia",
                        "¡Seleccione una sala para editar!");
            } else {
                BorderPane panelPrincipal = (BorderPane) miEscenario.getScene().getRoot();
                Pane panelAnterior = this;

                StackPane panelEditar = SalaControladorEditar.abrirEditar(
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
        miCajaHorizontal.getChildren().addAll(btnEliminar, btnActualizar, btnCancelar);
        miCajaVertical.getChildren().add(miCajaHorizontal);
    }

    /**
     * Método público para refrescar la tabla después de actualizar
     */
    public void refrescarTabla() {
        int cant = SalaControladorListar.cantidadSalas();
        miTitulo.setText("Administrar Salas - (" + cant + ") ");

        List<SalaDto> arrSalas = SalaControladorListar.arregloSalas();
        datosTabla.setAll(arrSalas);
        miTabla.refresh();
    }

    private boolean salaTieneFuncionesAsignadas(int idSala) {
        List<FuncionDto> funciones = FuncionControladorListar.arregloFunciones();

        for (FuncionDto funcion : funciones) {
            if (funcion.getIdSalaFuncion() != null
                    && funcion.getIdSalaFuncion().getIdSala() == idSala) {
                return true;
            }
        }

        return false;
    }

    private int contarFuncionesEnSala(int idSala) {
        List<FuncionDto> funciones = FuncionControladorListar.arregloFunciones();
        int contador = 0;

        for (FuncionDto funcion : funciones) {
            if (funcion.getIdSalaFuncion() != null
                    && funcion.getIdSalaFuncion().getIdSala() == idSala) {
                contador++;
            }
        }

        return contador;
    }
}
