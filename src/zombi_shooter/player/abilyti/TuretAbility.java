package zombi_shooter.player.abilyti;

import zombi_shooter.Bullet;
import zombi_shooter.Zomboid;

import java.awt.*;
import java.util.List;

public class TuretAbility implements Ability {

    private static final String NAME = "Turret";
    private static final String DESCRIPTION = "спавнит турель";
    private static final long COUDOWN_TIME = 40000;
    private static final long TIME_DURACION = 20000;
    private static final long TURRET_RANGE = 200;
    private static final int DMG = 3;
    private static final int FIRE_RATE = 150;
    private static final int SIZE = 13;
    private static final Color BASE_COLOR = new Color(0, 255, 248, 255);
    private static final Color GUN_COLOR = new Color(89, 89, 89);
    private static final Color HIGHT_LIGHT_COLOR = new Color(0, 193, 123);

    private boolean isActive = false;
    private boolean placed = false;
    private long lastActivacion = 0;
    private long lastShootTime = 0;
    private long activationTime = 0;
    private int turretX = 0;
    private int turretY = 0;
    private double gunRotation = 0.0;
    private Zomboid zomboid = null;
    private boolean showMuzzleFlash = false;
    private long muzzleFlashTime = 0;
    private List<TurretEffect> turretEffects;

    @Override
    public boolean activate(int playerX, int playerY, int mouseX, int mouseY) {
        if (isReady()) {
            if (!isActive && !placed) {
                isActive = true;
                return true;
            }
            if (isActive && !placed) {
                turretX = mouseX;
                turretY = mouseY;
                placed = true;
                activationTime = System.currentTimeMillis();
                lastActivacion = System.currentTimeMillis();
                return true;
            }
        }
        return false;
    }

    public void findTarget(List<Zomboid> zomboidList) {
        zomboid = null;
        double minDistant = TURRET_RANGE;
        for (Zomboid zomb : zomboidList) {
            double distance = Math.sqrt(Math.pow(zomb.getX() - turretX, 2) +
                    Math.pow(zomb.getY() - turretY, 2));
            if(distance <= minDistant){
                minDistant = distance;
                zomboid = zomb;
            }
        }
    }

    @Override
    public void update(long lastAbilityTime, List<Zomboid> listOfZomb, List<Bullet> listOfBullet) {
        if(!placed){
            return;
        }
        long currentTime = System.currentTimeMillis();
        if(currentTime - activationTime > TIME_DURACION){
            cleanUp();
            return;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public long getCouldown() {
        return 0;
    }

    @Override
    public long getRemaingCoudown() {
        return 0;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void cleanUp() {

    }
}
