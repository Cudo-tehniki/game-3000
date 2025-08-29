package zombi_shooter.player;

import zombi_shooter.player.Player;

import java.awt.*;

public abstract class Perk {

    private String name;
    private String description;
    private String buff;
    private String deBuff;
    private Color color;
    private String symbol;

    public Perk(String name, String description, String buff, String deBuff, Color color, String symbol) {
        this.name = name;
        this.description = description;
        this.buff = buff;
        this.deBuff = deBuff;
        this.color = color;
        this.symbol = symbol;
    }

    public abstract void applyEffect(Player player);

    public void drawCard(Graphics2D g2d, int x, int y, int width, int height, boolean isSelected) {
        // Тень карточки
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillRoundRect(x + 5, y + 5, width, height, 20, 20);

        // Основная карточка
        Color baseColor = isSelected ? color.brighter() : color;
        GradientPaint gradient = new GradientPaint(x, y, baseColor, x, y + height, baseColor.darker());
        g2d.setPaint(gradient);
        g2d.fillRoundRect(x, y, width, height, 20, 20);

        // Обводка карточки
        g2d.setColor(isSelected ? Color.YELLOW : Color.WHITE);
        g2d.setStroke(new BasicStroke(isSelected ? 3 : 2));
        g2d.drawRoundRect(x, y, width, height, 20, 20);

        // Иконка
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI Emoji", Font.BOLD, 48));
        FontMetrics fm = g2d.getFontMetrics();
        int iconX = x + (width - fm.stringWidth(symbol)) / 2;
        int iconY = y + 70;
        g2d.drawString(symbol, iconX, iconY);

        // Название перка
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        fm = g2d.getFontMetrics();
        int nameX = x + (width - fm.stringWidth(name)) / 2;
        g2d.drawString(name, nameX, y + 100);

        // Положительный эффект
        g2d.setColor(new Color(0, 255, 100));
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        drawWrappedText(g2d, buff, x + 10, y + 125, width - 20);

        // Отрицательный эффект
        g2d.setColor(new Color(255, 100, 100));
        drawWrappedText(g2d, deBuff, x + 10, y + 160, width - 20);

        // Описание
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        drawWrappedText(g2d, description, x + 10, y + 195, width - 20);
    }

    private void drawWrappedText(Graphics2D g2d, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g2d.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int currentY = y;

        for (String word : words) {
            String testLine = line.length() > 0 ? line + " " + word : word;
            if (fm.stringWidth(testLine) > maxWidth && line.length() > 0) {
                g2d.drawString(line.toString(), x, currentY);
                line = new StringBuilder(word);
                currentY += fm.getHeight();
            } else {
                line = new StringBuilder(testLine);
            }
        }
        if (line.length() > 0) {
            g2d.drawString(line.toString(), x, currentY);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuff() {
        return buff;
    }

    public void setBuff(String buff) {
        this.buff = buff;
    }

    public String getDeBuff() {
        return deBuff;
    }

    public void setDeBuff(String deBuff) {
        this.deBuff = deBuff;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
