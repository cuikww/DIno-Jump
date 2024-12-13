package dinojump;

import javax.swing.*;
import java.awt.*;

public class Platform extends GameObject implements Movable {
    private Image platformImage;
    private boolean isPassed;

    public Platform(int x, int y) {
        super(x, y, 60, 18);
        platformImage = new ImageIcon("src\\assets\\platform-g-normal.png").getImage();
        this.isPassed = false;
    }

    @Override
    public void moveDown(int dy) {
        y += dy;
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

}
