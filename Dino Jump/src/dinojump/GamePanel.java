package dinojump;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Doodler doodler;
    private ArrayList<Platform> platforms;
    private Image background, background2;
    private boolean gameOver;
    private int score;
    private int backgroundOffset;

    public GamePanel() {
        this.setFocusable(true);
        this.addKeyListener(this);
        background = new ImageIcon(getClass().getResource("/assets/All BG.png")).getImage();
        background2 = new ImageIcon(getClass().getResource("/assets/Space2.png")).getImage();
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

        // Dapatkan ukuran panel
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imageWidth = background.getWidth(null);
        int imageHeight = background.getHeight(null);

        // Menghitung ukuran background agar sesuai dengan panel
        double scaleX = (double) panelWidth / imageWidth;
        double scaleY = (double) panelHeight / imageHeight;
        double scaleFactor = Math.max(scaleX, scaleY); // Menggunakan faktor skala yang lebih besar agar tidak pecah

        // Skala background
        int scaledWidth = (int) (imageWidth * scaleFactor);
        int scaledHeight = (int) (imageHeight * scaleFactor);

        // Menggambar latar belakang secara looping
        g.drawImage(background, 0, -scaledHeight + panelHeight + backgroundOffset, scaledWidth, scaledHeight, null);
        g.drawImage(background, 0, panelHeight + backgroundOffset, scaledWidth, scaledHeight, null);

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
                backgroundOffset = 0;
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
