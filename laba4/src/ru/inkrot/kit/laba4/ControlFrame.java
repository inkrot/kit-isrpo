package ru.inkrot.kit.laba4;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.*;

public class ControlFrame extends JFrame {

    // constants
    private Font font = new Font("Arial", 0, 14);
    private int WIDTH = 355;
    private int HEIGHT = 455;

    // ui
    private JComboBox typeCombo;
    private JButton chooseImageButton;
    private JTextField textValueField;
    private JButton textColorButton;
    private JDialog colorChooseDialog;
    private JButton runButton;
    private JTextField findIdField;
    private JLabel foundObjectLabel;
    private JTextField newIdField;
    private JComboBox speedCombo;
    private JButton editButton;

    // control
    private Color textColor = new Color(0 ,0 ,0);

    public ControlFrame() {
        setTitle("УО");
        setSize(WIDTH, HEIGHT);
        addWindowListener(new WindowListener());
        setLocationRelativeTo(null);
        setDefaultLookAndFeelDecorated(false);
        setLayout(null);
        //setResizable(false);
        createUI();
        setVisible(true);
        repaint();

        colorChooseDialog = new RGBColorChooserPanel("Выберите цвет", e -> {
            textColor = (Color) e.getSource();
            textColorButton.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, textColor));
            colorChooseDialog.setVisible(false);
        });
        colorChooseDialog.setSize(570,290);
        colorChooseDialog.setLocationRelativeTo(null);
        colorChooseDialog.setVisible(false);
    }

    private JLabel newLabel(String title) {
        JLabel label = new JLabel(title);
        label.setFont(font);
        return label;
    }

    private JButton newButton(String title) {
        JButton button = new JButton(title);
        button.setFont(font);
        return button;
    }

    private JComboBox newCombo(String [] data) {
        JComboBox comboBox = new JComboBox(data);
        comboBox.setFont(font);
        return comboBox;
    }

    private JTextField newTextField() {
        JTextField textField = new JTextField();
        textField.setFont(font);
        return textField;
    }

    private void createUI() {
        JPanel runPanel = new JPanel();
        runPanel.setBounds(10, 10, WIDTH - 35, 170);
        runPanel.setLayout(new GridLayout(5, 2, 0, 5));
        add(runPanel);

        runPanel.add(newLabel("ФиО"));
        runPanel.add(typeCombo = newCombo(new String[]{"Картинка", "Надпись"}));

        runPanel.add(newLabel("Путь к картинке"));
        runPanel.add(chooseImageButton = newButton("Выбрать"));

        runPanel.add(newLabel("Надпись"));
        runPanel.add(textValueField = newTextField());

        runPanel.add(newLabel("Цвет надписи"));
        runPanel.add(textColorButton = newButton("Выбрать"));
        textColorButton.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, textColor));
        textColorButton.addActionListener(v -> colorChooseDialog.setVisible(true));

        runPanel.add(newLabel("Начальная скорость"));
        runPanel.add(textValueField = newTextField());

        add(runButton = newButton("Пуск"));
        runButton.setBounds(10, runPanel.getHeight() + runPanel.getX() + 10, WIDTH - 35, 30);

        JPanel findPanel = new JPanel();
        findPanel.setBounds(10, runPanel.getHeight() + runPanel.getX() + 50, WIDTH - 35, 100);
        findPanel.setLayout(new GridLayout(3, 2, 0, 5));
        add(findPanel);

        findPanel.add(newLabel("Найти ФиО"));
        findPanel.add(findIdField = newTextField());

        findPanel.add(newLabel("Новый id"));
        findPanel.add(newIdField = newTextField());
        newIdField.setEnabled(false);

        findPanel.add(newLabel("Скорость ФиО"));
        findPanel.add(speedCombo = newCombo(new String[]{"1", "2", "3", "4", "5"}));
        speedCombo.setEnabled(false);

        // when found: ФиО {id} выбран
        add(foundObjectLabel = new JLabel("ФиО не найден", SwingConstants.RIGHT));
        foundObjectLabel.setFont(new Font("Arial", 1, 14));
        foundObjectLabel.setBounds(10, runPanel.getHeight() + runPanel.getX() + 150, WIDTH - 35, 30);

        add(editButton = newButton("Изменить"));
        editButton.setEnabled(false);
        editButton.setBounds(10, runPanel.getHeight() + runPanel.getX() + 180, WIDTH - 35, 30);
    }
}