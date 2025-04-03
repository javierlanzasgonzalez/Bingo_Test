package bingo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Clase que muestra una ventana emergente con los créditos del juego Bingo.
 * Incluye imagen de fondo y animación tipo máquina de escribir para el texto.
 */
public class CreditosDialog extends JDialog {

    private BufferedImage backgroundImage; // Imagen de fondo para el panel

    /**
     * Constructor que crea el diálogo de créditos.
     * @param parent Ventana principal desde la que se invoca (puede ser null).
     */
    public CreditosDialog(JFrame parent) {
        super(parent, "Créditos del Bingo", false); // Diálogo no modal con título
        setAlwaysOnTop(true);                      // Siempre visible por encima de otras ventanas
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);// Al cerrar solo se cierra esta ventana
        setResizable(false);                       // No se puede redimensionar

        // Intentar cargar la imagen de fondo desde los recursos
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/bingo/creditos.png"));
        } catch (IOException e) {
            e.printStackTrace(); // Mostrar error si no se encuentra o falla al cargar
        }

        // Panel personalizado con la imagen de fondo pintada
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Dibuja la imagen escalada al tamaño del panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // Establece layout y márgenes al panel
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Área de texto que mostrará los créditos con animación
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Ubuntu Mono", Font.BOLD, 22)); // Fuente estilo consola
        textArea.setEditable(false);     // El usuario no puede editar el texto
        textArea.setOpaque(false);       // Hace el fondo del área de texto transparente
        textArea.setFocusable(false);    // No recibe foco
        textArea.setWrapStyleWord(true); // Ajusta palabras completas al hacer salto de línea
        textArea.setLineWrap(true);      // Permite salto de línea automático

        panel.add(textArea, BorderLayout.CENTER); // Añade el área de texto al centro del panel
        add(panel); // Añade el panel principal al diálogo

        // Texto de los créditos (multilínea usando """ desde Java 15+)
        String textoCreditos = """
                               
                                     🎉 Créditos del Bingo 🎉

                        Este programa ha sido desarrollado con
                    ilusión, esfuerzo y muchas ganas de aprender

                                      Trabajo realizado por:

                                    Javier Lanzas González
                             Samuel Donato Muñoz Povedano

                                       ¡Gracias por jugar!
                    ¡Buena suerte y que cante bingo el mejor! 🏆
                               
                """;

        // Establece el tamaño ideal según el texto completo (aunque se ocultará para animación)
        textArea.setText(textoCreditos);
        textArea.setSize(500, Short.MAX_VALUE);
        Dimension preferredSize = textArea.getPreferredSize();

        // Define el tamaño del diálogo en base al texto + márgenes
        setSize(preferredSize.width + 80, preferredSize.height + 100);
        textArea.setText(""); // Limpia el texto antes de animar

        setLocationRelativeTo(parent);  // Centra la ventana respecto al padre
        setVisible(true);               // Muestra el diálogo

        // Hilo que crea animación tipo "máquina de escribir"
        new Thread(() -> {
            for (char c : textoCreditos.toCharArray()) {
                // Añade carácter por carácter al área de texto desde el hilo principal
                SwingUtilities.invokeLater(() -> textArea.append(String.valueOf(c)));
                try {
                    Thread.sleep(40); // Espera 40ms entre cada carácter
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restablece el estado si es interrumpido
                }
            }
        }).start();
    }

    /**
     * Método estático para mostrar el diálogo desde otra clase.
     * @param parent Ventana desde la que se llama
     */
    public static void mostrar(JFrame parent) {
        new CreditosDialog(parent); // Instancia y muestra el diálogo
    }
}