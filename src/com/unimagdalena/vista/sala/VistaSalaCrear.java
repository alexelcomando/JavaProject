package com.unimagdalena.vista.sala;

import com.unimagdalena.controlador.sala.SalaControladorGrabar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorListar;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.model.Pelicula;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.unimagdalena.helpers.utilidad.SlideSwitch;

import java.io.File;
import java.util.List;

public class VistaSalaCrear extends StackPane {

    private static final int H_GAP = 10;
    private static final int V_GAP = 20;
    private static final int ALTO_FILA = 40;
    private static final int ALTO_CAJA = 35;
    private static final int TAMANYO_FUENTE = 18;
    private static final double AJUSTE_ARRIBA = 0.08;

    private final GridPane miGrilla;
    private final Rectangle miMarco;
    private final Stage miEscenario;

    private TextField cajaNombre;
    private ComboBox<PeliculaDto> cbmPelicula;
    private RadioButton rbDisponible;
    private ToggleGroup grupoDisponible;
    private CheckBox chkAsientos50, chkAsientos100, chkAsientos150;
    private SlideSwitch switchVip;

    private TextField cajaImagen;
    private ImageView imgPorDefecto;
    private ImageView imgPrevisualizar;
    private StackPane contenedorImagen;
    private String rutaSeleccionada;

    public VistaSalaCrear(Stage esce, double ancho, double alto) {
        this.miEscenario = esce;
        rutaSeleccionada = "";

        setAlignment(Pos.CENTER);

        miGrilla = new GridPane();
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE,
                Configuracion.MARCO_ANCHO_PORCENTAJE, Configuracion.DEGRADEE_ARREGLO,
                Configuracion.COLOR_BORDE);

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        setBackground(fondo);

        getChildren().add(miMarco);
        reUbicarFormulario();
        configurarLaGrilla();
        pintarTitulo();
        pintarFormulario();
        getChildren().add(miGrilla);
    }

    private void configurarLaGrilla() {
        miGrilla.setHgap(H_GAP);
        miGrilla.setVgap(V_GAP);
        miGrilla.setAlignment(Pos.TOP_CENTER);
        miGrilla.setPadding(new Insets(20));

        miGrilla.prefWidthProperty().bind(miMarco.widthProperty().multiply(0.9));
        miGrilla.maxWidthProperty().bind(miMarco.widthProperty().multiply(0.9));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(40);
        col2.setHgrow(Priority.ALWAYS);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(30);

        miGrilla.getColumnConstraints().addAll(col1, col2, col3);

        for (int j = 0; j < 9; j++) {
            RowConstraints fila = new RowConstraints();
            fila.setMinHeight(ALTO_FILA);
            fila.setPrefHeight(ALTO_FILA);
            miGrilla.getRowConstraints().add(fila);
        }
    }

    private void reUbicarFormulario() {
        Runnable organizar = () -> {
            double altoDelFormulario = miMarco.getHeight() * AJUSTE_ARRIBA;
            if (altoDelFormulario > 0) {
                miGrilla.setTranslateY(altoDelFormulario / 2 + altoDelFormulario);
            }
        };
        organizar.run();
        miMarco.heightProperty().addListener(((obs, antes, despues) -> {
            organizar.run();
        }));
    }

    private void pintarTitulo() {
        Text titulo = new Text("Crear Sala de Cine");
        titulo.setFill(Color.web(Configuracion.COLOR_BORDE));
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        GridPane.setHalignment(titulo, HPos.CENTER);
        GridPane.setMargin(titulo, new Insets(10, 0, 0, 0));
        miGrilla.add(titulo, 0, 0, 3, 1);
    }

    private void pintarFormulario() {
        // Nombre de la sala
        Label lblNombre = new Label("Nombre de la sala:");
        lblNombre.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNombre, 0, 1);

        cajaNombre = new TextField();
        cajaNombre.setPromptText("Ej: Sala Premium 1");
        cajaNombre.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cajaNombre, 1, 1);

        // Película
        Label lblPelicula = new Label("Película:");
        lblPelicula.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblPelicula, 0, 2);

        cbmPelicula = new ComboBox<>();
        List<PeliculaDto> peliculas = PeliculaControladorListar.arregloPeliculas();
        PeliculaDto opcionDefecto = new PeliculaDto();
        opcionDefecto.setIdPelicula(0);
        opcionDefecto.setNombrePelicula("Seleccione una película");
        peliculas.add(0, opcionDefecto);

        cbmPelicula.getItems().addAll(peliculas);
        cbmPelicula.getSelectionModel().select(0);
        cbmPelicula.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cbmPelicula, 1, 2);

        // Disponible
        Label lblDisponible = new Label("Disponible:");
        lblDisponible.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblDisponible, 0, 3);

        grupoDisponible = new ToggleGroup();
        rbDisponible = new RadioButton("Sí");
        rbDisponible.setToggleGroup(grupoDisponible);
        RadioButton rbNoDisponible = new RadioButton("No");
        rbNoDisponible.setToggleGroup(grupoDisponible);

        HBox panelDisponible = new HBox(10, rbDisponible, rbNoDisponible);
        miGrilla.add(panelDisponible, 1, 3);

        // Asientos
        Label lblAsientos = new Label("Número de asientos:");
        lblAsientos.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblAsientos, 0, 4);

        chkAsientos50 = new CheckBox("50 asientos");
        chkAsientos100 = new CheckBox("100 asientos");
        chkAsientos150 = new CheckBox("150 asientos");

        HBox panelAsientos = new HBox(10, chkAsientos50, chkAsientos100, chkAsientos150);
        miGrilla.add(panelAsientos, 1, 4);

        // VIP
        Label lblVip = new Label("Servicio VIP:");
        lblVip.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblVip, 0, 5);

        switchVip = new SlideSwitch(150, 40);
        switchVip.setTextos("VIP Activo", "VIP Inactivo");
        miGrilla.add(switchVip, 1, 5);

        // Imagen
        Label lblImagen = new Label("Imagen de la sala:");
        lblImagen.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblImagen, 0, 6);

        cajaImagen = new TextField();
        cajaImagen.setDisable(true);
        cajaImagen.setPrefHeight(ALTO_CAJA);

        Button btnEscogerImagen = new Button("+");
        btnEscogerImagen.setPrefHeight(ALTO_CAJA);
        btnEscogerImagen.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Selecciona una imagen");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
            File archivo = fc.showOpenDialog(miEscenario);

            if (archivo != null && archivo.exists()) {
                try {
                    File carpetaDestino = new File(Persistencia.RUTA_IMAGENES_EXTERNAS);
                    if (!carpetaDestino.exists()) {
                        carpetaDestino.mkdirs();
                    }

                    File destino = new File(carpetaDestino, archivo.getName());
                    java.nio.file.Files.copy(archivo.toPath(), destino.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                    rutaSeleccionada = destino.getAbsolutePath();
                    cajaImagen.setText(archivo.getName());
                    mostrarImagenPrevisualizada(rutaSeleccionada);
                } catch (Exception ex) {
                    Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error",
                            "No se pudo copiar la imagen: " + ex.getMessage());
                    rutaSeleccionada = "";
                }
            } else {
                rutaSeleccionada = "";
                mostrarImagenPorDefecto();
            }
        });

        HBox panelCajaImagen = new HBox(2, cajaImagen, btnEscogerImagen);
        HBox.setHgrow(cajaImagen, Priority.ALWAYS);
        panelCajaImagen.setAlignment(Pos.CENTER_LEFT);
        miGrilla.add(panelCajaImagen, 1, 6);

        // Contenedor para imagen con límites
        contenedorImagen = new StackPane();
        contenedorImagen.setMaxWidth(250);
        contenedorImagen.setMaxHeight(250);
        contenedorImagen.setMinWidth(190);
        contenedorImagen.setMinHeight(190);
        contenedorImagen.setAlignment(Pos.CENTER);
        contenedorImagen.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: transparent;");

        imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 190);
        contenedorImagen.getChildren().add(imgPorDefecto);

        GridPane.setHalignment(contenedorImagen, HPos.CENTER);
        miGrilla.add(contenedorImagen, 2, 1, 1, 5);

        // Botón guardar
        Button btnGrabar = new Button("Guardar Sala");
        btnGrabar.setMaxWidth(Double.MAX_VALUE);
        btnGrabar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnGrabar.setTextFill(Color.web(Configuracion.COLOR4));
        btnGrabar.setOnAction(e -> grabarSala());
        miGrilla.add(btnGrabar, 1, 7);
    }

    private void mostrarImagenPrevisualizada(String ruta) {
        contenedorImagen.getChildren().clear();

        try {
            imgPrevisualizar = Icono.previsualizar(ruta, 250);

            imgPrevisualizar.setFitWidth(240);
            imgPrevisualizar.setFitHeight(240);
            imgPrevisualizar.setPreserveRatio(true);

            contenedorImagen.getChildren().add(imgPrevisualizar);
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
            mostrarImagenPorDefecto();
        }
    }

    private void mostrarImagenPorDefecto() {
        contenedorImagen.getChildren().clear();
        imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 190);
        contenedorImagen.getChildren().add(imgPorDefecto);
    }

    private boolean formularioValido() {
        if (cajaNombre.getText().isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Error",
                    "Debes ingresar un nombre para la sala.");
            cajaNombre.requestFocus();
            return false;
        }

        if (cbmPelicula.getSelectionModel().getSelectedIndex() == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Error",
                    "Selecciona una película.");
            cbmPelicula.requestFocus();
            return false;
        }
        if (grupoDisponible.getSelectedToggle() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Error",
                    "Debes seleccionar si la sala está disponible (Sí o No).");
            return false;
        }

        if (obtenerNumeroAsientos() == 0) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Error",
                    "Selecciona el número de asientos (solo una opción).");
            return false;
        }

        if (rutaSeleccionada.isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Error",
                    "Selecciona una imagen para la sala.");
            return false;
        }

        return true;
    }

    private int obtenerNumeroAsientos() {
        int conteo = 0;
        int valor = 0;

        if (chkAsientos50.isSelected()) {
            conteo++;
            valor = 50;
        }
        if (chkAsientos100.isSelected()) {
            conteo++;
            valor = 100;
        }
        if (chkAsientos150.isSelected()) {
            conteo++;
            valor = 150;
        }

        if (conteo > 1) {
            Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario, "Error",
                    "Selecciona solo UNA opción de asientos.");
            return 0;
        }

        return valor;
    }

    private void grabarSala() {
        if (formularioValido()) {
            SalaDto dto = new SalaDto();
            dto.setNombreSala(cajaNombre.getText());

            PeliculaDto peliculaDto = cbmPelicula.getValue();
            Pelicula pelicula = new Pelicula();
            pelicula.setIdPelicula(peliculaDto.getIdPelicula());
            pelicula.setNombrePelicula(peliculaDto.getNombrePelicula());
            dto.setIdPeliculaSala(pelicula);

            dto.setEstaDisponibleSala(rbDisponible.isSelected());
            dto.setNumeroAsientosSala(obtenerNumeroAsientos());
            dto.setServicioVipSala(switchVip.isOn());
            dto.setNombreImagenPublicoSala(cajaImagen.getText());

            if (SalaControladorGrabar.crearSala(dto, rutaSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario, "Éxito",
                        "Sala guardada con éxito");
                limpiarFormulario();
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error",
                        "No se pudo guardar la sala.");
            }
        }
    }

    private void limpiarFormulario() {
        cajaNombre.setText("");
        cbmPelicula.getSelectionModel().select(0);
        grupoDisponible.selectToggle(null);
        chkAsientos50.setSelected(false);
        chkAsientos100.setSelected(false);
        chkAsientos150.setSelected(false);
        switchVip.setEstado(false);

        rutaSeleccionada = "";
        cajaImagen.setText("");
        mostrarImagenPorDefecto();

        cajaNombre.requestFocus();
    }
}
