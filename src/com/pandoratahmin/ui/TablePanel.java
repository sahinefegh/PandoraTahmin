package com.pandoratahmin.ui;

import com.pandoratahmin.database.SettingsDAO;
import com.pandoratahmin.database.UserDAO;
import com.pandoratahmin.main.Main;
import com.pandoratahmin.model.User;

import java.awt.*;
import java.io.InputStream;
import java.util.List;
import javax.swing.*;

public class TablePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private final int CELL_HEIGHT = 60;
    private final int WIDTH = 650;
    
    private UserDAO userDAO;
    private SettingsDAO settingsDAO;
    
    private JPanel tableContainer; 
    private JPanel bottomPanel;
    
    // Özel F1 Fontumuzu tutacağımız değişken
    private Font customF1Font;

    public TablePanel() {
        this.userDAO = new UserDAO();
        this.settingsDAO = new SettingsDAO();
        
        // --- 1. ÖZEL FONTU YÜKLEME ---
        loadCustomFont();
        
        this.setLayout(null);
            
        JPanel topPanel = new JPanel();
        topPanel.setBounds(0, 0, WIDTH, 40);
        topPanel.setLayout(null);
        topPanel.setBackground(Color.BLACK);
        add(topPanel);
        
        JLabel lblTitle = new JLabel("PUAN DURUMU");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(customF1Font.deriveFont(Font.BOLD, 20f)); 
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 0, WIDTH, 40);
        topPanel.add(lblTitle);
        
        tableContainer = new JPanel();
        tableContainer.setLayout(null);
        tableContainer.setBackground(Color.BLACK);
        add(tableContainer); 
        
        bottomPanel = new JPanel();
        bottomPanel.setLayout(null);
        add(bottomPanel);
        
        JButton btnRefresh = new JButton("Yenile");
        btnRefresh.setFont(customF1Font.deriveFont(Font.PLAIN, 14f));
        btnRefresh.setBounds(20, 10, 120, 40);
        btnRefresh.setFocusable(false);
        bottomPanel.add(btnRefresh);
        
        JButton btnReturnMainMenu = new JButton("Ana Menüye Dön");
        btnReturnMainMenu.setFont(customF1Font.deriveFont(Font.PLAIN, 14f));
        btnReturnMainMenu.setBounds(460, 10, 170, 40);
        btnReturnMainMenu.setFocusable(false);
        bottomPanel.add(btnReturnMainMenu);
        
        // --- EVENT LİSTENER'LAR ---
        btnRefresh.addActionListener(e -> refreshTable());
        
        btnReturnMainMenu.addActionListener(e -> {
            Main.mainMenuFrame.tableFrame.dispose();
            Main.mainMenuFrame.setVisible(true);
        });
        
        // İlk açılışta tabloyu doldur
        refreshTable();
    }
    
    // --- FONT YÜKLEME METODU ---
    private void loadCustomFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/F1-Regular.otf");
            if (is != null) {
                customF1Font = Font.createFont(Font.TRUETYPE_FONT, is);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customF1Font);
            } else {
                System.err.println("Uyarı: F1-Regular.otf bulunamadı! Varsayılan font kullanılacak.");
                customF1Font = new Font("Verdana", Font.BOLD, 16);
            }
        } catch (Exception e) {
            System.err.println("Font yüklenirken hata oluştu: " + e.getMessage());
            customF1Font = new Font("Verdana", Font.BOLD, 16);
        }
    }
    
    private void refreshTable() {
        tableContainer.removeAll(); 
        
        List<User> userList = userDAO.getAllUsers(); 
        int userCount = userList.size();
        
        int tableHeight = userCount * CELL_HEIGHT;
        int totalHeight = 40 + tableHeight + 60; 
        
        this.setPreferredSize(new Dimension(WIDTH, totalHeight));
        tableContainer.setBounds(0, 40, WIDTH, tableHeight);
        bottomPanel.setBounds(0, 40 + tableHeight, WIDTH, 60);
        
        for (int i = 0; i < userCount; i++) {
            User u = userList.get(i);
            
            Color teamColor = settingsDAO.getTeamColor(u.getTeam(), Color.DARK_GRAY);
            Color fontColor = getContrastColor(teamColor); 
            
            Font tableFont = customF1Font.deriveFont(Font.BOLD, 17f);
            
            JLabel labelNum = new JLabel(String.valueOf(i + 1));
            labelNum.setForeground(fontColor);
            labelNum.setBounds(0, CELL_HEIGHT * i, 60, CELL_HEIGHT - 1);
            labelNum.setHorizontalAlignment(SwingConstants.CENTER);
            labelNum.setBackground(teamColor);
            labelNum.setOpaque(true);
            labelNum.setFont(tableFont);
            tableContainer.add(labelNum);
            
            JLabel labelName = new JLabel("          " + u.getName());
            labelName.setForeground(fontColor);
            labelName.setBounds(60, CELL_HEIGHT * i, WIDTH - 120, CELL_HEIGHT - 1);
            labelName.setHorizontalAlignment(SwingConstants.LEFT);
            labelName.setBackground(teamColor);
            labelName.setOpaque(true);
            labelName.setFont(tableFont);
            tableContainer.add(labelName);
            
            JLabel labelPoints = new JLabel(String.valueOf(u.getPoints()));
            labelPoints.setForeground(fontColor);
            labelPoints.setBounds(WIDTH - 60, CELL_HEIGHT * i, 60, CELL_HEIGHT - 1);
            labelPoints.setHorizontalAlignment(SwingConstants.CENTER);
            labelPoints.setBackground(teamColor);
            labelPoints.setOpaque(true);
            labelPoints.setFont(tableFont);
            tableContainer.add(labelPoints);
        }
        
        tableContainer.revalidate();
        tableContainer.repaint();
        
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).pack();
            window.setLocationRelativeTo(null); 
        }
    }
    
    private Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }
}