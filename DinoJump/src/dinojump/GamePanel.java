package dinojump;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import java.io.IOException;


public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Doodler doodler;
    private ArrayList<Platform> platforms;
    private Image backgroundTanah;
    private Image backgroundSpace;
    private boolean gameOver;
    private int score;
    private int backgroundOffset;
    private String username;
    private int bestScore;
    private java.util.List<String> leaderboard;
    private List<Platform> passedPlatforms;
    private Clip jumpSound;
    private Clip gameOverSound;
    private Clip backSound;
    int y1;
    int y2;
    // private boolean firstImageActive = true;

    public GamePanel(String username, String selectedCharacter) {
        this.username = username;
        this.setFocusable(true);
        this.addKeyListener(this);

        // Muat background
        backgroundTanah = new ImageIcon(getClass().getResource("/assets/All BG.png")).getImage();
        backgroundSpace = new ImageIcon(getClass().getResource("/assets/Space2.png")).getImage();
        // rescale background
        // backgroundTanah = backgroundTanah.getScaledInstance(360, 630, Image.SCALE_SMOOTH);
        // backgroundSpace = backgroundSpace.getScaledInstance(360, 630, Image.SCALE_SMOOTH);

        y1 = 0;
        y2 = -getHeight();

        // Muat Doodler dengan karakter yang dipilih
        doodler = new Doodler(160, 430, selectedCharacter);

        platforms = new ArrayList<>();
        passedPlatforms = new ArrayList<>(); // Initialize passedPlatforms list here

        timer = new Timer(16, this); // ~60 FPS
        gameOver = false;
        score = 0;
        backgroundOffset = 0;
        this.bestScore = Database.getBestScore(username); // Ambil best score dari database
        this.leaderboard = Database.getTop3BestScores(); // Ambil leaderboard dari database
        loadSounds(); // Load sounds for jump and game over
    }

    public void loadSounds() {
        try {
            // Load jump sound effect
            AudioInputStream jumpStream = AudioSystem.getAudioInputStream(getClass().getResource("/assets/jumpSound.wav"));
            jumpSound = AudioSystem.getClip();
            jumpSound.open(jumpStream);

            // Load game over sound effect
            AudioInputStream gameOverStream = AudioSystem.getAudioInputStream(getClass().getResource("/assets/gameOver.wav"));
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(gameOverStream);

            AudioInputStream backsoundStream = AudioSystem.getAudioInputStream(getClass().getResource("/assets/inGame.wav"));
            backSound = AudioSystem.getClip();
            backSound.open(backsoundStream);
            backSound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Handle errors if the sound file is not found or cannot be played
        }
    }

    public void startGame() {
        timer.start();
        initializePlatforms();
    }

    private void initializePlatforms() {
        int screenWidth = getWidth();
        for (int i = 0; i < 6; i++) {
            Platform newPlatform;
            if (i == 0) {
                newPlatform = new Platform(160, 480, screenWidth, "normal");
            } else if (i == 2) {
                // Tambahkan platform breakable
                newPlatform = new Platform((int) (Math.random() * 300), 500 - i * 100, screenWidth, "breakable");
            } else {
                int x = (int) (Math.random() * 300);
                int y = 500 - i * 100;
                newPlatform = new Platform(x, y, screenWidth, "normal");
                if (i == 3) {
                    newPlatform.setSpeed(2); // Platform bergerak
                }
            }
    
            // Periksa apakah platform baru bertabrakan dengan platform yang sudah ada
            while (isOverlapping(newPlatform)) {
                newPlatform.resetPosition(500 - i * 100); // Geser ke posisi baru jika bertabrakan
            }
    
            platforms.add(newPlatform); // Tambahkan platform setelah memastikan tidak bertabrakan
        }
    }
        
    private boolean isOverlapping(Platform newPlatform) {
        for (Platform existingPlatform : platforms) {
            Rectangle newRect = new Rectangle(newPlatform.getX(), newPlatform.getY(), newPlatform.getWidth(), newPlatform.getHeight());
            Rectangle existingRect = new Rectangle(existingPlatform.getX(), existingPlatform.getY(), existingPlatform.getWidth(), existingPlatform.getHeight());
            if (newRect.intersects(existingRect)) {
                return true; // Platform bertabrakan
            }
        }
        return false; // Tidak ada tabrakan
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Graphics2D g2d = (Graphics2D) g;

        // Dapatkan ukuran panel
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imageWidth = backgroundTanah.getWidth(null);
        int imageHeight = backgroundTanah.getHeight(null);
        
        // // Menghitung ukuran background agar sesuai dengan panel
        double scaleX = (double) panelWidth / imageWidth;
        double scaleY = (double) panelHeight / imageHeight;
        double scaleFactor = Math.max(scaleX, scaleY);
        
        // // Skala background
        int scaledWidth = (int) (imageWidth * scaleFactor);
        int scaledHeight = (int) (imageHeight * scaleFactor);
        
        // Menggambar latar belakang secara looping
        g.drawImage(backgroundTanah, 0, -scaledHeight + panelHeight + backgroundOffset, scaledWidth, scaledHeight, null);
        g.drawImage(backgroundTanah, 0, panelHeight + backgroundOffset, scaledWidth, scaledHeight, null);
        
        if (backgroundOffset >= scaledHeight - panelHeight) {
            int offsetForBackground2 = backgroundOffset - (scaledHeight - panelHeight);
            g.drawImage(backgroundSpace, 0, -scaledHeight + panelHeight + offsetForBackground2, scaledWidth, scaledHeight, null);
            g.drawImage(backgroundSpace, 0, panelHeight + offsetForBackground2, scaledWidth, scaledHeight, null);
        }

        // if(firstImageActive){
        //     g2d.drawImage(backgroundTanah, 0, y1,  null);
        //     g2d.drawImage(backgroundSpace, 0, y2, null);
        // } else {
        //     g2d.drawImage(backgroundSpace, 0, y2, null);
        //     g2d.drawImage(backgroundSpace, 0, y2 - backgroundSpace.getHeight(null), null);
        // }
        // g2d.drawImage(backgroundTanah, 0, 100, panelWidth, panelHeight, null);
        // g2d.drawImage(backgroundSpace, 0, -100, panelWidth, panelHeight, null);

        if (gameOver) {
            // Game Over Panel Background (semi-transparent)
            g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
            g.fillRect(50, 150, panelWidth - 100, 200); // Panel for game over info
            
            // Game Over Message
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String gameOverText = "Game Over!";
            FontMetrics metrics = g.getFontMetrics();
            int x = (panelWidth - metrics.stringWidth(gameOverText)) / 2;
            int y = 200;
            g.drawString(gameOverText, x, y);
            
            // Score Display
            g.setFont(new Font("Arial", Font.PLAIN, 22));
            String scoreText = "Your Score: " + score;
            metrics = g.getFontMetrics();
            x = (panelWidth - metrics.stringWidth(scoreText)) / 2;
            g.drawString(scoreText, x, y + 40);
            
            // Best Score Display
            String bestScoreText = "Best Score: " + bestScore;
            metrics = g.getFontMetrics();
            x = (panelWidth - metrics.stringWidth(bestScoreText)) / 2;
            g.drawString(bestScoreText, x, y + 80);
            
            // Instructions: Press Space to play again
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            String instructions = "Press 'Space' to Play Again";
            metrics = g.getFontMetrics();
            x = (panelWidth - metrics.stringWidth(instructions)) / 2;
            g.drawString(instructions, x, y + 120);
    
            // --- Leaderboard Section ---
            int leaderboardPanelWidth = 250; // Width of the leaderboard panel
            int leaderboardPanelHeight = 100; // Height of the leaderboard panel
            int leaderboardX = (panelWidth - leaderboardPanelWidth) / 2; // Centered horizontally
            int leaderboardY = 30; // Positioned near the top
    
            // Leaderboard Background
            g.setColor(new Color(0, 0, 0, 150)); // Transparent black background
            g.fillRect(leaderboardX, leaderboardY, leaderboardPanelWidth, leaderboardPanelHeight);
    
            // Leaderboard Title
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Leaderboard", leaderboardX + 60, leaderboardY + 20); // Centered title
    
            // Leaderboard Entries
            g.setFont(new Font("Arial", Font.PLAIN, 17));
            int yPosition = leaderboardY + 40;
            for (int i = 0; i < leaderboard.size(); i++) {
                g.drawString((i + 1) + ". " + leaderboard.get(i), leaderboardX + 80, yPosition+2);
                yPosition += 20;
            }
    
            repaint();
            return;
        }
        
        // Gambar elemen game
        doodler.draw(g);
        for (Platform platform : platforms) {
            platform.draw(g);
        }
        
        // Menampilkan informasi skor dalam kotak transparan seperti saat bermain
        g.setColor(new Color(0, 0, 0, 150)); // Warna hitam dengan transparansi
        g.fillRect(10, 10, 150, 80); // Kotak transparan untuk teks
        
        g.setColor(Color.WHITE); // Warna teks putih
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("Best Score: " + bestScore, 20, 50); // Menampilkan best score
        g.drawString("Player: " + username, 20, 70); // Menampilkan username
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            doodler.update();
    
            for (Platform platform : platforms) {
                // Move the platform if it is the moving one
                if (platform.getSpeed() != 0) {
                    platform.moveLeftRight();
                }
    
                // Detect collision with the doodler
                if (Utilities.detectCollision(doodler, platform)) {
                    // If Doodler is falling and on top of the platform, jump
                    if (doodler.getVelocityY() > 0) {
                        doodler.setIsOnPlatform(true);
                        backSound.start();
    
                        doodler.jump();
    
                        // Play jump sound
                        jumpSound.setFramePosition(0); // Rewind the sound to the beginning
                        jumpSound.start();
    
                        // Handle breakable platforms
                        if (platform.isBreakable()) {
                            platform.incrementHitCount(); // Increment the hit count
    
                            // Check hit count
                            if (platform.getHitCount() == 1) {
                                // Change to broken image
                                platform.setIsPassed(true); // Mark as passed
                            } else if (platform.getHitCount() > 1) {
                                // Remove the platform after it breaks
                                platform.resetPosition(-20); // Move off-screen
                            }
                        } else {
                            // Normal platform handling
                            if (!platform.getIsPassed()) {
                                platform.setIsPassed(true);
    
                                // Get the current platform's index
                                int currentIndex = platforms.indexOf(platform);
    
                                // Check if this is the first platform the Doodler lands on
                                if (passedPlatforms.isEmpty()) {
                                    passedPlatforms.add(platform); // Add the first platform to passedPlatforms
                                    score = 1; // Score starts at 1 since the Doodler starts on the first platform
                                } else {
                                    // Get the last passed platform index
                                    Platform lastPassedPlatform = passedPlatforms.get(passedPlatforms.size() - 1);
                                    int lastIndex = platforms.indexOf(lastPassedPlatform);
    
                                    // Calculate the score based on index difference
                                    int diff = 0;
    
                                    // If current platform index is greater than the last one, normal difference
                                    if (currentIndex > lastIndex) {
                                        diff = currentIndex - lastIndex;
                                    }
                                    // If current platform index is less than the last one, circular difference
                                    else if (currentIndex < lastIndex) {
                                        diff = (platforms.size() - lastIndex) + currentIndex; // Wrap around
                                    }
    
                                    score += diff; // Update score with the calculated difference
                                }
    
                                passedPlatforms.add(platform); // Add this platform to passedPlatforms
                            }
                        }
                    }
    
                } else {
                    doodler.setIsOnPlatform(false);
                }
    
                // Move platforms that go off-screen
                if (platform.getY() > getHeight()) {
                    platform.resetPosition(-20); // Reset to the top of the screen
                }
            }
    
            // if(firstImageActive){
            //     y1 += 5;
            //     y2 += 5;
            //     if(y1 >= getHeight()){
            //         firstImageActive = false;
            //         y2 = 0;
            //     }
            // } else {
            //     y2 += 5;
            //     if(y2 >= getHeight()){
            //         y2 = 0;
            //     }
            // }
            // Move the platforms down if Doodler reaches the top
            if (doodler.getY() < 300) {
                backgroundOffset += 5;

                for (Platform platform : platforms) {
                    platform.moveDown(5);
                }
            }
            // Reset the background offset if it reaches the end
            if (backgroundOffset > backgroundTanah.getHeight(null) - getHeight()) {
                backgroundOffset = 0;
            }
            repaint();
            // Game Over Condition
            if (doodler.getY() > getHeight()) {
                y1 = 0;
                y2 = -getHeight();
                gameOver = true;
                backgroundOffset = 0;
    
                backSound.stop();
                backSound.setFramePosition(0);
    
                // Play game over sound
                gameOverSound.setFramePosition(0); // Rewind the sound to the beginning
                gameOverSound.start();
    
                // Update best score if the score is higher
                if (score > bestScore) {
                    bestScore = score;
                    Database.updateBestScore(username, bestScore); // Update best score in the database
                }
    
                // Refresh leaderboard
                this.leaderboard = Database.getTop3BestScores();
            }
        }
        repaint();
        revalidate();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            doodler.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            doodler.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            // Tombol Space untuk restart permainan
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
        doodler.resetPosition(160, 430);
        platforms.clear();
        initializePlatforms();
    }
}
