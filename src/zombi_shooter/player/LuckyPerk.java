package zombi_shooter.player;

import java.awt.*;

public class LuckyPerk extends Perk{

    public LuckyPerk() {
        super("Luck", "Больше шанс на редкие предметы", "+ better luck", "HP -35%", new Color(149, 106, 0), "🎰", "robot");
    }

    @Override
    public void applyEffect(Player player) {
        player.setLuckyMultiplayer(player.getLuckyMultiplayer() + 30);
        player.setMaxHealth((int) (player.getMaxHealth() * 0.65));
    }
}
