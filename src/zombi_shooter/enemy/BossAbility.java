package zombi_shooter.enemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BossAbility {

    private AbilityType type;
    private int x;
    private int y;
    private int radius;
    private int duration;
    private boolean isActive;
    private int dmg;
    private int startTime;
    private int warningDuracion;
    private boolean isWarning;
    private double animationTime;


    public BossAbility(AbilityType type, int x, int y, int dmg) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.radius = 50;
        this.duration = 2000;
        this.isActive = true;
        this.dmg = dmg;
        this.startTime = (int) System.currentTimeMillis();
        this.warningDuracion = 2500;
        this.isWarning = true;
        this.animationTime = 0;
    }


    public void update() {
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - startTime;
        if(timePassed >= warningDuracion){
            isWarning = false;
            isActive = true;
            long activeTime = timePassed - warningDuracion;
            if(activeTime > duration){
                isActive = false;
            }
            radius = (int) (80 + (timePassed / 25));
        } else {
            isWarning = true;
            isActive = false;
            long progress = timePassed / warningDuracion;
            radius = (int) (50 + (progress * 30));
        }
    }

    public void draw(Graphics2D g2d) {
        if (!isActive) return;

        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - startTime;

        if (isWarning) {
            drawWarningPhase(g2d, timePassed);
        } else {
            drawActivePhase(g2d, timePassed);
        }
    }

    private void drawWarningPhase(Graphics2D g2d, long timePassed) {
        double progress = Math.min(1.0, (double) timePassed / warningDuracion);
        int warningRadius = (int) (radius * progress);

        boolean flash = (timePassed / 300) % 2 == 0;
        int alpha = flash ? 200 : 160;

        Color warningColor = new Color(255, 0, 0, alpha);
        g2d.setColor(warningColor);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawOval(x - warningRadius, y - warningRadius, warningRadius * 2, warningRadius * 2);

        RadialGradientPaint gradient = new RadialGradientPaint(
                x, y, warningRadius,
                new float[]{0.0f, 0.7f, 1.0f},
                new Color[]{
                        new Color(255, 0, 0, alpha / 2),
                        new Color(255, 100, 0, alpha / 3),
                        new Color(255, 0, 0, 0)
                }
        );
        g2d.setPaint(gradient);
        g2d.fillOval(x - warningRadius, y - warningRadius, warningRadius * 2, warningRadius * 2);

        int timeLeft = (int) ((warningDuracion - timePassed) / 1000) + 1;
        if (timeLeft > 0) {
            g2d.setColor(new Color(255, 255, 255, 255));
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics fm = g2d.getFontMetrics();
            String warningText = "⚠ " + type.getName().toUpperCase() + " ⚠";
            int textX = x - fm.stringWidth(warningText) / 2;
            int textY = y - warningRadius - 20;

            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRect(textX - 5, textY - fm.getAscent() - 2, fm.stringWidth(warningText) + 10, fm.getHeight() + 4);

            g2d.setColor(Color.WHITE);
            g2d.drawString(warningText, textX, textY);

            String timeText = timeLeft + "s";
            fm = g2d.getFontMetrics();
            textX = x - fm.stringWidth(timeText) / 2;

            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRect(textX - 5, y - fm.getAscent() + 3, fm.stringWidth(timeText) + 10, fm.getHeight() + 4);

            g2d.setColor(new Color(255, 255, 0, 255));
            g2d.drawString(timeText, textX, y + 10);
        }
    }


    private void drawActivePhase(Graphics2D g2d, long timePassed) {
        long activeTime = timePassed - warningDuracion;
        int alpha = (int) (255 * (1.0 - (double) activeTime / duration));
        alpha = Math.max(0, Math.min(255, alpha));

        Color effectColor = new Color(
                type.getColor().getRed(),
                type.getColor().getGreen(),
                type.getColor().getBlue(),
                alpha
        );

        g2d.setColor(effectColor);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);

        RadialGradientPaint gradient = new RadialGradientPaint(
                x, y, radius,
                new float[]{0.0f, 0.5f, 1.0f},
                new Color[]{
                        effectColor,
                        new Color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), alpha / 2),
                        new Color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), 0)
                }
        );
        g2d.setPaint(gradient);
        g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        g2d.setColor(new Color(255, 255, 255, alpha));
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x - fm.stringWidth(type.getName()) / 2;
        int textY = y - radius - 10;
        g2d.drawString(type.getName(), textX, textY);
    }

    public boolean isPlayerHit(int pX, int pY, int pSize) {
        if (!isActive || isWarning) {
            return false;
        }
        double distance = Math.sqrt(Math.pow(pX - x, 2) + Math.pow(pY - y, 2));
        return distance <= radius + pSize;
    }

    public static List<AbilityType> getAbilityForLevel(int level){
        List<AbilityType> types = new ArrayList<>();
        switch (level){
            case 1:
                types.add(AbilityType.HEAVY_PUNCH);
                break;
            case 2:
                types.add(AbilityType.POISON_CLOUD);
                break;
            case 3:
                types.add(AbilityType.FIRE_BREATH);
                break;
            case 4:
                types.add(AbilityType.RUSH);
                break;
            default:
                types.add(AbilityType.RUSH);
                types.add(AbilityType.FIRE_BREATH);
        }
        return types;
    }

    public AbilityType getType() {
        return type;
    }

    public void setType(AbilityType type) {
        this.type = type;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
}
