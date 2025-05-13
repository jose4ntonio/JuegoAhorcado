package Hilos;

import javax.swing.*;
import java.util.function.Consumer;

public class Temporizador extends Thread {
    private final JLabel lblTiempo; // Etiqueta para mostrar tiempo
    private final Consumer<Boolean> callback; // Callback para fin de tiempo
    private volatile boolean activo = true; // Controla ejecución del hilo
    private int segundos = 60; // Tiempo inicial

    // Hilo: Configura temporizador
    public Temporizador(JLabel lblTiempo, Consumer<Boolean> callback) {
        this.lblTiempo = lblTiempo; // Asigna etiqueta
        this.callback = callback; // Asigna callback
    }

    // Hilo: Ejecuta conteo regresivo
    @Override
    public void run() {
        while (activo && segundos > 0) {
            // Actualiza tiempo en la GUI
            SwingUtilities.invokeLater(() -> lblTiempo.setText("Tiempo: " + segundos));
            try {
                Thread.sleep(1000); // Espera 1 segundo
            } catch (InterruptedException e) {
                e.printStackTrace(); // Maneja error
            }
            segundos--; // Reduce tiempo
        }
        if (activo) {
            // Ejecuta callback si tiempo se agota
            SwingUtilities.invokeLater(() -> callback.accept(false));
        }
    }

    // Hilo: Detiene el temporizador
    public void detener() {
        activo = false; // Desactiva hilo
    }
}