package zombi_shooter.enemy;

import java.awt.*;

public enum EnemyType {

    MELEE_ZOMBIE("melee zomb", new Color(255, 0, 0), 1, 1, 1),
    POISON_ZOMBIE("poison zomb", new Color(183, 255, 0), 0.75, 1.15, 1),
    FIRE_ZOMBIE("fire zomb", new Color(255, 111, 34), 1, 1.30, 0.50),
    TANK_ZOMBIE("tank zomb", new Color(143, 143, 143), 0.5, 3, 1.50);

    private final String name;
    private final Color color;
    private final double speedMultiplayer;
    private final double hpMultiplayer;
    private final double dmgMultiplayer;


    EnemyType(String name, Color color, double speedMultiplayer, double hpMultiplayer, double dmgMultiplayer) {
        this.name = name;
        this.color = color;
        this.speedMultiplayer = speedMultiplayer;
        this.hpMultiplayer = hpMultiplayer;
        this.dmgMultiplayer = dmgMultiplayer;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public double getSpeedMultiplayer() {
        return speedMultiplayer;
    }

    public double getHpMultiplayer() {
        return hpMultiplayer;
    }

    public double getDmgMultiplayer() {
        return dmgMultiplayer;
    }
}
