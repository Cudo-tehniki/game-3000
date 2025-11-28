package zombi_shooter.player;

import java.awt.*;

public class RedKlinokPerk extends Perk{

    public RedKlinokPerk() {
        super("Red klinok", "За каджого убитого зомби + 1хп", "+killToHP", "Speed -15%", new Color(131, 0, 0), "🗡️", "turret", "robot");


    }

    @Override
    public void applyEffect(Player player) {
        int increceSpeed = (int) (player.getBaseSpeed() * 0.15);
        player.setBaseSpeed(player.getBaseSpeed() - increceSpeed);
    }
}
