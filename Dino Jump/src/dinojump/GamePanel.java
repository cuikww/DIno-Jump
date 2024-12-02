package dinojump;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Doodler doodler;
    private ArrayList<Platform> platforms;
    private Image background;
    private boolean gameOver;
    private int score;
    private int backgroundOffset;

    public GamePanel() {
        this.setFocusable(true);
        this.addKeyListener(this);
        background = new ImageIcon(getClass().getResource("/assets/All BG.png")).getImage().getScaledInstance(360, 2000, Image.SCALE_SMOOTH);
        doodler = new Doodler(160, 480);
        platforms = new ArrayList<>();
        initializePlatforms();

        timer = new Timer(16, this); // ~60 FPS
        gameOver = false;
        score = 0;
        backgroundOffset = 0;
    }

    public void startGame() {
        timer.start();
    }

    private void initializePlatforms() {
        for (int i = 0; i < 6; i++) {
            if (i == 0) {
                platforms.add(new Platform(160, 480));
            } else {
                int x = (int) (Math.random() * 300);
                int y = 500 - i * 100;
                platforms.add(new Platform(x, y));
            }
        }
    }    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Menggambar latar belakang secara looping
        int imageHeight = background.getHeight(null);
        int panelHeight = getHeight();

        // Gambar latar belakang pertama
        g.drawImage(background, 0, -imageHeight + panelHeight + backgroundOffset, getWidth(), imageHeight, null);

        // Gambar latar belakang kedua (untuk looping)
        g.drawImage(background, 0, panelHeight + backgroundOffset, getWidth(), imageHeight, null);

        if (gameOver) {
            g.setColor(Color.BLACK);
            g.drawString("Game Over! Press Space to Restart", 80, 280);
            return;
        }

        doodler.draw(g);

        for (Platform platform : platforms) {
            platform.draw(g);
        }

        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            doodler.update();

            for (Platform platform : platforms) {
                platform.update();

                // Deteksi tabrakan dengan doodler
                if (Utilities.detectCollision(doodler, platform)) {
                    doodler.jump();
                }

                // Pindahkan platform yang keluar dari layar
                if (platform.y > getHeight()) {
                    platform.resetPosition(-20); // Reset ke bagian atas layar
                }
            }

            // Pindahkan platform ke bawah jika doodler mencapai ambang atas
            if (doodler.getY() < 200) {
                backgroundOffset += 5; 
                for (Platform platform : platforms) {
                    platform.moveDown(5);
                }
                score++;
            }

            if (backgroundOffset > background.getHeight(null) - getHeight()) {
                backgroundOffset = 0;
            }    

            // Kondisi game over
            if (doodler.getY() > getHeight()) {
                gameOver = true;
            }
        }
        repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            doodler.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            doodler.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        doodler.stop();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void restartGame() {
        gameOver = false;
        score = 0;
        doodler.resetPosition(160, 480);
        platforms.clear();
        initializePlatforms();
    }
}
