/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

/**
 *
 * @author Antonella
 */
public interface IGestionDatos {
    boolean AgregarRegistro(Registro reg);
    boolean BorrarRegistro(Registro reg);
    boolean EditarRegistro(Registro reg);
    Registro buscar(String criterio);
}