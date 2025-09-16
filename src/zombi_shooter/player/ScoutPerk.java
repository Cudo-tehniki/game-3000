package zombi_shooter.player;

import java.awt.*;

public class ScoutPerk extends  Perk{

    public ScoutPerk() {
        super("Light", "Бегун", "Speed +50%", "HP -50%", new Color(0, 224, 255), "👟", "turret");
    }

    @Override
    public void applyEffect(Player player) {
        player.setMaxHealth(player.getMaxHealth() / 2);
        int playerSpeed = player.getBaseSpeed() / 2;
        player.setBaseSpeed(player.getBaseSpeed() + playerSpeed);
    }
}
