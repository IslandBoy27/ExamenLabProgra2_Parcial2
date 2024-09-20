import javax.swing.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class PSNUsers {
    private RandomAccessFile raf;
    private Hashtable<String, Long> users;

    public PSNUsers(String fileName) {
        try {
            File archivo = new File(fileName);
            if (!archivo.exists()) {
                archivo.createNewFile(); // Crea el archivo si no existe
            }
            raf = new RandomAccessFile(archivo, "rw");
            users = new Hashtable<>();
            reloadHashTable();
        } catch (IOException e) {
            System.err.println("No se pudo abrir el archivo " + fileName + ": " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al abrir el archivo " + fileName, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public void addUser(String username) throws IOException {
        while (users.containsKey(username)) {
            username = JOptionPane.showInputDialog(null, "El usuario ya existe. Por favor, ingresa un nombre de usuario diferente:");
            if (username == null || username.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Operación cancelada o nombre vacío.");
                return; // Cancelar si se cierra o se ingresa un nombre vacío
            }
        }

        raf.seek(raf.length()); // Mover al final del archivo
        long pos = raf.getFilePointer();

        raf.writeUTF(username);
        raf.writeInt(0); // Acumulador de puntos por trofeos
        raf.writeInt(0); // Contador de trofeos
        raf.writeBoolean(true); // Registro activado

        users.put(username, pos);
        JOptionPane.showMessageDialog(null, "Usuario agregado con éxito.");
    }

    public void deactivateUser(String username) throws IOException {
        Long pos = users.get(username);
        if (pos == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        raf.seek(pos);
        raf.readUTF(); // Leer el nombre de usuario
        raf.readInt(); // Leer puntos
        raf.readInt(); // Leer contador de trofeos
        raf.writeBoolean(false); // Marcar como inactivo

        users.remove(username);
        JOptionPane.showMessageDialog(null, "Usuario desactivado exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void addTrophieTo(String username, String trophyGame, String trophyName, Trophy type) throws IOException {
        Long pos = users.get(username);
        if (pos == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        // Mover el puntero a la posición del usuario
        raf.seek(pos);

        // Leer el nombre de usuario
        String savedUsername = raf.readUTF();
        if (!savedUsername.equals(username)) {
            System.out.println("Error: nombres de usuario no coinciden.");
            return;
        }

        // Leer puntos y contador de trofeos
        int puntos = raf.readInt();         // Leer los puntos actuales
        int contadorTrofeos = raf.readInt(); // Leer el número de trofeos

        // Actualizar puntos y contador de trofeos
        raf.seek(pos + savedUsername.length() * 2 + 4 + 4); // Mover al lugar de los puntos
        raf.writeInt(puntos + type.puntos); // Sumar puntos
        raf.writeInt(contadorTrofeos + 1);  // Incrementar el contador de trofeos

        // Añadir el trofeo al final del archivo
        raf.seek(raf.length());
        raf.writeUTF(username);               // Escribir el nombre del usuario
        raf.writeUTF(type.toString());        // Escribir el tipo de trofeo
        raf.writeUTF(trophyGame);             // Escribir el nombre del juego
        raf.writeUTF(trophyName);             // Escribir el nombre del trofeo
        raf.writeUTF(new SimpleDateFormat("dd/MM/yyyy").format(new Date())); // Fecha actual

        System.out.println("Trofeo agregado con éxito.");
    }


    // Step 1: Modify reloadHashTable to load all users
    private void reloadHashTable() throws IOException {
        raf.seek(0); // Volver al inicio del archivo
        users = new Hashtable<>(); // Limpiar tabla actual

        while (raf.getFilePointer() < raf.length()) {
            long pos = raf.getFilePointer();
            String username = raf.readUTF();
            raf.readInt(); // Leer puntos pero no usar
            raf.readInt(); // Leer contador de trofeos pero no usar
            boolean activo = raf.readBoolean();

            users.put(username, pos); // Cargar todos los usuarios
        }
    }

    // Step 2: Modify playerInfo to correctly handle and display user information
    public void playerInfo(String username) {
        Long pos = users.get(username); // Buscar la posición del usuario en la tabla hash

        if (pos == null) {
            // Si el usuario no se encuentra, mostrar un pop-up de error
            JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Mover el puntero del archivo a la posición del usuario encontrado
            raf.seek(pos);

            // Leer los datos del usuario
            String nombreUsuario = raf.readUTF();
            int puntos = raf.readInt();
            int trofeosContador = raf.readInt(); // Número de trofeos del usuario
            boolean activo = raf.readBoolean();

            // Mensaje de depuración para verificar el estado de activación del usuario
            System.out.println("Usuario: " + nombreUsuario + " | Activo: " + activo);

            // Crear un StringBuilder para mostrar la información
            StringBuilder info = new StringBuilder();
            info.append("Código de Usuario: ").append(pos).append("\n")
                    .append("Nombre de Usuario: ").append(nombreUsuario).append("\n")
                    .append("Puntos: ").append(puntos).append("\n")
                    .append("Número de Trofeos: ").append(trofeosContador).append("\n")
                    .append("Activo: ").append(activo ? "Sí" : "No").append("\n");

            // Mostrar trofeos del usuario
            info.append("Trofeos:\n");

            // Variables para puntos totales y mostrar detalles de cada trofeo
            int puntosTotales = puntos;

            // Leer los trofeos
            long currentPosition = raf.getFilePointer();
            raf.seek(0); // Reset to the beginning of the file to read trophies
            while (raf.getFilePointer() < raf.length()) {
                try {
                    String codigoUsuario = raf.readUTF();
                    String tipoTrofeo = raf.readUTF();
                    String juegoTrofeo = raf.readUTF();
                    String descripcionTrofeo = raf.readUTF();
                    String fecha = raf.readUTF();

                    if (codigoUsuario.equals(username)) {
                        info.append(fecha).append(" - ").append(tipoTrofeo).append(" - ").append(juegoTrofeo).append(" - ").append(descripcionTrofeo).append("\n");
                    }
                } catch (EOFException e) {
                    break; // End of file reached
                }
            }
            raf.seek(currentPosition); // Restore the file pointer position

            // Mostrar los puntos totales
            info.append("Puntos Totales: ").append(puntosTotales).append("\n");

            // Mostrar la información del usuario en un pop-up
            JOptionPane.showMessageDialog(null, info.toString(), "Información del usuario", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            // Si ocurre algún error al leer el archivo, mostrar un pop-up de error
            JOptionPane.showMessageDialog(null, "Error al leer la información del usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Para obtener más detalles en la consola
        }
    }
}