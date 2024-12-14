package dinojump;

import javax.swing.*;
import java.awt.*;

public class Platform extends GameObject implements Movable {
    private Image platformImage;
    private Image brokenImage; // Gambar setelah platform dipijak
    private boolean isPassed;
    private int speedX;
    private int leftLimit;
    private int rightLimit;
    private String type; // Jenis platform ("normal" atau "breakable")
    private int hitCount; // Hitungan pijakan

    public Platform(int x, int y, int screenWidth, String type) {
        super(x, y, 60, 18);
        this.type = type;
        this.isPassed = false;
        this.speedX = 0;
        this.leftLimit = 0;
        this.rightLimit = screenWidth - width;
        this.hitCount = 0;

        if (type.equals("breakable")) {
            platformImage = new ImageIcon(getClass().getResource("/assets/platform-br-1.png")).getImage();
            brokenImage = new ImageIcon(getClass().getResource("/assets/platform-br-2.png")).getImage();
        } else {
            platformImage = new ImageIcon(getClass().getResource("/assets/platform-g-normal.png")).getImage();
        }
    }

    @Override
    public void moveDown(int dy) {
        y += dy;
    }

    public void moveLeftRight() {
        x += speedX;
        if (x <= leftLimit || x >= rightLimit) {
            speedX = -speedX;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (type.equals("breakable") && hitCount == 1) {
            g.drawImage(brokenImage, x, y, width, height, null); // Gambar platform rusak
        } else if (!(type.equals("breakable") && hitCount > 1)) {
            g.drawImage(platformImage, x, y, width, height, null); // Gambar platform normal
        }
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }

    public void resetPosition(int newY) {
        this.x = (int) (Math.random() * 300);
        this.y = newY;
        this.isPassed = false;
        this.hitCount = 0; // Reset hit count
    }

    public boolean getIsPassed() {
        return this.isPassed;
    }

    public void setIsPassed(boolean passed) {
        this.isPassed = passed;
    }

    public int getSpeed() {
        return this.speedX;
    }

    public void setSpeed(int speed) {
        this.speedX = speed;
    }

    public void setLimits(int left, int right) {
        this.leftLimit = left;
        this.rightLimit = right;
    }

    public boolean isBreakable() {
        return type.equals("breakable");
    }

    public int getHitCount() {
        return hitCount;
    }

    public void incrementHitCount() {
        hitCount++;
    }

    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
}
