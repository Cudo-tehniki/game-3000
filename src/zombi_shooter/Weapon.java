package zombi_shooter;

import java.awt.*;

public class Weapon {
    public enum WeaponType {
        PISTOL("Pistol", 300, 1, 5, Color.GRAY, 1),
        RIFLE("Rifle", 150, 2, 8, Color.DARK_GRAY, 1),
        SHOTGUN("Shotgun", 600, 3, 10, Color.RED, 2),
        MACHINE_GUN("Machine Gun", 80, 1, 12, Color.BLACK, 3),
        SNIPER("Sniper", 1000, 5, 15, Color.GREEN, 0);

        private final String name;
        private final int fireRate; // миллисекунды между выстрелами
        private final int damage;
        private final int bulletSpeed;
        private final Color bulletColor;
        private final int rarity; // 0-4, где 4 - самое редкое

        WeaponType(String name, int fireRate, int damage, int bulletSpeed, Color bulletColor, int rarity) {
            this.name = name;
            this.fireRate = fireRate;
            this.damage = damage;
            this.bulletSpeed = bulletSpeed;
            this.bulletColor = bulletColor;
            this.rarity = rarity;
        }

        public String getName() { return name; }
        public int getFireRate() { return fireRate; }
        public int getDamage() { return damage; }
        public int getBulletSpeed() { return bulletSpeed; }
        public Color getBulletColor() { return bulletColor; }
        public int getRarity() { return rarity; }
    }

    private WeaponType type;
    private int ammo;
    private int maxAmmo;

    public Weapon(WeaponType type) {
        this.type = type;
        this.maxAmmo = getMaxAmmoForType(type);
        this.ammo = maxAmmo;
    }

    public boolean isShotgun(){
        if (type == WeaponType.SHOTGUN){
            return  true;
        } else {
            return false;
        }
    }

    private int getMaxAmmoForType(WeaponType type) {
        switch (type) {
            case PISTOL: return 50;
            case RIFLE: return 30;
            case SHOTGUN: return 20;
            case MACHINE_GUN: return 100;
            case SNIPER: return 10;
            default: return 30;
        }
    }

    public boolean canShoot() {
        return ammo > 0;
    }

    public void shoot() {
        if (canShoot()) {
            ammo--;
        }
    }

    public void reload() {
        ammo = maxAmmo;
    }

    public static WeaponType getRandomWeapon() {
        // Система редкости: чем выше редкость, тем меньше шанс выпадения
        int random = (int) (Math.random() * 100);

        if (random < 40) return WeaponType.PISTOL;     // 40%
        else if (random < 65) return WeaponType.RIFLE;  // 25%
        else if (random < 80) return WeaponType.SHOTGUN; // 15%
        else if (random < 95) return WeaponType.MACHINE_GUN; // 15%
        else return WeaponType.SNIPER;                  // 5%
    }

    // Геттеры
    public WeaponType getType() { return type; }
    public int getAmmo() { return ammo; }
    public int getMaxAmmo() { return maxAmmo; }
    public String getName() { return type.getName(); }
    public int getFireRate() { return type.getFireRate(); }
    public int getDamage() { return type.getDamage(); }
    public int getBulletSpeed() { return type.getBulletSpeed(); }
    public Color getBulletColor() { return type.getBulletColor(); }
    public int getRarity() { return type.getRarity(); }
}