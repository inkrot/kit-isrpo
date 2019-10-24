package ru.inkrot.kit.part1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main extends JFrame {

    private JPanel valuesPanel;
    private JTextField xStartField, xEndField, nField;
    private JButton apply;
    private ChartsPanel chartsPanel;

    private Font font = new Font("Arial", 0, 16);

    private Dimension screenSize;

    Main() {
        setSize(900, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        initUI();
        setVisible(true);
        repaint();
    }

    private void initUI() {
        valuesPanel = new JPanel();
        valuesPanel.setLayout(new GridLayout(4,2, 3, 3));
        valuesPanel.setBackground(Color.GRAY);
        valuesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        valuesPanel.setBounds(11, 11, 250, 130);
        add(valuesPanel);

        valuesPanel.add(newLabel("X_нач:"));
        valuesPanel.add(xStartField = newField());

        valuesPanel.add(newLabel("X_кон:"));
        valuesPanel.add(xEndField = newField());

        valuesPanel.add(newLabel("n_max:"));
        valuesPanel.add(xEndField = newField());

        valuesPanel.add(newEmptyComponent());

        valuesPanel.add(apply = new JButton("Применить"));
        apply.setFont(font);
        apply.setFocusPainted(false);

        chartsPanel = new ChartsPanel();
        chartsPanel.setBounds(10, 10, getWidth() - 10, getHeight() - 10);
        add(chartsPanel);
    }

    private JLabel newLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField newField() {
        JTextField field = new JTextField();
        field.setFont(font);
        return field;
    }

    private JComponent newEmptyComponent() {
        JComponent component = new JButton();
        component.setVisible(false);
        return component;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        chartsPanel.setSize(Main.this.getWidth() - 35, Main.this.getHeight() - 58);
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new Main();
    }

    class ChartsPanel extends JPanel {

        int rangeMin = -15;
        int rangeMax = 15;

        ChartsPanel() {

        }

        private int map(int value, int valueRangeMin, int valueRangeMax) {
            double fromRange = valueRangeMax - valueRangeMin;
            double toRange = rangeMax - rangeMin;
            double scaleFactor = toRange / fromRange;
            double tmpValue = value - valueRangeMin;
            tmpValue *= scaleFactor;
            return (int) (tmpValue + rangeMin);
        }

        private int rX(double xCoord) {
            return (int) (getWidth() / 2 + xCoord);
        }

        private int rY(double yCoord) {
            return (int) (getHeight() / 2 + yCoord);
        }

        @Override
        public void paint(Graphics g) {
            int w = getWidth(), h = getHeight();

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, w, h);

            g.setColor(Color.BLUE);
            ((Graphics2D) g).setStroke(new BasicStroke(1));
            g.drawLine(w / 2, 40, w / 2, h - 40);
            g.drawLine(40, h / 2, w - 40, h / 2);

            g.setColor(Color.RED);
            ((Graphics2D) g).setStroke(new BasicStroke(1));
            double xStart = -100, xEnd = 100, dx = 0.3;
            int rMaxX = w / 2, rMaxY = h / 2;
            System.out.println(rMaxX - 80);
            for (double x = xStart + dx; x <= xEnd; x += dx) {
                g.drawLine(
                        map(rX(x - dx), 0, rMaxX),
                        map(rY(Math.atan(x - dx)), 0, rMaxY),
                        map(rX(x), 0, rMaxX),
                        map(rY(Math.atan(x)), 0, rMaxY)
                );
            }
        }
    }
}