package zombi_shooter.player.abilyti;

import zombi_shooter.Bullet;
import zombi_shooter.Zomboid;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class TuretAbility implements Ability {

    private static final String NAME = "Turret";
    private static final String DESCRIPTION = "спавнит турель";
    private static final long COUDOWN_TIME = 40000;
    private static final long TIME_DURACION = 20000;
    private static final long TURRET_RANGE = 200;
    private static final int DMG = 3;
    private static final int FIRE_RATE = 150;
    private static final int SIZE = 13;
    private static final Color BASE_COLOR = new Color(0, 255, 248, 255);
    private static final Color GUN_COLOR = new Color(89, 89, 89);
    private static final Color HIGHT_LIGHT_COLOR = new Color(0, 193, 123);

    private boolean isActive = false;
    private boolean placed = false;
    private long lastActivacion = 0;
    private long lastShootTime = 0;
    private long activationTime = 0;
    private int turretX = 0;
    private int turretY = 0;
    private double gunRotation = 0.0;
    private Zomboid zomboid = null;
    private boolean showMuzzleFlash = false;
    private long muzzleFlashTime = 0;
    private List<TurretEffect> turretEffects;

    @Override
    public boolean activate(int playerX, int playerY, int mouseX, int mouseY) {
        if (isReady()) {
            if (!isActive && !placed) {
                isActive = true;
                return true;
            }
            if (isActive && !placed) {
                turretX = mouseX;
                turretY = mouseY;
                placed = true;
                activationTime = System.currentTimeMillis();
                lastActivacion = System.currentTimeMillis();
                return true;
            }
        }
        return false;
    }

    public void findTarget(List<Zomboid> zomboidList) {
        zomboid = null;
        double minDistant = TURRET_RANGE;
        for (Zomboid zomb : zomboidList) {
            double distance = Math.sqrt(Math.pow(zomb.getX() - turretX, 2) +
                    Math.pow(zomb.getY() - turretY, 2));
            if(distance <= minDistant){
                minDistant = distance;
                zomboid = zomb;
            }
        }
    }

    public void updateEffect(long lastAbilityTime){
        turretEffects.removeIf(turretEffect -> {
            updateEffect(lastAbilityTime);
            return !turretEffect.isActive();
        });
    }

    @Override
    public void update(long lastAbilityTime, List<Zomboid> listOfZomb, List<Bullet> listOfBullet) {
        if(!placed){
            return;
        }
        long currentTime = System.currentTimeMillis();
        if(currentTime - activationTime > TIME_DURACION){
            cleanUp();
            return;
        }
        updateEffect(lastAbilityTime);
        findTarget(listOfZomb);
        if(zomboid != null){
            double dx = zomboid.getX() - turretX;
            double dy = zomboid.getY() - turretY;
            double targetRotation = Math.atan2(dy, dx);

            // Плавный поворот
            double angleDiff = targetRotation - gunRotation;
            while (angleDiff > Math.PI) angleDiff -= 2 * Math.PI;
            while (angleDiff < -Math.PI) angleDiff += 2 * Math.PI;

            gunRotation += angleDiff * 0.1; // скорость поворота

            // Стреляем, если цель в прицеле
            if (Math.abs(angleDiff) < 0.2 && currentTime - lastShootTime > FIRE_RATE) {
                shoot(listOfBullet);
                lastShootTime = currentTime;
            }

        }
        if(showMuzzleFlash && currentTime - muzzleFlashTime > 100){
            showMuzzleFlash = false;
        }
    }

    private void shoot(List<Bullet> listOfBullet) {
        if(zomboid == null){
            return;
        }
        // Создаем пулю
        double gunTipX = turretX + Math.cos(gunRotation) * (SIZE * 0.6);
        double gunTipY = turretY + Math.sin(gunRotation) * (SIZE * 0.6);

        Bullet bullet = new Bullet((int)gunTipX, (int)gunTipY,
                Math.cos(gunRotation), (int) Math.sin(gunRotation), DMG, Color.ORANGE);
        listOfBullet.add(bullet);

        // Эффекты выстрела
        showMuzzleFlash = true;
        muzzleFlashTime = System.currentTimeMillis();

        // Добавляем эффект дыма
        turretEffects.add(new TurretEffect(gunTipX, gunTipY, TurretEffect.SMOKE));
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (!isActive) return;

        if (!placed) {
            // Показываем радиус размещения при выборе места
            g2d.setColor(HIGHT_LIGHT_COLOR);
            g2d.fillOval((int) (turretX - TURRET_RANGE), (int) (turretY - TURRET_RANGE),
                    (int) (TURRET_RANGE * 2), (int) (TURRET_RANGE * 2));
            g2d.setColor(Color.YELLOW);
            g2d.drawOval((int) (turretX - TURRET_RANGE), (int) (turretY - TURRET_RANGE),
                    (int) (TURRET_RANGE * 2), (int) (TURRET_RANGE * 2));

            // Рисуем предпросмотр турели
            g2d.setColor(new Color(255, 255, 255, 150));
            drawTurretBody(g2d, turretX, turretY, 0);

            return;
        }

        // Рисуем радиус действия
        g2d.setColor(new Color(0, 255, 0, 30));
        g2d.fillOval((int) (turretX - TURRET_RANGE), (int) (turretY - TURRET_RANGE),
                (int) (TURRET_RANGE * 2), (int) (TURRET_RANGE * 2));

        // Рисуем турель
        drawTurretBody(g2d, turretX, turretY, gunRotation);

        // Рисуем эффекты
        for (TurretEffect effect : turretEffects) {
            effect.draw(g2d);
        }

        // Индикатор времени жизни
        drawLifeIndicator(g2d);
    }

    private void drawTurretBody(Graphics2D g2d, int x, int y, double rotation) {
        Graphics2D g = (Graphics2D) g2d.create();

        // Основание турели
        g.setColor(BASE_COLOR);
        g.fillOval(x - SIZE/2, y - SIZE/2, SIZE, SIZE);

        // Обводка основания
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawOval(x - SIZE/2, y - SIZE/2, SIZE
                , SIZE);

        // Поворачиваем для ствола
        AffineTransform oldTransform = g.getTransform();
        g.translate(x, y);
        g.rotate(rotation);

        // Ствол турели
        g.setColor(GUN_COLOR);
        g.fillRect(-5, -8, (int)(SIZE * 0.8), 16);

        // Обводка ствола
        g.setColor(Color.BLACK);
        g.drawRect(-5, -8, (int)(SIZE * 0.8), 16);

        // Вспышка выстрела
        if (showMuzzleFlash) {
            g.setColor(Color.YELLOW);
            g.fillOval((int)(SIZE * 0.6), -6, 12, 12);
            g.setColor(Color.RED);
            g.fillOval((int)(SIZE * 0.6) + 2, -4, 8, 8);
        }

        g.setTransform(oldTransform);

        // Центр турели
        g.setColor(Color.DARK_GRAY);
        g.fillOval(x - 8, y - 8, 16, 16);
        g.setColor(Color.BLACK);
        g.drawOval(x - 8, y - 8, 16, 16);

        // Индикатор активности
        if (placed && zomboid != null) {
            g.setColor(Color.GREEN);
            g.fillOval(x - 3, y - 3, 6, 6);
        }

        g.dispose();
    }

    private void drawLifeIndicator(Graphics2D g2d) {
        if (!placed) return;

        long currentTime = System.currentTimeMillis();
        long timeLeft = TIME_DURACION - (currentTime - activationTime);
        double percentage = (double) timeLeft / TIME_DURACION;

        // Полоска времени жизни
        int barWidth = 60;
        int barHeight = 8;
        int barX = turretX - barWidth / 2;
        int barY = turretY - SIZE / 2 - 15;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(barX - 1, barY - 1, barWidth + 2, barHeight + 2);

        Color barColor = percentage > 0.3 ? Color.GREEN :
                percentage > 0.1 ? Color.YELLOW : Color.RED;
        g2d.setColor(barColor);
        g2d.fillRect(barX, barY, (int)(barWidth * percentage), barHeight);

        // Время в секундах
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        String timeStr = String.valueOf((int)(timeLeft / 1000) + 1);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(timeStr, barX + barWidth/2 - fm.stringWidth(timeStr)/2, barY - 2);
    }


    @Override
    public boolean isActive() {
        if(placed || isActive){
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public long getCouldown() {
        return COUDOWN_TIME;
    }

    @Override
    public long getRemaingCoudown() {
        if (!isActive()){
            return getCouldown() - (System.currentTimeMillis() - lastActivacion);
        }
        return 0;
    }

    @Override
    public boolean isReady() {
        if(placed){
            return false;
        }
        return System.currentTimeMillis() - lastActivacion >= COUDOWN_TIME;
    }

    @Override
    public void cleanUp() {
        isActive = false;
        placed = false;
        zomboid = null;
        turretEffects.clear();
        showMuzzleFlash = false;
    }
}
