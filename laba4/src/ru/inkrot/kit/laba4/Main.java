package ru.inkrot.kit.laba4;

import javax.swing.*;
import java.awt.*;

public class Main {

    private static Dimension screenSize;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH = (int) screenSize.getWidth();
        SCREEN_HEIGHT = (int) screenSize.getHeight();
        new ControlFrame();
    }
}
