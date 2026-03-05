package com.unimagdalena.vista.sala;

import com.unimagdalena.controlador.funcion.FuncionControladorListar;
import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.controlador.empleado.EmpleadoControladorListar;
import com.unimagdalena.dto.EmpleadoDto;
import com.unimagdalena.controlador.sala.SalaControladorListar;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Marco;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

import java.util.List;
import javafx.scene.layout.Background;

public class VistaSalaListar extends StackPane {

    private final Rectangle miMarco;
    private final Stage miEscenario;
    private final VBox miCajaVertical;
    private final TableView<SalaDto> miTabla;

    private static final String ESTILO_CENTRAR = "-fx-alignment: CENTER;";
    private static final String ESTILO_IZQUIERDA = "-fx-alignment: CENTER-LEFT;";
    private static final String ESTILO_ELROJO = "-fx-text-fill: red; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELVERDE = "-fx-text-fill: green; " + ESTILO_CENTRAR;
    private static final String ESTILO_ELAMARILLO = "-fx-text-fill: orange; " + ESTILO_CENTRAR;

    public VistaSalaListar(Stage esce, double ancho, double alto) {
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
    }

    private void todoResponsive() {
        miCajaVertical.setAlignment(Pos.TOP_CENTER);
        miCajaVertical.prefWidthProperty().bind(miMarco.widthProperty());
        miCajaVertical.prefHeightProperty().bind(miMarco.heightProperty());
    }

    private void armarTitulo() {
        Region separadorTitulo = new Region();
        separadorTitulo.prefHeightProperty().bind(miEscenario.heightProperty().multiply(0.05));
        int cantidad = SalaControladorListar.cantidadSalas();
        Text miTitulo = new Text("Listado de Salas de Cine - (" + cantidad + ")");
        miTitulo.setFill(Color.web("#54e8b7"));
        miTitulo.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        miCajaVertical.getChildren().addAll(separadorTitulo, miTitulo);
    }

    private TableColumn<SalaDto, Integer> crearColumnaCodigo() {
        TableColumn<SalaDto, Integer> columna = new TableColumn<>("Código");
        columna.setCellValueFactory(new PropertyValueFactory<>("idSala"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.08));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<SalaDto, String> crearColumnaNombre() {
        TableColumn<SalaDto, String> columna = new TableColumn<>("Nombre");
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreSala"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<SalaDto, String> crearColumnaPelicula() {
        TableColumn<SalaDto, String> columna = new TableColumn<>("Película");

        columna.setCellValueFactory(obj -> {
            if (obj.getValue().getIdPeliculaSala() != null) {
                String nombrePelicula = obj.getValue().getIdPeliculaSala().getNombrePelicula();
                return new SimpleStringProperty(nombrePelicula != null ? nombrePelicula : "Sin película");
            }
            return new SimpleStringProperty("Sin película");
        });

        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.20));
        columna.setStyle(ESTILO_CENTRAR);
        return columna;
    }

    private TableColumn<SalaDto, String> crearColumnaDisponible() {
        TableColumn<SalaDto, String> columna = new TableColumn<>("Disponible");

        columna.setCellValueFactory(obj -> {
            Boolean disponible = obj.getValue().getEstaDisponibleSala();
            String estado = (disponible != null && disponible) ? "Sí" : "No";
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
                    setStyle(estado.equals("Sí") ? ESTILO_ELVERDE : ESTILO_ELROJO);
                }
            }
        });

        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.10));
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
            String estado = (vip != null && vip) ? "Sí" : "No";
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
                    setStyle(estado.equals("Sí") ? ESTILO_ELVERDE : ESTILO_CENTRAR);
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
        columna.setCellValueFactory(new PropertyValueFactory<>("nombreImagenPublicoSala"));
        columna.prefWidthProperty().bind(miTabla.widthProperty().multiply(0.24));
        columna.setStyle(ESTILO_IZQUIERDA);
        return columna;
    }

    private void configurarColumnas() {
        miTabla.getColumns().addAll(
                List.of(
                        crearColumnaCodigo(),
                        crearColumnaNombre(),
                        crearColumnaPelicula(),
                        crearColumnaDisponible(),
                        crearColumnaAsientos(),
                        crearColumnaVIP(),
                        crearColumnaEmpleados(),
                        crearColumnaFunciones(), // ← NUEVA COLUMNA
                        crearColumnaImagen()
                )
        );
    }

    private void crearTabla() {
        configurarColumnas();

        List<SalaDto> arrSalas = SalaControladorListar.arregloSalas();
        ObservableList<SalaDto> datosTabla = FXCollections.observableArrayList(arrSalas);
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

// Métodos para contar FUNCIONES 
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
