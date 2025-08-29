package zombi_shooter;

import java.awt.*;

class Chest {
    private final int SIZE = 30;
    private final Color CLOSED_COLOR = new Color(139, 69, 19); // Коричневый цвет
    private final Color OPEN_COLOR = new Color(205, 133, 63);  // Светло-коричневый
    private final Color GOLD_COLOR = new Color(255, 215, 0);   // Золотой цвет для украшений

    private int x;
    private int y;
    private boolean isOpen;
    private boolean canOpen; // Можно ли открыть (игрок рядом)
    private ChestItem item; // Предмет внутри сундука
    private boolean itemTaken; // Взят ли предмет

    public Chest(int x, int y) {
        this.x = x;
        this.y = y;
        this.isOpen = false;
        this.canOpen = false;
        this.itemTaken = false;
        this.item = ChestItem.generateRandomItem(); // Генерируем случайный предмет
    }

    public void draw(Graphics2D g2d) {
        // Основной корпус сундука
        g2d.setColor(isOpen ? OPEN_COLOR : CLOSED_COLOR);
        g2d.fillRect(x - SIZE/2, y - SIZE/2, SIZE, SIZE);

        // Обводка
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x - SIZE/2, y - SIZE/2, SIZE, SIZE);

        if (!isOpen) {
            // Замок для закрытого сундука
            g2d.setColor(GOLD_COLOR);
            g2d.fillRect(x - 4, y - 6, 8, 8);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x - 4, y - 6, 8, 8);
            g2d.fillOval(x - 2, y - 8, 4, 4);
            g2d.setColor(GOLD_COLOR);
            g2d.fillOval(x - 1, y - 7, 2, 2);
        } else {
            // Открытый сундук
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(x - 10, y - 6, 20, 6);

            g2d.setColor(new Color(80, 40, 20));
            g2d.fillRect(x + 5, y, 5, 10);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(x - 10, y - 6, 20, 6);
            g2d.drawRect(x + 5, y, 5, 10);

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillOval(x + 3, y - 2, 2, 2);

            // Рисуем предмет, если он не взят
            if (!itemTaken && item != null) {
                item.draw(g2d, x, y + 8);

                // Показываем описание предмета
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                FontMetrics fm = g2d.getFontMetrics();
                String desc = item.getDescription();
                int textWidth = fm.stringWidth(desc);

                // Фон для текста
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRect(x - textWidth/2 - 2, y + 20, textWidth + 4, 12);

                g2d.setColor(Color.WHITE);
                g2d.drawString(desc, x - textWidth/2, y + 30);
            }
        }

        // Индикатор возможности открытия/взятия
        if (canOpen) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g2d.getFontMetrics();

            String text;
            if (!isOpen) {
                text = "Press E";
            } else if (!itemTaken) {
                text = "Press E";
            } else {
                return; // Не показываем текст, если предмет уже взят
            }

            int textWidth = fm.stringWidth(text);

            // Фон для текста
            g2d.setColor(new Color(0, 0, 0, 128));
            g2d.fillRect(x - textWidth/2 - 2, y - SIZE/2 - 17, textWidth + 4, 14);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x - textWidth/2 - 2, y - SIZE/2 - 17, textWidth + 4, 14);

            g2d.setColor(Color.WHITE);
            g2d.drawString(text, x - textWidth/2, y - SIZE/2 - 5);
        }
    }

    public boolean open() {
        if (canOpen && !isOpen) {
            isOpen = true;
            return true;
        }
        return false;
    }

    public ChestItem takeItem() {
        if (isOpen && !itemTaken && canOpen) {
            itemTaken = true;
            return item;
        }
        return null;
    }

    public boolean isPlayerNear(int playerX, int playerY) {
        double distance = Math.sqrt(Math.pow(playerX - x, 2) + Math.pow(playerY - y, 2));
        canOpen = distance < 50;
        return canOpen;
    }

    public boolean isEmpty() {
        return isOpen && itemTaken;
    }

    // Геттеры и сеттеры
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean canOpen() {
        return canOpen;
    }

    public boolean canTakeItem() {
        return isOpen && !itemTaken && canOpen;
    }

    public int getSize() {
        return SIZE;
    }

    public ChestItem getItem() {
        return item;
    }
}