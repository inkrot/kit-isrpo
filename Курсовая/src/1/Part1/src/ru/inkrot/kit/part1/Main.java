package ru.inkrot.kit.part1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main extends JFrame {

    private JPanel valuesPanel;
    private JLabel label1, label2;
    private JTextField xStart1, xEnd1;
    private JTextField xStart2, xEnd2;
    private JButton apply;
    private ChartsPanel chartsPanel;

    private static Dimension screenSize;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    private Font font = new Font("Arial", 0, 16);

    Main() {
        setSize(900, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        valuesPanel = new JPanel();
        valuesPanel.setLayout(new GridLayout(3,2, 3, 10));
        valuesPanel.setBackground(Color.GRAY);
        valuesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        valuesPanel.setBounds(10, 10, 300, 120);
        add(valuesPanel);

        valuesPanel.add(label1 = newLabel("X (нач):"));
        valuesPanel.add(xStart1 = newField());

        valuesPanel.add(label1 = newLabel("X (кон):"));
        valuesPanel.add(xEnd1 = newField());

        JButton emptyButton = new JButton();
        emptyButton.setVisible(false);
        valuesPanel.add(emptyButton);

        valuesPanel.add(apply = new JButton("Применить"));
        apply.setFocusPainted(false);
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

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH = (int) screenSize.getWidth();
        SCREEN_HEIGHT = (int) screenSize.getHeight();
        new Main();
    }

    class ChartsPanel extends JFrame {

        ChartsPanel() {

        }
    }
}