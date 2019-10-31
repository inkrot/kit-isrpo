package ru.inkrot.kit.part2;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Main extends JFrame {

    private ArrayList<Shoes> shoes = new ArrayList<>();

    private Font buttonsFont = new Font("Arial", 0, 13);
    private Font resultFont = new Font("Arial", 0, 18);

    private JTextArea resultBox;

    Main() {
        loadDataFile("data.txt");
        createUI();
    }

    private void createUI() {
        setTitle("Часть 2. Зарипов Ислам - 4441");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(550, 400);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        JButton checkByVendorCodeButton, showBabyShoesButton, showFemaleShoesButton, showMaleShoesButton;

        add(checkByVendorCodeButton = newButton(5, 5, 130, 30, "По артикулу"));
        add(showBabyShoesButton = newButton(140, 5, 130, 30, "Детская обувь"));
        add(showFemaleShoesButton = newButton(275, 5, 130, 30, "Дамская обувь"));
        add(showMaleShoesButton = newButton(410, 5, 130, 30, "Мужская обувь"));
        resultBox = new JTextArea();
        resultBox.setBounds(5, 40, 535, 327);
        resultBox.setEditable(false);
        resultBox.setFont(resultFont);
        add(resultBox);

        checkByVendorCodeButton.addActionListener(e -> checkByVendorCode());
        showBabyShoesButton.addActionListener(e -> showBabyShoes());
        showFemaleShoesButton.addActionListener(e -> showFemaleShoes());
        showMaleShoesButton.addActionListener(e -> showMaleShoes());

        setVisible(true);
    }

    private JButton newButton(int x, int y, int w, int h, String text) {
        JButton button = new JButton(text);
        button.setBounds(x, y, w, h);
        button.setFont(buttonsFont);
        button.setFocusPainted(false);
        return button;
    }

    private void loadDataFile(String dataFilePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(dataFilePath)));
            String line;
            while ((line = br.readLine()) != null)
                shoes.add(parseDataLine(line));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Shoes parseDataLine(String line) {
        String arr[] = line.split(",");
        return new Shoes(arr[0], arr[1], Integer.valueOf(arr[2]), Integer.valueOf(arr[3]));
    }

    private void checkByVendorCode() {
        String vendorCode = JOptionPane.showInputDialog(this,"Артикул", "Ввод", JOptionPane.QUESTION_MESSAGE);
        for (Shoes s: shoes) {
            if (s.vendorCode.equals(vendorCode)) {
                String count = s.count > 0 ? "(в наличии " + s.count + " шт.)" : "(нет в наличии)";
                resultBox.setText("Найдены " + s.name + "\n\tартикул: " + vendorCode + "\n\tцена: " + s.price + " руб.\n\t" + count);
                return;
            }
        }
        resultBox.setText("Артикул не найден");
    }

    private void showByShoesVendorCodeType(char type) {
        StringBuilder sb = new StringBuilder();
        for (Shoes s : shoes) {
            if (s.vendorCode.charAt(0) == type) sb.append(s).append('\n');
        }
        resultBox.setText(sb.toString());
    }

    private void showBabyShoes() {
        showByShoesVendorCodeType('П');
    }

    private void showFemaleShoes() {
        showByShoesVendorCodeType('Д');
    }

    private void showMaleShoes() {
        showByShoesVendorCodeType('М');
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new Main();
    }

    class Shoes {
        String vendorCode, name;
        int count, price;

        public Shoes(String vendorCode, String name, int count, int price) {
            this.vendorCode = vendorCode;
            this.name = name;
            this.count = count;
            this.price = price;
        }

        @Override
        public String toString() {
            return "- " + name + " (" + vendorCode + ") в наличии: " + count + " шт.";
        }
    }
}
