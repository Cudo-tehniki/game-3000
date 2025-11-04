package zombi_shooter.enemy;

import java.awt.*;

public enum LocationType {

    CITY("city", new Color(43, 124,0), new Color(0,0,0, 0)),
    SWAMP("swamp", new Color(30, 87, 0), new Color(44, 122, 0, 21)),
    VULCAN("vulcan", new Color(147, 34, 0), new Color(131,0,0, 36)),
    DESERT("desert", new Color(255, 206, 161), new Color(255, 242, 142, 39));

    private final String name;
    private final Color color;
    private final Color skyColor;

    LocationType(String name, Color color, Color skyColor) {
        this.name = name;
        this.color = color;
        this.skyColor = skyColor;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Color getSkyColor() {
        return skyColor;
    }
}
