package zombi_shooter.player.abilyti;

import zombi_shooter.Bullet;
import zombi_shooter.Zomboid;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class RobotAbility implements Ability {

    private static final String NAME = "Robot";
    private static final String DESCRIPTION = "Spawn robot";
    private static final long COOLDOWN = 15000;
    private static final long DURACION = 15000;
    private static final int RANGE = 150;
    private static final int DMG = 40;
    private static final int ATTACK_RATE = 750;
    private static final int SIZE = 30;
    private static final double SPEED = 2.0;
    private static final Color BASE_COLOR = new Color(0, 0, 80);
    private static final Color DETAIL_COLOR = new Color(0, 0, 55);
    private static final Color HIGHT_LIGHT_COLOR = new Color(0, 0, 150);

    private boolean isActive = false;
    private boolean placed = false;
    private long lastActivacion = 0;
    private long lastAttackTime = 0;
    private long activactionTime = 0;
    private double x = 0;
    private double y = 0;
    private double direction = 0.0;
    private Zomboid target = null;
    private long attackEfectTime = 0;
    private boolean showAttackEffect = false;
    private List<RobotEffect> robotEffects = new ArrayList<>();

    @Override
    public boolean activate(int playerX, int playerY, int mouseX, int mouseY) {
        if (isReady()) {
            if (!isActive && !placed) {
                isActive = true;
                return true;
            }
            if (!placed) {
                x = mouseX;
                y = mouseY;
                placed = true;
                lastActivacion = System.currentTimeMillis();
                activactionTime = System.currentTimeMillis();
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(long lastAbilityTime, List<Zomboid> listOfZomb, List<Bullet> listOfBullet) {
        if (!placed) {
            return;
        }
        long time = System.currentTimeMillis();
        if (time - lastActivacion >= DURACION) {
            return;
        }
        updateEffect(lastAbilityTime);
        findTarget(listOfZomb);
        if (target != null) {
            double dX = target.getX() - x;
            double dY = target.getY() - y;
            double distance = Math.sqrt(dX * dX + dY * dY);
            direction = Math.atan2(dY, dX);
            if (distance > 15) {
                x += Math.cos(direction) * SPEED;
                y += Math.sin(direction) * SPEED;
            } else {
                if(time - lastAttackTime > ATTACK_RATE){
                    attack();
                    lastAttackTime = time;
                }
            }
        }
        if (showAttackEffect && time - attackEfectTime > 200){
            showAttackEffect = false;
        }
    }

    public void attack() {
        if (target == null) {
            return;
        }
        double distance = Math.sqrt(Math.pow(target.getX() - x, 2) +
                Math.pow(target.getY() - y, 2));
        if (distance <= 20) {
            target.takeDmg(DMG);
            if (target.isDead()) {
                target = null;
            }
            showAttackEffect = true;
            lastAttackTime = System.currentTimeMillis();
            robotEffects.add(new RobotEffect(x, y, RobotEffect.ATTACK));
        }
    }

    public void updateEffect(long lastAbilityTime) {
        robotEffects.removeIf(r -> {
            r.update(lastAbilityTime);
            return !r.isActive();
        });
    }

    public void findTarget(List<Zomboid> zomboidList) {
        if (target == null) {
            double minRange = RANGE;
            for (Zomboid zomb : zomboidList) {
                if (zomb.isDead()) {
                    continue;
                }
                double distance = Math.sqrt(Math.pow(zomb.getX() - x, 2) +
                        Math.pow(zomb.getY() - y, 2));

                if (distance <= minRange) {
                    minRange = distance;
                    target = zomb;
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (!placed) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - activactionTime > DURACION) {
            return;
        }

        drawRobot(g2d, (int)x, (int)y, direction);

        for (RobotEffect effect : robotEffects) {
            effect.draw(g2d);
        }

        drawLifeIndicator(g2d);
    }

    @Override
    public void draw(Graphics2D g2d, int mouseX, int mouseY) {
        if (!placed && !isReady()) {
            return;
        }

        if (!isActive) return;

        if (!placed) {
            g2d.setColor(new Color(HIGHT_LIGHT_COLOR.getRed(), HIGHT_LIGHT_COLOR.getGreen(), HIGHT_LIGHT_COLOR.getBlue(), 50));
            g2d.fillOval((int) (mouseX - RANGE), (int) (mouseY - RANGE),
                    (int) (RANGE * 2), (int) (RANGE * 2));
            g2d.setColor(Color.YELLOW);
            g2d.drawOval((int) (mouseX - RANGE), (int) (mouseY - RANGE),
                    (int) (RANGE * 2), (int) (RANGE * 2));

            g2d.setColor(new Color(255, 255, 255, 150));
            drawRobot(g2d, mouseX, mouseY, 0);

            return;
        }

        drawRobot(g2d, (int)x, (int)y, direction);

        for (RobotEffect effect : robotEffects) {
            effect.draw(g2d);
        }

        drawLifeIndicator(g2d);
    }

    private void drawRobot(Graphics2D g2d, int x, int y, double rotation) {
        Graphics2D g = (Graphics2D) g2d.create();

        AffineTransform oldTransform = g.getTransform();
        g.translate(x, y);
        g.rotate(rotation);

        g.setColor(BASE_COLOR);
        g.fillOval(-SIZE/2, -SIZE/2, SIZE, SIZE);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawOval(-SIZE/2, -SIZE/2, SIZE, SIZE);

        g.setColor(DETAIL_COLOR);
        g.fillRect(-SIZE/3, -SIZE/2, SIZE*2/3, SIZE/3);

        g.setColor(Color.BLACK);
        g.drawRect(-SIZE/3, -SIZE/2, SIZE*2/3, SIZE/3);

        if(showAttackEffect){
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(3));
            g.drawLine(SIZE/2, 0, SIZE/2 + 15, 0);
            g.setColor(Color.ORANGE);
            g.fillOval(SIZE/2 + 10, -5, 10, 10);
        }

        g.setTransform(oldTransform);

        if (placed && target != null) {
            g.setColor(Color.GREEN);
            g.fillOval(x - 3, y - 3, 6, 6);
        }

        g.dispose();
    }

    private void drawLifeIndicator(Graphics2D g2d) {
        if (!placed) return;

        long currentTime = System.currentTimeMillis();
        long timeLeft = DURACION - (currentTime - activactionTime);
        double percentage = (double) timeLeft / DURACION;

        int barWidth = 60;
        int barHeight = 8;
        int barX = (int)x - barWidth / 2;
        int barY = (int)y - SIZE / 2 - 15;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(barX - 1, barY - 1, barWidth + 2, barHeight + 2);

        Color barColor = percentage > 0.3 ? Color.GREEN :
                percentage > 0.1 ? Color.YELLOW : Color.RED;
        g2d.setColor(barColor);
        g2d.fillRect(barX, barY, (int)(barWidth * percentage), barHeight);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        String timeStr = String.valueOf((int)(timeLeft / 1000) + 1);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(timeStr, barX + barWidth/2 - fm.stringWidth(timeStr)/2, barY - 2);
    }

    @Override
    public boolean isActive() {
        if(!placed){
            return true;
        } else {
            long time = System.currentTimeMillis();
            if(time - activactionTime <= DURACION){
                return true;
            }
        }
        return false;
    }

    public boolean isPlaced(){
        return placed;
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
        return COOLDOWN;
    }

    @Override
    public long getRemaingCoudown() {
        long time1 = System.currentTimeMillis() - lastActivacion;
        return time1 - COOLDOWN;
    }

    @Override
    public boolean isReady() {
        if (placed) {
            return false;
        }
        return System.currentTimeMillis() - lastActivacion > COOLDOWN;
    }

    @Override
    public void cleanUp() {
        isActive = false;
        placed = false;
        target = null;
        robotEffects.clear();
        showAttackEffect = false;

    }
}
