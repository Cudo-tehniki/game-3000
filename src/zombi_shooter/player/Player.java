package zombi_shooter.player;

import java.awt.*;

public class Player {
    private int playerHealth;
    private int maxHealth;
    private int baseSpeed;
    private final int size = 15;
    private final Color colorOfPlayer = new Color(0, 0, 255);
    private int positionY;
    private int positionX;
    private double direction = 0;

    public Player(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public Player(int maxHealth, int positionY, int positionX) {
        this.playerHealth = maxHealth;
        this.maxHealth = maxHealth;
        this.positionY = positionY;
        this.positionX = positionX;
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(colorOfPlayer);
        graphics2D.fillOval(positionX - size, positionY - size, size * 2, size * 2);
        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.drawOval(positionX - size, positionY - size, size * 2, size * 2);
        graphics2D.setColor(Color.ORANGE);
        graphics2D.setStroke(new BasicStroke(3));

        int lineLength = size + 8;
        int endX = (int) (positionX + Math.cos(direction) * lineLength);
        int endY = (int) (positionY + Math.sin(direction) * lineLength);

        graphics2D.drawLine(positionX, positionY, endX, endY);
        graphics2D.setColor(Color.ORANGE);

        double arrowLength = 6;
        double arrowAngle = 0.5;

        int arrowX1 = (int) (endX - Math.cos(direction - arrowAngle) * arrowLength);
        int arrowY1 = (int) (endY - Math.sin(direction - arrowAngle) * arrowLength);

        int arrowX2 = (int) (endX - Math.cos(direction + arrowAngle) * arrowLength);
        int arrowY2 = (int) (endY - Math.sin(direction + arrowAngle) * arrowLength);

        graphics2D.drawLine(endX, endY, arrowX1, arrowY1);
        graphics2D.drawLine(endX, endY, arrowX2, arrowY2);
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void move(int stepX, int stepY, int mapW, int mapH) {
        stepX *= baseSpeed;
        stepY *= baseSpeed;
        positionY += stepY;
        positionX += stepX;
        if (positionX < size) {
            positionX = size;
        }
        if (positionY < size) {
            positionY = size;
        }
        if (positionX > mapW - size) {
            positionX = mapW - size;
        }
        if (positionY > mapH - size) {
            positionY = mapH - size;
        }
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public double getDirection() {
        return direction;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        setPlayerHealth(maxHealth);
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(int baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setBaseHp(){
        setMaxHealth(100);
    }
}
