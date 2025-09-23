package zombi_shooter.player.abilyti;

import zombi_shooter.Bullet;
import zombi_shooter.Zomboid;

import java.awt.*;
import java.util.List;

public interface Ability {

    boolean activate(int playerX, int playerY, int mouseX, int mouseY);
    void update(long lastAbilityTime, List<Zomboid> listOfZomb, List<Bullet> listOfBullet);
    void draw(Graphics2D g2d);
    void draw(Graphics2D g2d, int mouseX, int mouseY);
    boolean isActive();
    String getName();
    String description();
    long getCouldown();
    long getRemaingCoudown();
    boolean isReady();
    void cleanUp();
}
