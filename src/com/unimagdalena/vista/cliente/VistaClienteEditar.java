package com.unimagdalena.vista.cliente;

import com.unimagdalena.controlador.cliente.ClienteControladorEditar;
import com.unimagdalena.controlador.cliente.ClienteControladorVista;
import com.unimagdalena.controlador.pelicula.PeliculaControladorListar;
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Contenedor;
import com.unimagdalena.helpers.utilidad.Fondo;
import com.unimagdalena.helpers.utilidad.Formulario;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Marco;
import com.unimagdalena.helpers.utilidad.Mensaje;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
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

public class VistaClienteEditar extends SubScene {

    private final GridPane miGrilla;
    private final StackPane miFormulario;
    private final Stage laVentanaPrincipal;
    private final BorderPane panelPrincipal;
    private Pane panelCuerpo;

    private TextField txtNombre, txtImagen;
    private ComboBox<String> cmbTipoDoc;
    private ComboBox<PeliculaDto> cmbPelicula;
    private DatePicker dtpFecha;
    private RadioButton rbMasc, rbFem;
    private CheckBox chkVIP;
    private ImageView imgPreview;
    private String rutaImagenSeleccionada = "";

    private final ClienteDto objCliente;
    private final int posicion;

    private static final int TAMANYO_FUENTE = 16;
    
    public VistaClienteEditar(Stage ventanaPadre, BorderPane princ, Pane pane, double ancho,
            double alto, ClienteDto objClienteExterno, int posicionArchivo) {
        super(new StackPane(), ancho, alto);

        miFormulario = (StackPane) getRoot();
        miFormulario.setBackground(Fondo.asignarAleatorio(Configuracion.FONDOS));
        miFormulario.setAlignment(Pos.CENTER);

        laVentanaPrincipal = ventanaPadre;
        panelPrincipal = princ;
        panelCuerpo = pane;
        objCliente = objClienteExterno;
        posicion = posicionArchivo;
        miGrilla = new GridPane();

        crearMarco();
        configurarGrilla(ancho, alto);
        crearTitulo();
        crearFormulario();

        miFormulario.getChildren().add(miGrilla);
    }

    private void configurarGrilla(double ancho, double alto) {
        double anchoMarco = ancho * 0.5;

        miGrilla.setHgap(10);
        miGrilla.setVgap(20);
        miGrilla.setAlignment(Pos.TOP_CENTER);

        miGrilla.setPrefSize(anchoMarco, alto);
        miGrilla.setMinSize(anchoMarco, alto);
        miGrilla.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        miGrilla.prefWidthProperty().bind(laVentanaPrincipal.widthProperty().multiply(0.7));

        ColumnConstraints anchoColumna1 = new ColumnConstraints();
        anchoColumna1.setPercentWidth(40);

        ColumnConstraints anchoColumna2 = new ColumnConstraints();
        anchoColumna2.setPercentWidth(60);
        anchoColumna2.setHgrow(Priority.ALWAYS);

        miGrilla.getColumnConstraints().addAll(anchoColumna1, anchoColumna2);
    }

    private void crearMarco() {
        double ancho = Configuracion.MARCO_ANCHO_PORCENTAJE;
        double alto = Configuracion.MARCO_ALTO_PORCENTAJE;
        Rectangle miMarco = Marco.pintar(laVentanaPrincipal, alto, ancho, Configuracion.DEGRADEE_ARREGLO, Configuracion.COLOR_BORDE);
        miFormulario.getChildren().add(miMarco);
    }

    private void crearTitulo() {
        Region bloqueSeparador = new Region();
        bloqueSeparador.prefHeightProperty().bind(laVentanaPrincipal.heightProperty().multiply(0.05));
        miGrilla.add(bloqueSeparador, 0, 0, 2, 1);
        
        Text titulo = new Text("Actualizar Cliente ID: " + objCliente.getIdCliente());
        titulo.setFill(Color.web("#E82E68"));
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, 25));
        GridPane.setHalignment(titulo, HPos.CENTER);
        miGrilla.add(titulo, 0, 1, 2, 1);
    }

    private Label crearLabel(String texto) {
        Label l = new Label(texto);
        l.setFont(Font.font("Verdana", TAMANYO_FUENTE));
        return l;
    }

    private void crearFormulario() {
        // Nombre
        miGrilla.add(crearLabel("Nombre Completo:"), 0, 2);
        txtNombre = new TextField(objCliente.getNombreCliente());
        txtNombre.setMaxWidth(Double.MAX_VALUE);
        miGrilla.add(txtNombre, 1, 2);

        // Tipo Doc
        miGrilla.add(crearLabel("Tipo Documento:"), 0, 3);
        cmbTipoDoc = new ComboBox<>();
        cmbTipoDoc.getItems().addAll("Cédula", "Tarjeta Identidad", "Pasaporte");
        cmbTipoDoc.setValue(objCliente.getTipoDocumentoCliente());
        cmbTipoDoc.setMaxWidth(Double.MAX_VALUE);
        miGrilla.add(cmbTipoDoc, 1, 3);
        
        // Pelicula de Interés 
        List<PeliculaDto> arrPeliculas = PeliculaControladorListar.arregloPeliculas();
        // Se crea un objeto PeliculaDto por defecto para indicar "sin seleccionar"
        PeliculaDto peliculaDefecto = new PeliculaDto(0, "Seleccione película", LocalDate.MIN, null, false, "", "", "");
        arrPeliculas.add(0, peliculaDefecto);
        
        miGrilla.add(crearLabel("Pelicula de interes"), 0, 4);
        cmbPelicula = new ComboBox<>();
        cmbPelicula.setItems(FXCollections.observableArrayList(arrPeliculas));
        
        // CORRECCIÓN 1: Manejar si la película original es null
        PeliculaDto peliculaActual = objCliente.getIdPeliculaCliente();

        if (peliculaActual != null) {
            cmbPelicula.setValue(peliculaActual);
        } else {
            // Selecciona el valor por defecto si el cliente no tiene película asignada
            cmbPelicula.getSelectionModel().selectFirst(); 
        }
        
        cmbPelicula.setMaxWidth(Double.MAX_VALUE);
        miGrilla.add(cmbPelicula, 1, 4);

        // Fecha Nacimiento
        miGrilla.add(crearLabel("Fecha Nacimiento:"), 0, 5);
        dtpFecha = new DatePicker(objCliente.getFechaDeNacimientoCliente());
        dtpFecha.setMaxWidth(Double.MAX_VALUE);
        miGrilla.add(dtpFecha, 1, 5);

        // Sexo
        miGrilla.add(crearLabel("Sexo:"), 0, 6);
        ToggleGroup tg = new ToggleGroup();
        rbMasc = new RadioButton("Masculino"); rbMasc.setToggleGroup(tg);
        rbFem = new RadioButton("Femenino"); rbFem.setToggleGroup(tg);
        if (objCliente.getSexoCliente()) { // true = Masculino
            rbMasc.setSelected(true);
        } else {
            rbFem.setSelected(true);
        }
        HBox boxSexo = new HBox(10, rbMasc, rbFem);
        miGrilla.add(boxSexo, 1, 6);

        // VIP
        miGrilla.add(crearLabel("¿Es VIP?:"), 0, 7);
        chkVIP = new CheckBox("Acceso VIP");
        chkVIP.setSelected("Si".equals(objCliente.getEsAccesoVipCliente()));
        miGrilla.add(chkVIP, 1, 7);

        // Imagen
        miGrilla.add(crearLabel("Foto:"), 0, 8);
        txtImagen = new TextField(objCliente.getNombreImagenPublicoCliente()); txtImagen.setDisable(true);
        Button btnImg = new Button("+");
        btnImg.setOnAction(e -> seleccionarImagen());
        HBox boxImg = new HBox(5, txtImagen, btnImg);
        miGrilla.add(boxImg, 1, 8);

        // Previsualización (Inicial)
        imgPreview = Icono.obtenerFotosExternas(objCliente.getNombreImagenPrivadoCliente(), 120);
        GridPane.setHalignment(imgPreview, HPos.CENTER);
        miGrilla.add(imgPreview, 1, 9);

        // Botones
        Button btnGuardar = new Button("ACTUALIZAR CLIENTE");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnGuardar.setStyle("-fx-base: " + Configuracion.COLOR4 + "; -fx-text-fill: white; -fx-font-weight: bold;");
        btnGuardar.setOnAction(e -> actualizarCliente());
        miGrilla.add(btnGuardar, 1, 10);
        
        Button btnRegresar = new Button("Regresar");
        btnRegresar.setMaxWidth(Double.MAX_VALUE);
        btnRegresar.setTextFill(Color.web("#6C3483"));
        btnRegresar.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        miGrilla.add(btnRegresar, 1, 11);
        btnRegresar.setOnAction((ActionEvent e) -> {
            regresarAAdministrar();
        });
    }
    
    private void seleccionarImagen() {
        FileChooser fc = Formulario.selectorImagen("Foto Cliente", "Imágenes", new String[]{"*.png", "*.jpg"});
        rutaImagenSeleccionada = GestorImagen.obtenerRutaImagen(txtImagen, fc);
        if(!rutaImagenSeleccionada.isEmpty()) {
            miGrilla.getChildren().remove(imgPreview);
            imgPreview = Icono.previsualizar(rutaImagenSeleccionada, 120);
            GridPane.setHalignment(imgPreview, HPos.CENTER);
            miGrilla.add(imgPreview, 1, 9);
        }
    }

    private Boolean formularioValido() {
        if(txtNombre.getText().isBlank()) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "Error", "El nombre es obligatorio.");
            return false;
        }
        if(dtpFecha.getValue() == null) {
            Mensaje.mostrar(Alert.AlertType.WARNING, null, "Error", "La fecha es obligatoria.");
            return false;
        }
        return true;
    }

    private void actualizarCliente() {
        if(formularioValido()) {
            ClienteDto dtoActualizado = new ClienteDto();
            dtoActualizado.setIdCliente(objCliente.getIdCliente());
            dtoActualizado.setNombreCliente(txtNombre.getText());
            dtoActualizado.setTipoDocumentoCliente(cmbTipoDoc.getValue());
            // REMOVIDO: dtoActualizado.setNumeroDocumentoCliente(null);
            dtoActualizado.setFechaDeNacimientoCliente(dtpFecha.getValue());
            dtoActualizado.setSexoCliente(rbMasc.isSelected());
            dtoActualizado.setEsAccesoVipCliente(chkVIP.isSelected() ? "Si" : "No");
            dtoActualizado.setNombreImagenPublicoCliente(txtImagen.getText());
            dtoActualizado.setNombreImagenPrivadoCliente(objCliente.getNombreImagenPrivadoCliente()); // Conservar

            // CORRECCIÓN: Asignar la película seleccionada con verificación
            PeliculaDto peliculaSeleccionada = cmbPelicula.getValue();
            
            // Asignar la película solo si no se seleccionó el objeto por defecto (ID 0)
            if (peliculaSeleccionada != null && peliculaSeleccionada.getIdPelicula() != 0) {
                dtoActualizado.setIdPeliculaCliente(peliculaSeleccionada);
            } else {
                // Asignar NULL si se seleccionó el ítem por defecto o si es null (desasignar)
                dtoActualizado.setIdPeliculaCliente(null); 
            }
            // Fin de Corrección

            if(ClienteControladorEditar.actualizar(posicion, dtoActualizado, rutaImagenSeleccionada)) {
                Mensaje.mostrar(Alert.AlertType.INFORMATION, null, "Éxito", "Cliente actualizado.");
            } else {
                Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", "No se pudo actualizar el cliente.");
            }
            
            regresarAAdministrar();
        }
    }
    
    private void regresarAAdministrar() {
        panelCuerpo = ClienteControladorVista.abrirAdministrar(
                        laVentanaPrincipal,
                        panelPrincipal, panelCuerpo,
                        Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor());
        panelPrincipal.setCenter(null);
        panelPrincipal.setCenter(panelCuerpo);
    }
}