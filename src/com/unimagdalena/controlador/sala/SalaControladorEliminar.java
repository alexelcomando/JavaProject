package com.unimagdalena.controlador.sala;

import com.unimagdalena.servicio.SalaServicio;
import com.unimagdalena.helpers.utilidad.Mensaje;
import javafx.scene.control.Alert;

public class SalaControladorEliminar {
    
    private static final SalaServicio SERVICIO = new SalaServicio();
    
    public static boolean borrar(int idSala) {
        boolean exito = SERVICIO.delete(idSala);
        
        if (!exito) {
            Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", 
                    "No se pudo eliminar la sala");
        }
        
        return exito;
    }
}