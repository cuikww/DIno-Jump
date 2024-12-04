package dinojump;

import javax.swing.*;
import java.awt.*;

public class Doodler extends GameObject {
    private Image dinoLeft;
    private Image dinoRight;
    private int velocityY;
    private int velocityX;
    private boolean facingRight;

    public Doodler(int x, int y) {
        super(x, y, 50, 50);
        dinoLeft = new ImageIcon(getClass().getResource("/assets/player-dino-left.png")).getImage();
        dinoRight = new ImageIcon(getClass().getResource("/assets/player-dino-right.png")).getImage();
        velocityY = 0;
        velocityX = 0;
        facingRight = true;
    }

    public void update() {
        velocityY += 1; // Gravity
        y += velocityY;

        x += velocityX;
        if (x < 0) x = 360;
        if (x > 360) x = 0;

        // Mencegah doodler keluar dari layar
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
        velocityY = -15;
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
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }
}

