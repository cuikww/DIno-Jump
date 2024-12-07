package dinojump;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage {
    private JFrame frame;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private String username;

    public LoginPage() {
        // Frame utama
        frame = new JFrame("Dino Jump");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 630); // Sesuai ukuran GamePanel
        frame.setLayout(new CardLayout());

        // Panel login dan register
        loginPanel = createLoginPanel();
        registerPanel = createRegisterPanel();

        // Tambahkan panel ke frame
        frame.add(loginPanel, "Login");
        frame.add(registerPanel, "Register");

        // Tampilkan frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblUsername = createStyledLabel("Username:");
        JLabel lblPassword = createStyledLabel("Password:");
        JTextField txtUsername = new JTextField(15);
        JPasswordField txtPassword = new JPasswordField(15);
        JButton btnLogin = createStyledButton("Login");
        JButton btnRegister = createStyledButton("Register");
        JLabel lblError = createErrorLabel();

        // Layout konfigurasi
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblUsername, gbc);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblPassword, gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Error Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(lblError, gbc);

        // Login Button
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(btnLogin, gbc);

        // Register Button
        gbc.gridx = 1;
        panel.add(btnRegister, gbc);

        // Action Listener untuk Login
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText().trim();
                String password = new String(txtPassword.getPassword());

                if (Database.login(username, password)) {
                    lblError.setText("");
                    frame.dispose(); // Tutup login frame
                    LoginPage.this.username = username; // Menyimpan username yang berhasil login
                    openGamePanel(); // Membuka game panel
                } else {
                    lblError.setText("Invalid username or password.");
                }
            }
        });

        // Action Listener untuk membuka panel register
        btnRegister.addActionListener(e -> showPanel("Register"));

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblUsername = createStyledLabel("Username:");
        JLabel lblPassword = createStyledLabel("Password:");
        JLabel lblConfirmPassword = createStyledLabel("Confirm Password:");
        JTextField txtUsername = new JTextField(15);
        JPasswordField txtPassword = new JPasswordField(15);
        JPasswordField txtConfirmPassword = new JPasswordField(15);
        JButton btnRegister = createStyledButton("Register");
        JButton btnBack = createStyledButton("Back");
        JLabel lblError = createErrorLabel();

        // Layout konfigurasi
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblUsername, gbc);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblPassword, gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblConfirmPassword, gbc);
        gbc.gridx = 1;
        panel.add(txtConfirmPassword, gbc);

        // Error Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(lblError, gbc);

        // Register Button
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(btnRegister, gbc);

        // Back Button
        gbc.gridx = 1;
        panel.add(btnBack, gbc);

        // Action Listener untuk Register
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText().trim();
                String password = new String(txtPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    lblError.setText("Fields cannot be empty.");
                } else if (!password.equals(confirmPassword)) {
                    lblError.setText("Passwords do not match.");
                } else if (Database.isUsernameTaken(username)) {
                    lblError.setText("Username already exists.");
                } else {
                    if (Database.register(username, password)) {
                        lblError.setText("Registration successful.");
                        showPanel("Login");
                    } else {
                        lblError.setText("Registration failed.");
                    }
                }
            }
        });

        // Action Listener untuk kembali ke Login
        btnBack.addActionListener(e -> showPanel("Login"));

        return panel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JLabel createErrorLabel() {
        JLabel label = new JLabel("");
        label.setForeground(Color.RED);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void openGamePanel() {
        JFrame gameFrame = new JFrame("Dino Jump");
        GamePanel gamePanel = new GamePanel(username); // Pass username ke GamePanel

        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(360, 630);
        gameFrame.add(gamePanel);
        gameFrame.setResizable(false);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        gamePanel.startGame();
    }

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
        cl.show(frame.getContentPane(), panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
