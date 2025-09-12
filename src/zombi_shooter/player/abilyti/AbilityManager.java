package zombi_shooter.player.abilyti;

import zombi_shooter.Bullet;
import zombi_shooter.Zomboid;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilityManager {

    private Map<String, Ability> abilities;
    private List<Ability> activeAbility;
    private Ability choosenAbility;
    private Point mousePosicion = new Point(0, 0);
    private Point playerPosicion = new Point(0, 0);

    public AbilityManager() {
        this.abilities = new HashMap<>();
        this.activeAbility = new ArrayList<>();
        abilities.put("turret", new TuretAbility());
    }

    public boolean addAbility(String idAbility) {
        Ability ability = abilities.get(idAbility);
        if (ability != null) {
            activeAbility.add(ability);
            return true;
        }
        return false;
    }

    public boolean removeAbility(String idAbility) {
        Ability ability = abilities.get(idAbility);
        if (ability != null) {
            ability.cleanUp();
            activeAbility.remove(ability);
            if (choosenAbility == ability) {
                choosenAbility = null;
            }
            return true;
        }
        return false;
    }

    public boolean selectAbility(int indexKey) {
        if (indexKey > 0 && indexKey < activeAbility.size()) {
            choosenAbility = activeAbility.get(indexKey - 1);
            return true;
        }
        return false;
    }

    public boolean activeAbility() {
        if (choosenAbility != null) {
            boolean activate = choosenAbility.activate(playerPosicion.x, playerPosicion.y, mousePosicion.x, mousePosicion.y);
            if (activate) {
                choosenAbility = null;
            }
            return activate;
        }
        return false;
    }

    public boolean handleKeyPress(int keyId) {
        if (keyId == KeyEvent.VK_SPACE) {
            return activeAbility();
        }
        if (keyId >= KeyEvent.VK_1 && keyId <= KeyEvent.VK_9) {
            int newId = keyId - KeyEvent.VK_0;
            return selectAbility(newId);
        }
        return false;
    }

    public boolean handleMouseClick(MouseEvent mouseEvent) {
        mousePosicion.setLocation(mouseEvent.getX(), mouseEvent.getY());
        if (mouseEvent.getButton() == MouseEvent.BUTTON2 && choosenAbility != null) {
            return activeAbility();
        }
        return false;
    }

    public void update(long lastAbilityTime, List<Zomboid> zomboidList, List<Bullet> bulletList) {
        for (Ability a : activeAbility) {
            a.update(lastAbilityTime, zomboidList, bulletList);
        }
        activeAbility.removeIf(a -> {
            if (!a.isActive() && a != choosenAbility) {
                a.cleanUp();
                return true;
            }
            return false;
        });
    }

    public void draw(Graphics2D graphics2D){
        for(Ability a: activeAbility){
            a.draw(graphics2D);
        }
    }

    public void drawUI(Graphics2D g2d, int windowWidth, int windowHeight) {
        if (activeAbility.isEmpty()) return;

        // Панель способностей внизу экрана
        int panelHeight = 80;
        int panelY = windowHeight - panelHeight - 10;
        int slotSize = 60;
        int spacing = 10;
        int totalWidth = activeAbility.size() * slotSize + (activeAbility.size() - 1) * spacing;
        int startX = (windowWidth - totalWidth) / 2;

        // Фон панели
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(startX - 10, panelY - 10, totalWidth + 20, panelHeight, 10, 10);

        // Слоты способностей
        for (int i = 0; i < activeAbility.size(); i++) {
            Ability ability = activeAbility.get(i);
            int slotX = startX + i * (slotSize + spacing);

            // Фон слота
            Color slotColor = ability == choosenAbility ?
                    new Color(255, 215, 0, 200) : new Color(100, 100, 100, 200);
            g2d.setColor(slotColor);
            g2d.fillRoundRect(slotX, panelY, slotSize, slotSize, 8, 8);

            // Обводка
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(slotX, panelY, slotSize, slotSize, 8, 8);

            // Иконка способности (пока просто текст)
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            String iconText = ability.getName().substring(0, Math.min(3, ability.getName().length())).toUpperCase();
            FontMetrics fm = g2d.getFontMetrics();
            int textX = slotX + (slotSize - fm.stringWidth(iconText)) / 2;
            int textY = panelY + (slotSize - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(iconText, textX, textY - 10);

            // Номер клавиши
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            String keyNumber = String.valueOf(i + 1);
            g2d.setColor(Color.YELLOW);
            g2d.drawString(keyNumber, slotX + 5, panelY + 15);

            // Индикатор готовности
            if (!ability.isReady()) {
                // Перезарядка
                long remaining = ability.getRemaingCoudown();
                double percentage = 1.0 - (double) remaining / ability.getCouldown();

                g2d.setColor(new Color(255, 0, 0, 150));
                g2d.fillRoundRect(slotX, panelY, slotSize, (int) (slotSize * (1 - percentage)), 8, 8);

                // Время перезарядки
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                String timeText = String.valueOf((int) (remaining / 1000) + 1);
                fm = g2d.getFontMetrics();
                int timeX = slotX + (slotSize - fm.stringWidth(timeText)) / 2;
                int timeY = panelY + slotSize / 2 + fm.getAscent() / 2;
                g2d.drawString(timeText, timeX, timeY);
            }
        }

        // Информация о выбранной способности
        if (choosenAbility != null) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            String abilityInfo = choosenAbility.getName() + " - " + choosenAbility.description();
            FontMetrics fm = g2d.getFontMetrics();
            int infoX = (windowWidth - fm.stringWidth(abilityInfo)) / 2;
            int infoY = panelY - 20;
            g2d.drawString(abilityInfo, infoX, infoY);

            // Подсказка по активации
            if (choosenAbility.isReady()) {
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                String hint = "Нажмите ПРОБЕЛ или ЛКМ для активации";
                fm = g2d.getFontMetrics();
                int hintX = (windowWidth - fm.stringWidth(hint)) / 2;
                g2d.drawString(hint, hintX, infoY - 20);
            }
        } else if (!activeAbility.isEmpty()) {

            // Подсказка по выбору способности
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            String hint = "Нажмите 1-" + activeAbility.size() + " для выбора способности";
            FontMetrics fm = g2d.getFontMetrics();
            int hintX = (windowWidth - fm.stringWidth(hint)) / 2;
            g2d.drawString(hint, hintX, panelY - 20);
        }
    }

    public boolean isActiveAbility(){
        if(!activeAbility.isEmpty()){
            return true;
        }
        return false;
    }

    public void cleanUp(){
        for (Ability a: activeAbility){
            a.cleanUp();
        }
        activeAbility.clear();
        choosenAbility = null;
    }

    public List<Ability> getActiveAbility() {
        return activeAbility;
    }

    public void setActiveAbility(List<Ability> activeAbility) {
        this.activeAbility = activeAbility;
    }

    public Ability getChoosenAbility() {
        return choosenAbility;
    }

    public void setChoosenAbility(Ability choosenAbility) {
        this.choosenAbility = choosenAbility;
    }
}

