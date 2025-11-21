package zombi_shooter.player.abilyti;

import java.awt.*;

public class RobotEffect {

    public static final int ATTACK = 1;

    private double x, y;
    private int type;
    private long startTime;
    private long duration;
    private Color color;
    private double size;

    public RobotEffect(double x, double y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.startTime = System.currentTimeMillis();

        if (type == ATTACK) {
            this.duration = 300;
            this.color = Color.RED;
            this.size = 8;
        }
    }

    public void update(long deltaTime) {
        if (type == ATTACK) {
            size += 0.3;
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
}
