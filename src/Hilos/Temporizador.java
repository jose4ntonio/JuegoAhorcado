package Hilos;

import javax.swing.*;
import java.util.function.Consumer;

public class Temporizador extends Thread {

    private final JLabel lblTiempo;
    private final Consumer<Boolean> callback;
    private volatile boolean activo = true;
    private int segundos = 60;

    public Temporizador(JLabel lblTiempo, Consumer<Boolean> callback) {
        this.lblTiempo = lblTiempo;
        this.callback = callback;
    }

    @Override
    public void run() {
        while (activo && segundos > 0) {
            SwingUtilities.invokeLater(() -> lblTiempo.setText("Tiempo: " + segundos));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            segundos--;
        }
        if (activo) {
            SwingUtilities.invokeLater(() -> callback.accept(false));
        }
    }

    public void detener() {
        activo = false;
    }
}
