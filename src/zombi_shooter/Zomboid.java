package zombi_shooter;

import zombi_shooter.enemy.EnemyType;

import java.awt.*;

public class Zomboid {
    private Color COLOR_OF_ZOMBOID = new Color(255, 0, 0);
    private int x;
    private int y;
    private int size;
    private double speed;
    private EnemyType enemyType;
    private int hp;
    private int maxHp;
    private int dmg;

    public Zomboid(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = 15 + (int) (Math.random() * 16);
        this.speed = 2.0 + (double) (Math.random()) * 3;
        this.maxHp = 50;
        this.hp = maxHp;
        this.dmg = 1;
        this.enemyType = EnemyType.MELEE_ZOMBIE;
    }

    public Zomboid(Color COLOR_OF_ZOMBOID, int x, int y) {
        this.COLOR_OF_ZOMBOID = COLOR_OF_ZOMBOID;
        this.x = x;
        this.y = y;
        this.size = 15 + (int) (Math.random() * 16);
        this.speed = 2.0 + (double) (Math.random()) * 3;
        this.maxHp = 50;
        this.hp = maxHp;
        this.dmg = 1;
        this.enemyType = EnemyType.MELEE_ZOMBIE;
    }

    public Zomboid(int x, int y, EnemyType enemyType) {
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
        this.COLOR_OF_ZOMBOID = COLOR_OF_ZOMBOID;
        this.size = 15 + (int) (Math.random() * 16);
        this.speed = 2.0 + (double) (Math.random()) * 3 * enemyType.getSpeedMultiplayer();
        this.maxHp = (int) (50 * enemyType.getHpMultiplayer());
        this.hp = maxHp;
        this.dmg = (int) (1 * enemyType.getDmgMultiplayer());
    }

    public void update(int targetX, int targetY) {
        double deltaX = targetX - x;
        double deltaY = targetY - y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (distance > 0) {
            double moveX = (deltaX / distance) * speed;
            double moveY = (deltaY / distance) * speed;
            x += moveX;
            y += moveY;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(COLOR_OF_ZOMBOID);
        g2d.fillOval(x - size / 2, y - size / 2, size, size);

        // Добавляем глаза
        g2d.setColor(Color.BLACK);
        g2d.fillOval((int) (x - size / 4), (int) (y - size / 4), 3, 3);
        g2d.fillOval((int) (x + size / 6), (int) (y - size / 4), 3, 3);

        // Обводка
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(x - size / 2, y - size / 2, size, size);
    }

    public void drawHpBar(Graphics2D g2d) {
        if (hp >= maxHp) {
            return;
        }
        int barWidth = size;
        int barHight = 4;
        int barX = x - barWidth / 2;
        int barY = y - size / 2 - 8;
        g2d.setColor(new Color(51, 0, 0, 255));
        g2d.fillRect(barX, barY, barWidth, barHight);
        int currentHp = hp / maxHp * barWidth;
        g2d.setColor(new Color(255, 0, 0));
        g2d.fillRect(barX, barY, currentHp, barHight);
        g2d.setColor(new Color(0, 0, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(barX, barY, barWidth, barHight);

    }

    public void takeDmg(int dmg){
        this.hp -= dmg;
        if(this.hp <= 0){
            this.hp = 0;
        }
    }

    public boolean isDead(){
        return hp == 0;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(EnemyType enemyType) {
        this.enemyType = enemyType;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
