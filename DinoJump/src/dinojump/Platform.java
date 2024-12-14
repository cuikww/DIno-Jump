package dinojump;

import javax.swing.*;
import java.awt.*;

public class Platform extends GameObject implements Movable {
    private Image platformImage;
    private boolean isPassed;
    private int speedX;
    private int leftLimit; // Left boundary for oscillation
    private int rightLimit;

    public Platform(int x, int y, int screenWidth) {
        super(x, y, 60, 18);
        platformImage = new ImageIcon("src\\assets\\platform-g-normal.png").getImage();
        this.isPassed = false;
        this.speedX = 0;
        this.leftLimit = 0;
        this.rightLimit = screenWidth - width;
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
        g.drawImage(platformImage, x, y, width, height, null);
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
}
