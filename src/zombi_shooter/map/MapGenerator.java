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
    private static final Color TREE_COLOR = new Color(0, 180, 0);
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
}
