package zombi_shooter.player;

public class Camera {
    private int x;
    private int y;
    private int sizeOfWidth;
    private int sizeOfHeight;

    public Camera(double startX, double startY, int sizeOfWidth, int sizeOfHeight) {
        this.sizeOfWidth = sizeOfWidth;
        this.sizeOfHeight = sizeOfHeight;
        // Используем setPosition для применения ограничений с самого начала
        setPosition((int) startX, (int) startY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSizeOfWidth() {
        return sizeOfWidth;
    }

    public int getSizeOfHeight() {
        return sizeOfHeight;
    }

    /**
     * Центрирует камеру на заданной позиции
     */
    public void setPosition(int centerX, int centerY) {
        this.x = centerX - sizeOfWidth / 2;
        this.y = centerY - sizeOfHeight / 2;
    }

    /**
     * Применяет ограничения границ мира к позиции камеры
     */
    private void applyWorldBounds(int worldWidth, int worldHeight) {
        // Ограничиваем x координату
        if (x < 0) {
            x = 0;
        } else if (x > worldWidth - sizeOfWidth) {
            x = worldWidth - sizeOfWidth;
        }

        // Ограничиваем y координату
        if (y < 0) {
            y = 0;
        } else if (y > worldHeight - sizeOfHeight) {
            y = worldHeight - sizeOfHeight;
        }
    }

    public void folowPlayer(int playerX, int playerY, int worldWidth, int worldHeight) {
        // Центрируем камеру на игроке
        setPosition(playerX, playerY);

        // Применяем ограничения границ мира
        applyWorldBounds(worldWidth, worldHeight);
    }

    /**
     * Проверяет, виден ли объект в области камеры
     * @param objectX x координата объекта
     * @param objectY y координата объекта
     * @param objectSize размер объекта (радиус)
     * @return true если объект виден, false если нет
     */
    public boolean isVisible(int objectX, int objectY, int objectSize) {
        // Оптимизированная проверка пересечения прямоугольников
        return objectX + objectSize >= x &&
                objectX - objectSize <= x + sizeOfWidth &&
                objectY + objectSize >= y &&
                objectY - objectSize <= y + sizeOfHeight;
    }

    /**
     * Переводит мировые координаты в экранные координаты
     * @param worldX мировая x координата
     * @return экранная x координата
     */
    public int worldToScreenX(int worldX) {
        return worldX - x;
    }

    /**
     * Переводит мировые координаты в экранные координаты
     * @param worldY мировая y координата
     * @return экранная y координата
     */
    public int worldToScreenY(int worldY) {
        return worldY - y;
    }

    /**
     * Переводит экранные координаты в мировые координаты
     * @param screenX экранная x координата
     * @return мировая x координата
     */
    public int screenToWorldX(int screenX) {
        return screenX + x;
    }

    /**
     * Переводит экранные координаты в мировые координаты
     * @param screenY экранная y координата
     * @return мировая y координата
     */
    public int screenToWorldY(int screenY) {
        return screenY + y;
    }
}