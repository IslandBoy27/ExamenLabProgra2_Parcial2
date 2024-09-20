import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainPSN extends JFrame {

    private PSNUsers psnUsers;

    public MainPSN() {
        setTitle("PSN User Management");
        setSize(1000, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));

        // Crear componentes para agregar usuarios
        JPanel addUserPanel = new JPanel(new FlowLayout());
        JLabel addUserLabel = new JLabel("Agregar Usuario: ");
        JTextField addUserField = new JTextField(15);
        JButton addUserButton = new JButton("Agregar");

        addUserPanel.add(addUserLabel);
        addUserPanel.add(addUserField);
        addUserPanel.add(addUserButton);

        // Crear componentes para desactivar usuarios
        JPanel deactivateUserPanel = new JPanel(new FlowLayout());
        JLabel deactivateUserLabel = new JLabel("Desactivar Usuario: ");
        JTextField deactivateUserField = new JTextField(15);
        JButton deactivateUserButton = new JButton("Desactivar");

        deactivateUserPanel.add(deactivateUserLabel);
        deactivateUserPanel.add(deactivateUserField);
        deactivateUserPanel.add(deactivateUserButton);

        // Crear componentes para agregar trofeos a usuarios
        JPanel addTrophyPanel = new JPanel(new FlowLayout());
        JLabel addTrophyLabel = new JLabel("Agregar Trofeo - Usuario: ");
        JTextField addTrophyUserField = new JTextField(10);
        JTextField addTrophyGameField = new JTextField(10);
        JTextField addTrophyNameField = new JTextField(10);
        JComboBox<Trophy> trophyTypeBox = new JComboBox<>(Trophy.values());
        JButton addTrophyButton = new JButton("Agregar Trofeo");

        addTrophyPanel.add(addTrophyLabel);
        addTrophyPanel.add(addTrophyUserField);
        addTrophyPanel.add(new JLabel("Juego: "));
        addTrophyPanel.add(addTrophyGameField);
        addTrophyPanel.add(new JLabel("Descripcion Trofeo: "));
        addTrophyPanel.add(addTrophyNameField);
        addTrophyPanel.add(trophyTypeBox);
        addTrophyPanel.add(addTrophyButton);

        // Crear componentes para buscar información del usuario
        JPanel searchUserPanel = new JPanel(new FlowLayout());
        JLabel searchUserLabel = new JLabel("Buscar Usuario: ");
        JTextField searchUserField = new JTextField(15);
        JButton searchUserButton = new JButton("Buscar");

        searchUserPanel.add(searchUserLabel);
        searchUserPanel.add(searchUserField);
        searchUserPanel.add(searchUserButton);

        // Añadir todos los paneles al panel principal
        mainPanel.add(addUserPanel);
        mainPanel.add(deactivateUserPanel);
        mainPanel.add(addTrophyPanel);
        mainPanel.add(searchUserPanel);

        add(mainPanel, BorderLayout.CENTER);

        // Inicializar la lógica del archivo PSN
        psnUsers = new PSNUsers("psn.psn");

        // Acción para agregar usuario
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = addUserField.getText().trim();
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor ingresa un nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    psnUsers.addUser(username);
                    addUserField.setText(""); // Limpiar el campo
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción para desactivar usuario
        deactivateUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = deactivateUserField.getText().trim();
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor ingresa un nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    psnUsers.deactivateUser(username);
                    deactivateUserField.setText(""); // Limpiar el campo
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al desactivar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción para agregar trofeo a un usuario
        addTrophyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = addTrophyUserField.getText().trim();
                String game = addTrophyGameField.getText().trim();
                String trophy = addTrophyNameField.getText().trim();
                Trophy type = (Trophy) trophyTypeBox.getSelectedItem();

                if (username.isEmpty() || game.isEmpty() || trophy.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor ingresa todos los datos del trofeo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    psnUsers.addTrophieTo(username, game, trophy, type);
                    addTrophyUserField.setText("");
                    addTrophyGameField.setText("");
                    addTrophyNameField.setText("");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar trofeo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción para buscar la información de un usuario
        searchUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = searchUserField.getText().trim();
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor ingresa un nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                psnUsers.playerInfo(username);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainPSN mainPSN = new MainPSN();
                mainPSN.setVisible(true);
            }
        });
    }
}