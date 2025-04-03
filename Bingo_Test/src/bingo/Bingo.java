package bingo;

import javax.swing.*;
import java.awt.*;
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
        frame.setSize(600, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Establecer icono
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

        // Título con imagen PNG
        ImageIcon tituloIcon = new ImageIcon(Bingo.class.getResource("/bingo/titulo.png"));
        JLabel lblTitulo = new JLabel(tituloIcon);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelFondo.add(lblTitulo, BorderLayout.NORTH);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(35, 0, 0, 0)); // márgenes

        // Panel de botones centrado y con control de tamaño
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 150, 20, 150)); // márgenes

        // Configuración común para botones
        Dimension tamañoBoton = new Dimension(220, 40);
        Font fuenteBoton = new Font("Ubuntu Mono", Font.BOLD, 18);

        JButton btnJugar = new JButton("🎮 Jugar Bingo");
        JButton btnGestionClientes = new JButton("👤 Gestionar Clientes");
        JButton btnCreditos = new JButton("🎬 Créditos");
        JButton btnSalir = new JButton("❌ Salir");

        for (JButton btn : new JButton[]{btnJugar, btnGestionClientes, btnCreditos, btnSalir}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(tamañoBoton);
            btn.setPreferredSize(tamañoBoton);
            btn.setFont(fuenteBoton);
            panelBotones.add(btn);
            panelBotones.add(Box.createVerticalStrut(15)); // Espacio entre botones
        }

        // Acciones de los botones
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
