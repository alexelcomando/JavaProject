
package com.unimagdalena.helpers.utilidad;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Componente de switch deslizable personalizado para JavaFX
 * Permite alternar entre dos estados (Sí/No) con animación visual
 */
public class SlideSwitch extends StackPane {
    
    private boolean isOn = true;
    private final Circle knob;
    private final Label label;
    private final HBox background;
    
    // Estilos personalizables
    private String estiloEncendido = "-fx-background-radius: 25; -fx-background-color: linear-gradient(to right, lightgreen, green);";
    private String estiloApagado = "-fx-background-radius: 25; -fx-background-color: linear-gradient(to right, #ff4d4d, darkred);";
    private String textoEncendido = "Sí";
    private String textoApagado = "No";
    
    /**
     * Constructor por defecto con tamaño estándar
     */
    public SlideSwitch() {
        this(200, 50);
    }
    
    /**
     * Constructor con tamaño personalizado
     * @param ancho Ancho del switch
     * @param alto Alto del switch
     */
    public SlideSwitch(double ancho, double alto) {
        setPrefSize(ancho, alto);
        
        // Configurar fondo
        background = new HBox();
        background.setPrefSize(ancho, alto);
        background.setStyle(estiloEncendido);
        background.setAlignment(Pos.CENTER);
        
        // Configurar etiqueta
        label = new Label(textoEncendido);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        // Configurar perilla (knob)
        double radioKnob = alto / 2.5;
        knob = new Circle(radioKnob, Color.WHITE);
        knob.setStroke(Color.GRAY);
        knob.setStrokeWidth(2);
        
        // Posición inicial (encendido = izquierda)
        double desplazamiento = (ancho / 2) - radioKnob - 10;
        knob.setTranslateX(-desplazamiento);
        
        getChildren().addAll(background, label, knob);
        
        configurarEventos(desplazamiento);
    }
    
    private void configurarEventos(double desplazamiento) {
        // Evento de arrastre
        knob.setOnMousePressed(e -> {
            double x = e.getX();
            if (x > -desplazamiento && x < desplazamiento) {
                knob.setTranslateX(x);
            }
        });
        
        // Evento al soltar
        knob.setOnMouseReleased(e -> {
            if (knob.getTranslateX() > 0) {
                apagar(desplazamiento);
            } else {
                encender(desplazamiento);
            }
        });
        
        // Evento de clic directo en el fondo
        background.setOnMouseClicked(e -> toggle(desplazamiento));
    }
    
    /**
     * Enciende el switch (estado = true)
     */
    private void encender(double desplazamiento) {
        isOn = true;
        knob.setTranslateX(-desplazamiento);
        background.setStyle(estiloEncendido);
        label.setText(textoEncendido);
    }
    
    /**
     * Apaga el switch (estado = false)
     */
    private void apagar(double desplazamiento) {
        isOn = false;
        knob.setTranslateX(desplazamiento);
        background.setStyle(estiloApagado);
        label.setText(textoApagado);
    }
    
    /**
     * Alterna entre encendido y apagado
     * @param desplazamiento
     */
    public void toggle(double desplazamiento) {
        if (isOn) {
            apagar(desplazamiento);
        } else {
            encender(desplazamiento);
        }
    }
    
    /**
     * Retorna el estado actual del switch
     * @return true si está encendido, false si está apagado
     */
    public boolean isOn() {
        return isOn;
    }
    
    /**
     * Establece el estado del switch programáticamente
     * @param estado true para encender, false para apagar
     */
    public void setEstado(boolean estado) {
        double desplazamiento = (getPrefWidth() / 2) - knob.getRadius() - 10;
        if (estado) {
            encender(desplazamiento);
        } else {
            apagar(desplazamiento);
        }
    }
    
    /**
     * Reinicia el switch a su estado inicial (encendido)
     */
    public void reset() {
        double desplazamiento = (getPrefWidth() / 2) - knob.getRadius() - 10;
        encender(desplazamiento);
    }
    
    /**
     * Personaliza los estilos del switch
     * @param estiloOn Estilo CSS cuando está encendido
     * @param estiloOff Estilo CSS cuando está apagado
     */
    public void setEstilos(String estiloOn, String estiloOff) {
        this.estiloEncendido = estiloOn;
        this.estiloApagado = estiloOff;
        background.setStyle(isOn ? estiloEncendido : estiloApagado);
    }
    
    /**
     * Personaliza los textos del switch
     * @param textoOn Texto cuando está encendido
     * @param textoOff Texto cuando está apagado
     */
    public void setTextos(String textoOn, String textoOff) {
        this.textoEncendido = textoOn;
        this.textoApagado = textoOff;
        label.setText(isOn ? textoEncendido : textoApagado);
    }
}