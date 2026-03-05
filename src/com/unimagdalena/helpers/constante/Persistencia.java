package com.unimagdalena.helpers.constante;

import java.io.File;

public class Persistencia {

    public static final String RUTA_PROYECTO = System.getProperty("user.dir");
    public static final String NOMBRE_BASE_DATOS_GENEROS = "generos.txt";
    public static final String NOMBRE_BASE_DATOS_PELICULAS = "peliculas.txt";
    public static final String NOMBRE_BASE_DATOS_SALAS = "salas.txt";
    public static final String NOMBRE_BASE_DATOS_CONFITERIAS = "confiterias.txt";
    public static final String NOMBRE_BASE_DATOS_CLIENTES = "clientes.txt";
    public static final String NOMBRE_BASE_DATOS_EMPLEADOS = "empleados.txt";
    public static final String NOMBRE_BASE_DATOS_TICKETS = "tickets.txt";
    public static final String NOMBRE_BASE_DATOS_FUNCIONES = "funciones.txt";
    public static final String NOMBRE_BASE_DATOS_RESERVAS = "reservas.txt";
    public static final String NOMBRE_CARPETA = "LaBaseDeDatos";
    public static final String NOMBRE_CARPETA_IMAGENES_EXTERNAS = "lasImagenes";
    private final static String RECURSO = "/com/unimagdalena/recurso/";
    public final static String RUTA_ESTILO_BTN_ACERCA = RECURSO + "estilo/BtnAcerca.css";

    public static final String SEPARADOR_COLUMNAS = ";";
    public static final String SEPARADOR_CARPETAS = File.separator;

    public static final String NOMBRE_GENERO = RUTA_PROYECTO
            + SEPARADOR_CARPETAS + NOMBRE_CARPETA
            + SEPARADOR_CARPETAS + NOMBRE_BASE_DATOS_GENEROS;

    public static final String NOMBRE_PELICULA = RUTA_PROYECTO
            + SEPARADOR_CARPETAS + NOMBRE_CARPETA
            + SEPARADOR_CARPETAS + NOMBRE_BASE_DATOS_PELICULAS;

    public static final String NOMBRE_SALA = RUTA_PROYECTO
            + SEPARADOR_CARPETAS + NOMBRE_CARPETA
            + SEPARADOR_CARPETAS + NOMBRE_BASE_DATOS_SALAS;

    public static final String NOMBRE_CONFITERIA = RUTA_PROYECTO
            + SEPARADOR_CARPETAS + NOMBRE_CARPETA
            + SEPARADOR_CARPETAS + NOMBRE_BASE_DATOS_CONFITERIAS;

    public static final String NOMBRE_CLIENTE = RUTA_PROYECTO
            + SEPARADOR_CARPETAS + NOMBRE_CARPETA
            + SEPARADOR_CARPETAS + NOMBRE_BASE_DATOS_CLIENTES;

    public static final String NOMBRE_EMPLEADO = RUTA_PROYECTO
            + SEPARADOR_CARPETAS + NOMBRE_CARPETA
            + SEPARADOR_CARPETAS + NOMBRE_BASE_DATOS_EMPLEADOS;

    public static final String NOMBRE_TICKET = RUTA_PROYECTO
            + SEPARADOR_CARPETAS + NOMBRE_CARPETA
            + SEPARADOR_CARPETAS + NOMBRE_BASE_DATOS_TICKETS;

    public static final String NOMBRE_FUNCIONE = RUTA_PROYECTO
            + SEPARADOR_CARPETAS + NOMBRE_CARPETA
            + SEPARADOR_CARPETAS + NOMBRE_BASE_DATOS_FUNCIONES;

    public static final String NOMBRE_RESERVA = RUTA_PROYECTO
            + SEPARADOR_CARPETAS + NOMBRE_CARPETA
            + SEPARADOR_CARPETAS + NOMBRE_BASE_DATOS_RESERVAS;

    public static final String RUTA_IMAGENES_INTERNAS 
            = "/com/unimagdalena/recurso/imagenes/";
    public static final String RUTA_IMAGENES_EXTERNAS = RUTA_PROYECTO
            + SEPARADOR_CARPETAS + NOMBRE_CARPETA_IMAGENES_EXTERNAS;

}
