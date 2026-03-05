package com.unimagdalena.vista.gestor;

import com.unimagdalena.controlador.cliente.ClienteControladorVista;
import com.unimagdalena.controlador.confiteria.ConfiteriaControladorVista;
import com.unimagdalena.controlador.empleado.EmpleadoControladorVista;
import com.unimagdalena.controlador.funcion.FuncionControladorVista;
import com.unimagdalena.controlador.genero.GeneroControladorVista;
import com.unimagdalena.controlador.gestor.SalidaControlador;
import com.unimagdalena.controlador.pelicula.PeliculaControladorVista;
import com.unimagdalena.controlador.reserva.ReservaControladorVista;
import com.unimagdalena.controlador.sala.SalaControladorListar;
import com.unimagdalena.controlador.sala.SalaControladorVista;
import com.unimagdalena.controlador.ticket.TicketControladorListar;
import com.unimagdalena.controlador.ticket.TicketControladorVista;
import com.unimagdalena.helpers.constante.Configuracion;
import com.unimagdalena.helpers.constante.Contenedor;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.Icono;
import com.unimagdalena.helpers.utilidad.Mensaje;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class VistaCabecera extends SubScene {

    private final int MENU_ANCHO = 200;
    private final int MENU_ALTO = 35;
    private final int MENU_ESPACIO_X = 20;

    private Pane miPanelCuerpo;
    private final Stage miEscenario;
    private final HBox miPanelCabecera;
    private final BorderPane miPanelPrincipal;

    public VistaCabecera(
            Stage esce,
            BorderPane prin,
            Pane pane,
            double anchoPanel,
            double altoCabecera) {
        //Inicio constructor
        super(new HBox(), anchoPanel, altoCabecera);
        miPanelCabecera = (HBox) this.getRoot();
        miPanelCabecera.setAlignment(Pos.CENTER_LEFT);

        miPanelPrincipal = prin;
        miPanelCuerpo = pane;
        miEscenario = esce;

        miPanelCabecera.setSpacing(MENU_ESPACIO_X);
        miPanelCabecera.setPadding(new Insets(0, 30, 0, 30));
        miPanelCabecera.setPrefHeight(Contenedor.ALTO_CABECERA.getValor());
        miPanelCabecera.setStyle(Configuracion.CABECERA_COLOR_FONDO);

        crearBotones();
    }

    public HBox getMiPanelCabecera() {
        return miPanelCabecera;
    }

    private void agregaralmenu(MenuButton menu) {
        menu.setCursor(Cursor.HAND);
        menu.setPrefWidth(MENU_ANCHO);

        miPanelCabecera.getChildren().add(menu);
    }

    private void crearBotones() {
        menuGenero();
        menuPelicula();
        menuSala();
        menuFuncion();
        menuCliente();
        menuReserva();
        menuConfiteria();
        menuTicket();
        menuEmpleado();
        btnSalir();
        btnAcerca(500, 650);
    }

    private void menuGenero() {
        MenuItem opcion01 = new MenuItem("Crear Género");
        MenuItem opcion02 = new MenuItem("Listar Géneros");
        MenuItem opcion03 = new MenuItem("Administrar Géneros");
        MenuItem opcion04 = new MenuItem("Carrusel");

        opcion01.setOnAction((evento) -> {
            miPanelPrincipal.setCenter(
                    GeneroControladorVista.abrirCrear(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion02.setOnAction((e) -> {
            miPanelPrincipal.setCenter(
                    GeneroControladorVista.abrirListar(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion03.setOnAction((e) -> {
            miPanelPrincipal.setCenter(GeneroControladorVista.abrirAdministrar(miEscenario, miPanelPrincipal, miPanelCuerpo, Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor()));
        });
        opcion04.setOnAction((e) -> {
            int posicioninicial = 0;
            miPanelPrincipal.setCenter(GeneroControladorVista.carrusel(miEscenario, miPanelPrincipal, miPanelCuerpo, Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(), posicioninicial));
        });

        MenuButton menuBotones = new MenuButton("Géneros");
        menuBotones.setPrefWidth(MENU_ANCHO);
        menuBotones.setPrefWidth(MENU_ALTO);
        menuBotones.setGraphic(Icono.obtenerIcono("iconoGenero.png", 25));
        menuBotones.getItems().addAll(opcion01, opcion02, opcion03, opcion04);
        agregaralmenu(menuBotones);
    }

    private void menuPelicula() {
        MenuItem opcion01 = new MenuItem("Crear Pelicula");
        MenuItem opcion02 = new MenuItem("Listar Peliculas");
        MenuItem opcion03 = new MenuItem("Administras Peliculas");
        MenuItem opcion04 = new MenuItem("Carrusel");

        opcion01.setOnAction((evento) -> {
            miPanelPrincipal.setCenter(PeliculaControladorVista.abrirCrear(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion02.setOnAction((e) -> {
            miPanelPrincipal.setCenter(PeliculaControladorVista.abrirListar(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
            ;
        });
        opcion03.setOnAction((e) -> {
            miPanelPrincipal.setCenter(PeliculaControladorVista.abrirAdministrar(miEscenario, miPanelPrincipal, miPanelCuerpo, Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor()));
        });
        opcion04.setOnAction((e) -> {
            int posicioninicial = 0;
            miPanelPrincipal.setCenter(PeliculaControladorVista.carrusel(miEscenario, miPanelPrincipal, miPanelCuerpo, Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(), posicioninicial));
        });

        MenuButton menuBotones = new MenuButton("Peliculas");
        menuBotones.setPrefWidth(MENU_ANCHO);
        menuBotones.setPrefWidth(MENU_ALTO);
        menuBotones.setGraphic(Icono.obtenerIcono("iconoPelicula.png", 25));
        menuBotones.getItems().addAll(opcion01, opcion02, opcion03, opcion04);
        agregaralmenu(menuBotones);

    }

    private void menuSala() {
        MenuItem opcion01 = new MenuItem("Crear Sala");
        MenuItem opcion02 = new MenuItem("Listar salas");
        MenuItem opcion03 = new MenuItem("Administrar salas");
        MenuItem opcion04 = new MenuItem("Carrusel");

        opcion01.setOnAction((evento) -> {
            miPanelPrincipal.setCenter(
                    SalaControladorVista.abrirCrear(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion02.setOnAction((e) -> {
            miPanelPrincipal.setCenter(
                    SalaControladorVista.abrirListar(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion03.setOnAction((e) -> {
            miPanelPrincipal.setCenter(
                    SalaControladorVista.abrirAdministrar(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP)
            );
        });
        opcion04.setOnAction((e) -> {

            int cantidadSalas = SalaControladorListar.cantidadSalas();

            if (cantidadSalas == 0) {
                Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario,
                        "Sin salas", "No hay salas para mostrar en el carrusel");
            } else {
                miPanelPrincipal.setCenter(
                        SalaControladorVista.abrirCarrusel(
                                miEscenario,
                                miPanelPrincipal,
                                null,
                                0,
                                Configuracion.ANCHO_APP,
                                Contenedor.ALTO_CUERPO.getValor()
                        )
                );
            }
        });

        MenuButton menuBotones = new MenuButton("Salas");
        menuBotones.setPrefWidth(MENU_ANCHO);
        menuBotones.setPrefWidth(MENU_ALTO);
        menuBotones.setGraphic(Icono.obtenerIcono("iconoSala.jpeg", 25));
        menuBotones.getItems().addAll(opcion01, opcion02, opcion03, opcion04);
        agregaralmenu(menuBotones);

    }

    private void menuCliente() {
        MenuItem opcion01 = new MenuItem("Resgistrar Cliente");
        MenuItem opcion02 = new MenuItem("Listar Clientes");
        MenuItem opcion03 = new MenuItem("Administrar Clientes");
        MenuItem opcion04 = new MenuItem("Carrusel");

        opcion01.setOnAction((evento) -> {
            miPanelPrincipal.setCenter(ClienteControladorVista.abrirCrear(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion02.setOnAction((e) -> {
            miPanelPrincipal.setCenter(ClienteControladorVista.abrirListar(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion03.setOnAction((e) -> {
            miPanelPrincipal.setCenter(ClienteControladorVista.abrirAdministrar(
                    miEscenario, miPanelPrincipal, miPanelCuerpo, Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor()));
        });
        opcion04.setOnAction((e) -> {
            int posicioninicial = 0;
            miPanelPrincipal.setCenter(ClienteControladorVista.carrusel(
                    miEscenario, miPanelPrincipal, miPanelCuerpo, Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(), posicioninicial));
        });

        MenuButton menuBotones = new MenuButton("Cliente");
        menuBotones.setPrefWidth(MENU_ANCHO);
        menuBotones.setPrefWidth(MENU_ALTO);
        menuBotones.setGraphic(Icono.obtenerIcono("iconoCliente.png", 25));
        menuBotones.getItems().addAll(opcion01, opcion02, opcion03, opcion04);
        agregaralmenu(menuBotones);
    }

    private void menuReserva() {
        MenuItem opcion01 = new MenuItem("Crear Reserva");
        MenuItem opcion02 = new MenuItem("Listar Reserva");
        MenuItem opcion03 = new MenuItem("Administrar Reserva");
        MenuItem opcion04 = new MenuItem("Carrusel Reserva");

        opcion01.setOnAction((evento) -> {
            miPanelPrincipal.setCenter(ReservaControladorVista.abrirCrear(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion02.setOnAction((e) -> {
            miPanelPrincipal.setCenter(ReservaControladorVista.abrirListar(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion03.setOnAction((e) -> {
            miPanelPrincipal.setCenter(ReservaControladorVista.abrirAdministrar(miEscenario, miPanelPrincipal, miPanelCuerpo, Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor()));
        });
        opcion04.setOnAction((e) -> {
            int posicioninicial = 0;
            miPanelPrincipal.setCenter(ReservaControladorVista.carrusel(miEscenario, miPanelPrincipal, miPanelCuerpo, Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(), posicioninicial));
        });

        MenuButton menuBotones = new MenuButton("Reserva");
        menuBotones.setPrefWidth(MENU_ANCHO);
        menuBotones.setPrefWidth(MENU_ALTO);
        menuBotones.setGraphic(Icono.obtenerIcono("iconoReserva.png", 25));
        menuBotones.getItems().addAll(opcion01, opcion02, opcion03, opcion04);
        agregaralmenu(menuBotones);
    }

    private void menuConfiteria() {
        MenuItem opcion01 = new MenuItem("Crear Producto");
        MenuItem opcion02 = new MenuItem("Listar Productos");
        MenuItem opcion03 = new MenuItem("Administrar Productos");
        MenuItem opcion04 = new MenuItem("Carrusel");

        opcion01.setOnAction((evento) -> {
            miPanelPrincipal.setCenter(ConfiteriaControladorVista.abrirCrear(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion02.setOnAction((e) -> {
            miPanelPrincipal.setCenter(ConfiteriaControladorVista.abrirListar(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion03.setOnAction((e) -> {
            miPanelPrincipal.setCenter(ConfiteriaControladorVista.abrirAdministar(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion04.setOnAction((e) -> {
            int posicioninicial = 0;
            miPanelPrincipal.setCenter(ConfiteriaControladorVista.abrirCarrusel(miEscenario, miPanelPrincipal, miPanelCuerpo, posicioninicial, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });

        MenuButton menuBotones = new MenuButton("Confitería");
        menuBotones.setPrefWidth(MENU_ANCHO);
        menuBotones.setPrefWidth(MENU_ALTO);
        menuBotones.setGraphic(Icono.obtenerIcono("iconoConfiteria.jpeg", 25));
        menuBotones.getItems().addAll(opcion01, opcion02, opcion03, opcion04);
        agregaralmenu(menuBotones);
    }

    private void menuTicket() {
        MenuItem opcion01 = new MenuItem("Resgistrar Ticket");
        MenuItem opcion02 = new MenuItem("Listar Tickets");
        MenuItem opcion03 = new MenuItem("Administrar Tickets");
        MenuItem opcion04 = new MenuItem("Carrusel Tickets");

        opcion01.setOnAction((evento) -> {
            miPanelPrincipal.setCenter(
                    TicketControladorVista.abrirCrear(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP)
            );
        });

        opcion02.setOnAction((e) -> {
            miPanelPrincipal.setCenter(
                    TicketControladorVista.abrirListar(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP)
            );
        });

        opcion03.setOnAction((e) -> {
            miPanelPrincipal.setCenter(
                    TicketControladorVista.abrirAdministrar(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP)
            );
        });

        opcion04.setOnAction((e) -> {
            // Verificar si hay tickets antes de abrir el carrusel
            int cantidadTickets = TicketControladorListar.cantidadTickets();

            if (cantidadTickets == 0) {
                Mensaje.mostrar(Alert.AlertType.WARNING, miEscenario,
                        "Sin tickets", "No hay tickets para mostrar en el carrusel");
            } else {
                miPanelPrincipal.setCenter(
                        TicketControladorVista.abrirCarrusel(
                                miEscenario,
                                miPanelPrincipal,
                                null, // panelAnterior es null porque viene del menú
                                0, // índice inicial (primer ticket)
                                Configuracion.ANCHO_APP,
                                Contenedor.ALTO_CUERPO.getValor()
                        )
                );
            }
        });

        MenuButton menuBotones = new MenuButton("Tickets");
        menuBotones.setPrefWidth(MENU_ANCHO);
        menuBotones.setPrefWidth(MENU_ALTO);
        menuBotones.setGraphic(Icono.obtenerIcono("iconoTicket.png", 25));
        menuBotones.getItems().addAll(opcion01, opcion02, opcion03, opcion04);
        agregaralmenu(menuBotones);
    }

    private void menuEmpleado() {
        MenuItem opcion01 = new MenuItem("Registrar Empleado");
        MenuItem opcion02 = new MenuItem("Listar Empleados");
        MenuItem opcion03 = new MenuItem("Administrar Empleados");
        MenuItem opcion04 = new MenuItem("Carrusel");

        opcion01.setOnAction((evento) -> {
            miPanelPrincipal.setCenter(EmpleadoControladorVista.abrirCrear(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion02.setOnAction((e) -> {
            miPanelPrincipal.setCenter(EmpleadoControladorVista.abrirListar(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
            ;
        });
        opcion03.setOnAction((e) -> {
            miPanelPrincipal.setCenter(EmpleadoControladorVista.abrirAdministrar(miEscenario, miPanelPrincipal, miPanelCuerpo, Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor()));
        });
        opcion04.setOnAction((e) -> {
            int posicioninicial = 0;
            miPanelPrincipal.setCenter(EmpleadoControladorVista.carrusel(miEscenario, miPanelPrincipal, miPanelCuerpo, Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(), posicioninicial));
        });

        MenuButton menuBotones = new MenuButton("Empleados");
        menuBotones.setPrefWidth(MENU_ANCHO);
        menuBotones.setPrefWidth(MENU_ALTO);
        menuBotones.setGraphic(Icono.obtenerIcono("iconoEmpleado.png", 25));
        menuBotones.getItems().addAll(opcion01, opcion02, opcion03, opcion04);
        agregaralmenu(menuBotones);
    }

    private void menuFuncion() {
        MenuItem opcion01 = new MenuItem("Crear Funcion");
        MenuItem opcion02 = new MenuItem("Listar Funcion");
        MenuItem opcion03 = new MenuItem("Administrar Funcion");
        MenuItem opcion04 = new MenuItem("Carrusel");

        opcion01.setOnAction((evento) -> {
            miPanelPrincipal.setCenter(FuncionControladorVista.abrirCrear(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
        });
        opcion02.setOnAction((e) -> {
            miPanelPrincipal.setCenter(FuncionControladorVista.abrirListar(miEscenario, Configuracion.ANCHO_APP, Configuracion.ALTO_APP));
            ;
        });
        opcion03.setOnAction((e) -> {
            miPanelPrincipal.setCenter(FuncionControladorVista.abrirAdministrar(miEscenario, miPanelPrincipal, miPanelCuerpo, Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor()));
        });
        opcion04.setOnAction((e) -> {
            int posicioninicial = 0;
            miPanelPrincipal.setCenter(FuncionControladorVista.carrusel(miEscenario, miPanelPrincipal, miPanelCuerpo, Configuracion.ANCHO_APP, Contenedor.ALTO_CUERPO.getValor(), posicioninicial));
        });

        MenuButton menuBotones = new MenuButton("Funciones");
        menuBotones.setPrefWidth(MENU_ANCHO);
        menuBotones.setPrefWidth(MENU_ALTO);
        menuBotones.setGraphic(Icono.obtenerIcono("iconoFuncion.png", 25));
        menuBotones.getItems().addAll(opcion01, opcion02, opcion03, opcion04);
        agregaralmenu(menuBotones);
    }

    private void btnSalir() {
        Button btnSalir = new Button("Salir");
        btnSalir.setCursor(Cursor.HAND);
        btnSalir.setPrefWidth(MENU_ANCHO);
        btnSalir.setPrefHeight(MENU_ALTO);
        btnSalir.setGraphic(Icono.obtenerIcono("iconoSalir.png", 25));

        btnSalir.setOnAction((ActionEvent event) -> {
            event.consume();
            SalidaControlador.verificar(miEscenario);
        });

        miPanelCabecera.getChildren().add(btnSalir);
    }

    private void btnAcerca(double anchoFlotante, double altoFlotante) {
        Button botonAyuda = new Button("?");
        botonAyuda.setOnAction((ActionEvent event) -> {
            VistaAcerca.mostrar(miEscenario, anchoFlotante, altoFlotante);
        });

        botonAyuda.setPrefWidth(30);
        botonAyuda.setId("btn-ayuda");
        botonAyuda.setCursor(Cursor.HAND);
        botonAyuda.getStylesheets().
                add(getClass().getResource(Persistencia.RUTA_ESTILO_BTN_ACERCA).toExternalForm());

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);

        miPanelCabecera.getChildren().add(espacio);
        miPanelCabecera.getChildren().add(botonAyuda);
    }

}
