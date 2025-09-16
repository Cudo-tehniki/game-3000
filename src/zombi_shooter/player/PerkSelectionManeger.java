package zombi_shooter.player;

import zombi_shooter.player.abilyti.Ability;
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
                new ChancePerk(),
                new SurvivalistPerk(),
                new RedKlinokPerk()
        };

        this.selectedPerks = new Perk[2];
        this.isSelectionActive = false;
        this.selectedIndex = 0;
        this.selectedConfirm = false;
    }

    public void startSelectionRandomPerk() {
        Perk firstSelectedPerk = availablePerks[(int) (Math.random() * availablePerks.length)];
        selectedPerks[0] = firstSelectedPerk;
        do {
            Perk secondSelectedPerk = availablePerks[(int) (Math.random() * availablePerks.length)];
            selectedPerks[1] = secondSelectedPerk;
            acceptAbility(firstSelectedPerk);
            acceptAbility(secondSelectedPerk);
        } while (selectedPerks[1] == selectedPerks[0]);
        this.isSelectionActive = true;
        this.selectedIndex = 0;
        this.selectedConfirm = false;
    }

    private void acceptAbility(Perk selected) {
        if(selected.hasAbility() && abilityManager != null){
            for(String k: selected.getAbilityKeys()){
                abilityManager.addAbility(k);
            }
        }
    }

    public void handleKeyboardInput(int keyCode) {
        if (isSelectionActive) {
            switch (keyCode) {
                case 37:
                    selectedIndex = 0;
                    break;
                case 39:
                    selectedIndex = 1;
                    break;
                case 10:
                    selectedConfirm = true;
                    isSelectionActive = false;
                    break;
            }
        }
    }

    public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {
        if (!isSelectionActive) return;

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
            int cardX = startX + i * (cardWidth + cardSpacing);
            boolean isSelected = (i == selectedIndex);
            selectedPerks[i].drawCard(g2d, cardX, cardY, cardWidth, cardHeight, isSelected);
        }
    }

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
        return selectedPerks[selectedIndex];
    }

    public void setAbilityManager(AbilityManager abilityManager) {
        this.abilityManager = abilityManager;
    }
}
