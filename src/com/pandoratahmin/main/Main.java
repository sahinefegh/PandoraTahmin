package com.pandoratahmin.main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;
import com.pandoratahmin.ui.MainMenuFrame;
import com.pandoratahmin.database.DatabaseManager;

public class Main {
public static MainMenuFrame mainMenuFrame;
    

    public static void main(String[] args) {
        
        DatabaseManager.initializeDatabase();
        
		SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            FlatLightLaf.setup();
            mainMenuFrame = new MainMenuFrame();
            mainMenuFrame.setVisible(true);
        });
	}
}
