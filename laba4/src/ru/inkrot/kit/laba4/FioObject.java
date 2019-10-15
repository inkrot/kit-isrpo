package ru.inkrot.kit.laba4;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadLocalRandom;

public class FioObject {

    public static int MAX_NUMBER_OF_OBJECTS = 200;
    public static int IMAGE_SIZE = 80;
    public static Font TEXT_FONT = new Font("Tahoma", 1, 16);

    private static CopyOnWriteArraySet<FioObject> allObjects = new CopyOnWriteArraySet<>();

    private static double[] speedMap = { 0.2, 0.3, 0.5, 0.7, 1.0 };

    public static CopyOnWriteArraySet<FioObject> getAllObjects() {
        return allObjects;
    }

    private static void addObject(FioObject fioObject) {
        if (allObjects.size() < MAX_NUMBER_OF_OBJECTS)
            allObjects.add(fioObject);
    }

    public static void addObject(String text, Color textColor, int speed) {
        addObject(new FioObject(text, textColor, speed));
    }

    public static void addObject(BufferedImage image, int speed) {
        addObject(new FioObject(image, speed));
    }

    public static boolean isFreeId(int id) {
        for (FioObject obj : allObjects) {
            if (obj.getId() == id) return false;
        }
        return true;
    }

    private FioType type;

    private int id;

    private int speed = 1;

    private double x = 0, y = 0;
    private double dx = 0, dy = 0;

    private BufferedImage image;

    private String text;
    private Color textColor;

    private FioObject(BufferedImage image, int speed) {
        if (! isCorrectSpeed(speed)) return;
        getNextId();
        this.type = FioType.PICTURE;
        this.image = image;
        this.speed = speed;
        setLocation(0, 0);
        setDirection(randomAngle());
    }

    private FioObject(String text, Color textColor, int speed) {
        if (! isCorrectSpeed(speed)) return;
        getNextId();
        this.type = FioType.TEXT;
        this.text = text;
        this.textColor = textColor;
        this.speed = speed;
        setLocation(1, 13);
        setDirection(randomAngle());
    }

    private int randomAngle() {
        return ThreadLocalRandom.current().nextInt(0, 90 + 1);
    }

    private boolean isCorrectSpeed(int speed) {
        if (speed >= 1 && speed <= 5) return true;
        return false;
    }

    private void getNextId() {
        int firstFreeId = 1;
        while (! isFreeId(firstFreeId)) {
            firstFreeId++;
        }
        id = firstFreeId;
    }

    public void nextStep(int width, int height) {
        Dimension d = getDimension();
        if (y >= height - d.getHeight()) dy = -dy;
        else if (x >= width - d.getWidth()) dx = -dx;
        else if (y <= -1) dy = -dy;
        else if (x <= -1) dx = -dx;
        double s = speedMap[speed - 1];
        x += dx * s;
        y += dy * s;
    }

    /**
     * @param angle of direction in degrees
     */
    public void setDirection(double angle) {
        double rad = angle * Math.PI / 180;
        dx = Math.cos(rad);
        dy = Math.sin(rad);
    }

    public void setLocation(int x, int y) {
        this.x = (double) x;
        this.y = (double) y;
    }

    public Dimension getTextSize() {
        BufferedImage b = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics g = b.getGraphics();
        FontMetrics metrics = g.getFontMetrics(TEXT_FONT);
        int hgt = metrics.getHeight();
        int adv = metrics.stringWidth(text);
        return new Dimension(adv, hgt);
    }

    public Dimension getDimension() {
        return type == FioType.TEXT ? getTextSize() : new Dimension(IMAGE_SIZE, IMAGE_SIZE);
    }

    public int getX() {
        return (int) x;
    }

    public void setX(int x) {
        this.x = (double) x;
    }

    public int getY() {
        return (int) y;
    }

    public void setY(int y) {
        this.y = (double) y;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    /**
     *
     * @param id new identifier of object
     * @return error code or 0 - success,
     * codes: 1 - id is bussy;
     *        2 - id within wrong rangle (allowed: 1 - 5)
     */
    public int setId(int id) {
        if (! isFreeId(id)) return 1;
        if (! (id >= 1 && id <= MAX_NUMBER_OF_OBJECTS)) return 2;
        this.id = id;
        return 0;
    }

    public FioType getType() {
        return type;
    }

    public void setType(FioType type) {
        this.type = type;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }
}

