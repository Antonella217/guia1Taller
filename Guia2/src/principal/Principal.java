/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package principal;

import java.util.Scanner;

/**
 *
 * @author Antonella
 */
public class Principal {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RegistroUsuario gestion = new RegistroUsuario();
        int opcion;

        do {
            System.out.println("\n===== CRUD USUARIOS =====");
            System.out.println("1. Agregar usuario");
            System.out.println("2. Buscar usuario");
            System.out.println("3. Editar usuario");
            System.out.println("4. Borrar usuario");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opcion: ");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Ingrese nombre: ");
                    String nombre = sc.nextLine();

                    System.out.print("Ingrese correo: ");
                    String correo = sc.nextLine();

                    Registro nuevo = new Registro(id, nombre, correo, true);

                    if (gestion.AgregarRegistro(nuevo)) {
                        System.out.println("Usuario agregado correctamente.");
                    } else {
                        System.out.println("No se pudo agregar. El ID ya existe.");
                    }
                    break;

                case 2:
                    System.out.print("Ingrese ID, nombre o correo a buscar: ");
                    String criterio = sc.nextLine();

                    Registro encontrado = gestion.buscar(criterio);

                    if (encontrado != null) {
                        System.out.println("\nUsuario encontrado:");
                        System.out.println("ID: " + encontrado.getId());
                        System.out.println("Nombre: " + encontrado.getNombre());
                        System.out.println("Correo: " + encontrado.getCorreo());
                        System.out.println("Activo: " + encontrado.isActivo());
                    } else {
                        System.out.println("Usuario no encontrado.");
                    }
                    break;

                case 3:
                    System.out.print("Ingrese el ID del usuario a editar: ");
                    int idEditar = sc.nextInt();
                    sc.nextLine();

                    Registro existente = gestion.buscar(String.valueOf(idEditar));

                    if (existente != null) {
                        System.out.print("Ingrese nuevo nombre: ");
                        String nuevoNombre = sc.nextLine();

                        System.out.print("Ingrese nuevo correo: ");
                        String nuevoCorreo = sc.nextLine();

                        Registro actualizado = new Registro(idEditar, nuevoNombre, nuevoCorreo, true);

                        if (gestion.EditarRegistro(actualizado)) {
                            System.out.println("Usuario editado correctamente.");
                        } else {
                            System.out.println("No se pudo editar el usuario.");
                        }
                    } else {
                        System.out.println("Usuario no encontrado.");
                    }
                    break;

                case 4:
                    System.out.print("Ingrese el ID del usuario a borrar: ");
                    int idBorrar = sc.nextInt();
                    sc.nextLine();

                    Registro aBorrar = new Registro();
                    aBorrar.setId(idBorrar);

                    if (gestion.BorrarRegistro(aBorrar)) {
                        System.out.println("Usuario borrado correctamente.");
                    } else {
                        System.out.println("No se encontro el usuario.");
                    }
                    break;

                case 5:
                    System.out.println("Programa finalizado.");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 5);

        sc.close();
    }
}