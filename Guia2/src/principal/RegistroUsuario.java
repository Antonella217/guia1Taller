/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RegistroUsuario implements IGestionDatos {

    private final String nombreArchivo = "usuarios.dat";

    private static final int TAM_NOMBRE = 30;
    private static final int TAM_CORREO = 40;

    // int = 4 bytes
    // nombre = 30 chars * 2 bytes = 60
    // correo = 40 chars * 2 bytes = 80
    // boolean = 1 byte
    private static final int TAM_REGISTRO = 4 + (TAM_NOMBRE * 2) + (TAM_CORREO * 2) + 1;

    public RegistroUsuario() {
        try {
            File archivo = new File(nombreArchivo);
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error al crear el archivo.");
            e.printStackTrace();
        }
    }

    private String ajustarTexto(String texto, int longitud) {
        StringBuilder sb = new StringBuilder(texto);
        if (sb.length() > longitud) {
            return sb.substring(0, longitud);
        }
        while (sb.length() < longitud) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private void escribirRegistro(RandomAccessFile archivo, Registro reg) throws IOException {
        archivo.writeInt(reg.getId());
        archivo.writeChars(ajustarTexto(reg.getNombre(), TAM_NOMBRE));
        archivo.writeChars(ajustarTexto(reg.getCorreo(), TAM_CORREO));
        archivo.writeBoolean(reg.isActivo());
    }

    private Registro leerRegistro(RandomAccessFile archivo) throws IOException {
        int id = archivo.readInt();

        char[] nombreChars = new char[TAM_NOMBRE];
        for (int i = 0; i < TAM_NOMBRE; i++) {
            nombreChars[i] = archivo.readChar();
        }
        String nombre = new String(nombreChars).trim();

        char[] correoChars = new char[TAM_CORREO];
        for (int i = 0; i < TAM_CORREO; i++) {
            correoChars[i] = archivo.readChar();
        }
        String correo = new String(correoChars).trim();

        boolean activo = archivo.readBoolean();

        return new Registro(id, nombre, correo, activo);
    }

    private long cantidadRegistros(RandomAccessFile archivo) throws IOException {
        return archivo.length() / TAM_REGISTRO;
    }

    private boolean existeId(int id) {
        try (RandomAccessFile archivo = new RandomAccessFile(nombreArchivo, "r")) {
            long total = cantidadRegistros(archivo);

            for (int i = 0; i < total; i++) {
                archivo.seek(i * TAM_REGISTRO);
                Registro reg = leerRegistro(archivo);

                if (reg.getId() == id && reg.isActivo()) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al verificar ID.");
        }
        return false;
    }

    @Override
    public boolean AgregarRegistro(Registro reg) {
        if (existeId(reg.getId())) {
            return false;
        }

        try (RandomAccessFile archivo = new RandomAccessFile(nombreArchivo, "rw")) {
            archivo.seek(archivo.length());
            escribirRegistro(archivo, reg);
            return true;
        } catch (IOException e) {
            System.out.println("Error al agregar registro.");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean BorrarRegistro(Registro reg) {
        try (RandomAccessFile archivo = new RandomAccessFile(nombreArchivo, "rw")) {
            long total = cantidadRegistros(archivo);

            for (int i = 0; i < total; i++) {
                archivo.seek(i * TAM_REGISTRO);
                Registro actual = leerRegistro(archivo);

                if (actual.getId() == reg.getId() && actual.isActivo()) {
                    archivo.seek(i * TAM_REGISTRO);
                    actual.setActivo(false); // borrado lógico
                    escribirRegistro(archivo, actual);
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al borrar registro.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean EditarRegistro(Registro reg) {
        try (RandomAccessFile archivo = new RandomAccessFile(nombreArchivo, "rw")) {
            long total = cantidadRegistros(archivo);

            for (int i = 0; i < total; i++) {
                archivo.seek(i * TAM_REGISTRO);
                Registro actual = leerRegistro(archivo);

                if (actual.getId() == reg.getId() && actual.isActivo()) {
                    archivo.seek(i * TAM_REGISTRO);
                    escribirRegistro(archivo, reg);
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al editar registro.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Registro buscar(String criterio) {
        try (RandomAccessFile archivo = new RandomAccessFile(nombreArchivo, "r")) {
            long total = cantidadRegistros(archivo);

            for (int i = 0; i < total; i++) {
                archivo.seek(i * TAM_REGISTRO);
                Registro reg = leerRegistro(archivo);

                if (reg.isActivo()) {
                    if (String.valueOf(reg.getId()).equals(criterio)
                            || reg.getNombre().equalsIgnoreCase(criterio)
                            || reg.getCorreo().equalsIgnoreCase(criterio)) {
                        return reg;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al buscar registro.");
            e.printStackTrace();
        }
        return null;
    }
}