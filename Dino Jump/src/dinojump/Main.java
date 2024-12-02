package dinojump;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dino Jump");
        GamePanel gamePanel = new GamePanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 630);
        frame.add(gamePanel);
        frame.setResizable(false);
        frame.setVisible(true);

        gamePanel.startGame();
    }
}
