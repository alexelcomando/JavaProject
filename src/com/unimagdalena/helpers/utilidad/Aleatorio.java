
package com.unimagdalena.helpers.utilidad;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Aleatorio {
       public static Integer entero(int inicio, int fin) {
        int valor;
        Random aleatorio = new Random();
        valor = aleatorio.nextInt(fin - inicio + 1) + inicio;
        return valor;
    }

    public static Double doble(Double inicio, Double fin) {
        Double valor;
        Random aleatorio = new Random();
        valor = inicio + (fin - inicio) * aleatorio.nextDouble();
        valor = Math.round(valor * 100.0) / 100.0;
        return valor;
    }

    public static String texto(int cantCaracteres) {
        char caracter;
        String cadenaTexto, diccionario;
        int i, posicion, limiteDiccionario;
        cadenaTexto = "";
        diccionario = "abcdefghijklmnopqrstuvwxyz";
        limiteDiccionario = diccionario.length() - 1;
        for (i = 0; i < cantCaracteres; i++) {
            posicion = entero(0, limiteDiccionario);
            caracter = diccionario.charAt(posicion);
            cadenaTexto = cadenaTexto + caracter;
        }
        return cadenaTexto;
    }

    public static String fecha(Date fechaIni, Date fechaFin) {
        String patronFecha = "yyyy-MM-dd";
        int unDiaMili = 1000 * 60 * 60 * 24;
        SimpleDateFormat miFormato = new SimpleDateFormat(patronFecha);
        long inicioMili = fechaIni.getTime();
        long finMili = fechaFin.getTime() + unDiaMili;
        long aleatorioMill = ThreadLocalRandom.current().nextLong(inicioMili, finMili);
        Date fechaAleatoria = new Date(aleatorioMill);
        return miFormato.format(fechaAleatoria);
    }
}
