package zombi_shooter.enemy;

import java.awt.*;

public enum AbilityType {

    HEAVY_PUNCH("heavy punch", 2, new Color(255, 0,0)),
    POISON_CLOUD("poison cloud", 3, new Color(0, 143, 71)),
    FIRE_BREATH("fire breath", 0.5, new Color(200, 113, 0)),
    RUSH("rush", 4, new Color(97, 97, 97));

    private final String name;
    private final double dmgMultiplayer;
    private final Color color;

    AbilityType(String name, double dmgMultiplayer, Color color) {
        this.name = name;
        this.dmgMultiplayer = dmgMultiplayer;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public double getDmgMultiplayer() {
        return dmgMultiplayer;
    }

    public Color getColor() {
        return color;
    }
}
