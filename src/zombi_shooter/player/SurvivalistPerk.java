package zombi_shooter.player;

import zombi_shooter.Weapon;

import java.awt.*;

public class SurvivalistPerk extends Perk{

    private Weapon.WeaponType weaponType;

    public SurvivalistPerk() {
        super("Survivalist", "Вы готовы ко всему", "spawn with SHOTGUN", "HP -80%", new Color(255, 235, 169), "⛏️", "turret", "robot");
        weaponType = Weapon.WeaponType.SHOTGUN;
    }

    @Override
    public void applyEffect(Player player) {
        int playerHP = (int) (0.8 * player.getMaxHealth());
        player.setMaxHealth(player.getMaxHealth() - playerHP);
    }

    public Weapon.WeaponType getWeaponType() {
        return weaponType;
    }
}
