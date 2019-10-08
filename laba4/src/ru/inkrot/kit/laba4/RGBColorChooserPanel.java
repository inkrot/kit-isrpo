package ru.inkrot.kit.laba4;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

class RGBColorChooserPanel extends JDialog implements ActionListener {

    private final JColorChooser jCC;
    private final JPanel        panel;

    ActionListener onColorChosen;

    public RGBColorChooserPanel(final String title, ActionListener onColorChosen) {
        this.setTitle(title);
        this.onColorChosen = onColorChosen;
        this.jCC = new JColorChooser();
        try {
            this.modifyJColorChooser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.panel = new JPanel();
        this.panel.add(this.jCC);
        this.panel.setOpaque(false);
        this.jCC.setOpaque(false);
        this.jCC.setPreviewPanel(new JPanel());
        this.jCC.setColor(120, 20, 57);
        this.add(this.panel, BorderLayout.CENTER);
        JButton button = new JButton("Ок");
        button.addActionListener(this);
        this.add(button, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.pack();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2);
        this.setResizable(false);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        e.setSource(jCC.getColor());
        onColorChosen.actionPerformed(e);
    }

    private void modifyJColorChooser() throws Exception {
        final AbstractColorChooserPanel[] panels = this.jCC.getChooserPanels();
        for (final AbstractColorChooserPanel accp : panels) {
            if (!accp.getDisplayName().equals("HSL")) {
                this.jCC.removeChooserPanel(accp);
            }
        }
        final AbstractColorChooserPanel[] colorPanels = this.jCC.getChooserPanels();
        final AbstractColorChooserPanel cp = colorPanels[0];
        Field f = null;
        f = cp.getClass().getDeclaredField("panel");
        f.setAccessible(true);
        Object colorPanel = null;
        colorPanel = f.get(cp);
        Field f2 = null;
        f2 = colorPanel.getClass().getDeclaredField("spinners");
        f2.setAccessible(true);
        Object rows = null;
        rows = f2.get(colorPanel);
        final Object transpSlispinner = Array.get(rows, 3);
        Field f3 = null;
        f3 = transpSlispinner.getClass().getDeclaredField("slider");
        f3.setAccessible(true);
        JSlider slider = null;
        slider = (JSlider) f3.get(transpSlispinner);
        slider.setVisible(false);
        Field f4 = null;
        f4 = transpSlispinner.getClass().getDeclaredField("spinner");
        f4.setAccessible(true);
        JSpinner spinner = null;
        spinner = (JSpinner) f4.get(transpSlispinner);
        spinner.setVisible(false);
        Field f5 = null;
        f5 = transpSlispinner.getClass().getDeclaredField("label");
        f5.setAccessible(true);
        JLabel label = null;
        label = (JLabel) f5.get(transpSlispinner);
        label.setVisible(false);
    }
}
