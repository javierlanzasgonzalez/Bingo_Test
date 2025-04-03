package bingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MenuGUI {

    // --- MÉTODO PARA AÑADIR PLACEHOLDER ---
    public static void ponerPlaceholder(JTextField campo, String placeholder) {
        campo.setText(placeholder);
        campo.setForeground(Color.GRAY);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(Color.GRAY);
                }
            }
        });
    }

    //metodo ver todas las ventanas
    public static void mostrarVentanaGestionClientes() {
        JFrame gestionFrame = new JFrame("Gestión de Clientes");
        gestionFrame.setSize(700, 500);
        gestionFrame.setResizable(false);
        gestionFrame.setLocationRelativeTo(null);
        gestionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane pestañas = new JTabbedPane();

        pestañas.addTab("Agregar", crearPanelAgregar(gestionFrame));
        pestañas.addTab("Buscar", crearPanelBuscar());
        pestañas.addTab("Modificar", crearPanelModificar(gestionFrame));
        pestañas.addTab("Eliminar", crearPanelEliminar(gestionFrame));
        pestañas.addTab("Ver Todos", crearPanelVerTodos());

        gestionFrame.add(pestañas);
        gestionFrame.setVisible(true);
    }

    //Metodo para agregar un cliente nuevo
    private static JPanel crearPanelAgregar(JFrame gestionFrame) {
        JPanel panelAgregar = new JPanel(new GridLayout(7, 2, 10, 10));
        panelAgregar.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60)); // Margen para centrar mejor

        JTextField txtDNI = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtApellido1 = new JTextField();
        JTextField txtApellido2 = new JTextField();
        JTextField txtFecha = new JTextField();

        // Ajustar tamaño de campos
        Dimension campoPequeno = new Dimension(120, 25);
        txtDNI.setPreferredSize(campoPequeno);
        txtNombre.setPreferredSize(campoPequeno);
        txtApellido1.setPreferredSize(campoPequeno);
        txtApellido2.setPreferredSize(campoPequeno);
        txtFecha.setPreferredSize(campoPequeno);

        // Poner placeholders
        MenuGUI.ponerPlaceholder(txtDNI, "Ej: 12345678A");
        MenuGUI.ponerPlaceholder(txtFecha, "Ej: 31/12/2000");

        panelAgregar.add(new JLabel("DNI:"));
        panelAgregar.add(txtDNI);
        panelAgregar.add(new JLabel("Nombre:"));
        panelAgregar.add(txtNombre);
        panelAgregar.add(new JLabel("Primer Apellido:"));
        panelAgregar.add(txtApellido1);
        panelAgregar.add(new JLabel("Segundo Apellido:"));
        panelAgregar.add(txtApellido2);
        panelAgregar.add(new JLabel("Fecha Nacimiento (dd/mm/yyyy):"));
        panelAgregar.add(txtFecha);
        panelAgregar.add(new JLabel()); // Espacio vacío

        JButton btnGuardar = new JButton("Guardar Cliente");
        btnGuardar.addActionListener(e -> {
            try {
                String dni = txtDNI.getText().trim().toUpperCase();
                if (dni.equals("EJ: 12345678A") || !dni.matches("\\d{8}[A-Z]")) {
                    JOptionPane.showMessageDialog(gestionFrame, "DNI inválido. Debe tener 8 números y una letra mayúscula.");
                    return;
                }
                if (Database.SQLComprobarCliente(dni)) {
                    JOptionPane.showMessageDialog(gestionFrame, "El cliente ya existe.");
                    return;
                }

                String nombre = txtNombre.getText().trim();
                String ap1 = txtApellido1.getText().trim();
                String ap2 = txtApellido2.getText().trim();
                String fecha = txtFecha.getText().trim();

                if (fecha.equals("Ej: 31/12/2000") || !fecha.matches("^([0][1-9]|[12][0-9]|3[01])/([0][1-9]|1[0-2])/\\d{4}$")) {
                    JOptionPane.showMessageDialog(gestionFrame, "Formato de fecha inválido (dd/mm/yyyy).");
                    return;
                }

                var conn = ConexionBD.getConnection();
                if (conn != null) {
                    var stmt = conn.prepareStatement("INSERT INTO clientes VALUES (?, ?, ?, ?, ?, ?)");
                    stmt.setString(1, dni);
                    stmt.setString(2, nombre);
                    stmt.setString(3, ap1);
                    stmt.setString(4, ap2);
                    stmt.setString(5, fecha);
                    stmt.setInt(6, 0);
                    stmt.execute();
                    JOptionPane.showMessageDialog(gestionFrame, "Cliente guardado correctamente.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(gestionFrame, "Error al guardar: " + ex.getMessage());
            }
        });

        panelAgregar.add(btnGuardar);
        return panelAgregar;
    }

    //Metodo para buscar un cliente
    private static JPanel crearPanelBuscar() {
        JPanel panelBuscar = new JPanel(new BorderLayout(10, 10));
        JPanel panelSuperior = new JPanel(new FlowLayout());
        JTextField txtBuscarDNI = new JTextField(10);
        JButton btnBuscar = new JButton("Buscar");

        JTextArea resultado = new JTextArea(6, 30); // Tamaño más pequeño
        resultado.setEditable(false);
        resultado.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultado.setLineWrap(true); // Para que el texto se ajuste a la línea
        resultado.setWrapStyleWord(true);

        // También puedes usar esto para mayor control del tamaño:
        resultado.setPreferredSize(new Dimension(300, 120)); // Tamaño visual

        btnBuscar.addActionListener(e -> {
            String dni = txtBuscarDNI.getText().trim().toUpperCase();
            try {
                if (!dni.matches("\\d{8}[A-Z]")) {
                    resultado.setText("DNI inválido");
                    return;
                }
                var conn = ConexionBD.getConnection();
                var stmt = conn.prepareStatement("SELECT * FROM clientes WHERE dni = ?");
                stmt.setString(1, dni);
                var rs = stmt.executeQuery();

                if (rs.next()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("DNI: ").append(rs.getString(1)).append("\n");
                    sb.append("Nombre: ").append(rs.getString(2)).append("\n");
                    sb.append("Apellido 1: ").append(rs.getString(3)).append("\n");
                    sb.append("Apellido 2: ").append(rs.getString(4)).append("\n");
                    sb.append("Nacimiento: ").append(rs.getString(5)).append("\n");
                    sb.append("Victorias: ").append(rs.getString(6)).append("\n");
                    resultado.setText(sb.toString());
                } else {
                    resultado.setText("Cliente no encontrado");
                }

            } catch (Exception ex) {
                resultado.setText("Error: " + ex.getMessage());
            }
        });

        ponerPlaceholder(txtBuscarDNI, "Ej: 12345678A");

        panelSuperior.add(new JLabel("DNI: "));
        panelSuperior.add(txtBuscarDNI);
        panelSuperior.add(btnBuscar);
        panelBuscar.add(panelSuperior, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(resultado);
        scroll.setPreferredSize(new Dimension(500, 300)); // Tamaño fijo

        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCentro.add(scroll);

        panelBuscar.add(panelCentro, BorderLayout.CENTER);
        return panelBuscar;
    }

    //Metodo para eliminar un cliente
    private static JPanel crearPanelEliminar(JFrame gestionFrame) {
        JPanel panelEliminar = new JPanel(new FlowLayout());
        JTextField txtEliminarDNI = new JTextField(10);
        JButton btnEliminar = new JButton("Eliminar");

        btnEliminar.addActionListener(e -> {
            String dni = txtEliminarDNI.getText().trim().toUpperCase();
            if (!dni.matches("\\d{8}[A-Z]")) {
                JOptionPane.showMessageDialog(gestionFrame, "DNI inválido.");
                return;
            }
            try {
                if (!Database.SQLComprobarCliente(dni)) {
                    JOptionPane.showMessageDialog(gestionFrame, "El cliente no existe.");
                    return;
                }
                var conn = ConexionBD.getConnection();
                var stmt = conn.prepareStatement("DELETE FROM clientes WHERE dni = ?");
                stmt.setString(1, dni);
                stmt.execute();
                JOptionPane.showMessageDialog(gestionFrame, "Cliente eliminado correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(gestionFrame, "Error al eliminar: " + ex.getMessage());
            }
        });

        ponerPlaceholder(txtEliminarDNI, "Ej: 12345678A");

        panelEliminar.add(new JLabel("DNI: "));
        panelEliminar.add(txtEliminarDNI);
        panelEliminar.add(btnEliminar);
        return panelEliminar;
    }

    //Metodo para modificar un cliente
    private static JPanel crearPanelModificar(JFrame gestionFrame) {
        JPanel panelModificar = new JPanel(new BorderLayout(10, 10));
        JPanel buscarModificarPanel = new JPanel(new FlowLayout());
        JTextField txtModificarDNI = new JTextField(10);
        JButton btnCargarDatos = new JButton("Cargar Datos");
        JPanel formularioModificar = new JPanel(new GridLayout(7, 2, 10, 10));

        JTextField mDNI = new JTextField();
        JTextField mNombre = new JTextField();
        JTextField mAp1 = new JTextField();
        JTextField mAp2 = new JTextField();
        JTextField mFecha = new JTextField();

        JButton btnActualizarCliente = new JButton("Actualizar Cliente");
        btnActualizarCliente.setEnabled(false);

        // Ajustar tamaño de campos
        Dimension campoPequeno = new Dimension(120, 25);
        txtModificarDNI.setPreferredSize(campoPequeno);
        mDNI.setPreferredSize(campoPequeno);
        mNombre.setPreferredSize(campoPequeno);
        mAp1.setPreferredSize(campoPequeno);
        mAp2.setPreferredSize(campoPequeno);
        mFecha.setPreferredSize(campoPequeno);

        btnCargarDatos.addActionListener(e -> {
            String dni = txtModificarDNI.getText().trim().toUpperCase();
            if (!dni.matches("\\d{8}[A-Z]")) {
                JOptionPane.showMessageDialog(gestionFrame, "DNI inválido.");
                return;
            }
            try {
                var conn = ConexionBD.getConnection();
                var stmt = conn.prepareStatement("SELECT * FROM clientes WHERE dni = ?");
                stmt.setString(1, dni);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    mDNI.setText(rs.getString(1));
                    mDNI.setForeground(Color.BLACK);

                    mNombre.setText(rs.getString(2));
                    mNombre.setForeground(Color.BLACK);

                    mAp1.setText(rs.getString(3));
                    mAp1.setForeground(Color.BLACK);

                    mAp2.setText(rs.getString(4));
                    mAp2.setForeground(Color.BLACK);

                    mFecha.setText(rs.getString(5));
                    mFecha.setForeground(Color.BLACK);

                    btnActualizarCliente.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(gestionFrame, "Cliente no encontrado.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(gestionFrame, "Error al cargar datos: " + ex.getMessage());
            }
        });

        btnActualizarCliente.addActionListener(e -> {
            String dniOriginal = txtModificarDNI.getText().trim().toUpperCase();
            String nuevoDNI = mDNI.getText().trim().toUpperCase();
            String nombre = mNombre.getText().trim();
            String ap1 = mAp1.getText().trim();
            String ap2 = mAp2.getText().trim();
            String fecha = mFecha.getText().trim();

            if (!nuevoDNI.matches("\\d{8}[A-Z]")) {
                JOptionPane.showMessageDialog(gestionFrame, "Nuevo DNI inválido.");
                return;
            }
            if (!fecha.matches("^([0][1-9]|[12][0-9]|3[01])/([0][1-9]|1[0-2])/\\d{4}$")) {
                JOptionPane.showMessageDialog(gestionFrame, "Formato de fecha inválido.");
                return;
            }
            try {
                var conn = ConexionBD.getConnection();
                var stmt = conn.prepareStatement("UPDATE clientes SET dni = ?, nombre = ?, apellido1 = ?, apellido2 = ?, fecha = ? WHERE dni = ?");
                stmt.setString(1, nuevoDNI);
                stmt.setString(2, nombre);
                stmt.setString(3, ap1);
                stmt.setString(4, ap2);
                stmt.setString(5, fecha);
                stmt.setString(6, dniOriginal);
                stmt.execute();
                JOptionPane.showMessageDialog(gestionFrame, "Cliente actualizado correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(gestionFrame, "Error al actualizar: " + ex.getMessage());
            }
        });

        ponerPlaceholder(txtModificarDNI, "Ej: 12345678A");
        ponerPlaceholder(mDNI, "Ej: 12345678A");
        ponerPlaceholder(mFecha, "Ej: 31/12/2000");

        buscarModificarPanel.add(new JLabel("DNI actual: "));
        buscarModificarPanel.add(txtModificarDNI);
        buscarModificarPanel.add(btnCargarDatos);

        formularioModificar.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60)); // Centrar

        formularioModificar.add(new JLabel("Nuevo DNI:"));
        formularioModificar.add(mDNI);
        formularioModificar.add(new JLabel("Nombre:"));
        formularioModificar.add(mNombre);
        formularioModificar.add(new JLabel("Primer Apellido:"));
        formularioModificar.add(mAp1);
        formularioModificar.add(new JLabel("Segundo Apellido:"));
        formularioModificar.add(mAp2);
        formularioModificar.add(new JLabel("Fecha Nacimiento (dd/mm/yyyy):"));
        formularioModificar.add(mFecha);
        formularioModificar.add(new JLabel());
        formularioModificar.add(btnActualizarCliente);

        panelModificar.add(buscarModificarPanel, BorderLayout.NORTH);
        panelModificar.add(formularioModificar, BorderLayout.CENTER);
        return panelModificar;
    }

    //Metodo para ver todos los cliente
    private static JPanel crearPanelVerTodos() {
        JPanel panelVerTodos = new JPanel(new BorderLayout(10, 10));
        JTextArea areaTodos = new JTextArea();
        areaTodos.setEditable(false);
        areaTodos.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JButton btnActualizar = new JButton("Mostrar Clientes");

        btnActualizar.addActionListener(e -> {
            areaTodos.setText("");
            try {
                var conn = ConexionBD.getConnection();
                var stmt = conn.prepareStatement("SELECT * FROM clientes");
                var rs = stmt.executeQuery();
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("%-12s %-10s %-31s %-15s %-10s\n",
                        "DNI", "Nombre", "Apellidos", "Nacimiento", "Victorias"));
                sb.append("---------------------------------------------------------------------------------\n");
                while (rs.next()) {
                    sb.append(String.format("%-12s %-10s %-15s %-15s %-15s %-10s\n",
                            rs.getString(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6)));
                }
                areaTodos.setText(sb.toString());
            } catch (Exception ex) {
                areaTodos.setText("Error al cargar clientes: " + ex.getMessage());
            }
        });

        JPanel panelBotonActualizar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonActualizar.add(btnActualizar);

        panelVerTodos.add(panelBotonActualizar, BorderLayout.SOUTH);
        panelVerTodos.add(new JScrollPane(areaTodos), BorderLayout.CENTER);

        return panelVerTodos;
    }

}
