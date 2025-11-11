package zombi_shooter.player.abilyti;

import zombi_shooter.Bullet;
import zombi_shooter.Zomboid;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class TuretAbility implements Ability {

    private static final String NAME = "Turret";
    private static final String DESCRIPTION = "спавнит турель";
    private static final long COUDOWN_TIME = 1000;
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
    private List<TurretEffect> turretEffects = new ArrayList<>();

    @Override
    public boolean activate(int playerX, int playerY, int mouseX, int mouseY) {
        System.out.println("TuretAbility.activate() called. isReady: " + isReady() + ", isActive: " + isActive + ", placed: " + placed);
        if (isReady()) {
            if (!isActive && !placed) {
                isActive = true;
                System.out.println("Turret activation mode enabled");
                return true;
            }
            if (isActive && !placed) {
                turretX = mouseX;
                turretY = mouseY;
                placed = true;
                activationTime = System.currentTimeMillis();
                lastActivacion = System.currentTimeMillis();
                System.out.println("Turret placed at: " + turretX + ", " + turretY);
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
        if (zomboid != null) {
            System.out.println("Turret found target: zombie at (" + zomboid.getX() + ", " + zomboid.getY() + "), distance: " + minDistant);
        }
    }

    public void updateEffect(long lastAbilityTime){
        turretEffects.removeIf(turretEffect -> {
            turretEffect.update(lastAbilityTime);
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
            // Время истекло - турель больше не функционирует, но не сбрасываем placed
            // Это позволит AbilityManager удалить турель из списка
            System.out.println("Turret time expired");
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
                gunRotation, 8, DMG, Color.ORANGE);  // Используем gunRotation как направление и скорость 8
        listOfBullet.add(bullet);
        
        System.out.println("Turret shooting at angle: " + gunRotation + " towards zombie at (" + zomboid.getX() + ", " + zomboid.getY() + ")");

        // Эффекты выстрела
        showMuzzleFlash = true;
        muzzleFlashTime = System.currentTimeMillis();

        // Добавляем эффект дыма
        turretEffects.add(new TurretEffect(gunTipX, gunTipY, TurretEffect.SMOKE));
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Рисуем турель только если она размещена и время не истекло
        if (!placed) {
            return; // Не размещена или на кулдауне - не рисуем
        }
        
        // Проверяем, что время жизни не истекло
        long currentTime = System.currentTimeMillis();
        if (currentTime - activationTime > TIME_DURACION) {
            return; // Время истекло - не рисуем
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

    @Override
    public void draw(Graphics2D g2d, int mouseX, int mouseY) {
        // Не рисуем ничего, если турель на кулдауне (не готова и не размещена)
        if (!placed && !isReady()) {
            return; // На кулдауне - не показываем предпросмотр
        }
        
        if (!isActive) return;

        if (!placed) {
            // Показываем радиус размещения при выборе места
            g2d.setColor(new Color(HIGHT_LIGHT_COLOR.getRed(), HIGHT_LIGHT_COLOR.getGreen(), HIGHT_LIGHT_COLOR.getBlue(), 50));
            g2d.fillOval((int) (mouseX - TURRET_RANGE), (int) (mouseY - TURRET_RANGE),
                    (int) (TURRET_RANGE * 2), (int) (TURRET_RANGE * 2));
            g2d.setColor(Color.YELLOW);
            g2d.drawOval((int) (mouseX - TURRET_RANGE), (int) (mouseY - TURRET_RANGE),
                    (int) (TURRET_RANGE * 2), (int) (TURRET_RANGE * 2));

            // Рисуем предпросмотр турели
            g2d.setColor(new Color(255, 255, 255, 150));
            drawTurretBody(g2d, mouseX, mouseY, 0);

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
        // Турель считается "активной" для UI если:
        // 1. Она не размещена (доступна для размещения) 
        // 2. Она размещена и время жизни не истекло
        // 3. Она на кулдауне (чтобы показывать время перезарядки)
        if (!placed) {
            return true; // Доступна для использования или на кулдауне
        } else {
            // Размещена - проверяем время жизни
            long currentTime = System.currentTimeMillis();
            boolean timeNotExpired = currentTime - activationTime <= TIME_DURACION;
            return timeNotExpired;
        }
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
        if (isReady()) {
            return 0; // Готова к использованию
        }
        long remaining = getCouldown() - (System.currentTimeMillis() - lastActivacion);
        return Math.max(0, remaining); // Не возвращаем отрицательные значения
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
    
    public void resetForCooldown() {
        // Сбрасываем состояние турели для начала кулдауна, но сохраняем lastActivacion
        isActive = false;
        placed = false;
        zomboid = null;
        turretEffects.clear();
        showMuzzleFlash = false;
        // lastActivacion НЕ сбрасываем - он нужен для расчета кулдауна
        System.out.println("Turret reset for cooldown. Last activation: " + lastActivacion);
    }
    
    public boolean isPlaced() {
        return placed;
    }
}
