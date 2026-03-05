package com.unimagdalena.vista.sala;

import com.unimagdalena.controlador.funcion.FuncionControladorListar;
import com.unimagdalena.dto.FuncionDto;

import com.unimagdalena.controlador.empleado.EmpleadoControladorListar;
import com.unimagdalena.dto.EmpleadoDto;
import java.util.List;

import com.unimagdalena.controlador.sala.SalaControladorEditar;
import com.unimagdalena.controlador.sala.SalaControladorEliminar;
import com.unimagdalena.controlador.sala.SalaControladorListar;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VistaSalaCarrusel extends StackPane {

    private final BorderPane miBorderPane;
    private final Stage miEscenario;
    private final VBox organizadorVertical;

    private Pane panelCuerpo;
    private final BorderPane panelPrincipal;

    private int indiceActual;
    private int totalSalas;
    private SalaDto objCargado;

    private StringProperty propTitulo;
    private StringProperty propNombre;
    private StringProperty propPelicula;
    private BooleanProperty propDisponible;
    private IntegerProperty propAsientos;
    private BooleanProperty propVip;
    private ObjectProperty<Image> propImagen;

    public VistaSalaCarrusel(Stage esce, BorderPane panelPrinc, Pane panelAnt, int indice) {
        this.miEscenario = esce;
        this.panelPrincipal = panelPrinc;
        this.panelCuerpo = panelAnt;

        miBorderPane = new BorderPane();
        organizadorVertical = new VBox();

        indiceActual = indice;
        totalSalas = SalaControladorListar.cantidadSalas();

        if (totalSalas == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, esce, "Sin salas",
                    "No hay salas registradas para mostrar en el carrusel");
            setAlignment(Pos.CENTER);
            getChildren().add(miBorderPane);
            return;
        }

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        setBackground(fondo);

        if (indice < 0 || indice >= totalSalas) {
            indiceActual = 0;
        }

        objCargado = SalaControladorListar.obtenerUno(indiceActual);

        setAlignment(Pos.CENTER);

        configurarOrganizadorVertical();
        crearTitulo();

        construirPanelIzquierdo(0.14);
        construirPanelDerecho(0.14);
        construirPanelCentro();

        getChildren().add(miBorderPane);
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

    private void construirPanelIzquierdo(double porcentaje) {
        Button btnAnterior = new Button();
        btnAnterior.setGraphic(Icono.obtenerIcono("btnAtras.png", 80));
        btnAnterior.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        btnAnterior.setCursor(Cursor.HAND);
        btnAnterior.setOnAction(e -> navegarCarrusel("Anterior"));

        StackPane panelIzquierdo = new StackPane();
        panelIzquierdo.prefWidthProperty().bind(miEscenario.widthProperty().multiply(porcentaje));
        panelIzquierdo.getChildren().add(btnAnterior);
        miBorderPane.setLeft(panelIzquierdo);
    }

    private void construirPanelDerecho(double porcentaje) {
        Button btnSiguiente = new Button();
        btnSiguiente.setGraphic(Icono.obtenerIcono("btnSiguiente.png", 80));
        btnSiguiente.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        btnSiguiente.setCursor(Cursor.HAND);
        btnSiguiente.setOnAction(e -> navegarCarrusel("Siguiente"));

        StackPane panelDerecho = new StackPane();
        panelDerecho.prefWidthProperty().bind(miEscenario.widthProperty().multiply(porcentaje));
        panelDerecho.getChildren().add(btnSiguiente);
        miBorderPane.setRight(panelDerecho);
    }

    private void navegarCarrusel(String direccion) {
        indiceActual = obtenerIndice(direccion, indiceActual, totalSalas);
        objCargado = SalaControladorListar.obtenerUno(indiceActual);

        actualizarDatosCarrusel();
    }

    private void actualizarDatosCarrusel() {
        propTitulo.set("Detalle de la Sala (" + (indiceActual + 1) + " / " + totalSalas + ")");
        propNombre.set(objCargado.getNombreSala());

        String nombrePelicula = objCargado.getIdPeliculaSala() != null
                ? objCargado.getIdPeliculaSala().getNombrePelicula()
                : "Sin película asignada";
        propPelicula.set(nombrePelicula);

        propDisponible.set(objCargado.getEstaDisponibleSala());
        propAsientos.set(objCargado.getNumeroAsientosSala());
        propVip.set(objCargado.getServicioVipSala());

        // Actualizar imagen
        try {
            String rutaNuevaImagen = Persistencia.RUTA_IMAGENES_EXTERNAS
                    + Persistencia.SEPARADOR_CARPETAS
                    + objCargado.getNombreImagenPrivadoSala();
            FileInputStream imgArchivo = new FileInputStream(rutaNuevaImagen);
            Image imgnueva = new Image(imgArchivo);
            propImagen.set(imgnueva);
        } catch (FileNotFoundException ex) {
            System.out.println("No se encontró la imagen: " + ex.getMessage());
            propImagen.set(new Image(getClass().getResourceAsStream(
                    Persistencia.RUTA_IMAGENES_INTERNAS + "imgNoDisponible.png")));
        }
    }

    private void configurarOrganizadorVertical() {
        organizadorVertical.setSpacing(15);
        organizadorVertical.setAlignment(Pos.TOP_CENTER);
        organizadorVertical.setPadding(new Insets(20));
        organizadorVertical.prefWidthProperty().bind(miEscenario.widthProperty());
        organizadorVertical.prefHeightProperty().bind(miEscenario.heightProperty());
    }

    private void crearTitulo() {
        Region bloqueSeparador = new Region();
        bloqueSeparador.prefHeightProperty().bind(miEscenario.heightProperty().multiply(0.05));
        organizadorVertical.getChildren().add(0, bloqueSeparador);

        propTitulo = new SimpleStringProperty("Detalle de la Sala (" + (indiceActual + 1) + " / " + totalSalas + ")");

        Label lblTitulo = new Label();
        lblTitulo.textProperty().bind(propTitulo);
        lblTitulo.setTextFill(Color.web("#54e8b7"));
        lblTitulo.setFont(Font.font("verdana", FontWeight.BOLD, 28));
        organizadorVertical.getChildren().add(lblTitulo);
    }

    private void panelOpciones() {
        int anchoBoton = 45;
        int tamanioIcono = 20;

        Button btnEliminar = new Button();
        btnEliminar.setPrefWidth(anchoBoton);
        btnEliminar.setPrefHeight(anchoBoton);
        btnEliminar.setCursor(Cursor.HAND);
        btnEliminar.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_BORRAR, tamanioIcono));
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5;");
        btnEliminar.setOnAction(e -> eliminarSalaActual());

        Button btnEditar = new Button();
        btnEditar.setPrefWidth(anchoBoton);
        btnEditar.setPrefHeight(anchoBoton);
        btnEditar.setCursor(Cursor.HAND);
        btnEditar.setGraphic(Icono.obtenerIcono(Configuracion.ICONO_EDITAR, tamanioIcono));
        btnEditar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5;");
        btnEditar.setOnAction(e -> editarSalaActual());

        HBox panelHorizontalBotones = new HBox(10);
        panelHorizontalBotones.setAlignment(Pos.CENTER);
        panelHorizontalBotones.getChildren().addAll(btnEliminar, btnEditar);

        organizadorVertical.getChildren().add(panelHorizontalBotones);
    }

    private void eliminarSalaActual() {
        // VALIDACIÓN 1: Verificar si tiene empleados
        if (salaTieneEmpleadosAsignados(objCargado.getIdSala())) {
            int cantidadEmpleados = contarEmpleadosEnSala(objCargado.getIdSala());

            String mensaje = String.format(
                    "No se puede eliminar esta sala.\n\n"
                    + "La sala '%s' tiene %d empleado%s asignado%s.\n\n"
                    + "Debe reasignar o eliminar los empleados antes de eliminar la sala.",
                    objCargado.getNombreSala(),
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

            return;
        }

        // VALIDACIÓN 2: Verificar si tiene funciones
        if (salaTieneFuncionesAsignadas(objCargado.getIdSala())) {
            int cantidadFunciones = contarFuncionesEnSala(objCargado.getIdSala());

            String mensaje = String.format(
                    "No se puede eliminar esta sala.\n\n"
                    + "La sala '%s' tiene %d función%s programada%s.\n\n"
                    + "Debe eliminar o reprogramar las funciones antes de eliminar la sala.",
                    objCargado.getNombreSala(),
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

            return;
        }

        // Si no tiene ni empleados ni funciones, proceder con la eliminación
        String texto = String.format(
                "¿Estás seguro de eliminar esta sala?\n\n"
                + "Código: %d\n"
                + "Nombre: %s\n\n"
                + "¡Esta acción es irreversible!",
                objCargado.getIdSala(),
                objCargado.getNombreSala()
        );

        Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
        msg.setTitle("Confirmar Eliminación");
        msg.setHeaderText(null);
        msg.setContentText(texto);
        msg.initOwner(miEscenario);

        if (msg.showAndWait().get() == ButtonType.OK) {
            if (SalaControladorEliminar.borrar(objCargado.getIdSala())) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario,
                        "Éxito", "Sala eliminada correctamente");

                totalSalas = SalaControladorListar.cantidadSalas();

                if (totalSalas == 0) {
                    Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario,
                            "Sin salas", "No hay más salas para mostrar");
                    regresarAAdministrar();
                    return;
                }

                if (indiceActual >= totalSalas) {
                    indiceActual = totalSalas - 1;
                }

                objCargado = SalaControladorListar.obtenerUno(indiceActual);
                actualizarDatosCarrusel();

            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario,
                        "Error", "No se pudo eliminar la sala");
            }
        }
    }

    private void editarSalaActual() {
        StackPane panelEditar = SalaControladorEditar.abrirEditar(
                miEscenario,
                panelPrincipal,
                this,
                objCargado
        );
        panelPrincipal.setCenter(panelEditar);
    }

    private void regresarAAdministrar() {
        VistaSalaAdministrar vistaAdmin = new VistaSalaAdministrar(
                miEscenario,
                miEscenario.getWidth(),
                miEscenario.getHeight()
        );
        panelPrincipal.setCenter(vistaAdmin);
    }

    private void construirPanelCentro() {
        StackPane centerPane = new StackPane();

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        centerPane.setBackground(fondo);

        Rectangle miMarco = Marco.pintar(miEscenario, 0.65, 0.75,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        centerPane.getChildren().addAll(miMarco, organizadorVertical);

        panelOpciones();

        // Nombre de la sala
        propNombre = new SimpleStringProperty(objCargado.getNombreSala());
        Label lblNombre = new Label();
        lblNombre.textProperty().bind(propNombre);
        lblNombre.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        lblNombre.setTextFill(Color.web("#2C3E50"));
        organizadorVertical.getChildren().add(lblNombre);

        // Imagen
        propImagen = new SimpleObjectProperty<>();

        try {
            String rutaNuevaImagen = Persistencia.RUTA_IMAGENES_EXTERNAS
                    + Persistencia.SEPARADOR_CARPETAS
                    + objCargado.getNombreImagenPrivadoSala();
            FileInputStream imgArchivo = new FileInputStream(rutaNuevaImagen);
            Image imgNueva = new Image(imgArchivo);
            propImagen.set(imgNueva);
        } catch (FileNotFoundException ex) {
            System.out.println("Imagen no encontrada: " + ex.getMessage());
            propImagen.set(new Image(getClass().getResourceAsStream(
                    Persistencia.RUTA_IMAGENES_INTERNAS + "imgNoDisponible.png")));
        }

        ImageView imgSala = new ImageView();
        imgSala.imageProperty().bind(propImagen);
        imgSala.setFitHeight(280);
        imgSala.setPreserveRatio(true);
        imgSala.setSmooth(true);
        organizadorVertical.getChildren().add(imgSala);

        // Película asignada
        String nombrePelicula = objCargado.getIdPeliculaSala() != null
                ? objCargado.getIdPeliculaSala().getNombrePelicula()
                : "Sin película asignada";
        propPelicula = new SimpleStringProperty(nombrePelicula);
        Label lblPelicula = new Label();
        lblPelicula.textProperty().bind(Bindings.concat("Película: ", propPelicula));
        lblPelicula.setFont(Font.font("Verdana", FontWeight.NORMAL, 18));
        lblPelicula.setTextFill(Color.web("#34495E"));
        organizadorVertical.getChildren().add(lblPelicula);

        // Disponibilidad
        propDisponible = new SimpleBooleanProperty(objCargado.getEstaDisponibleSala());
        Label lblDisponible = new Label();
        lblDisponible.textProperty().bind(Bindings.createStringBinding(
                () -> "Estado: " + (propDisponible.get() ? "Disponible" : "No disponible"),
                propDisponible
        ));
        lblDisponible.setFont(Font.font("Verdana", FontWeight.NORMAL, 18));
        lblDisponible.textFillProperty().bind(
                propDisponible.map(dato -> dato ? Color.web("#27AE60") : Color.web("#E74C3C"))
        );
        organizadorVertical.getChildren().add(lblDisponible);

        // Número de asientos
        propAsientos = new SimpleIntegerProperty(objCargado.getNumeroAsientosSala());
        Label lblAsientos = new Label();
        lblAsientos.textProperty().bind(Bindings.concat("Capacidad: ", propAsientos.asString(), " asientos"));
        lblAsientos.setFont(Font.font("Verdana", FontWeight.NORMAL, 18));
        lblAsientos.setTextFill(Color.web("#34495E"));
        organizadorVertical.getChildren().add(lblAsientos);

        // Servicio VIP
        propVip = new SimpleBooleanProperty(objCargado.getServicioVipSala());
        Label lblVip = new Label();
        lblVip.textProperty().bind(Bindings.createStringBinding(
                () -> propVip.get() ? "⭐ Servicio VIP" : "Servicio estándar",
                propVip
        ));
        lblVip.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        lblVip.textFillProperty().bind(
                propVip.map(dato -> dato ? Color.web("#F39C12") : Color.web("#7F8C8D"))
        );
        organizadorVertical.getChildren().add(lblVip);

        miBorderPane.setCenter(centerPane);
    }

    private static Integer obtenerIndice(String opcion, int indice, int total) {
        int nuevoIndice = indice;
        int limite = total - 1;

        switch (opcion.toLowerCase()) {
            case "anterior" ->
                nuevoIndice = (indice == 0) ? limite : indice - 1;
            case "siguiente" ->
                nuevoIndice = (indice == limite) ? 0 : indice + 1;
        }
        return nuevoIndice;
    }

    public void refrescarCarrusel() {
        totalSalas = SalaControladorListar.cantidadSalas();

        if (totalSalas == 0) {
            regresarAAdministrar();
            return;
        }

        if (indiceActual >= totalSalas) {
            indiceActual = totalSalas - 1;
        }

        objCargado = SalaControladorListar.obtenerUno(indiceActual);
        actualizarDatosCarrusel();
    }
}
