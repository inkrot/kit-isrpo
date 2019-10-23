package ru.inkrot.kit.part1;

import javax.swing.*;

public class Main extends JFrame {

    Main() {
        setSize(900, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initUI();
        setVisible(true);
    }

    private void initUI() {

    }

    public static void main(String[] args) {
        new Main();
    }
}
