package ru.inkrot.kit.part1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

public class Main extends JFrame {

    private JPanel valuesPanel;
    private JTextField xStartField, xEndField, nMaxField, bField;
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
        valuesPanel.setLayout(new GridLayout(5,2, 3, 3));
        valuesPanel.setBackground(Color.GRAY);
        valuesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        valuesPanel.setBounds(11, 11, 250, 170);
        add(valuesPanel);

        valuesPanel.add(newLabel("X_нач:"));
        valuesPanel.add(xStartField = newField());

        valuesPanel.add(newLabel("X_кон:"));
        valuesPanel.add(xEndField = newField());

        valuesPanel.add(newLabel("n_max:"));
        valuesPanel.add(nMaxField = newField());

        valuesPanel.add(newLabel("b:"));
        valuesPanel.add(bField = newField());

        valuesPanel.add(newEmptyComponent());

        valuesPanel.add(apply = new JButton("Применить"));
        apply.setFont(font);
        apply.setFocusPainted(false);
        apply.addActionListener(e -> applyValues());

        chartsPanel = new ChartsPanel();
        chartsPanel.setBounds(0, 0, getWidth(), getHeight());
        xStartField.setText(String.valueOf(chartsPanel.xStart));
        xEndField.setText(String.valueOf(chartsPanel.xEnd));
        nMaxField.setText(String.valueOf(chartsPanel.nMax));
        bField.setText(String.valueOf(chartsPanel.b));
        add(chartsPanel);
    }

    private JLabel newLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        return label;
    }

    private JFormattedTextField newField() {
        JFormattedTextField field = new JFormattedTextField(NumberFormat.getIntegerInstance());
        field.setFont(font);
        return field;
    }

    private JComponent newEmptyComponent() {
        JComponent component = new JButton();
        component.setVisible(false);
        return component;
    }

    private int getValue(JTextField field) {
        return Integer.parseInt(field.getText());
    }

    private void applyValues() {
        int xStart, xEnd, nMax, b;
        try {
            xStart = getValue(xStartField);
            xEnd = getValue(xEndField);
            nMax = getValue(nMaxField);
            b = getValue(bField);
            chartsPanel.xStart = xStart;
            chartsPanel.xEnd = xEnd;
            chartsPanel.nMax = nMax;
            chartsPanel.b = b;
            repaint();
        } catch (Exception e) { }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        chartsPanel.setSize(Main.this.getWidth() - 22, Main.this.getHeight() - 56);
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new Main();
    }

    class ChartsPanel extends JPanel {

        int xStart = -8;
        int xEnd = 8;
        int nMax = 100;
        int b = 1;
        int sc = 100;

        ChartsPanel() {
            addMouseWheelListener(e -> {
                if (e.getWheelRotation() < 0) sc += 3;
                else sc -= sc > 1 ? 3 : 0;
                Main.this.repaint();
            });
        }

        private int rX(double xCoord) {
            return (int) (getWidth() / 2 + xCoord * sc);
        }

        private int rY(double yCoord) {
            return (int) (getHeight() / 2 - yCoord * sc);
        }

        private double y(double x) {
            if (x > 1) {
                double y = Math.PI;
                for (int n = 0; n < nMax; n++) {
                    y += Math.pow(-1, n + 1) / ((2 * n + 1) * Math.pow(x, 2 * n + 1));
                }
                return y;
            } else throw new IllegalArgumentException("x must be more than 1");
        }

        private double z(double x) {
            return Math.atan(x) + (double) b;
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

            g2.setStroke(new BasicStroke(2));
            int rMaxX = w / 2;
            double dx = ((xEnd - xStart) / (double) rMaxX);
            for (double x = xStart + dx; x <= xEnd; x += dx) {
                if (x - dx > 1) {
                    g.setColor(Color.YELLOW);
                    g.drawLine(rX(x - dx),
                            rY(y(x - dx)),
                            rX(x),
                            rY(y(x))
                    );
                }

                g.setColor(Color.BLUE);
                g.drawLine(rX(x - dx),
                        rY(z(x - dx)),
                        rX(x),
                        rY(z(x))
                );
            }
        }
    }
}