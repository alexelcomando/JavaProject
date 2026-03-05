package com.unimagdalena.vista.sala;

import com.unimagdalena.controlador.sala.SalaControladorEditar;
import com.unimagdalena.controlador.pelicula.PeliculaControladorListar;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.model.Pelicula;
import com.unimagdalena.model.Genero;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import com.unimagdalena.helpers.utilidad.SlideSwitch;
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

import java.io.File;
import java.util.List;

public class VistaSalaEditar extends StackPane {

    private static final int H_GAP = 10;
    private static final int V_GAP = 20;
    private static final int ALTO_FILA = 40;
    private static final int ALTO_CAJA = 35;
    private static final int TAMANYO_FUENTE = 18;

    private final GridPane miGrilla;
    private final Rectangle miMarco;

    private final Stage miEscenario;
    private final BorderPane panelPrincipal;
    private final Pane panelAnterior;
    private VistaSalaAdministrar vistaAdministrar;
    private VistaSalaCarrusel vistaCarrusel;

    private TextField cajaNombre;
    private ComboBox<PeliculaDto> cbmPelicula;
    private RadioButton rbDisponible;
    private ToggleGroup grupoDisponible;
    private CheckBox chkAsientos50, chkAsientos100, chkAsientos150;
    private SlideSwitch switchVip;

    private TextField cajaImagen;
    private ImageView imgPorDefecto;
    private ImageView imgPrevisualizar;
    private String rutaSeleccionada;

    private final SalaDto sala;

    public VistaSalaEditar(Stage esce, BorderPane panelPrincipal, Pane panelAnterior, SalaDto sala) {
        this.miEscenario = esce;
        this.panelPrincipal = panelPrincipal;
        this.panelAnterior = panelAnterior;
        this.sala = sala;
        this.rutaSeleccionada = "";

        // Guardar referencia a la vista anterior
        if (panelAnterior instanceof VistaSalaAdministrar) {
            this.vistaAdministrar = (VistaSalaAdministrar) panelAnterior;
            this.vistaCarrusel = null;
        } else if (panelAnterior instanceof VistaSalaCarrusel) {
            this.vistaCarrusel = (VistaSalaCarrusel) panelAnterior;
            this.vistaAdministrar = null;
        } else {
            this.vistaAdministrar = null;
            this.vistaCarrusel = null;
        }

        Background fondo = Fondo.asignarAleatorio(Configuracion.FONDOS);
        setBackground(fondo);

        setAlignment(Pos.CENTER);

        miGrilla = new GridPane();
        miMarco = Marco.pintar(esce, Configuracion.MARCO_ALTO_PORCENTAJE, Configuracion.MARCO_ANCHO_PORCENTAJE,
                Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        getChildren().add(miMarco);

        configurarLaGrilla();
        pintarTitulo();
        pintarFormulario();
        cargarDatos();
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

        for (int j = 0; j < 10; j++) {
            RowConstraints fila = new RowConstraints();
            fila.setMinHeight(ALTO_FILA);
            fila.setPrefHeight(ALTO_FILA);
            miGrilla.getRowConstraints().add(fila);
        }
    }

    private void pintarTitulo() {
        Text titulo = new Text("Editar Sala de Cine");
        titulo.setFill(Color.web(Configuracion.COLOR_BORDE));
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        GridPane.setHalignment(titulo, javafx.geometry.HPos.CENTER);
        GridPane.setMargin(titulo, new Insets(10, 0, 0, 0));
        miGrilla.add(titulo, 0, 0, 3, 1);
    }

    private void pintarFormulario() {
        // Nombre
        Label lblNombre = new Label("Nombre de la sala:");
        lblNombre.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        miGrilla.add(lblNombre, 0, 1);

        cajaNombre = new TextField();
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
        cbmPelicula.setPrefHeight(ALTO_CAJA);
        miGrilla.add(cbmPelicula, 1, 2);

        // Disponible
        Label lblDisponible = new Label("Disponible:");
        lblDisponible.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        grupoDisponible = new ToggleGroup();
        rbDisponible = new RadioButton("Sí");
        rbDisponible.setToggleGroup(grupoDisponible);
        RadioButton rbNo = new RadioButton("No");
        rbNo.setToggleGroup(grupoDisponible);
        HBox hbDisponible = new HBox(10, rbDisponible, rbNo);
        miGrilla.add(lblDisponible, 0, 3);
        miGrilla.add(hbDisponible, 1, 3);

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

        Button btnImagen = new Button("+");
        btnImagen.setPrefHeight(ALTO_CAJA);
        btnImagen.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
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

                    rutaSeleccionada = archivo.getName();
                    cajaImagen.setText(rutaSeleccionada);

                    mostrarImagenPrevisualizada(destino.getAbsolutePath());
                } catch (Exception ex) {
                    Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error",
                            "No se pudo copiar la imagen: " + ex.getMessage());
                }
            }
        });

        HBox hbImagen = new HBox(2, cajaImagen, btnImagen);
        HBox.setHgrow(cajaImagen, Priority.ALWAYS);
        hbImagen.setAlignment(Pos.CENTER_LEFT);
        miGrilla.add(hbImagen, 1, 6);

        // Imagen por defecto
        imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 150);
        miGrilla.add(imgPorDefecto, 2, 1, 1, 5);

        Button btnActualizar = new Button("Actualizar Sala");
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setFont(Font.font("Verdana", FontWeight.BOLD, TAMANYO_FUENTE));
        btnActualizar.setOnAction(e -> actualizarSala());
        miGrilla.add(btnActualizar, 1, 7);

        Button btnRegresar = new Button("Regresar");
        btnRegresar.setMaxWidth(Double.MAX_VALUE);
        btnRegresar.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        btnRegresar.setTextFill(Color.web("#6C3483"));
        btnRegresar.setOnAction(e -> {
            if (vistaAdministrar != null) {
                vistaAdministrar.refrescarTabla();
            } else if (vistaCarrusel != null) {
                vistaCarrusel.refrescarCarrusel();
            }
            panelPrincipal.setCenter(panelAnterior);
        });
        miGrilla.add(btnRegresar, 1, 8);
    }

    private void cargarDatos() {
        if (sala == null) {
            return;
        }

        cajaNombre.setText(sala.getNombreSala());

        // Película
        if (sala.getIdPeliculaSala() != null) {
            int idPelicula = sala.getIdPeliculaSala().getIdPelicula();
            for (PeliculaDto peli : cbmPelicula.getItems()) {
                if (peli.getIdPelicula() == idPelicula) {
                    cbmPelicula.getSelectionModel().select(peli);
                    break;
                }
            }
        } else {
            cbmPelicula.getSelectionModel().select(0);
        }

        // Disponible
        if (sala.getEstaDisponibleSala()) {
            rbDisponible.setSelected(true);
        } else {
            grupoDisponible.selectToggle(grupoDisponible.getToggles().get(1));
        }

        // Asientos
        int asientos = sala.getNumeroAsientosSala();
        chkAsientos50.setSelected(asientos == 50);
        chkAsientos100.setSelected(asientos == 100);
        chkAsientos150.setSelected(asientos == 150);

        // VIP
        switchVip.setEstado(sala.getServicioVipSala());

        // Imagen
        String nombreImagen = sala.getNombreImagenPrivadoSala();
        if (nombreImagen != null && !nombreImagen.isEmpty()) {
            rutaSeleccionada = nombreImagen;
            cajaImagen.setText(nombreImagen);

            ImageView imgCargada = Icono.obtenerFotosExternas(nombreImagen, 250);
            if (imgCargada != null) {
                if (miGrilla.getChildren().contains(imgPorDefecto)) {
                    miGrilla.getChildren().remove(imgPorDefecto);
                }

                // Configurar límites para la imagen cargada
                imgCargada.setFitWidth(250);
                imgCargada.setFitHeight(250);
                imgCargada.setPreserveRatio(true);
                imgCargada.setSmooth(true);

                GridPane.setHalignment(imgCargada, javafx.geometry.HPos.CENTER);
                miGrilla.add(imgCargada, 2, 1, 1, 5);
                imgPrevisualizar = imgCargada;
            } else {
                mostrarImagenPorDefecto();
            }
        } else {
            rutaSeleccionada = "";
            cajaImagen.setText("");
            mostrarImagenPorDefecto();
        }
    }

    private void mostrarImagenPrevisualizada(String ruta) {
        if (imgPrevisualizar != null && miGrilla.getChildren().contains(imgPrevisualizar)) {
            miGrilla.getChildren().remove(imgPrevisualizar);
        }
        if (miGrilla.getChildren().contains(imgPorDefecto)) {
            miGrilla.getChildren().remove(imgPorDefecto);
        }

        try {
            imgPrevisualizar = Icono.previsualizar(ruta, 250);

            // Configurar límites para que no se salga del marco
            imgPrevisualizar.setFitWidth(250);
            imgPrevisualizar.setFitHeight(250);
            imgPrevisualizar.setPreserveRatio(true);
            imgPrevisualizar.setSmooth(true);

            GridPane.setHalignment(imgPrevisualizar, javafx.geometry.HPos.CENTER);
            miGrilla.add(imgPrevisualizar, 2, 1, 1, 5);
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
            mostrarImagenPorDefecto();
        }
    }

    private void mostrarImagenPorDefecto() {
        if (imgPrevisualizar != null && miGrilla.getChildren().contains(imgPrevisualizar)) {
            miGrilla.getChildren().remove(imgPrevisualizar);
        }
        if (!miGrilla.getChildren().contains(imgPorDefecto)) {
            imgPorDefecto = Icono.obtenerIcono("imgNoDisponible.png", 190);

            // Configurar límites
            imgPorDefecto.setFitWidth(190);
            imgPorDefecto.setFitHeight(190);
            imgPorDefecto.setPreserveRatio(true);

            GridPane.setHalignment(imgPorDefecto, javafx.geometry.HPos.CENTER);
            miGrilla.add(imgPorDefecto, 2, 1, 1, 5);
        }
    }

    private int obtenerNumeroAsientos() {
        if (chkAsientos50.isSelected()) {
            return 50;
        } else if (chkAsientos100.isSelected()) {
            return 100;
        } else if (chkAsientos150.isSelected()) {
            return 150;
        }
        return 50; // Valor por defecto
    }

    private Pelicula convertirDtoAModelo(PeliculaDto dto) {
        Pelicula pelicula = new Pelicula();
        pelicula.setIdPelicula(dto.getIdPelicula());
        pelicula.setNombrePelicula(dto.getNombrePelicula());
        pelicula.setFechaEstrenoPelicula(dto.getFechaEstrenoPelicula());

        // Convertir GeneroDto a Genero si existe
        if (dto.getIdGeneroPelicula() != null) {
            Genero genero = new Genero();
            genero.setIdGenero(dto.getIdGeneroPelicula().getIdGenero());
            genero.setNombreGenero(dto.getIdGeneroPelicula().getNombreGenero());
            pelicula.setIdGeneroPelicula(genero);
        }

        pelicula.setEs3dPelicula(dto.getEs3dPelicula());
        pelicula.setClasificacionEdadPelicula(dto.getClasificacionEdadPelicula());
        pelicula.setNombreImagenPublicoPelicula(dto.getNombreImagenPublicoPelicula());
        pelicula.setNombreImagenPrivadoPelicula(dto.getNombreImagenPrivadoPelicula());
        return pelicula;
    }

    private void actualizarSala() {
        try {
            sala.setNombreSala(cajaNombre.getText());

            // Película
            PeliculaDto peliculaDto = cbmPelicula.getValue();
            if (peliculaDto != null && peliculaDto.getIdPelicula() > 0) {
                Pelicula pelicula = convertirDtoAModelo(peliculaDto);
                sala.setIdPeliculaSala(pelicula);
            }

            sala.setEstaDisponibleSala(rbDisponible.isSelected());
            sala.setNumeroAsientosSala(obtenerNumeroAsientos());
            sala.setServicioVipSala(switchVip.isOn());

            // Imagen
            String rutaParaActualizar = "";
            if (rutaSeleccionada != null && !rutaSeleccionada.isEmpty()
                    && !rutaSeleccionada.equals(sala.getNombreImagenPrivadoSala())) {
                sala.setNombreImagenPublicoSala(rutaSeleccionada);
                String rutaCompleta = Persistencia.RUTA_IMAGENES_EXTERNAS + File.separator + rutaSeleccionada;
                File archivoImagen = new File(rutaCompleta);

                if (archivoImagen.exists()) {
                    rutaParaActualizar = rutaCompleta;
                }
            }

            boolean exito = SalaControladorEditar.actualizar(sala, rutaParaActualizar);
            if (exito) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, miEscenario, "Éxito",
                        "Sala actualizada correctamente");

                if (vistaAdministrar != null) {
                    vistaAdministrar.refrescarTabla();
                } else if (vistaCarrusel != null) {
                    vistaCarrusel.refrescarCarrusel();
                }
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error",
                        "No se pudo actualizar la sala");
            }
        } catch (Exception ex) {
            Mensaje.mostrar(Alert.AlertType.ERROR, miEscenario, "Error",
                    "Error al actualizar la sala: " + ex.getMessage());
        }
    }
}
