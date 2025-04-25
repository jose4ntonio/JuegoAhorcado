
import GUI.JuegoAhorcado;
import javax.swing.SwingUtilities;

public class IniciarJuego {

    public static void main(String[] args) {
        // Ejecutar la interfaz gráfica en el hilo de despacho de eventos
        SwingUtilities.invokeLater(() -> {
            JuegoAhorcado juego = new JuegoAhorcado();
            juego.setVisible(true);
        });
    }
}
