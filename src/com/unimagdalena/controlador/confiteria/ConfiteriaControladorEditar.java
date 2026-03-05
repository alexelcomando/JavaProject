package com.unimagdalena.controlador.confiteria;

import com.unimagdalena.servicio.ConfiteriaServicio;
import com.unimagdalena.dto.ConfiteriaDto;
import com.unimagdalena.helpers.utilidad.Mensaje;
import com.unimagdalena.vista.confiteria.VistaConfiteriaEditar;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ConfiteriaControladorEditar {

    private static final ConfiteriaServicio SERVICIO = new ConfiteriaServicio();

    /**
     * Abre la vista de edición de confitería
     *
     * @param escenario       Stage principal
     * @param dtoSeleccionado ConfiteriaDto a editar
     * @return panel StackPane con la vista de edición
     */
public static StackPane abrirEditar(Stage escenario,
                                   BorderPane panelPrincipal,
                                   Pane panelAnterior,
                                   ConfiteriaDto dtoSeleccionado) {

  VistaConfiteriaEditar vistaEditar = new VistaConfiteriaEditar(
        escenario,
        panelPrincipal,
        panelAnterior,
        dtoSeleccionado
);


    return vistaEditar;
}




    /**
     * Actualiza el objeto en el archivo usando ConfiteriaServicio
     *
     * @param dto     ConfiteriaDto con los nuevos datos
     * @param rutaImg ruta de la nueva imagen (si se seleccionó)
     * @return true si se actualizó correctamente
     */
    public static boolean actualizar(ConfiteriaDto dto, String rutaImg) {
        boolean exito = SERVICIO.updateSet(dto.getIdConfiteria(), dto, rutaImg);

        if (!exito) {
            Mensaje.mostrar(Alert.AlertType.ERROR,
                    null,
                    "Error",
                    "No se pudo actualizar la confitería");
        }

        return exito;
    }

    /**
     * Obtiene un ConfiteriaDto por código
     *
     * @param codigo ID del producto
     * @return ConfiteriaDto o null
     */
    public static ConfiteriaDto getOne(int codigo) {
        return SERVICIO.getOne(codigo);
    }
}
