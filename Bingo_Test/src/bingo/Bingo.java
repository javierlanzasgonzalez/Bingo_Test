package bingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;

/**
 * Interfaz principal del juego Bingo, reemplaza el menú por consola.
 */
public class Bingo {

    /**
     * Punto de entrada del programa.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Bingo::mostrarMenuPrincipal);
    }

    /**
     * Muestra el menú principal del Bingo como una ventana gráfica.
     */
    public static void mostrarMenuPrincipal() {
        JFrame frame = new JFrame("Menú Principal - Bingo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Establecer icono temporal
        ImageIcon icono = new ImageIcon(Bingo.class.getResource("/bingo/bingo_icon.png"));
        frame.setIconImage(icono.getImage());

        // Panel con imagen de fondo
        JPanel panelFondo = new JPanel() {
            Image fondo = new ImageIcon(getClass().getResource("/bingo/menu_fondo.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelFondo.setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 10, 10));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("\uD83C\uDFB2 Bienvenido al BINGO", SwingConstants.CENTER);
        titulo.setFont(new Font("Ubuntu Mono", Font.PLAIN, 35));
        titulo.setForeground(Color.BLACK);

        JButton btnJugar = new JButton("🎮 Jugar Bingo");
        btnJugar.setPreferredSize(new Dimension(150,50));
        btnJugar.setMaximumSize(new Dimension(150,50));
        btnJugar.setFont(new Font("Ubuntu Mono",Font.BOLD,24));
        JButton btnGestionClientes = new JButton("👤 Gestionar Clientes");
        JButton btnCreditos = new JButton("🎬 Créditos");
        JButton btnSalir = new JButton("❌ Salir");

        btnJugar.addActionListener(e -> {
            try {
                String usuario = Database.SQLBuscarClienteGUI();
                if (usuario != null) {
                    String[] cliente = usuario.split("\\s+");
                    jugarBingo(cliente);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error al buscar cliente: " + ex.getMessage());
            }
        });

        btnGestionClientes.addActionListener(e -> MenuGUI.mostrarVentanaGestionClientes());
        btnCreditos.addActionListener(e -> CreditosDialog.mostrar(frame));
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(titulo);
        panelBotones.add(btnJugar);
        panelBotones.add(btnGestionClientes);
        panelBotones.add(btnCreditos);
        panelBotones.add(btnSalir);

        panelFondo.add(panelBotones, BorderLayout.CENTER);
        frame.setContentPane(panelFondo);
        frame.setVisible(true);
    }

    /**
     * Inicia el juego del Bingo con un cliente dado.
     *
     * @param cliente Array de Strings con la información del cliente.
     */
    public static void jugarBingo(String[] cliente) {
        BingoCarton carton = new BingoCarton();
        BingoJuego juego = new BingoJuego(carton);
        BingoGUI gui = new BingoGUI(null, juego, cliente);
        gui.setVisible(true);
    }
}
