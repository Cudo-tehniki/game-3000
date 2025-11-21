package zombi_shooter.player;

import java.awt.*;

public class TankPerk extends Perk{
    public TankPerk() {
        super("Tank", "Джагернаут", "HP +100%", "Speed -20%", new Color(115, 115, 115), "🛡️", "turret", "robot");
    }

    @Override
    public void applyEffect(Player player) {
        player.setMaxHealth(player.getPlayerHealth() * 2);
        player.setPlayerHealth(player.getMaxHealth());
        int increeseSpeed = (int) (player.getBaseSpeed() * 0.2);
        player.setBaseSpeed(player.getBaseSpeed() - increeseSpeed);
    }
}
