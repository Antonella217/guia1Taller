/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guias4;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author Antonella
 */


public class Conexion {
    
    private final String url = "jdbc:mysql://localhost:3306/crud_usuarios";
    private final String user = "root";
    private final String password = "isabella";

    public Connection conectar() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Conexion exitosa");
        } catch (SQLException e) {
            System.out.println("Error de conexion: " + e.getMessage());
        }
        return con;
    }
}