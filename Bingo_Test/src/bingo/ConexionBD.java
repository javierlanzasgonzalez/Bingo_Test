package bingo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL="jdbc:mysql://158.179.217.53:3306/bingo";
    private static final String USER="root";
    private static final String PASSWORD="Contraseña#1234";
    
    public static Connection getConnection(){
        Connection conexion = null;
        try {
            conexion= DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error en la conexion con la database: " + e.getMessage());
        }
        return conexion;
    }
}