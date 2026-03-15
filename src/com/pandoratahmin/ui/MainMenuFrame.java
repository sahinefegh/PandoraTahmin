package com.pandoratahmin.ui;

import java.net.URL;
import javax.swing.*;

import com.pandoratahmin.main.Main;

import java.awt.*;
import java.awt.event.*;

public class MainMenuFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    public static ImageIcon pandoraIcon;
    URL iconURL = getClass().getResource("/images/icon.jpg");

    JLabel lblCalendar = new JLabel("Yarış Takvimi");
    JLabel lblCalculatePoints = new JLabel("Puan Hesapla");
    JLabel lblResults = new JLabel("Sonuçlar");
    JLabel lblEditUser = new JLabel("Kişi Düzenle");
    JLabel lblSettings = new JLabel("Ayarlar");
    JLabel lblExit = new JLabel("Çıkış");

    JLabel lblRaceResults = new JLabel("Yarış Sonuçları");
    JLabel lblTable = new JLabel("Puan Durumu");
    JLabel lblReturnMainMenu = new JLabel("Geri Dön");

    public static JFrame calendarFrame;
    public static JFrame calculatePointsFrame;
    public static JFrame editUserFrame;
    public static JFrame settingsFrame;
    public static JFrame tableFrame;
    public static JFrame raceResultsFrame;

    public MainMenuFrame() {
        // --- 1. FRAME AYARLARI ---
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Pandora Tahmin - Ana Sayfa");
        this.setResizable(false);

        if (iconURL != null) {
            pandoraIcon = new ImageIcon(iconURL);
            this.setIconImage(pandoraIcon.getImage());
        } else {
            System.err.println("Uyarı: Ana ikon bulunamadı! (/images/icon.jpg)");
        }

        // Arayüz elemanlarını tutacak ana panel
        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setPreferredSize(new Dimension(750, 510));
        this.setContentPane(contentPane);

        // --- 2. ANA MENÜ BUTONLARININ DÖNGÜ İLE OLUŞTURULMASI ---
        JLabel[] mainLabels = { lblCalendar, lblCalculatePoints, lblResults, lblEditUser, lblSettings, lblExit };
        String[] mainIcons = { "calendar.png", "calculator.png", "chequeredFlag.png", "user.png", "settings.png",
                "saveandexit.png" };
        Font mainFont = FontManager.getFont(Font.BOLD, 18);

        int startX = 90, startY = 50, xOffset = 205, yOffset = 230;

        for (int i = 0; i < mainLabels.length; i++) {
            JLabel lbl = mainLabels[i];

            URL imgURL = getClass().getResource("/images/" + mainIcons[i]);
            if (imgURL != null) {
                lbl.setIcon(new ImageIcon(imgURL));
            }

            lbl.setFont(mainFont);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setVerticalTextPosition(SwingConstants.BOTTOM);
            lbl.setHorizontalTextPosition(SwingConstants.CENTER);
            lbl.setIconTextGap(10);

            int x = startX + (i % 3) * xOffset;
            int y = startY + (i / 3) * yOffset;
            lbl.setBounds(x, y, 150, 170);

            contentPane.add(lbl);
        }

        // --- 3. ALT MENÜ (SONUÇLAR) BUTONLARININ DÖNGÜ İLE OLUŞTURULMASI ---
        JLabel[] subLabels = { lblRaceResults, lblTable, lblReturnMainMenu };
        String[] subIcons = { "chequeredFlag.png", "puantablosu.png", "return128.png" };
        Font subFont = FontManager.getFont(Font.BOLD, 17);

        for (int i = 0; i < subLabels.length; i++) {
            JLabel lbl = subLabels[i];

            URL imgURL = getClass().getResource("/images/" + subIcons[i]);
            if (imgURL != null) {
                lbl.setIcon(new ImageIcon(imgURL));
            }

            lbl.setFont(subFont);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setVerticalTextPosition(SwingConstants.BOTTOM);
            lbl.setHorizontalTextPosition(SwingConstants.CENTER);
            lbl.setIconTextGap(10);
            lbl.setVisible(false);

            int x = startX + (i * xOffset);
            lbl.setBounds(x, 150, (i == 0 ? 170 : 150), 170);

            contentPane.add(lbl);
        }

        this.pack();
        this.setLocationRelativeTo(null);

        // --- 4. EVENT LISTENER'LARI BAĞLAMA ---
        setupListeners();
    }

    // Event işlerini düzenli tutmak için ayrı bir metod
    private void setupListeners() {
        // Çıkış Butonu
        lblExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Sistemden çıkılıyor...");
                System.exit(0);
            }
        });

        // "Sonuçlar" menüsüne geçiş
        lblResults.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                toggleMenu(false);
            }
        });

        // "Ana Menü"ye dönüş
        lblReturnMainMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                toggleMenu(true);
            }
        });

        lblEditUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (editUserFrame != null) {
                    editUserFrame.dispose();
                }

                editUserFrame = new GHFrame();
                editUserFrame.setTitle("Pandora Tahmin - Kişiler");
                EditUserPanel editUserPanel = new EditUserPanel();
                editUserFrame.getContentPane().add(editUserPanel);
                editUserFrame.pack();
                editUserFrame.setResizable(false);
                editUserFrame.setLocationRelativeTo(null);
                editUserFrame.setVisible(true);
                Main.mainMenuFrame.setVisible(false);
            }
        });

        lblCalendar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (calendarFrame != null) {
                    calendarFrame.dispose();
                }

                calendarFrame = new GHFrame();
                calendarFrame.setTitle("Pandora Tahmin - Yarışlar");
                CalendarPanel calendarPanel = new CalendarPanel();
                calendarFrame.getContentPane().add(calendarPanel);
                calendarFrame.pack();
                calendarFrame.setResizable(false);
                calendarFrame.setLocationRelativeTo(null);
                calendarFrame.setVisible(true);
                Main.mainMenuFrame.setVisible(false);
            }
        });

        lblCalculatePoints.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (calculatePointsFrame != null) {
                    calculatePointsFrame.dispose();
                }

                calculatePointsFrame = new GHFrame();
                calculatePointsFrame.setTitle("Pandora Tahmin - Puan Hesapla");
                CalculatePanel calculatePanel = new CalculatePanel();
                calculatePointsFrame.getContentPane().add(calculatePanel);
                calculatePointsFrame.pack();
                calculatePointsFrame.setResizable(false);
                calculatePointsFrame.setLocationRelativeTo(null);
                calculatePointsFrame.setVisible(true);
                Main.mainMenuFrame.setVisible(false);
            }
        });

        lblTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (tableFrame != null) {
                    tableFrame.dispose();
                }

                tableFrame = new GHFrame();
                tableFrame.setTitle("Pandora Tahmin - Puan Tablosu");
                tableFrame.getContentPane().add(new TablePanel());

                tableFrame.setResizable(false);
                tableFrame.pack();
                tableFrame.setLocationRelativeTo(null);
                tableFrame.setVisible(true);
                Main.mainMenuFrame.setVisible(false);
            }
        });

        lblRaceResults.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (raceResultsFrame != null)
                    raceResultsFrame.dispose();

                raceResultsFrame = new GHFrame();
                raceResultsFrame.setTitle("Pandora Tahmin - Yarış Sonuçları");
                raceResultsFrame.getContentPane().add(new RaceResultPanel());
                raceResultsFrame.setResizable(false);
                raceResultsFrame.pack();
                raceResultsFrame.setLocationRelativeTo(null);
                raceResultsFrame.setVisible(true);
                Main.mainMenuFrame.setVisible(false);
            }
        });

        lblSettings.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (settingsFrame != null) {
                    settingsFrame.dispose();
                }

                settingsFrame = new GHFrame();
                settingsFrame.setTitle("Pandora Tahmin - Ayarlar");
                SettingsPanel settingsPanel = new SettingsPanel();
                settingsFrame.getContentPane().add(settingsPanel);
                settingsFrame.pack();
                settingsFrame.setResizable(false);
                settingsFrame.setLocationRelativeTo(null);
                settingsFrame.setVisible(true);
                Main.mainMenuFrame.setVisible(false);
            }
        });
    }

    // Ana menü ile Alt menü arasındaki görünürlük geçişini tek metotta hallediyoruz
    private void toggleMenu(boolean showMain) {
        lblCalendar.setVisible(showMain);
        lblCalculatePoints.setVisible(showMain);
        lblResults.setVisible(showMain);
        lblEditUser.setVisible(showMain);
        lblSettings.setVisible(showMain);
        lblExit.setVisible(showMain);

        lblRaceResults.setVisible(!showMain);
        lblTable.setVisible(!showMain);
        lblReturnMainMenu.setVisible(!showMain);

        this.setTitle(showMain ? "Pandora Tahmin - Ana Sayfa" : "Pandora Tahmin - Sonuçlar");
    }
}