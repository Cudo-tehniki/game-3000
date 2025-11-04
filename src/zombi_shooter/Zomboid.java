package zombi_shooter;

import zombi_shooter.enemy.EnemyType;

import java.awt.*;

public class Zomboid {
    private Color COLOR_OF_ZOMBOID = new Color(255, 0, 0);
    private int x;
    private int y;
    private int size;
    private double speed;
    private EnemyType enemyType;
    private int hp;
    private int maxHp;
    private int dmg;
    private double animationTime = Math.random() * Math.PI * 2;
    private double lastX = 0;
    private double lastY = 0;

    public Zomboid(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = 15 + (int) (Math.random() * 16);
        this.speed = 2.0 + (double) (Math.random()) * 3;
        this.maxHp = 50;
        this.hp = maxHp;
        this.dmg = 1;
        this.enemyType = EnemyType.MELEE_ZOMBIE;
    }

    public Zomboid(Color COLOR_OF_ZOMBOID, int x, int y) {
        this.COLOR_OF_ZOMBOID = COLOR_OF_ZOMBOID;
        this.x = x;
        this.y = y;
        this.size = 15 + (int) (Math.random() * 16);
        this.speed = 2.0 + (double) (Math.random()) * 3;
        this.maxHp = 50;
        this.hp = maxHp;
        this.dmg = 1;
        this.enemyType = EnemyType.MELEE_ZOMBIE;
    }

    public Zomboid(int x, int y, EnemyType enemyType) {
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
        this.COLOR_OF_ZOMBOID = enemyType.getColor();
        this.size = 15 + (int) (Math.random() * 16);
        this.speed = 2.0 + (double) (Math.random()) * 3 * enemyType.getSpeedMultiplayer();
        this.maxHp = (int) (50 * enemyType.getHpMultiplayer());
        this.hp = maxHp;
        this.dmg = (int) (1 * enemyType.getDmgMultiplayer());
    }

    public void update(int targetX, int targetY) {
        double deltaX = targetX - x;
        double deltaY = targetY - y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (distance > 0) {
            double moveX = (deltaX / distance) * speed;
            double moveY = (deltaY / distance) * speed;
            lastX = x;
            lastY = y;
            x += moveX;
            y += moveY;
            animationTime += 0.4;
            if (animationTime > Math.PI * 2) {
                animationTime -= Math.PI * 2;
            }
        } else {
            animationTime += 0.1;
            if (animationTime > Math.PI * 2) {
                animationTime -= Math.PI * 2;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        double sinWave = Math.sin(animationTime);
        double cosWave = Math.cos(animationTime);
        
        Color baseColor = COLOR_OF_ZOMBOID;
        Color darkerColor = new Color(
            Math.max(0, baseColor.getRed() - 50),
            Math.max(0, baseColor.getGreen() - 50),
            Math.max(0, baseColor.getBlue() - 50)
        );
        Color headColor = new Color(
            Math.max(0, baseColor.getRed() - 80),
            Math.max(0, baseColor.getGreen() - 80),
            Math.max(0, baseColor.getBlue() - 80)
        );
        Color darkestColor = new Color(
            Math.max(0, baseColor.getRed() - 100),
            Math.max(0, baseColor.getGreen() - 100),
            Math.max(0, baseColor.getBlue() - 100)
        );
        
        int headSize = (int) (size * 0.5);
        int bodyWidth = (int) (size * 0.7);
        int bodyHeight = (int) (size * 1.2);
        
        double bodySway = sinWave * 3;
        double headBob = Math.abs(sinWave) * 2;
        double armSwing = sinWave * 12;
        double legSwing = sinWave * 10;
        
        int shadowX = x;
        int shadowY = y + size / 2 + 2;
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillOval(shadowX - size / 2, shadowY - size / 6, size, size / 3);
        
        int centerX = x;
        int centerY = y;
        
        g2d.translate(centerX, centerY);
        g2d.rotate(bodySway * 0.08);
        
        int bodyX = 0;
        int bodyY = (int)(-headSize * 0.25 + headBob);
        
        g2d.setColor(darkerColor);
        g2d.fillOval(bodyX - bodyWidth / 2, bodyY, bodyWidth, bodyHeight);
        
        g2d.setColor(darkestColor);
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawOval(bodyX - bodyWidth / 2, bodyY, bodyWidth, bodyHeight);
        
        drawCrackNetwork(g2d, bodyX - bodyWidth / 2, bodyY, bodyWidth, bodyHeight, darkestColor);
        
        int headX = bodyX;
        int headY = bodyY - headSize / 2;
        
        g2d.setColor(headColor);
        g2d.fillOval(headX - headSize / 2, headY, headSize, headSize);
        
        g2d.setColor(darkestColor);
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawOval(headX - headSize / 2, headY, headSize, headSize);
        
        drawCrackNetwork(g2d, headX - headSize / 2, headY, headSize, headSize, darkestColor);
        
        int armBaseY = bodyY + headSize / 5;
        int armWidth = size / 4;
        int armLength = (int)(size * 0.75);
        
        double leftArmAngle = Math.toRadians(armSwing);
        double rightArmAngle = Math.toRadians(-armSwing);
        
        int leftArmX = (int)(-bodyWidth / 2 - armWidth / 2 + Math.sin(leftArmAngle) * armWidth);
        int rightArmX = (int)(bodyWidth / 2 - armWidth / 2 + Math.sin(rightArmAngle) * armWidth);
        int leftArmY = (int)(armBaseY - armLength / 2 + Math.cos(leftArmAngle) * 8);
        int rightArmY = (int)(armBaseY - armLength / 2 + Math.cos(rightArmAngle) * 8);
        
        g2d.setColor(baseColor);
        g2d.fillOval(leftArmX, leftArmY, armWidth, armLength);
        g2d.fillOval(rightArmX, rightArmY, armWidth, armLength);
        
        g2d.setColor(darkestColor);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(leftArmX, leftArmY, armWidth, armLength);
        g2d.drawOval(rightArmX, rightArmY, armWidth, armLength);
        
        drawCrackNetwork(g2d, leftArmX, leftArmY, armWidth, armLength, darkestColor);
        drawCrackNetwork(g2d, rightArmX, rightArmY, armWidth, armLength, darkestColor);
        
        int legBaseY = bodyY + bodyHeight;
        int legWidth = size / 5;
        int legLength = (int)(size * 0.6);
        int footWidth = (int)(legWidth * 1.8);
        int footHeight = legWidth / 2;
        
        int leftLegOffset = (int)(legSwing * 1.2);
        int rightLegOffset = (int)(-legSwing * 1.2);
        
        int leftLegX = -size / 5 + leftLegOffset;
        int rightLegX = size / 5 + rightLegOffset;
        
        g2d.setColor(baseColor);
        g2d.fillOval(leftLegX - legWidth / 2, legBaseY, legWidth, legLength);
        g2d.fillOval(rightLegX - legWidth / 2, legBaseY, legWidth, legLength);
        
        g2d.setColor(darkestColor);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(leftLegX - legWidth / 2, legBaseY, legWidth, legLength);
        g2d.drawOval(rightLegX - legWidth / 2, legBaseY, legWidth, legLength);
        
        g2d.setColor(darkerColor);
        g2d.fillOval(leftLegX - footWidth / 2, legBaseY + legLength - footHeight / 2, footWidth, footHeight);
        g2d.fillOval(rightLegX - footWidth / 2, legBaseY + legLength - footHeight / 2, footWidth, footHeight);
        
        g2d.setColor(darkestColor);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(leftLegX - footWidth / 2, legBaseY + legLength - footHeight / 2, footWidth, footHeight);
        g2d.drawOval(rightLegX - footWidth / 2, legBaseY + legLength - footHeight / 2, footWidth, footHeight);
        
        drawCrackNetwork(g2d, leftLegX - legWidth / 2, legBaseY, legWidth, legLength, darkestColor);
        drawCrackNetwork(g2d, rightLegX - legWidth / 2, legBaseY, legWidth, legLength, darkestColor);
        
        g2d.setColor(new Color(140, 0, 0));
        int woundSize = Math.max(4, size / 8);
        g2d.fillOval(bodyX - bodyWidth / 3, bodyY + bodyHeight / 4, woundSize, woundSize);
        g2d.fillOval(bodyX + bodyWidth / 4, bodyY + bodyHeight / 2, woundSize, woundSize);
        g2d.fillOval(bodyX - bodyWidth / 6, bodyY + bodyHeight * 0.7, woundSize, woundSize);
        g2d.fillOval(bodyX + bodyWidth / 3, bodyY + bodyHeight / 3, woundSize, woundSize);
        
        g2d.rotate(-bodySway * 0.08);
        g2d.translate(-centerX, -centerY);
        
        int eyeY = headY + headSize / 4;
        int eyeSize = Math.max(5, size / 6);
        int eyeSpacing = size / 6;
        
        g2d.setColor(new Color(0, 255, 0, 240));
        g2d.fillOval(headX - eyeSpacing - eyeSize / 2, eyeY, eyeSize, eyeSize);
        g2d.fillOval(headX + eyeSpacing - eyeSize / 2, eyeY, eyeSize, eyeSize);
        
        g2d.setColor(Color.BLACK);
        int pupilSize = Math.max(4, eyeSize / 2);
        int pupilOffset = (int)(Math.sin(animationTime * 0.7) * 2);
        g2d.fillOval(headX - eyeSpacing - pupilSize / 2 + pupilOffset, eyeY, pupilSize, pupilSize);
        g2d.fillOval(headX + eyeSpacing - pupilSize / 2 + pupilOffset, eyeY, pupilSize, pupilSize);
        
        int mouthY = headY + headSize * 0.65;
        int mouthWidth = (int)(size * 0.35);
        int mouthHeight = size / 8;
        
        g2d.setColor(new Color(5, 5, 5));
        g2d.fillOval(headX - mouthWidth / 2, mouthY, mouthWidth, mouthHeight);
        
        g2d.setColor(Color.WHITE);
        int toothSize = Math.max(3, size / 12);
        for (int i = 0; i < 5; i++) {
            int toothX = headX - mouthWidth / 2 + (i + 1) * (mouthWidth / 6);
            g2d.fillRect(toothX - toothSize / 2, mouthY, toothSize, toothSize + 2);
        }
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
    
    private void drawCrackNetwork(Graphics2D g2d, int x, int y, int width, int height, Color crackColor) {
        g2d.setColor(new Color(crackColor.getRed(), crackColor.getGreen(), crackColor.getBlue(), 180));
        g2d.setStroke(new BasicStroke(1f));
        
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        
        for (int i = 0; i < 8; i++) {
            double angle = (i * Math.PI * 2) / 8 + animationTime * 0.1;
            int startX = centerX;
            int startY = centerY;
            int endX = (int)(centerX + Math.cos(angle) * (width / 2 + height / 2) / 2);
            int endY = (int)(centerY + Math.sin(angle) * (width / 2 + height / 2) / 2);
            
            if (endX >= x && endX <= x + width && endY >= y && endY <= y + height) {
                g2d.drawLine(startX, startY, endX, endY);
            }
        }
        
        for (int i = 0; i < 5; i++) {
            int crackX = x + (int)(Math.random() * width);
            int crackY = y + (int)(Math.random() * height);
            int crackLength = (int)(Math.min(width, height) * 0.3);
            double crackAngle = Math.random() * Math.PI * 2;
            int crackEndX = (int)(crackX + Math.cos(crackAngle) * crackLength);
            int crackEndY = (int)(crackY + Math.sin(crackAngle) * crackLength);
            
            if (crackEndX >= x && crackEndX <= x + width && crackEndY >= y && crackEndY <= y + height) {
                g2d.drawLine(crackX, crackY, crackEndX, crackEndY);
            }
        }
    }

    public void drawHpBar(Graphics2D g2d) {
        if (hp >= maxHp) {
            return;
        }
        int barWidth = size;
        int barHight = 4;
        int barX = x - barWidth / 2;
        int barY = y - size / 2 - 8;
        g2d.setColor(new Color(51, 0, 0, 255));
        g2d.fillRect(barX, barY, barWidth, barHight);
        int currentHp = hp / maxHp * barWidth;
        g2d.setColor(new Color(255, 0, 0));
        g2d.fillRect(barX, barY, currentHp, barHight);
        g2d.setColor(new Color(0, 0, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(barX, barY, barWidth, barHight);

    }

    public void takeDmg(int dmg){
        this.hp -= dmg;
        if(this.hp <= 0){
            this.hp = 0;
        }
    }

    public boolean isDead(){
        return hp == 0;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(EnemyType enemyType) {
        this.enemyType = enemyType;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
