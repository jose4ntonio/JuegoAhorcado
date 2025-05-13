package GUI;

import Hilos.Temporizador;
import Logica.Palabras;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class JuegoAhorcado extends JFrame {

    private static final int WIDTH = 350; // Ancho de la ventana
    private static final int HEIGHT = 450; // Alto de la ventana
    private String palabraSecreta; // Palabra a adivinar
    private StringBuilder palabraOculta; // Palabra con guiones
    private int intentosFallidos = 0; // Contador de errores
    private final int MAX_INTENTOS = 9; // Máximo de intentos
    private JLabel lblPalabra, lblTiempo, lblMensaje; // Etiquetas de la GUI
    private JTextField txtLetra; // Campo para ingresar letras
    private Temporizador temporizador; // Hilo del temporizador
    private boolean juegoActivo = false; // Estado del juego

// ------ GUI: Configura la ventana principal del juego
    public JuegoAhorcado() {
        setTitle("Juego del Ahorcado"); // Título de la ventana
        setSize(WIDTH, HEIGHT); // Tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la app al cerrar ventana
        setLocationRelativeTo(null); // Centra la ventana
        setLayout(new BorderLayout()); // Organiza componentes

        // Muestra mensaje inicial con instrucciones
        mostrarMensajeBienvenida();

        // Listener para mensaje al cerrar ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(
                        JuegoAhorcado.this,
                        "¡Gracias por jugar al Juego del Ahorcado!",
                        "Adiós",
                        JOptionPane.INFORMATION_MESSAGE
                ); // Mensaje de despedida
            }
        });

// ------ GUI: Panel para dibujar el ahorcado
        JPanel panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarAhorcado(g); // Dibuja el ahorcado según intentos
            }
        };
        panelDibujo.setPreferredSize(new Dimension(WIDTH, 200)); // Tamaño del panel
        panelDibujo.setBackground(new Color(135, 206, 235)); // Fondo azul cielo

        // GUI: Panel central con palabra, tiempo, mensaje y entrada
        JPanel panelCentral = new JPanel(new GridLayout(4, 1));
        panelCentral.setBackground(new Color(144, 238, 144)); // Fondo verde suave
        lblPalabra = new JLabel("", SwingConstants.CENTER); // Muestra palabra oculta
        lblPalabra.setFont(new Font("Arial", Font.BOLD, 24));
        lblPalabra.setForeground(Color.BLACK);
        lblTiempo = new JLabel("Tiempo: 60", SwingConstants.CENTER); // Muestra tiempo
        lblTiempo.setForeground(Color.BLACK);
        lblMensaje = new JLabel("Ingresa una letra para comenzar", SwingConstants.CENTER); // Muestra mensajes
        lblMensaje.setForeground(Color.BLACK);
        txtLetra = new JTextField(1); // Campo para una letra
        txtLetra.setHorizontalAlignment(JTextField.CENTER);

        panelCentral.add(lblPalabra);
        panelCentral.add(lblTiempo);
        panelCentral.add(lblMensaje);
        panelCentral.add(txtLetra);

// ------ GUI: Panel con botón de iniciar
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(255, 255, 224)); // Fondo amarillo claro
        JButton btnIniciar = new JButton("Iniciar/Reiniciar"); // Botón para empezar
        btnIniciar.setBackground(Color.WHITE);
        panelBotones.add(btnIniciar);

    // ------  Expresión lambda: Inicia el juego al hacer clic
        btnIniciar.addActionListener(e -> iniciarJuego());

    // ------ Expresión lambda: Procesa letra al presionar Enter
        txtLetra.addActionListener(e -> procesarLetra());

        add(panelDibujo, BorderLayout.NORTH); // Panel dibujo arriba
        add(panelCentral, BorderLayout.CENTER); // Panel central en el medio
        add(panelBotones, BorderLayout.SOUTH); // Botones abajo
    }

// ------ GUI: Muestra diálogo de bienvenida con animación
    private void mostrarMensajeBienvenida() {
        // Crea ventana de bienvenida
        JDialog dialog = new JDialog(this, "Bienvenido", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(430, 490); // Tamaño del diálogo
        dialog.setLocationRelativeTo(this); // Centra el diálogo

        // Panel para animación de imágenes
        JPanel panelAnimacion = new JPanel();
        panelAnimacion.setBackground(new Color(255, 255, 255, 0)); // Fondo transparente

        // Carga imágenes
        ImageIcon imagen1 = new ImageIcon("itson.png");
        ImageIcon imagen2 = new ImageIcon("itson2.png");

        // Escala imágenes
        Image img1 = imagen1.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        Image img2 = imagen2.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon imagenEscalada1 = new ImageIcon(img1);
        ImageIcon imagenEscalada2 = new ImageIcon(img2);

        JLabel labelAnimacion = new JLabel(imagenEscalada1); // Muestra imagen inicial
        panelAnimacion.add(labelAnimacion);

    // ------ Expresión lambda: Alterna imágenes cada segundo
        Timer timer = new Timer(1000, e -> {
            if (labelAnimacion.getIcon() == imagenEscalada1) {
                labelAnimacion.setIcon(imagenEscalada2); // Cambia a imagen 2
            } else {
                labelAnimacion.setIcon(imagenEscalada1); // Cambia a imagen 1
            }
        });
        timer.start();

        // Texto de bienvenida
        JTextArea textoBienvenida = new JTextArea();
        textoBienvenida.setText("""
            ¡Bienvenido al Juego del Ahorcado!
            
            Instrucciones:
            - Adivina la palabra secreta ingresando una letra a la vez.
            - Tienes 9 intentos antes de que el muñeco se complete.
            - Dispones de 60 segundos por partida.
            - Solo se aceptan letras de la 'a' a la 'z'.
            - El juego usa palabras de 'palabras.txt', que puedes editar.
            - Haz clic en 'Iniciar/Reiniciar' para comenzar.
            
            ¡Buena suerte! :)
                         
            Hecho por:
                       José Antonio González Valle - 235621.
                       Manuel Donato Hernandez Burgos - 181539.
                       Luis Francisco Salido Varela - 187523.
                       
            """);
        textoBienvenida.setEditable(false); // No editable
        textoBienvenida.setFont(new Font("Arial", Font.BOLD, 12));

    // ------ Expresión lambda: Cierra diálogo al hacer clic en OK
        JButton btnOk = new JButton("OK");
        btnOk.addActionListener(e -> {
            timer.stop(); // Detiene animación
            dialog.dispose(); // Cierra diálogo
        });

        dialog.add(panelAnimacion, BorderLayout.NORTH); // Animación arriba
        dialog.add(new JScrollPane(textoBienvenida), BorderLayout.CENTER); // Texto en el centro
        dialog.add(btnOk, BorderLayout.SOUTH); // Botón abajo

        dialog.setVisible(true); // Muestra diálogo
    }

    // Inicia una nueva partida
    private void iniciarJuego() {
        palabraSecreta = Palabras.obtenerPalabraAleatoria(); // Obtiene palabra
        palabraOculta = new StringBuilder("_".repeat(palabraSecreta.length())); // Crea guiones
        intentosFallidos = 0; // Reinicia intentos
        juegoActivo = true; // Activa el juego
        lblPalabra.setText(palabraOculta.toString()); // Muestra guiones
        lblMensaje.setText("Ingresa una letra"); // Mensaje inicial
        txtLetra.setText(""); // Limpia campo
        txtLetra.setEnabled(true); // Activa campo
        if (temporizador != null) {
            temporizador.detener(); // Detiene temporizador anterior
        }
// ------Hilo: Inicia temporizador
// ------Expresión lambda: Pasa método terminarJuego como callback
        temporizador = new Temporizador(lblTiempo, this::terminarJuego); // Nuevo temporizador
        temporizador.start(); // Inicia temporizador
        repaint(); // Redibuja ahorcado
    }

    // Procesa letra ingresada
    private void procesarLetra() {
        if (!juegoActivo) {
            return; // Ignora si juego no está activo
        }

        String entrada = txtLetra.getText().trim().toLowerCase(); // Obtiene letra
        txtLetra.setText(""); // Limpia campo

// ------ Expresión regular: Valida que sea una letra a-z
        if (!Pattern.matches("[a-z]", entrada)) {
            lblMensaje.setText("¡Ingresa solo una letra (a-z)!"); // Error si no es letra
            return;
        }

        char letra = entrada.charAt(0); // Convierte a char
        boolean acierto = false; // Bandera de acierto

        // Busca letra en la palabra
        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (palabraSecreta.charAt(i) == letra) {
                palabraOculta.setCharAt(i, letra); // Actualiza palabra oculta
                acierto = true; // Marca acierto
            }
        }

        if (!acierto) {
            intentosFallidos++; // Incrementa errores
            lblMensaje.setText("Letra incorrecta. Intentos restantes: " + (MAX_INTENTOS - intentosFallidos)); // Mensaje de error
        } else {
            lblMensaje.setText("¡Buen intento! Ingresa otra letra"); // Mensaje de acierto
        }

        lblPalabra.setText(palabraOculta.toString()); // Actualiza palabra
        repaint(); // Redibuja ahorcado

        // Verifica fin del juego
        if (palabraOculta.toString().equals(palabraSecreta)) {
            terminarJuego(true); // Gana si adivina
        } else if (intentosFallidos >= MAX_INTENTOS) {
            terminarJuego(false); // Pierde si agota intentos
        }
    }

    // Termina el juego
    private void terminarJuego(boolean gano) {
        juegoActivo = false; // Desactiva juego
        temporizador.detener(); // Detiene temporizador
        txtLetra.setEnabled(false); // Desactiva campo
        if (gano) {
            lblMensaje.setText("¡Ganaste! La palabra era: " + palabraSecreta); // Mensaje de victoria
        } else {
            lblMensaje.setText("Perdiste. La palabra era: " + palabraSecreta); // Mensaje de derrota
        }
    }

    // Dibuja el ahorcado según intentos
    private void dibujarAhorcado(Graphics g) {
        // Fondo: Sol
        g.setColor(Color.YELLOW);
        g.fillOval(20, 20, 40, 40); // Dibuja sol
        g.setColor(new Color(255, 215, 0)); // Rayos
        for (int i = 0; i < 360; i += 45) {
            double rad = Math.toRadians(i);
            int x1 = 40 + (int) (20 * Math.cos(rad));
            int y1 = 40 + (int) (20 * Math.sin(rad));
            int x2 = 40 + (int) (30 * Math.cos(rad));
            int y2 = 40 + (int) (30 * Math.sin(rad));
            g.drawLine(x1, y1, x2, y2); // Dibuja rayos
        }

        // Fondo: Nubes
        g.setColor(Color.WHITE);
        // Nube 1
        g.fillOval(80, 40, 50, 30);
        g.fillOval(100, 30, 50, 30);
        g.fillOval(120, 40, 50, 30);
        // Nube 2
        g.fillOval(250, 20, 50, 30);
        g.fillOval(270, 10, 50, 30);
        g.fillOval(290, 20, 50, 30);

        // Estructura del ahorcado
        g.setColor(Color.BLACK);
        g.drawLine(100, 180, 200, 180); // Base horizontal
        g.drawLine(150, 180, 150, 50); // Poste vertical
        g.drawLine(150, 50, 200, 50); // Poste superior
        g.drawLine(200, 50, 200, 70); // Cuerda

        if (intentosFallidos >= 1) { // Cabeza
            g.drawOval(190, 70, 20, 20);
        }
        if (intentosFallidos >= 2) { // Ojos
            g.fillOval(195, 78, 3, 3);
            g.fillOval(202, 78, 3, 3);
        }
        if (intentosFallidos >= 3) { // Boca
            g.drawArc(195, 83, 10, 5, 0, -180);
        }
        if (intentosFallidos >= 4) { // Cuerpo
            g.drawLine(200, 90, 200, 130);
        }
        if (intentosFallidos >= 5) { // Brazo izquierdo
            g.drawLine(200, 100, 180, 110);
        }
        if (intentosFallidos >= 6) { // Brazo derecho
            g.drawLine(200, 100, 220, 110);
        }
        if (intentosFallidos >= 7) { // Botones del torso
            g.fillOval(198, 100, 4, 4);
            g.fillOval(198, 110, 4, 4);
            g.fillOval(198, 120, 4, 4);
        }
        if (intentosFallidos >= 8) { // Pierna izquierda
            g.drawLine(200, 130, 180, 150);
        }
        if (intentosFallidos >= 9) { // Pierna derecha
            g.drawLine(200, 130, 220, 150);
        }
    }
}
