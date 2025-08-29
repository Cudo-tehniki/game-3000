package zombi_shooter;

import java.awt.*;

public class BossZombie {
    private final Color BOSS_COLOR = new Color(150, 0, 0); // Темно-красный цвет для босса
    private final Color BOSS_OUTLINE = new Color(100, 0, 0); // Еще более темный для обводки
    private final Color EYE_COLOR = Color.RED; // Красные глаза

    private int x;
    private int y;
    private int health;
    private int maxHealth;
    private int size;
    private int speed;
    private int damage;

    // Простая анимация для босса
    private double animationTimer = 0;

    // Дополнительные эффекты
    private boolean takingDamage = false;
    private long damageTime = 0;

    public BossZombie(int x, int y, int health, int size, int speed, int damage) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.maxHealth = health;
        this.size = size;
        this.speed = speed;
        this.damage = damage;
    }

    public void update(int targetX, int targetY) {
        // Движение к игроку (как обычный зомби, но медленнее)
        double deltaX = targetX - x;
        double deltaY = targetY - y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > 0) {
            double moveX = (deltaX / distance) * speed;
            double moveY = (deltaY / distance) * speed;
            x += moveX;
            y += moveY;
        }

        // Обновляем анимацию
        animationTimer += 0.1;

        // Сброс эффекта получения урона
        if (takingDamage && System.currentTimeMillis() - damageTime > 200) {
            takingDamage = false;
        }
    }

    public void draw(Graphics2D g2d) {
        // Рисуем тень под боссом
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillOval(x - size/2 + 3, y - size/2 + 3, size, size);

        // Цвет босса (мигает красным при получении урона)
        Color bossColor = takingDamage ? Color.WHITE : BOSS_COLOR;
        g2d.setColor(bossColor);
        g2d.fillOval(x - size/2, y - size/2, size, size);

        // Полоска здоровья над боссом
        drawHealthBar(g2d);

        // Глаза (больше и страшнее)
        g2d.setColor(EYE_COLOR);
        int eyeSize = 12;
        g2d.fillOval(x - size/3, y - size/3, eyeSize, eyeSize);
        g2d.fillOval(x + size/6, y - size/3, eyeSize, eyeSize);

        // Зрачки
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x - size/3 + 3, y - size/3 + 3, 6, 6);
        g2d.fillOval(x + size/6 + 3, y - size/3 + 3, 6, 6);

        // Рот (злая улыбка)
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawArc(x - size/3, y + size/6, (int) (size/1.5), size/3, 0, -180);

        // Зубы
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < 5; i++) {
            int toothX = x - size/3 + (i * size/7);
            int toothY = y + size/6;
            g2d.fillRect(toothX, toothY, 3, 8);
        }

        // Обводка босса
        g2d.setColor(BOSS_OUTLINE);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawOval(x - size/2, y - size/2, size, size);

        // Эффект пульсации (простая анимация)
        if (Math.sin(animationTimer) > 0.5) {
            g2d.setColor(new Color(255, 0, 0, 50));
            g2d.fillOval(x - size/2 - 8, y - size/2 - 8, size + 16, size + 16);
        }

        // Название босса
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2d.getFontMetrics();
        String bossName = "BOSS";
        int textWidth = fm.stringWidth(bossName);
        g2d.drawString(bossName, x - textWidth/2, y - size/2 - 35);
    }

    private void drawHealthBar(Graphics2D g2d) {
        int barWidth = size;
        int barHeight = 8;
        int barX = x - barWidth/2;
        int barY = y - size/2 - 15;

        // Фон полоски здоровья
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(barX, barY, barWidth, barHeight);

        // Полоска здоровья
        float healthPercent = (float) health / maxHealth;
        int healthWidth = (int) (barWidth * healthPercent);

        // Цвет в зависимости от здоровья
        if (healthPercent > 0.6f) {
            g2d.setColor(Color.RED);
        } else if (healthPercent > 0.3f) {
            g2d.setColor(Color.ORANGE);
        } else {
            g2d.setColor(Color.DARK_GRAY);
        }

        g2d.fillRect(barX, barY, healthWidth, barHeight);

        // Обводка полоски
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(barX, barY, barWidth, barHeight);
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }

        takingDamage = true;
        damageTime = System.currentTimeMillis();
    }

    public boolean collidesWith(Bullet bullet) {
        double distance = Math.sqrt(
                Math.pow(bullet.getX() - x, 2) +
                        Math.pow(bullet.getY() - y, 2)
        );
        return distance < size/2 + bullet.getSIZE_OF_BULLET()/2;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}