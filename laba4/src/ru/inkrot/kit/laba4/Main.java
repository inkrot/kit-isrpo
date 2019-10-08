package ru.inkrot.kit.laba4;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        new ControlFrame();
        //new DemoFrame();
    }
}
