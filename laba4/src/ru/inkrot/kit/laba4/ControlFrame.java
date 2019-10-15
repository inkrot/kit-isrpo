package ru.inkrot.kit.laba4;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ControlFrame extends JFrame {

    // constants
    private Font font = new Font("Arial", 0, 14);
    public static int WIDTH = 345;
    public static int HEIGHT = 445;

    // ui
    private JComboBox typeCombo;
    private JButton chooseImageButton;
    private JTextField textValueField;
    private JButton textColorButton;
    private JTextField startSpeedField;
    private JDialog colorChooseDialog;
    private JButton runButton;
    private JTextField findIdField;
    private JLabel foundObjectLabel;
    private JTextField newIdField;
    private JComboBox speedCombo;
    private JButton editButton;
    private KeyListener numbersKeyListener;

    // control
    private FioType selectedType = FioType.PICTURE;
    private File selectedFile = null;
    private Color textColor = Color.BLACK;
    private FioObject selectedFio = null;

    public ControlFrame() {
        setTitle("УО");
        int x = (Main.SCREEN_WIDTH / 2) - (WIDTH + DemoFrame.WIDTH) / 2;
        int y = (Main.SCREEN_HEIGHT / 2) - DemoFrame.WIDTH / 2;
        setLocation(x, y);
        setSize(WIDTH, HEIGHT);
        addWindowListener(new WindowListener());
        setDefaultLookAndFeelDecorated(false);
        setLayout(null);
        setResizable(false);
        initListeners();
        createUI();

        colorChooseDialog = new ColorChooserPanel("Выберите цвет", e -> {
            textColor = (Color) e.getSource();
            textColorButton.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, textColor));
            colorChooseDialog.setVisible(false);
        });
        colorChooseDialog.setSize(570,290);
        colorChooseDialog.setLocationRelativeTo(null);
        colorChooseDialog.setVisible(false);

        new DemoFrame(this);
        setVisible(true);
        repaint();
    }

    private void initListeners() {
        numbersKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                JTextField field = (JTextField) e.getSource();
                char c = e.getKeyChar();
                if (! isNumber(c)) e.consume();
                if (field.equals(startSpeedField) && (! (c >= '1' && c <= '5') || field.getText().length() > 0)) e.consume();
            }
            @Override
            public void keyPressed(KeyEvent e) {

            }
            @Override
            public void keyReleased(KeyEvent e) {
                JTextField field = (JTextField) e.getSource();
                if (field.equals(findIdField)) findId();
            }
        };
    }

    private JLabel newLabel(String title) {
        JLabel label = new JLabel(title);
        label.setFont(font);
        return label;
    }

    private JButton newButton(String title) {
        JButton button = new JButton(title);
        button.setFont(font);
        button.setFocusable(false);
        return button;
    }

    private JComboBox newCombo(String [] data) {
        JComboBox comboBox = new JComboBox(data);
        comboBox.setFont(font);
        comboBox.setFocusable(false);
        return comboBox;
    }

    private JTextField newTextField(String text) {
        JTextField textField = new JTextField(text);
        textField.setFont(font);
        return textField;
    }

    private void createUI() {
        int w = WIDTH - 25;
        JPanel runPanel = new JPanel();
        runPanel.setBounds(10, 10, w, 170);
        runPanel.setLayout(new GridLayout(5, 2, 0, 5));
        add(runPanel);

        runPanel.add(newLabel("ФиО"));
        runPanel.add(typeCombo = newCombo(new String[]{"Картинка", "Надпись"}));
        typeCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) setFioType(e.getItem().toString());
        });

        runPanel.add(newLabel("Картинка"));
        runPanel.add(chooseImageButton = newButton("Выбрать"));
        chooseImageButton.addActionListener(e -> chooseImage());

        runPanel.add(newLabel("Надпись"));
        runPanel.add(textValueField = newTextField(""));

        runPanel.add(newLabel("Цвет надписи"));
        runPanel.add(textColorButton = newButton("Выбрать"));
        textColorButton.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, textColor));
        textColorButton.addActionListener(e -> colorChooseDialog.setVisible(true));

        runPanel.add(newLabel("Нач. скорость (1-5)"));
        runPanel.add(startSpeedField = newTextField("1"));
        startSpeedField.setTransferHandler(null);
        startSpeedField.addKeyListener(numbersKeyListener);

        add(runButton = newButton("Пуск"));
        runButton.setBounds(10, runPanel.getY() + runPanel.getHeight() + 10, w, 30);
        runButton.addActionListener(e -> runObject());

        JPanel findPanel = new JPanel();
        findPanel.setBounds(10, runPanel.getY() + runPanel.getHeight() + 60, w, 100);
        findPanel.setLayout(new GridLayout(3, 2, 0, 5));
        add(findPanel);

        findPanel.add(newLabel("Найти ФиО"));

        findPanel.add(findIdField = newTextField(""));
        findIdField.setTransferHandler(null);
        findIdField.addKeyListener(numbersKeyListener);

        findPanel.add(newLabel("Новый id"));
        findPanel.add(newIdField = newTextField(""));
        newIdField.setTransferHandler(null);
        newIdField.addKeyListener(numbersKeyListener);

        findPanel.add(newLabel("Скорость ФиО"));
        findPanel.add(speedCombo = newCombo(new String[]{"1", "2", "3", "4", "5"}));

        add(foundObjectLabel = new JLabel("ФиО не найден", SwingConstants.RIGHT));
        foundObjectLabel.setFont(new Font("Arial", 1, 14));
        foundObjectLabel.setBounds(10, findPanel.getY() + findPanel.getHeight(), w, 30);

        add(editButton = newButton("Изменить"));
        editButton.setBounds(10, findPanel.getY() + findPanel.getHeight() + 25, w, 30);
        editButton.addActionListener(e -> editFio());

        setFioType("Картинка");
        searchFailed();
    }

    private void editFio() {
        if (selectedFio == null) return;
        String newIdStr = newIdField.getText();
        String newSpeedStr = speedCombo.getSelectedItem().toString();
        try {
            int newSpeed = Integer.parseInt(newSpeedStr);
            selectedFio.setSpeed(newSpeed);
            if (newIdStr.length() > 0) {
                int newId = Integer.parseInt(newIdStr);
                int result = selectedFio.setId(newId);
                if (result == 1)
                    showMessageBox("Ошибка", "Идентификатор занят", JOptionPane.ERROR_MESSAGE);
                else if (result == 2)
                    showMessageBox("Ошибка", "Идентификатор должен быть в дипазоне от 1 до " + FioObject.MAX_NUMBER_OF_OBJECTS, JOptionPane.ERROR_MESSAGE);
            }
            findId();
        } catch (Exception e) {}
    }

    private void showMessageBox(String title, String message, int type) {
        JOptionPane.showMessageDialog(null, message, title, type);
    }

    private boolean isNumber(char ch){
        return ch >= '0' && ch <= '9';
    }

    private void findId() {
        selectedFio = null;
        if (findIdField.getText().length() < 1) {
            searchFailed();
            return;
        }
        String idStr = findIdField.getText();
        try {
            int id = Integer.valueOf(idStr);
            Set<FioObject> objects = Collections.synchronizedSet(FioObject.getAllObjects());
            for (FioObject obj : objects) {
                if (obj.getId() == id) {
                    selectedFio = obj;
                    searchSuccessful();
                    return;
                }
            }
            searchFailed();
        } catch (Exception exception) {
            findIdField.setText("");
        }
    }

    private void searchSuccessful() {
        foundObjectLabel.setText("ФиО " + selectedFio.getId() + " выбран");
        speedCombo.setSelectedItem(String.valueOf(selectedFio.getSpeed()));
        newIdField.setEnabled(true);
        speedCombo.setEnabled(true);
        foundObjectLabel.setEnabled(true);
        editButton.setEnabled(true);
    }

    private void searchFailed() {
        foundObjectLabel.setText("ФиО не найден");
        speedCombo.setSelectedIndex(0);
        newIdField.setText("");
        newIdField.setEnabled(false);
        speedCombo.setEnabled(false);
        foundObjectLabel.setEnabled(false);
        editButton.setEnabled(false);
    }

    private void runObject() {
        int speed = 1;
        try {
            speed = Integer.parseInt(startSpeedField.getText());
        } catch (Exception e) {}
        switch (selectedType) {
            case PICTURE:
                try {
                    if (selectedFile != null)
                        FioObject.addObject(ImageIO.read(selectedFile), speed);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case TEXT:
                String text = textValueField.getText();
                if (text.length() > 0)
                    FioObject.addObject(text, textColor, speed);
                break;
        }
        findId();
    }

    private void setFioType(String option) {
        if (option.equals("Картинка")) {
            textValueField.setVisible(false);
            textColorButton.setVisible(false);
            chooseImageButton.setVisible(true);
            selectedType = FioType.PICTURE;
        } else if (option.equals("Надпись")) {
            chooseImageButton.setVisible(false);
            textValueField.setVisible(true);
            textColorButton.setVisible(true);
            selectedType = FioType.TEXT;
        }
    }

    private void chooseImage() {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir") + "\\src\\resources");
        fc.setFileSelectionMode( JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileNameExtensionFilter("Картинки", "png", "jpg", "jpeg", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION )
        {
            selectedFile = fc.getSelectedFile();
            String name = selectedFile.getName();
            chooseImageButton.setText(name);
        }
    }
}