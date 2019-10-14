package ru.inkrot.kit.laba4;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextLayout;
import java.util.Collections;
import java.util.Set;

public class DemoFrame extends JFrame {

    // constants
    public static int WIDTH = 800;
    public static int HEIGHT = 700;
    private int FPS = 60;

    private DemoPanel demoPanel;

    private Thread repaintThread;
    private Thread movingThread;

    public DemoFrame(ControlFrame controlFrame) {
        setTitle("ДО");
        setLocation(controlFrame.getX() + controlFrame.getWidth() + 10, controlFrame.getY());
        //setUndecorated(true);
        setSize(WIDTH, HEIGHT);
        addWindowListener(new WindowListener());
        setDefaultLookAndFeelDecorated(false);
        setLayout(null);
        setResizable(false);

        repaintThread = new Thread(() -> {
            while(true) {
                demoPanel.repaint();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {}
            }
        });

        movingThread = new Thread(() -> {
            while(true) {
                moveAll();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {}
            }
        });

        demoPanel = new DemoPanel();
        int mx = 0;
        int my = 0;
        demoPanel.setBounds(mx, my, getWidth() - mx, getHeight() - my);
        add(demoPanel);

        repaintThread.start();
        movingThread.start();

        setVisible(true);
    }

    private void moveAll() {
        Set<FioObject> objects = FioObject.getAllObjects();
        for (FioObject obj : objects) {
            obj.nextStep(demoPanel.getWidth() - 6, demoPanel.getHeight() - 28);
        }
    }

    class DemoPanel extends JPanel {

        private Color backgroundColor = new Color(111, 253, 255);
        private Color idTextColor = new Color(31, 89, 255);

        private Font idTextFont = new Font("Tahoma", 0, 20);

        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            // background
            g2d.setColor(backgroundColor);
            g2d.fillRect(0 , 0, getWidth(), getHeight());

            // draw objects
            Set<FioObject> objects = Collections.synchronizedSet(FioObject.getAllObjects());
            for (FioObject obj : objects) {
                int x = obj.getX(), y = obj.getY();
                if (obj.getType() == FioType.PICTURE) {
                    int size = FioObject.IMAGE_SIZE;
                    g2d.drawImage(obj.getImage(), x, y, size, size, null);
                } else if (obj.getType() == FioType.TEXT) {
                    g2d.setFont(FioObject.TEXT_FONT);
                    g2d.setColor(obj.getTextColor());
                    g2d.drawString(obj.getText(), x, y);
                }
                Dimension d = obj.getDimension();
                //g2d.setColor(idTextColor);
                //g2d.setFont(idTextFont);
                String text = "[ id: " + obj.getId() + " ]";
                //g2d.drawString(text, x + (int)d.getWidth() + 4, y + (int)d.getHeight());

                TextLayout textLayout = new TextLayout(text, idTextFont, g2d.getFontRenderContext());
                g2d.setPaint(Color.BLACK);
                textLayout.draw(g2d, x + (int)d.getWidth() - 1, y + (int)d.getHeight() - 1);
                textLayout.draw(g2d, x + (int)d.getWidth(), y + (int)d.getHeight() - 1);
                textLayout.draw(g2d, x + (int)d.getWidth() + 1, y + (int)d.getHeight() + 1);
                textLayout.draw(g2d, x + (int)d.getWidth() - 1, y + (int)d.getHeight() + 1);
                textLayout.draw(g2d, x + (int)d.getWidth() + 1, y + (int)d.getHeight() - 1);

                g2d.setPaint(Color.RED);
                textLayout.draw(g2d, x + (int)d.getWidth(), y + (int)d.getHeight());
            }
        }
    }
}