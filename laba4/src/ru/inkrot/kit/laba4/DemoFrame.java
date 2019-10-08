package ru.inkrot.kit.laba4;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.*;

public class DemoFrame extends JFrame {

    // constants
    private Font font = new Font("Arial", 0, 14);
    private int WIDTH = 800;
    private int HEIGHT = 700;
    private int FPS = 30;

    private Thread repaintThread;

    public DemoFrame() {
        setTitle("ДО");
        setSize(WIDTH, HEIGHT);
        addWindowListener(new WindowListener());
        setLocationRelativeTo(null);
        setDefaultLookAndFeelDecorated(false);
        setLayout(null);
        setResizable(false);

        new Thread(() -> {
            while(true) {
                repaint();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {}
            }
        }).start();

        setVisible(true);
    }

    private Color bgColor = new Color(71, 214, 255);

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(bgColor);
        g2d.fillRect(0 , 0, WIDTH, HEIGHT);


    }
}