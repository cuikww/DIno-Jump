package dinojump;

import javax.swing.*;
import java.awt.*;

public class Doodler extends GameObject {
    private Image dinoLeft;
    private Image dinoRight;
    private int velocityY;
    private int velocityX;
    private boolean facingRight;
    private boolean isOnPlatform;

    public Doodler(int x, int y, String characterName) {
        super(x, y, 50, 50);

        // Memuat gambar berdasarkan karakter yang dipilih
        dinoLeft = new ImageIcon(getClass().getResource("/assets/" + characterName + "_left.png")).getImage();
        dinoRight = new ImageIcon(getClass().getResource("/assets/" + characterName + "_right.png")).getImage();

        velocityY = 0;
        velocityX = 0;
        facingRight = true;
        isOnPlatform = false;  // Doodler mulai di udara
    }

    public void update() {
        // Jika Doodler berada di udara dan tidak sedang melompat, beri gravitasi
        if (!isOnPlatform) {
            velocityY += 1; // Gravity
        } else {
            // Jika Doodler di platform, pastikan dia tidak terus jatuh
            if (velocityY > 0) {
                velocityY = 0; 
            }
        }
    
        // Update posisi Doodler
        y += velocityY;
        x += velocityX;
    
        // Memastikan Doodler tidak keluar dari batas layar horizontal
        if (x < 0) x = 360;
        if (x > 360) x = 0;
    
        // Cegah Doodler keluar dari batas layar vertikal atas
        if (y < 0) {
            y = 0;
            velocityY = 0;
        }
    }

    public void draw(Graphics g) {
        if (facingRight) {
            g.drawImage(dinoRight, x, y, width, height, null);
        } else {
            g.drawImage(dinoLeft, x, y, width, height, null);
        }
    }

    public void jump() {
        if (isOnPlatform) {
            velocityY = -15;  
            isOnPlatform = false;
        }
    }

    public void moveLeft() {
        velocityX = -5;
        facingRight = false;
    }

    public void moveRight() {
        velocityX = 5;
        facingRight = true;
    }

    public void stop() {
        velocityX = 0;
    }

    public void resetPosition(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        velocityY = 0;
        velocityX = 0;
        facingRight = true;
        isOnPlatform = false;  // Doodler mulai di udara
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void setIsOnPlatform(boolean status) {
        this.isOnPlatform = status;
    }

    public boolean getIsOnPlatform() {
        return isOnPlatform;
    }
}
