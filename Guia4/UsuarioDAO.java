/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guias4;

/**
 *
 * @author Antonella
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    Conexion conexion = new Conexion();

    public boolean guardar(Usuario u) {
        String sql = "INSERT INTO usuario (cedula, nombre, apellido, edad) VALUES (?, ?, ?, ?)";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getCedula());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setInt(4, u.getEdad());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
    System.out.println("Error al guardar: " + e.getMessage());
    e.printStackTrace();
    return false;
}
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setCedula(rs.getString("cedula"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setEdad(rs.getInt("edad"));
                lista.add(u);
            }

        } catch (Exception e) {
            System.out.println("Error al listar: " + e.getMessage());
        }

        return lista;
    }

    public boolean modificar(Usuario u) {
        String sql = "UPDATE usuario SET cedula=?, nombre=?, apellido=?, edad=? WHERE id=?";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getCedula());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setInt(4, u.getEdad());
            ps.setInt(5, u.getId());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al modificar: " + e.getMessage());
            return false;
        }
    }
    public Usuario buscarPorCedula(String cedula) {
    String sql = "SELECT * FROM usuario WHERE cedula = ?";
    Usuario u = null;

    try (Connection con = conexion.conectar()) {

        if (con == null) {
            System.out.println("La conexion es nula");
            return null;
        }

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, cedula);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            u = new Usuario();
            u.setId(rs.getInt("id"));
            u.setCedula(rs.getString("cedula"));
            u.setNombre(rs.getString("nombre"));
            u.setApellido(rs.getString("apellido"));
            u.setEdad(rs.getInt("edad"));
        }

    } catch (Exception e) {
        System.out.println("Error al buscar: " + e.getMessage());
        e.printStackTrace();
    }

    return u;
}

    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuario WHERE id=?";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }
}