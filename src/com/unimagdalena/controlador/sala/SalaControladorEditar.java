package com.unimagdalena.controlador.sala;

import com.unimagdalena.servicio.SalaServicio;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.vista.sala.VistaSalaEditar;
import com.unimagdalena.helpers.utilidad.Mensaje;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.List;

public class SalaControladorEditar {
    
    private static final SalaServicio SERVICIO = new SalaServicio();
    
    public static StackPane abrirEditar(Stage escenario, BorderPane panelPrincipal, 
                                        Pane panelAnterior, SalaDto dtoSeleccionado) {
        VistaSalaEditar vistaEditar = new VistaSalaEditar(
                escenario, panelPrincipal, panelAnterior, dtoSeleccionado
        );
        return vistaEditar;
    }
    
    public static boolean actualizar(SalaDto dto, String rutaImg) {
        List<SalaDto> salas = SERVICIO.SelectFrom();
        int indice = -1;
        
        for (int i = 0; i < salas.size(); i++) {
            if (salas.get(i).getIdSala() == dto.getIdSala()) {
                indice = i;
                break;
            }
        }
        
        if (indice == -1) {
            Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", 
                    "No se encontró la sala a actualizar");
            return false;
        }
        
        boolean exito = SERVICIO.updateSet(indice, dto, rutaImg);
        
        if (!exito) {
            Mensaje.mostrar(Alert.AlertType.ERROR, null, "Error", 
                    "No se pudo actualizar la sala");
        }
        
        return exito;
    }
    
    public static SalaDto getOne(int codigo) {
        return SERVICIO.getOne(codigo);
    }
}