package bingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

public class BingoGUI extends JDialog {

    private final BingoJuego juego;
    private final JLabel[][] etiquetasCarton;
    private final JLabel lblNumero;
    private final JButton btnSacarNumero;
    public BingoGUI(Window parent, BingoJuego juego, String[] cliente) {

        super(parent, ("Cliente: "+ cliente[1] + " " + cliente[2] + " " + cliente[3] ), ModalityType.APPLICATION_MODAL);
        this.juego = juego;
        etiquetasCarton = new JLabel[3][9];

        setAlwaysOnTop(true);
        setLayout(new BorderLayout());
        setResizable(false);

        // Panel para el cartón de bingo
        JPanel panelCarton = new JPanel(new GridLayout(3, 9));
        int[][] carton = juego.getCarton().getCarton();

        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 9; col++) {
                if (carton[fila][col] == 0) {
                    // Crear una etiqueta con el logo si la casilla está vacía
                    ImageIcon icono = new ImageIcon(getClass().getResource("/bingo/logo.png"));
                    Image imagenEscalada = icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Ajusta tamaño
                    etiquetasCarton[fila][col] = new JLabel(new ImageIcon(imagenEscalada));
                    etiquetasCarton[fila][col].setBackground(new Color(1, 100, 60));
                    etiquetasCarton[fila][col].setForeground(Color.DARK_GRAY);
                    etiquetasCarton[fila][col].setOpaque(true);
                } else {
                    etiquetasCarton[fila][col] = new JLabel(String.valueOf(carton[fila][col]), SwingConstants.CENTER);
                    etiquetasCarton[fila][col].setFont(new Font("Ubuntu Mono", Font.BOLD, 30));
                    etiquetasCarton[fila][col].setForeground(new Color(1, 100, 60));
                    etiquetasCarton[fila][col].setOpaque(true);
                    etiquetasCarton[fila][col].setBackground(new Color(255, 255, 255));
                }
                // Borde negro exterior
                Border bordeExterior = new LineBorder(new Color(1, 100, 60), 1);

                // Borde blanco interior (más fino, opcional)
                Border bordeBlanco = new LineBorder(Color.WHITE, 1);

                // Borde combinado: negro → blanco → negro
                Border bordeCompuesto = new CompoundBorder(bordeExterior, new CompoundBorder(bordeBlanco, bordeExterior));

                // Asignar el borde a la celda
                etiquetasCarton[fila][col].setBorder(bordeCompuesto);
                panelCarton.add(etiquetasCarton[fila][col]);
            }
        }

        // Panel para mostrar el número sacado
        JPanel panelNumero = new JPanel();
        lblNumero = new JLabel("Presiona 'Sacar Número'", SwingConstants.CENTER);
        lblNumero.setFont(new Font("Ubuntu Mono", Font.BOLD, 20));
        panelNumero.add(lblNumero);

        // Botón para sacar números
        btnSacarNumero = new JButton("Sacar Número");
        btnSacarNumero.addActionListener((ActionEvent e) -> {
            int numero = juego.sacarNumero();
            lblNumero.setText("Número: " + numero);
            actualizarCarton();

            if (juego.getCarton().verificarPrimeraLinea()) {
                JOptionPane.showMessageDialog(BingoGUI.this, "¡Has hecho línea! 🎉");
            }

            if (juego.getCarton().verificarBingo()) {
                mostrarVentanaGanador();
                btnSacarNumero.setEnabled(false);
                try {
                    Database.SQLmodificarVictorias(cliente[0]);
                } catch (SQLException ex) {
                    Logger.getLogger(BingoGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Agregar componentes a la ventana
        add(panelCarton, BorderLayout.CENTER);
        add(panelNumero, BorderLayout.NORTH);
        add(btnSacarNumero, BorderLayout.SOUTH);

        setSize(500, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void actualizarCarton() {
        boolean[][] marcados = juego.getCarton().getMarcados();
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 9; col++) {
                if (marcados[fila][col]) {
                    etiquetasCarton[fila][col].setForeground(Color.RED);
                }
            }
        }
    }

    private void mostrarVentanaGanador() {
        JDialog dialogo = new JDialog(this, "¡BINGO!", true);
        dialogo.setLayout(new BorderLayout());

        JLabel mensaje = new JLabel("¡BINGO! Has ganado 🎉", SwingConstants.CENTER);
        mensaje.setFont(new Font("Ubuntu Mono", Font.BOLD, 24));
        mensaje.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cargar el GIF animado
        ImageIcon gif = new ImageIcon(getClass().getResource("/bingo/bingo.gif"));
        JLabel imagen = new JLabel(gif, SwingConstants.CENTER);

        dialogo.add(mensaje, BorderLayout.NORTH);
        dialogo.add(imagen, BorderLayout.CENTER);

        // Tamaño y posición
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setVisible(true);
    }
}
