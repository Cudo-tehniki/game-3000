package zombi_shooter;

import java.awt.*;

public class ChestItem {
    public enum ItemType {
        WEAPON("Weapon", Color.ORANGE),
        AMMO("Ammo", Color.YELLOW),
        HEALTH("Health", Color.RED),
        SCORE_BONUS("Score Bonus", Color.CYAN);

        private final String name;
        private final Color color;

        ItemType(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public String getName() { return name; }
        public Color getColor() { return color; }
    }

    private ItemType type;
    private Object value; // Может быть Weapon, Integer для патронов/здоровья/очков
    private String description;

    public ChestItem(ItemType type, Object value, String description) {
        this.type = type;
        this.value = value;
        this.description = description;
    }

    // Фабричные методы для создания различных предметов
    public static ChestItem createWeapon(Weapon.WeaponType weaponType) {
        Weapon weapon = new Weapon(weaponType);
        return new ChestItem(ItemType.WEAPON, weapon, "New " + weapon.getName());
    }

    public static ChestItem createAmmo(int amount) {
        return new ChestItem(ItemType.AMMO, amount, "+" + amount + " Ammo");
    }

    public static ChestItem createHealth(int amount) {
        return new ChestItem(ItemType.HEALTH, amount, "+" + amount + " Health");
    }

    public static ChestItem createScoreBonus(int amount) {
        return new ChestItem(ItemType.SCORE_BONUS, amount, "+" + amount + " Points");
    }

    // Генерация случайного предмета с настраиваемыми шансами
    public static ChestItem generateRandomItem() {
        int random = (int) (Math.random() * 100);

        if (random < 50) {
            // 50% - оружие
            return createWeapon(Weapon.getRandomWeapon());
        } else if (random < 70) {
            // 20% - патроны
            int ammoAmount = 20 + (int) (Math.random() * 31); // 20-50 патронов
            return createAmmo(ammoAmount);
        } else if (random < 85) {
            // 15% - здоровье
            int healthAmount = 25 + (int) (Math.random() * 26); // 25-50 здоровья
            return createHealth(healthAmount);
        } else {
            // 15% - бонус очков
            int scoreBonus = 100 + (int) (Math.random() * 201); // 100-300 очков
            return createScoreBonus(scoreBonus);
        }
    }

    public void draw(Graphics2D g2d, int x, int y) {
        // Рисуем иконку предмета
        g2d.setColor(type.getColor());
        g2d.fillOval(x - 8, y - 8, 16, 16);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x - 8, y - 8, 16, 16);

        // Рисуем символ в зависимости от типа
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        String symbol = getSymbol();
        int textWidth = fm.stringWidth(symbol);
        g2d.drawString(symbol, x - textWidth/2, y + 4);
    }

    private String getSymbol() {
        switch (type) {
            case WEAPON: return "W";
            case AMMO: return "A";
            case HEALTH: return "H";
            case SCORE_BONUS: return "S";
            default: return "?";
        }
    }

    // Геттеры
    public ItemType getType() { return type; }
    public Object getValue() { return value; }
    public String getDescription() { return description; }

    public Weapon getWeapon() {
        return type == ItemType.WEAPON ? (Weapon) value : null;
    }

    public Integer getIntValue() {
        return (type == ItemType.AMMO || type == ItemType.HEALTH || type == ItemType.SCORE_BONUS)
                ? (Integer) value : null;
    }
}