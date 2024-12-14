package dinojump;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel {
    private String username;
    private int bestScore;
    private String selectedCharacter = "dino-kuning";  // Default character
    private JLabel lblCharacterPreview;  // Label to show the selected character preview image
    private JFrame startFrame;  // Reference to the StartFrame

    public StartPanel(String username, JFrame startFrame) {
        this.username = username;
        this.startFrame = startFrame;  // Set the reference for the StartFrame
        this.bestScore = Database.getBestScore(username);

        // Setting layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allow elements to expand horizontally

        // Tampilkan username dan best score
        JLabel lblUsername = new JLabel("Username: " + username);
        JLabel lblBestScore = new JLabel("Best Score: " + bestScore);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 24));
        lblBestScore.setFont(new Font("Arial", Font.PLAIN, 16));
        lblUsername.setForeground(Color.WHITE);
        lblBestScore.setForeground(Color.WHITE);

        // Username label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Make the label span across two columns
        add(lblUsername, gbc);

        // Best score label
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Make the label span across two columns
        add(lblBestScore, gbc);

        // Choose character label
        JLabel lblChooseCharacter = new JLabel("Choose Character:");
        lblChooseCharacter.setFont(new Font("Arial", Font.BOLD, 16));
        lblChooseCharacter.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Reset to 1 column
        add(lblChooseCharacter, gbc);

        // Create the character selection dropdown
        String[] characters = {"dino-kuning", "dino-ungu", "dino-minty"};
        JComboBox<String> characterDropdown = new JComboBox<>(characters);
        characterDropdown.setFont(new Font("Arial", Font.PLAIN, 14));
        characterDropdown.setBackground(new Color(70, 130, 180));
        characterDropdown.setForeground(Color.WHITE);
        characterDropdown.setFocusable(false);
        characterDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCharacter = (String) characterDropdown.getSelectedItem();
                updateCharacterPreview();
            }
        });

        // Character dropdown
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Reset to 1 column
        add(characterDropdown, gbc);

        // Create a JLabel to show the character preview image
        lblCharacterPreview = new JLabel();
        updateCharacterPreview(); // Update the preview to the default character image

        // Character preview image
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Make the image preview span two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the preview image
        add(lblCharacterPreview, gbc);

        // Start Game button
        JButton btnStartGame = new JButton("Start Game");
        btnStartGame.setFont(new Font("Arial", Font.BOLD, 14));
        btnStartGame.setBackground(new Color(70, 130, 180));
        btnStartGame.setForeground(Color.WHITE);
        btnStartGame.setFocusPainted(false);
        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGamePanel();
            }
        });

        // Start Game button
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Make the button span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        add(btnStartGame, gbc);

        setBackground(new Color(30, 30, 50)); // Dark background color
    }

    private void updateCharacterPreview() {
        // Load the image based on the selected character
        String imagePath = "/assets/" + selectedCharacter + "_left.png";  // Path to character image
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));

        // Resize the image to fit the preview
        Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);

        // Update the preview label with the new icon
        lblCharacterPreview.setIcon(icon);
    }

    private void openGamePanel() {
        // Dispose the StartFrame when moving to the GamePanel
        startFrame.dispose();

        // Create the game frame and panel
        JFrame gameFrame = new JFrame("Dino Jump");
        GamePanel gamePanel = new GamePanel(username, selectedCharacter); // Pass username and selected character

        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(360, 630);
        gameFrame.add(gamePanel);
        gameFrame.setResizable(false);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        gamePanel.startGame();
    }
}
