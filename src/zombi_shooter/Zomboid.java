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
            animationTime += 0.3;
            if (animationTime > Math.PI * 2) {
                animationTime -= Math.PI * 2;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        double sinWave = Math.sin(animationTime);
        
        Color baseColor = COLOR_OF_ZOMBOID;
        Color darkerColor = new Color(
            Math.max(0, baseColor.getRed() - 40),
            Math.max(0, baseColor.getGreen() - 40),
            Math.max(0, baseColor.getBlue() - 40)
        );
        Color headColor = new Color(
            Math.max(0, baseColor.getRed() - 60),
            Math.max(0, baseColor.getGreen() - 60),
            Math.max(0, baseColor.getBlue() - 60)
        );
        
        int headSize = (int) (size * 0.45);
        int bodyWidth = (int) (size * 0.65);
        int bodyHeight = (int) (size * 1.1);
        
        double bodySway = sinWave * 2;
        double headBob = Math.abs(sinWave) * 1.5;
        double armSwing = sinWave * 8;
        double legSwing = sinWave * 6;
        
        int shadowX = x;
        int shadowY = y + size / 2;
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillOval(shadowX - size / 3, shadowY - size / 8, (int)(size * 0.7), size / 4);
        
        int centerX = x;
        int centerY = y;
        
        g2d.translate(centerX, centerY);
        g2d.rotate(bodySway * 0.05);
        
        int bodyX = 0;
        int bodyY = (int)(-headSize * 0.3 + headBob);
        
        g2d.setColor(darkerColor);
        g2d.fillOval(bodyX - bodyWidth / 2, bodyY, bodyWidth, bodyHeight);
        
        g2d.setColor(new Color(80, 0, 0));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(bodyX - bodyWidth / 2, bodyY, bodyWidth, bodyHeight);
        
        int headX = bodyX;
        int headY = bodyY - headSize / 2;
        
        g2d.setColor(headColor);
        g2d.fillOval(headX - headSize / 2, headY, headSize, headSize);
        
        g2d.setColor(new Color(60, 60, 60));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(headX - headSize / 2, headY, headSize, headSize);
        
        int armBaseY = bodyY + headSize / 4;
        int armWidth = size / 5;
        int armLength = (int)(size * 0.7);
        
        double leftArmSwing = armSwing;
        double rightArmSwing = -armSwing;
        
        int leftArmX = (int)(-bodyWidth / 2 - armWidth / 2 + Math.sin(leftArmSwing * 0.1) * 3);
        int rightArmX = (int)(bodyWidth / 2 - armWidth / 2 + Math.sin(rightArmSwing * 0.1) * 3);
        int leftArmY = (int)(armBaseY - armLength / 2 + Math.cos(leftArmSwing * 0.1) * 5);
        int rightArmY = (int)(armBaseY - armLength / 2 + Math.cos(rightArmSwing * 0.1) * 5);
        
        g2d.setColor(baseColor);
        g2d.fillOval(leftArmX, leftArmY, armWidth, armLength);
        g2d.fillOval(rightArmX, rightArmY, armWidth, armLength);
        
        int legBaseY = bodyY + bodyHeight;
        int legWidth = size / 6;
        int legLength = (int)(size * 0.55);
        
        int leftLegOffset = (int)(legSwing * 0.8);
        int rightLegOffset = (int)(-legSwing * 0.8);
        
        g2d.setColor(baseColor);
        g2d.fillOval(-size / 5 + leftLegOffset - legWidth / 2, legBaseY, legWidth, legLength);
        g2d.fillOval(size / 5 + rightLegOffset - legWidth / 2, legBaseY, legWidth, legLength);
        
        g2d.rotate(-bodySway * 0.05);
        g2d.translate(-centerX, -centerY);
        
        int eyeY = headY + headSize / 4;
        int eyeSize = Math.max(4, size / 7);
        int eyeSpacing = size / 7;
        
        g2d.setColor(new Color(0, 255, 0, 220));
        g2d.fillOval(headX - eyeSpacing - eyeSize / 2, eyeY, eyeSize, eyeSize);
        g2d.fillOval(headX + eyeSpacing - eyeSize / 2, eyeY, eyeSize, eyeSize);
        
        g2d.setColor(Color.BLACK);
        int pupilSize = Math.max(3, eyeSize / 2);
        int pupilOffset = (int)(Math.sin(animationTime * 0.5) * 1);
        g2d.fillOval(headX - eyeSpacing - pupilSize / 2 + pupilOffset, eyeY, pupilSize, pupilSize);
        g2d.fillOval(headX + eyeSpacing - pupilSize / 2 + pupilOffset, eyeY, pupilSize, pupilSize);
        
        int mouthY = headY + headSize * 0.6;
        int mouthWidth = (int)(size * 0.3);
        int mouthHeight = size / 10;
        
        g2d.setColor(new Color(10, 10, 10));
        g2d.fillOval(headX - mouthWidth / 2, mouthY, mouthWidth, mouthHeight);
        
        g2d.setColor(Color.WHITE);
        int toothSize = Math.max(2, size / 14);
        for (int i = 0; i < 4; i++) {
            int toothX = headX - mouthWidth / 2 + (i + 1) * (mouthWidth / 5);
            g2d.fillRect(toothX - toothSize / 2, mouthY, toothSize, toothSize + 1);
        }
        
        g2d.setColor(new Color(120, 0, 0));
        int woundSize = Math.max(3, size / 9);
        g2d.fillOval(bodyX - bodyWidth / 3, bodyY + bodyHeight / 4, woundSize, woundSize);
        g2d.fillOval(bodyX + bodyWidth / 4, bodyY + bodyHeight / 2, woundSize, woundSize);
        g2d.fillOval(bodyX - bodyWidth / 6, bodyY + bodyHeight * 0.7, woundSize, woundSize);
        
        g2d.setColor(new Color(60, 0, 0));
        g2d.setStroke(new BasicStroke(1f));
        for (int i = 0; i < 3; i++) {
            int scratchX = bodyX - bodyWidth / 3 + i * (bodyWidth / 3);
            int scratchY = bodyY + bodyHeight / 3 + i * 3;
            g2d.drawLine(scratchX, scratchY, scratchX + 4, scratchY + 4);
        }
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
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
