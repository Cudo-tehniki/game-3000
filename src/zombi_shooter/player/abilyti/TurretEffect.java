package zombi_shooter.player.abilyti;

import java.awt.*;
import java.util.List;

public class TurretEffect {
    public static final int SMOKE = 1;

    private double x, y;
    private int type;
    private long startTime;
    private long duration;
    private Color color;
    private double size;

    public TurretEffect(double x, double y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.startTime = System.currentTimeMillis();

        if (type == SMOKE) {
            this.duration = 1000;
            this.color = Color.LIGHT_GRAY;
            this.size = 5;
        }
    }

    public void update(long deltaTime) {
        if (type == SMOKE) {
            y -= 0.5;
            size += 0.1;
        }
    }

    public void draw(Graphics2D g2d) {
        long elapsed = System.currentTimeMillis() - startTime;
        double alpha = 1.0 - (double) elapsed / duration;

        if (alpha <= 0) return;

        Color drawColor = new Color(color.getRed(), color.getGreen(),
                color.getBlue(), (int)(alpha * 255));
        g2d.setColor(drawColor);
        g2d.fillOval((int)(x - size), (int)(y - size),
                (int)(size * 2), (int)(size * 2));
    }

    public boolean isActive() {
        return System.currentTimeMillis() - startTime < duration;
    }

    public void updateEffect(long lastAbilityTime, List<TurretEffect> turretEffects){
        turretEffects.removeIf(turretEffect -> {
            turretEffect.updateEffect(lastAbilityTime, turretEffects);
            return !turretEffect.isActive();
        });
    }
}
