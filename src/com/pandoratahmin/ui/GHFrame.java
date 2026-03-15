package com.pandoratahmin.ui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.pandoratahmin.main.Main;

import java.awt.event.WindowEvent;
import java.net.URL;

public class GHFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public GHFrame() {
        URL iconURL = getClass().getResource("/images/icon.jpg");
        if (iconURL != null) {
            this.setIconImage(new ImageIcon(iconURL).getImage());
        }
    }

    @Override
    protected void processWindowEvent(final WindowEvent e) {
        super.processWindowEvent(e);

        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (Main.mainMenuFrame != null) {
                Main.mainMenuFrame.setVisible(true);
            }
            this.dispose();
        }
    }
}