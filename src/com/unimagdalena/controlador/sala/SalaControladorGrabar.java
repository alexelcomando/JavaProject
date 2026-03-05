package com.unimagdalena.controlador.sala;

import com.unimagdalena.servicio.SalaServicio;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.helpers.utilidad.Mensaje;
import javafx.scene.control.Alert;

public class SalaControladorGrabar {
    
    private static final SalaServicio SERVICIO = new SalaServicio();
    
    public static boolean crearSala(SalaDto dto, String rutaImagen) {
        boolean exito = SERVICIO.insert(dto, rutaImagen);
        
        if (!exito) {
            Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", 
                    "No se pudo guardar la sala");
        }
        
        return exito;
    }
}