package zombi_shooter.player;

import java.awt.*;

public class ChancePerk extends Perk{

    public ChancePerk() {
        super("Chance", "Больше шанс на редкие предметы", "+ better luck", "HP -35%", new Color(149, 106, 0), "🎰");
    }

    @Override
    public void applyEffect(Player player) {

    }
}
