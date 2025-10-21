package zombi_shooter.player;

import zombi_shooter.player.abilyti.AbilityManager;

import java.awt.*;

public class PerkSelectionManeger {
    private Perk[] availablePerks;
    private Perk[] selectedPerks;
    private boolean isSelectionActive;
    private int selectedIndex;
    private boolean selectedConfirm;
    private AbilityManager abilityManager;

    public PerkSelectionManeger() {
        this.availablePerks = new Perk[]{
                new TankPerk(),
                new ScoutPerk(),
                new LuckyPerk(),
                new SurvivalistPerk(),
                new RedKlinokPerk()
        };

        this.selectedPerks = new Perk[2];
        this.isSelectionActive = false;
        this.selectedIndex = 0;
        this.selectedConfirm = false;
    }

    public void startSelectionRandomPerk() {
        // Выбираем случайные перки
        Perk firstSelectedPerk = availablePerks[(int) (Math.random() * availablePerks.length)];
        selectedPerks[0] = firstSelectedPerk;
        
        do {
            Perk secondSelectedPerk = availablePerks[(int) (Math.random() * availablePerks.length)];
            selectedPerks[1] = secondSelectedPerk;
        } while (selectedPerks[1] == selectedPerks[0]);
        
        // Активируем экран выбора
        this.isSelectionActive = true;
        this.selectedIndex = 0;
        this.selectedConfirm = false;
        
        // ВАЖНО: НЕ добавляем способности сразу, только после выбора игрока
        System.out.println("Perk selection started. Available perks: " + 
                          selectedPerks[0].getName() + ", " + selectedPerks[1].getName());
    }

    private void acceptAbility(Perk selected) {
        if(selected.hasAbility() && abilityManager != null){
            for(String k: selected.getAbilityKeys()){
                boolean added = abilityManager.addAbility(k);
                System.out.println("Added ability: " + k + ", success: " + added);
            }
        }
    }

    public void handleKeyboardInput(int keyCode) {
        if (isSelectionActive) {
            System.out.println("Key pressed during perk selection: " + keyCode);
            switch (keyCode) {
                case java.awt.event.KeyEvent.VK_LEFT: // LEFT arrow
                case java.awt.event.KeyEvent.VK_A:
                    selectedIndex = 0;
                    System.out.println("Selected left perk: " + selectedPerks[0].getName());
                    break;
                case 39: // RIGHT arrow  
                case java.awt.event.KeyEvent.VK_D:
                    selectedIndex = 1;
                    System.out.println("Selected right perk: " + selectedPerks[1].getName());
                    break;
                case 10: // ENTER
                // Removed duplicate case java.awt.event.KeyEvent.VK_ENTER
                case java.awt.event.KeyEvent.VK_SPACE:
                    System.out.println("Confirming perk selection: " + getSelectedPerk().getName());
                    // ТЕПЕРЬ добавляем способности выбранного перка
                    acceptAbility(getSelectedPerk());
                    selectedConfirm = true;
                    isSelectionActive = false;
                    break;
            }
        }
    }

    public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {
        if (!isSelectionActive) {
            return;
        }

        System.out.println("Drawing perk selection screen");

        // Полупрозрачный фон
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, screenWidth, screenHeight);

        // Заголовок
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics fm = g2d.getFontMetrics();
        String title = "ВЫБЕРИТЕ ПЕРК";
        int titleX = (screenWidth - fm.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 80);

        // Подсказка
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        fm = g2d.getFontMetrics();
        String hint = "Используйте ← → для выбора, ENTER для подтверждения";
        int hintX = (screenWidth - fm.stringWidth(hint)) / 2;
        g2d.drawString(hint, hintX, screenHeight - 50);

        // Карточки перков
        int cardWidth = 250;
        int cardHeight = 220;
        int cardSpacing = 50;
        int totalWidth = cardWidth * 2 + cardSpacing;
        int startX = (screenWidth - totalWidth) / 2;
        int cardY = (screenHeight - cardHeight) / 2;

        for (int i = 0; i < 2; i++) {
            if (selectedPerks[i] != null) {
                int cardX = startX + i * (cardWidth + cardSpacing);
                boolean isSelected = (i == selectedIndex);
                selectedPerks[i].drawCard(g2d, cardX, cardY, cardWidth, cardHeight, isSelected);
            }
        }

        // Дополнительная отладочная информация
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Selected: " + selectedIndex + " | Confirm: " + selectedConfirm, 10, screenHeight - 20);
    }

    // Getters and setters
    public Perk[] getAvailablePerks() {
        return availablePerks;
    }

    public void setAvailablePerks(Perk[] availablePerks) {
        this.availablePerks = availablePerks;
    }

    public Perk[] getSelectedPerks() {
        return selectedPerks;
    }

    public void setSelectedPerks(Perk[] selectedPerks) {
        this.selectedPerks = selectedPerks;
    }

    public boolean isSelectionActive() {
        return isSelectionActive;
    }

    public void setSelectionActive(boolean selectionActive) {
        isSelectionActive = selectionActive;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public boolean isSelectedConfirm() {
        return selectedConfirm;
    }

    public void setSelectedConfirm(boolean selectedConfirm) {
        this.selectedConfirm = selectedConfirm;
    }

    public Perk getSelectedPerk() {
        if (selectedPerks != null && selectedIndex >= 0 && selectedIndex < selectedPerks.length) {
            return selectedPerks[selectedIndex];
        }
        return null;
    }

    public void setAbilityManager(AbilityManager abilityManager) {
        this.abilityManager = abilityManager;
    }
}