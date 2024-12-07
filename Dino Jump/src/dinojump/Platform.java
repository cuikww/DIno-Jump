package dinojump;

import javax.swing.*;
import java.awt.*;

public class Platform extends GameObject implements Movable {
    private Image platformImage;

    public Platform(int x, int y) {
        super(x, y, 60, 18);
        platformImage = new ImageIcon("src\\assets\\platform-g-normal.png").getImage();

    }

    @Override
    public void update() {}

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

    // Tambahkan getter untuk y
    public int getY() {
        return this.y;
    }

    // Tambahkan getter untuk x jika diperlukan
    public int getX() {
        return this.x;
    }

    public void resetPosition(int newY) {
        this.x = (int) (Math.random() * 300);
        this.y = newY;                       
    }
}
