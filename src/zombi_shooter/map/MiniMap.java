package zombi_shooter.map;

import zombi_shooter.BossZombie;
import zombi_shooter.Chest;
import zombi_shooter.Zomboid;
import zombi_shooter.player.Player;

import java.awt.*;
import java.util.List;

public class MiniMap {

    private final int MINI_MAP_SIZE = 150;
    private final int MINI_MAP_MARGE = 30;
    private final Color MINI_MAP_BACKGROUND = new Color(0, 0, 0, 205);
    private final Color MINI_MAP_BORDER = new Color(0, 0, 0, 155);
    private final Color PLAYER_COLOR = new Color(0, 0, 255);
    private final Color ZOMBIE_COLOR = new Color(255, 0, 0);
    private final Color SUNDUK_COLOR = new Color(110, 55, 0);
    private final Color BOSS_COLOR = new Color(100, 0, 100);
    private final Color ROADS_COLOR = new Color(115, 115, 115);

    private MapGenerator mapGenerator;
    private int mapWidth;
    private int mapHeight;
    private int windowWidth;
    private int windowHeight;

    public MiniMap(int mapWidth, int mapHeight, int windowWidth, int windowHeight, MapGenerator mapGenerator) {
        this.mapGenerator = mapGenerator;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public void draw(Graphics2D g2d, List<Zomboid> zomb, List<Chest> chests, Player player, BossZombie boss, boolean isBoss) {
        int x = windowWidth - MINI_MAP_MARGE - MINI_MAP_SIZE;
        int y = MINI_MAP_MARGE;

        g2d.setColor(MINI_MAP_BACKGROUND);
        g2d.fillRect(x, y, MINI_MAP_SIZE, MINI_MAP_SIZE);
        g2d.setColor(MINI_MAP_BORDER);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, MINI_MAP_SIZE, MINI_MAP_SIZE);

        double scaleX = (double) MINI_MAP_SIZE / mapWidth;
        double scaleY = (double) MINI_MAP_SIZE / mapHeight;

        drawRoads(g2d, x, y, scaleX, scaleY);
        drawPlayer(g2d, player, x, y, scaleX, scaleY);
        drawZomb(g2d, zomb, x, y, scaleX, scaleY);
        drawSunduks(g2d, chests, x, y, scaleX, scaleY);
        if (isBoss){
            drawBoss(g2d, boss, x, y, scaleX, scaleY);
        }
    }

    public void drawRoads(Graphics2D g2d, int miniMapX, int miniMapY, double scaleX, double scaleY) {
        g2d.setColor(ROADS_COLOR);
        g2d.setStroke(new BasicStroke(2));
        for (var road : mapGenerator.getRoads()) {
            int x1 = (int) (miniMapX + (road.x * scaleX));
            int y1 = (int) (miniMapY + (road.y * scaleY));
            int x2 = (int) (miniMapX + ((road.x + road.width) * scaleX));
            int y2 = (int) (miniMapY + ((road.y + road.height) * scaleY));

            if (road.width > road.height) {
                g2d.drawLine(x1, y1 + road.height / 2, x2, y2 + road.height / 2 );
            } else {
                g2d.drawLine(x1 + road.width / 2, y1, x2 + road.width / 2, y2);
            }
        }
    }

    private void drawZomb(Graphics2D g2d, List<Zomboid> zombs, int miniMapX, int miniMapY, double scaleX, double scaleY) {
        g2d.setColor(ZOMBIE_COLOR);
        for (Zomboid zomb : zombs) {
            int x = (int) (miniMapX + (zomb.getX() * scaleX));
            int y = (int) (miniMapY + (zomb.getY()) * scaleY);
            if(x >= miniMapX && y >= miniMapY && x <= miniMapX + MINI_MAP_SIZE && y <= miniMapY + MINI_MAP_SIZE){
                g2d.fillOval(x, y, 2, 2);
            }


        }

    }

    private void drawSunduks(Graphics2D g2d, List<Chest> chests, int miniMapX, int miniMapY, double scaleX, double scaleY) {
        g2d.setColor(SUNDUK_COLOR);
        for (Chest c : chests) {
            int x = (int) (miniMapX + (c.getX() * scaleX));
            int y = (int) (miniMapY + (c.getY() * scaleY));
            if(!c.isOpen()){
                g2d.fillRect(x, y, 2, 2);
            }
        }
    }

    private void drawBoss(Graphics2D graphics2D, BossZombie boss, int miniMapX, int miniMapY, double scaleX, double scaleY) {
        graphics2D.setColor(BOSS_COLOR);
        int x = (int) (miniMapX + (boss.getX() * scaleX));
        int y = (int) (miniMapY + (boss.getY()) * scaleY);
        graphics2D.fillOval(x, y, 3, 3);
    }

    public void drawPlayer(Graphics2D g2d, Player player, int miniMapX, int miniMapY, double scaleX, double scaleY) {
        g2d.setColor(PLAYER_COLOR);
        int x = (int) (miniMapX + (player.getPositionX() * scaleX));
        int y = (int) (miniMapY + (player.getPositionY() * scaleY));
        g2d.fillOval(x, y, 4, 4);
    }
}
