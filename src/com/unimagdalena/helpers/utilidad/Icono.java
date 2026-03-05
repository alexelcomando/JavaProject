package com.unimagdalena.helpers.utilidad;

import com.unimagdalena.controlador.gestor.SalidaControlador;
import com.unimagdalena.helpers.constante.Persistencia;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Icono {

    public static ImageView obtenerIcono(String nombreIcono, int alto) {
        String rutaIcono = Persistencia.RUTA_IMAGENES_INTERNAS + nombreIcono;
        InputStream iconoSalirStream = SalidaControlador.class.getResourceAsStream(rutaIcono);

        Image iconoBasico = new Image(iconoSalirStream);
        ImageView iconoMostrar = new ImageView(iconoBasico);
        if (alto != 0) {
            iconoMostrar.setFitHeight(alto);
        }

        iconoMostrar.setPreserveRatio(true);
        iconoMostrar.setSmooth(true);
        return iconoMostrar;
    }

    public static ImageView obtenerFotosExternas(String nombreIcono, int alto) {
        Image imgBasica;
        String iconoSalirRuta;
        ImageView imgMostrar = null;

        iconoSalirRuta = Persistencia.RUTA_IMAGENES_EXTERNAS + Persistencia.SEPARADOR_CARPETAS + nombreIcono;
        try (FileInputStream archivo = new FileInputStream(iconoSalirRuta)) {
            imgBasica = new Image(archivo);
            imgMostrar = new ImageView(imgBasica);
            if (alto != 0) {
                imgMostrar.setFitHeight(alto);
            }

            imgMostrar.setPreserveRatio(true);
            imgMostrar.setSmooth(true);
        } catch (IOException miError) {
            System.out.println("Error al cargar la foto externa: " + miError.getMessage());
        }

        return imgMostrar;
    }

    public static ImageView previsualizar(String rutaImagen, int alto) {
        Image imgBasica;
        ImageView imgMostrar = null;

        try (FileInputStream archivo = new FileInputStream(rutaImagen)) {
            imgBasica = new Image(archivo);
            imgMostrar = new ImageView(imgBasica);
            if (alto != 0) {
                imgMostrar.setFitHeight(alto);
            }
            imgMostrar.setPreserveRatio(true);
            imgMostrar.setSmooth(true);

        } catch (IOException miError) {
            System.out.println("Error al cargar la foto externa: " + miError.getMessage());
        }

        return imgMostrar;
    }
}
