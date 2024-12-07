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
    private boolean background1Complete;  // Menandakan apakah background1 sudah selesai
    
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
        background1Complete = false;  // Mulai dengan background1
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
    
        // Menghitung ukuran background pertama agar sesuai dengan panel
        int imageWidth = background.getWidth(null);
        int imageHeight = background.getHeight(null);
        double scaleX = (double) panelWidth / imageWidth;
        double scaleY = (double) panelHeight / imageHeight;
        double scaleFactor = Math.max(scaleX, scaleY); // Gunakan faktor skala yang lebih besar agar tidak pecah
        int scaledWidth = (int) (imageWidth * scaleFactor);
        int scaledHeight = (int) (imageHeight * scaleFactor);
    
        // Menggambar background pertama hanya sekali
        if (!background1Complete) {
            // Menggambar background pertama dua kali untuk menciptakan efek looping
            g.drawImage(background, 0, -scaledHeight + panelHeight + backgroundOffset, scaledWidth, scaledHeight, null);
            g.drawImage(background, 0, panelHeight + backgroundOffset, scaledWidth, scaledHeight, null);
    
            // Jika background1 sudah selesai ditampilkan (setelah offset melebihi tinggi gambar)
            if (backgroundOffset >= scaledHeight) {
                backgroundOffset = 0;  // Reset offset untuk background pertama
                background1Complete = true; // Background pertama selesai, lanjut ke background2
            }
        }
    
        // Menggambar background kedua setelah background pertama selesai
        if (background1Complete) {
            // Menggambar background2 tanpa stretching, menjaga aspek rasio
            int bg2Width = background2.getWidth(null);
            int bg2Height = background2.getHeight(null);
            double bg2ScaleX = (double) panelWidth / bg2Width;
            double bg2ScaleY = (double) panelHeight / bg2Height;
            double bg2ScaleFactor = Math.max(bg2ScaleX, bg2ScaleY); // Skalakan dengan faktor yang sesuai
    
            int scaledBg2Width = (int) (bg2Width * bg2ScaleFactor);
            int scaledBg2Height = (int) (bg2Height * bg2ScaleFactor);
    
            // Offset untuk memastikan background2 terus bergerak dengan mulus
            int loopOffset = backgroundOffset % scaledBg2Height; // Menggunakan modulus untuk looping mulus
    
            // Gambar dua latar belakang (background2) yang bergerak terus-menerus
            g.drawImage(background2, 0, -scaledBg2Height + panelHeight + loopOffset, scaledBg2Width, scaledBg2Height, null);
            g.drawImage(background2, 0, panelHeight + loopOffset, scaledBg2Width, scaledBg2Height, null);
        }
    
        // Jika permainan selesai (game over)
        if (gameOver) {
            g.setColor(Color.BLACK);
            g.drawString("Game Over! Press Space to Restart", 80, 280);
            return;
        }
    
        // Gambar elemen lainnya (doodler, platform, dan skor)
        doodler.draw(g);
    
        for (Platform platform : platforms) {
            platform.draw(g);
        }
    
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
        repaint();
        revalidate();
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
                backgroundOffset += 5;  // Update background offset
                for (Platform platform : platforms) {
                    platform.moveDown(5);
                }
                score++;
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
        background1Complete = false; // Reset ke background1
    }
}
