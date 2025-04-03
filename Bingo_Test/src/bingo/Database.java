package bingo;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Database {

    /**
     * Metodo para agregar un cliente
     *
     * @throws java.sql.SQLException
     */
    public static void SQLAgregarCliente() throws SQLException {
        Scanner entrada = new Scanner(System.in);

        try (Connection conexion = ConexionBD.getConnection()) {
            if (conexion != null) {
                try {
                    PreparedStatement stmt = conexion.prepareStatement("INSERT INTO clientes VALUES (?, ?, ?, ?, ?, ?)");
                    String codigo = GestionClientes.introducirDNI(entrada);
                    stmt.setString(1, codigo);
                    if (!SQLComprobarCliente(codigo)) {
                        stmt.setString(2, GestionClientes.introducirNombre(entrada));
                        stmt.setString(3, GestionClientes.introducirApellido1(entrada));
                        stmt.setString(4, GestionClientes.introducirApellido2(entrada));
                        stmt.setString(5, GestionClientes.introducirFecha(entrada));
                        stmt.setInt(6, 0);

                        boolean update = stmt.execute();
                        System.out.println((update) ? "No se ha podido crear el cliente" : "Se ha creado el cliente correctamente");
                    } else {
                        System.out.println("El cliente con ese DNI ya existe.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error en la consulta: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Metodo para mostrar los clientes
     *
     * @throws java.sql.SQLException
     */
    public static void SQLMostrarClientes() throws SQLException {
        try (Connection conexion = ConexionBD.getConnection()) {
            if (conexion != null) {
                try {
                    PreparedStatement stmt = conexion.prepareStatement("SELECT * FROM clientes");
                    ResultSet resultado = stmt.executeQuery();
                    System.out.println("\n----- LISTADO DE CLIENTES ----------------------------------------------------------------------\n");
                    System.out.printf("%-15s %-15s %-20s %-20s %-15s\n",
                            "DNI", "Nombre", "Apellidos", "Fecha de Nacimiento", "Partidas Ganadas");
                    System.out.printf("%-15s %-15s %-20s %-20s %-15s\n",
                            "---", "------", "---------", "-------------------", "----------------\n");
                    while (resultado.next()) {
                        String dni = resultado.getString(1);
                        String nombre = resultado.getString(2);
                        String apellido1 = resultado.getString(3);
                        String apellido2 = resultado.getString(4);
                        String fechaNacimiento = resultado.getString(5);
                        String partidasGanadas = resultado.getString(6);
                        String apellidos = apellido1 + " " + apellido2;

                        System.out.printf("%-15s %-15s %-20s %-20s %-15s\n",
                                dni, nombre, apellidos, fechaNacimiento, partidasGanadas);
                    }
                    System.out.println("\n------------------------------------------------------------------------------------------------\n");
                } catch (SQLException e) {
                    System.out.println("Error en la consulta: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Funcion para comprobar si existe el cliente y devuelve un booleano
     *
     * @param codigo
     * @return boolean
     * @throws java.sql.SQLException
     */
    public static boolean SQLComprobarCliente(String codigo) throws SQLException {
        try (Connection conexion = ConexionBD.getConnection()) {
            if (conexion != null) {
                try {
                    PreparedStatement stmt = conexion.prepareStatement("SELECT * FROM clientes WHERE dni=?");
                    stmt.setString(1, codigo);
                    ResultSet resultado = stmt.executeQuery();
                    while (resultado.next()) {
                        String dni = resultado.getString(1);
                        if (dni.equals(codigo)) {
                            return true;
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Error. Debe introducir un numero.\n");
                }
            }
        }
        return false;
    }

    /**
     * Metodo para eliminar un cliente
     *
     * @throws java.sql.SQLException
     */
    public static void SQLEliminarCliente() throws SQLException {
        Scanner entrada = new Scanner(System.in);
        try (Connection conexion = ConexionBD.getConnection()) {
            if (conexion != null) {
                try {
                    PreparedStatement stmt = conexion.prepareStatement("DELETE FROM clientes WHERE dni=?");
                    String codigo = GestionClientes.introducirDNI(entrada);
                    stmt.setString(1, codigo);
                    if (SQLComprobarCliente(codigo)) {
                        boolean update = stmt.execute();
                        System.out.println((update) ? "No se ha podido eliminar el cliente" : "Se ha eliminado el cliente correctamente");
                    } else {
                        System.out.println("El cliente que desea eliminar no existe.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error en la consulta: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Metodo para buscar un cliente
     *
     * @return cliente
     * @throws java.sql.SQLException
     */
    public static String SQLBuscarClienteGUI() throws SQLException {
        JTextField campoDNI = new JTextField("Ej: 12345678A");
        campoDNI.setForeground(Color.GRAY);

        // Evento para borrar placeholder al enfocar
        campoDNI.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campoDNI.getText().equals("Ej: 12345678A")) {
                    campoDNI.setText("");
                    campoDNI.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campoDNI.getText().isEmpty()) {
                    campoDNI.setForeground(Color.GRAY);
                    campoDNI.setText("Ej: 12345678A");
                }
            }
        });

        int result = JOptionPane.showConfirmDialog(
                null,
                campoDNI,
                "Introduzca el DNI del cliente",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        String codigo = campoDNI.getText().trim().toUpperCase();

        if (!codigo.matches("\\d{8}[A-Z]")) {
            JOptionPane.showMessageDialog(null, "Formato de DNI inválido (Ej: 12345678A).");
            return null;
        }

        String cliente = null;

        try (Connection conexion = ConexionBD.getConnection()) {
            if (conexion != null) {
                var stmt = conexion.prepareStatement("SELECT * FROM clientes WHERE dni=?");
                stmt.setString(1, codigo);
                var resultado = stmt.executeQuery();
                if (resultado.next()) {
                    String nombre = resultado.getString(2);
                    String apellido1 = resultado.getString(3);
                    String apellido2 = resultado.getString(4);
                    String fechaNacimiento = resultado.getString(5);
                    String partidasGanadas = resultado.getString(6);
                    cliente = codigo + " " + nombre + " " + apellido1 + " " + apellido2 + " " + fechaNacimiento + " " + partidasGanadas;
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
                }
            }
        }

        return cliente;
    }

    /**
     * Metodo para añadir una victoria a un cliente
     *
     * @param codigo
     * @throws java.sql.SQLException
     */
    public static void SQLmodificarVictorias(String codigo) throws SQLException {
        try (Connection conexion = ConexionBD.getConnection()) {
            if (conexion != null) {
                try {
                    PreparedStatement stmt = conexion.prepareStatement("UPDATE clientes SET partidas = partidas+1 WHERE dni=? ");
                    stmt.setString(1, codigo);
                    stmt.execute();

                } catch (SQLException e) {
                    System.out.println("Error en la consulta: " + e.getMessage());
                }
            }
        }
    }
}
