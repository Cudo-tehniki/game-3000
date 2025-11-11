package zombi_shooter.player;

import java.awt.*;

public class Player {
    private int playerHealth;
    private int maxHealth;
    private int baseSpeed;
    private final int size = 15;
    private final Color colorOfPlayer = new Color(50, 100, 200);
    private int positionY;
    private int positionX;
    private double direction = 0;
    private int luckyMultiplayer = 0;
    private double animationTime = 0;
    private boolean isMoving = false;

    public Player(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public Player(int maxHealth, int positionY, int positionX) {
        this.playerHealth = maxHealth;
        this.maxHealth = maxHealth;
        this.positionY = positionY;
        this.positionX = positionX;
    }

    public void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (isMoving) {
            animationTime += 0.3;
            if (animationTime > Math.PI * 2) {
                animationTime -= Math.PI * 2;
            }
        } else {
            animationTime *= 0.95;
        }
        
        double sinWave = Math.sin(animationTime);
        
        Color baseColor = colorOfPlayer;
        Color darkerColor = new Color(
            Math.max(0, baseColor.getRed() - 30),
            Math.max(0, baseColor.getGreen() - 30),
            Math.max(0, baseColor.getBlue() - 30)
        );
        Color darkestColor = new Color(
            Math.max(0, baseColor.getRed() - 60),
            Math.max(0, baseColor.getGreen() - 60),
            Math.max(0, baseColor.getBlue() - 60)
        );
        Color skinColor = new Color(255, 220, 177);
        Color darkerSkinColor = new Color(220, 180, 140);
        
        int headSize = (int) (size * 0.55);
        int bodyWidth = (int) (size * 0.75);
        int bodyHeight = (int) (size * 1.1);
        
        double headBob = Math.abs(sinWave) * 1.5;
        double armSwing = sinWave * 10;
        double legSwing = sinWave * 8;
        
        int shadowX = positionX;
        int shadowY = positionY + size / 2 + 2;
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillOval(shadowX - size / 2, shadowY - size / 6, size, size / 3);
        
        int centerX = positionX;
        int centerY = positionY;
        
        g2d.translate(centerX, centerY);
        g2d.rotate(direction);
        
        int bodyX = 0;
        int bodyY = (int)(-headSize * 0.2 + headBob);
        
        g2d.setColor(darkerColor);
        g2d.fillOval(bodyX - bodyWidth / 2, bodyY, bodyWidth, bodyHeight);
        
        g2d.setColor(darkestColor);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(bodyX - bodyWidth / 2, bodyY, bodyWidth, bodyHeight);
        
        int headX = bodyX;
        int headY = bodyY - headSize / 2;
        
        g2d.setColor(skinColor);
        g2d.fillOval(headX - headSize / 2, headY, headSize, headSize);
        
        g2d.setColor(darkerSkinColor);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(headX - headSize / 2, headY, headSize, headSize);
        
        int armBaseY = bodyY + headSize / 4;
        int armWidth = size / 5;
        int armLength = (int)(size * 0.7);
        
        double leftArmAngle = Math.toRadians(armSwing);
        
        int weaponArmX = (int)(bodyWidth / 2 - armWidth / 2);
        int weaponArmY = (int)(armBaseY - armLength / 2);
        
        int leftArmX = (int)(-bodyWidth / 2 - armWidth / 2 + Math.sin(leftArmAngle) * armWidth);
        int leftArmY = (int)(armBaseY - armLength / 2 + Math.cos(leftArmAngle) * 5);
        
        g2d.setColor(skinColor);
        g2d.fillOval(weaponArmX, weaponArmY, armWidth, armLength);
        g2d.fillOval(leftArmX, leftArmY, armWidth, armLength);
        
        g2d.setColor(darkerSkinColor);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(weaponArmX, weaponArmY, armWidth, armLength);
        g2d.drawOval(leftArmX, leftArmY, armWidth, armLength);
        
        int weaponLength = size + 6;
        int weaponEndX = weaponArmX + armWidth / 2 + weaponLength;
        int weaponEndY = weaponArmY + armLength / 2;
        
        g2d.setColor(new Color(60, 40, 20));
        g2d.setStroke(new BasicStroke(3f));
        g2d.drawLine(weaponArmX + armWidth / 2, weaponArmY + armLength / 2, weaponEndX, weaponEndY);
        
        g2d.setColor(new Color(80, 80, 80));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawLine(weaponArmX + armWidth / 2, weaponArmY + armLength / 2, weaponEndX - 2, weaponEndY);
        
        int legBaseY = bodyY + bodyHeight;
        int legWidth = size / 6;
        int legLength = (int)(size * 0.55);
        int footWidth = (int)(legWidth * 1.6);
        int footHeight = legWidth / 2;
        
        int leftLegOffset = (int)(legSwing * 1.0);
        int rightLegOffset = (int)(-legSwing * 1.0);
        
        int leftLegX = -size / 6 + leftLegOffset;
        int rightLegX = size / 6 + rightLegOffset;
        
        g2d.setColor(darkerColor);
        g2d.fillOval(leftLegX - legWidth / 2, legBaseY, legWidth, legLength);
        g2d.fillOval(rightLegX - legWidth / 2, legBaseY, legWidth, legLength);
        
        g2d.setColor(darkestColor);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(leftLegX - legWidth / 2, legBaseY, legWidth, legLength);
        g2d.drawOval(rightLegX - legWidth / 2, legBaseY, legWidth, legLength);
        
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillOval(leftLegX - footWidth / 2, legBaseY + legLength - footHeight / 2, footWidth, footHeight);
        g2d.fillOval(rightLegX - footWidth / 2, legBaseY + legLength - footHeight / 2, footWidth, footHeight);
        
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(leftLegX - footWidth / 2, legBaseY + legLength - footHeight / 2, footWidth, footHeight);
        g2d.drawOval(rightLegX - footWidth / 2, legBaseY + legLength - footHeight / 2, footWidth, footHeight);
        
        g2d.rotate(-direction);
        
        int eyeY = headY + headSize / 3;
        int eyeSize = Math.max(4, size / 7);
        int eyeSpacing = size / 7;
        
        g2d.setColor(Color.WHITE);
        g2d.fillOval(headX - eyeSpacing - eyeSize / 2, eyeY, eyeSize, eyeSize);
        g2d.fillOval(headX + eyeSpacing - eyeSize / 2, eyeY, eyeSize, eyeSize);
        
        g2d.setColor(new Color(50, 150, 255));
        int pupilSize = Math.max(3, eyeSize / 2);
        double lookDirection = direction;
        int pupilOffsetX = (int)(Math.cos(lookDirection) * 1);
        int pupilOffsetY = (int)(Math.sin(lookDirection) * 1);
        g2d.fillOval(headX - eyeSpacing - pupilSize / 2 + pupilOffsetX, eyeY + pupilOffsetY, pupilSize, pupilSize);
        g2d.fillOval(headX + eyeSpacing - pupilSize / 2 + pupilOffsetX, eyeY + pupilOffsetY, pupilSize, pupilSize);
        
        g2d.setColor(Color.BLACK);
        int blackPupilSize = Math.max(2, pupilSize / 2);
        g2d.fillOval(headX - eyeSpacing - blackPupilSize / 2 + pupilOffsetX, eyeY + blackPupilSize / 4 + pupilOffsetY, blackPupilSize, blackPupilSize);
        g2d.fillOval(headX + eyeSpacing - blackPupilSize / 2 + pupilOffsetX, eyeY + blackPupilSize / 4 + pupilOffsetY, blackPupilSize, blackPupilSize);
        
        int mouthY = (int) (headY + headSize * 0.7);
        int mouthWidth = (int)(size * 0.3);
        int mouthHeight = size / 10;
        
        g2d.setColor(new Color(200, 150, 150));
        g2d.fillOval(headX - mouthWidth / 2, mouthY, mouthWidth, mouthHeight);
        
        g2d.translate(-centerX, -centerY);
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void move(int stepX, int stepY, int mapW, int mapH) {
        isMoving = (stepX != 0 || stepY != 0);
        stepX *= baseSpeed;
        stepY *= baseSpeed;
        positionY += stepY;
        positionX += stepX;
        if (positionX < size) {
            positionX = size;
        }
        if (positionY < size) {
            positionY = size;
        }
        if (positionX > mapW - size) {
            positionX = mapW - size;
        }
        if (positionY > mapH - size) {
            positionY = mapH - size;
        }
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public double getDirection() {
        return direction;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        setPlayerHealth(maxHealth);
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(int baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setBaseHp(){
        setMaxHealth(100);
    }

    public int getLuckyMultiplayer() {
        return luckyMultiplayer;
    }

    public void setLuckyMultiplayer(int luckyMultiplayer) {
        this.luckyMultiplayer = luckyMultiplayer;
    }

    public int getSize() {
        return size;
    }
}
