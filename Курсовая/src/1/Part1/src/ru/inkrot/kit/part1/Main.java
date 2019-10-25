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
        initListeners();
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

    private void initListeners() {

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
        int sc = 10;

        ChartsPanel() {
            addMouseWheelListener(e -> {
                if (e.getWheelRotation() < 0) sc += 3;
                else sc -= 3;
                Main.this.repaint();
            });
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
            return (int) (getWidth() / 2 + xCoord * sc);
        }

        private int rY(double yCoord) {
            return (int) (getHeight() / 2 + yCoord * sc);
        }

        @Override
        public void paint(Graphics g) {
            int w = getWidth(), h = getHeight();

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, w, h);

            g.setColor(Color.GRAY);
            ((Graphics2D) g).setStroke(new BasicStroke(1));
            g.drawLine(w / 2, 40, w / 2, h - 40);
            g.drawLine(40, h / 2, w - 40, h / 2);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(1));
            int rMaxX = w / 2, rMaxY = h / 2;
            double xStart = -50, xEnd = 50, dx = ((xEnd - xStart) / rMaxX);
            for (double x = xStart + dx; x <= xEnd; x += dx) {
                g.drawLine(rX(x - dx),
                        rY(-Math.atan(x - dx)),
                        rX(x),
                        rY(-Math.atan(x))
                );
            }
        }
    }
}