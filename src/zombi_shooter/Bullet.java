package zombi_shooter;

import java.awt.*;

public class Bullet {
    private double x;
    private double y;
    private final double deltaX;
    private final double deltaY;
    private final int SIZE_OF_BULLET = 5;
    private final int damage;
    private final Color color;

    public Bullet(int startX, int startY, double direction, int speed, int damage, Color color) {
        this.x = startX;
        this.y = startY;
        this.deltaX = Math.cos(direction) * speed;
        this.deltaY = Math.sin(direction) * speed;
        this.damage = damage;
        this.color = color;
    }

    public Bullet(int startX, int startY, double direction) {
        this(startX, startY, direction, 5, 1, Color.YELLOW);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int) x - SIZE_OF_BULLET / 2, (int) y - SIZE_OF_BULLET / 2, SIZE_OF_BULLET, SIZE_OF_BULLET);

        // Добавляем обводку для лучшей видимости
        g2d.setColor(Color.BLACK);
        g2d.drawOval((int) x - SIZE_OF_BULLET / 2, (int) y - SIZE_OF_BULLET / 2, SIZE_OF_BULLET, SIZE_OF_BULLET);
    }

    public void update() {
        x += deltaX;
        y += deltaY;
    }

    public boolean isBulletOutSideWindow(int mapWidth, int mapHeight) {
        return x < 0 || x > mapWidth || y < 0 || y > mapHeight;
    }

    public boolean collidesWith(Zomboid zomboid) {
        double distance = Math.sqrt(
                Math.pow(x - zomboid.getX(), 2) +
                        Math.pow(y - zomboid.getY(), 2)
        );
        return distance < SIZE_OF_BULLET + zomboid.getSize()/2;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getSIZE_OF_BULLET() {
        return SIZE_OF_BULLET;
    }

    public int getDamage() {
        return damage;
    }

    public Color getColor() {
        return color;
    }
}