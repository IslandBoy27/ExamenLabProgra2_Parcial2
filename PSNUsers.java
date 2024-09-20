import javax.swing.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class PSNUsers {
    private RandomAccessFile raf;
    private HashTable users;

    public PSNUsers(String fileName) {
        try {
            File archivo = new File(fileName);
            if (!archivo.exists()) {
                archivo.createNewFile(); // Crea el archivo si no existe
            }
            raf = new RandomAccessFile(archivo, "rw");
            users = new HashTable();
            reloadHashTable();
        } catch (IOException e) {
            System.err.println("No se pudo abrir el archivo " + fileName + ": " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al abrir el archivo " + fileName, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reloadHashTable() throws IOException {
        raf.seek(0); // Volver al inicio del archivo
        users = new HashTable(); // Limpiar tabla actual

        while (raf.getFilePointer() < raf.length()) {
            long pos = raf.getFilePointer();
            String username = raf.readUTF();
            int puntos = raf.readInt();
            int contadorTrofeos = raf.readInt();
            boolean activo = raf.readBoolean();

            if (activo) { // Solo cargar usuarios activos
                users.add(username, pos);
            }

            // Saltar el resto de los datos de usuario
        }
    }

    public void addUser(String username) throws IOException {
        while (users.search(username) != -1) {
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

        users.add(username, pos);
        JOptionPane.showMessageDialog(null, "Usuario agregado con éxito.");
    }


    public void deactivateUser(String username) throws IOException {
        long pos = users.search(username);
        if (pos == -1) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        raf.seek(pos + username.length() + 8); // Saltar los primeros datos hasta llegar a "activo"
        raf.writeBoolean(false); // Marcar como inactivo

        users.remove(username);
        System.out.println("Usuario desactivado.");
    }

    public void addTrophieTo(String username, String trophyGame, String trophyName, Trophy type) throws IOException {
        long pos = users.search(username);
        if (pos == -1) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        // Leer y actualizar los puntos y trofeos del usuario
        raf.seek(pos + username.length());
        int puntos = raf.readInt();
        int contadorTrofeos = raf.readInt();

        raf.seek(pos + username.length());
        raf.writeInt(puntos + type.puntos); // Sumar puntos
        raf.writeInt(contadorTrofeos + 1); // Incrementar el contador de trofeos

        // Agregar el trofeo al archivo
        raf.seek(raf.length());
        raf.writeUTF(username);
        raf.writeUTF(type.toString());
        raf.writeUTF(trophyGame);
        raf.writeUTF(trophyName);
        raf.writeUTF(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        System.out.println("Trofeo agregado con éxito.");
    }

    public void playerInfo(String username) {
        Long pos = users.search(username); // Buscar la posición del usuario en la tabla hash

        if (pos == null || pos == -1) {
            // Si el usuario no se encuentra, mostrar un pop-up de error
            JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Aquí no hay que usar try-catch a menos que se necesite
        try {
            // Mover el puntero del archivo a la posición del usuario encontrado
            raf.seek(pos);

            // Leer los datos del usuario
            String nombreUsuario = raf.readUTF();
            boolean activo = raf.readBoolean();
            int puntos = raf.readInt();
            int trofeos = raf.readInt();

            if (!activo) {
                // Si el usuario está desactivado, mostrar un pop-up
                JOptionPane.showMessageDialog(null, "El usuario está desactivado", "Información", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Construir la información a mostrar
            String info = "Username: " + nombreUsuario + "\n" +
                    "Puntos: " + puntos + "\n" +
                    "Trofeos: " + trofeos + "\n" +
                    "Activo: Sí";

            // Mostrar la información del usuario en un pop-up
            JOptionPane.showMessageDialog(null, info, "Información del usuario", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            // Si ocurre algún error al leer el archivo, mostrar un pop-up de error
            JOptionPane.showMessageDialog(null, "Error al leer la información del usuario", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



}
