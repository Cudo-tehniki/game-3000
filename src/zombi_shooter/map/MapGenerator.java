package zombi_shooter.map;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {
    private static final Color GRASS_COLOR = new Color(0, 150, 0);
    private static final Color DARK_GRASS_COLOR = new Color(0, 100, 0);
    private static final Color ROAD_COLOR = new Color(50, 50, 50);
    private static final Color TREE_COLOR = new Color(45, 255, 0);
    private static final Color ROCK_COLOR = new Color(100, 100, 100);

    private BufferedImage mapImage;
    private int mapWidth;
    private int mapHeight;
    private Random random;
    private List<Rectangle> roads;
    private List<Point> trees;
    private List<Point> rocks;

    public MapGenerator(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.random = new Random();
        this.roads = new ArrayList<>();
        this.trees = new ArrayList<>();
        this.rocks = new ArrayList<>();
        generateMap();
    }

    public void drawGrassBackground(Graphics2D g2d) {
        GradientPaint gradient = new GradientPaint(0, 0, GRASS_COLOR, mapWidth, mapHeight, DARK_GRASS_COLOR);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, mapWidth, mapHeight);

    }

    public void addGrassVariation(Graphics2D graphics2D) {
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(mapWidth);
            int y = random.nextInt(mapHeight);
            int size = 20 + random.nextInt(80);
            Color grassVariation;
            if (random.nextBoolean()) {
                grassVariation = new Color(0, 190, 0);
            } else {
                grassVariation = new Color(0, 60, 0);
            }
            graphics2D.setColor(grassVariation);
            graphics2D.fillOval(x - size / 2, y - size / 2, size, size);
        }

    }

    public void generateRoads(Graphics2D g2d) {
        g2d.setColor(ROAD_COLOR);
        g2d.setStroke(new BasicStroke(40, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int roadY = mapHeight / 2;
        g2d.drawLine(0, roadY, mapWidth, roadY);
        roads.add(new Rectangle(0, roadY, mapWidth, 40));
        int roadX = mapWidth / 2;
        g2d.drawLine(roadX, 0, roadX, mapHeight);
        roads.add(new Rectangle(roadX, 0, 40, mapHeight));
        for (int i = 0; i < 3; i++) {
            int x1 = random.nextInt(mapWidth);
            int y1 = random.nextInt(mapHeight);
            int x2 = random.nextInt(mapWidth);
            int y2 = random.nextInt(mapHeight);
            g2d.setStroke(new BasicStroke(25, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(x1, y1, x2, y2);
        }
        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                10.0f, new float[]{20.0f, 20.0f}, 0.0f));
        g2d.drawLine(0, roadY, mapWidth, roadY);
        g2d.drawLine(roadX, 0, roadX, mapHeight);
    }

    public void drawTree(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(101, 67, 33));
        g2d.fillRect(x - 3, y - 5, 6, 15);
        g2d.setColor(TREE_COLOR);
        g2d.fillOval(x - 12, y - 25, 24, 24);
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillOval(x - 8, y + 8, 16, 8);
        g2d.setColor(new Color(50, 150, 50));
        g2d.fillOval(x - 8, y - 16, 16, 16);
    }

    public void drawRock(Graphics2D g2d, int x, int y) {
        int size = 5 + random.nextInt(15);
        g2d.setColor(ROCK_COLOR);
        g2d.fillOval(x - size / 2, y - size / 2, size + 3, size);
        g2d.setColor(new Color(150, 150, 150));
        g2d.fillOval(x - size / 3, y - size / 3, (size + 3) / 2, size / 2);
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillOval(x - size / 4, y - size / 3, (size + 3) / 2, size / 4);
    }

    public void generateRocks(Graphics2D g2d) {
        for (int i = 0; i < 30; i++) {
            int x = random.nextInt(mapWidth);
            int y = random.nextInt(mapHeight);
            if (!checkIsOnRoad(x, y)) {
                drawRock(g2d, x, y);
                rocks.add(new Point(x, y));
            }
        }
    }

    public void generateTrees(Graphics2D g2d) {
        for (int i = 0; i < 70; i++) {
            int x = random.nextInt(mapWidth);
            int y = random.nextInt(mapHeight);
            if (!checkIsOnRoad(x, y)) {
                drawTree(g2d, x, y);
                trees.add(new Point(x, y));
            }
        }
    }

    public boolean checkIsOnRoad(int x, int y) {
        for (Rectangle r : roads) {
            if (r.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    private void addTexture(Graphics2D g2d) {
        for (int i = 0; i < mapWidth * mapHeight / 200; i++) {
            int x = random.nextInt(mapWidth);
            int y = random.nextInt(mapHeight);

            if (checkIsOnRoad(x, y)) continue;

            int red = Math.max(0, Math.min(255, GRASS_COLOR.getRed() + random.nextInt(40) - 20));
            int green = Math.max(0, Math.min(255, GRASS_COLOR.getGreen() + random.nextInt(40) - 20));
            int blue = Math.max(0, Math.min(255, GRASS_COLOR.getBlue() + random.nextInt(20) - 10));

            Color textureColor = new Color(red, green, blue, 100);

            g2d.setColor(textureColor);
            g2d.fillRect(x, y, 1, 1);
        }
    }


    public void draw(Graphics2D g2d, int offsetX, int offsetY, int viewWidth, int viewHeight) {
        int startX = Math.max(0, offsetX);
        int startY = Math.max(0, offsetY);
        int endX = Math.min(mapWidth, offsetX + viewWidth);
        int endY = Math.min(mapHeight, offsetY + viewHeight);

        if (startX < endX && startY < endY) {
            BufferedImage subImage = mapImage.getSubimage(
                    startX, startY, endX - startX, endY - startY
            );
            g2d.drawImage(subImage, startX - offsetX, startY - offsetY, null);
        }
    }

    public void generateMap() {
        mapImage = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = mapImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrassBackground(g2d);
        addGrassVariation(g2d);
        generateRoads(g2d);
        generateRocks(g2d);
        generateTrees(g2d);
        addTexture(g2d);
        g2d.dispose();
    }

    public BufferedImage getMapImage() {
        return mapImage;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public Random getRandom() {
        return random;
    }

    public List<Rectangle> getRoads() {
        return roads;
    }

    public List<Point> getTrees() {
        return trees;
    }

    public List<Point> getRocks() {
        return rocks;
    }
}
