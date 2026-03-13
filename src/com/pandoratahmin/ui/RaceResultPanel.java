package com.pandoratahmin.ui;

import com.pandoratahmin.database.PredictionDAO;
import com.pandoratahmin.database.RaceDAO;
import com.pandoratahmin.database.UserDAO;
import com.pandoratahmin.main.Main;
import com.pandoratahmin.model.Prediction;
import com.pandoratahmin.model.Race;
import com.pandoratahmin.model.User;
import com.pandoratahmin.service.ScoreCalculationService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RaceResultPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private RaceDAO raceDAO;
    private UserDAO userDAO;
    private PredictionDAO predictionDAO;
    private ScoreCalculationService scoreService;
    
    private int ROW_HEIGHT = 40;
    private int WIDTH = 925;
    
    private JPanel tableContainer;
    private JComboBox<Race> cmbxRace;
    private JButton btnReturnMainMenu;

    public RaceResultPanel() {
        this.raceDAO = new RaceDAO();
        this.userDAO = new UserDAO();
        this.predictionDAO = new PredictionDAO();
        this.scoreService = new ScoreCalculationService();
        
        this.setPreferredSize(new Dimension(WIDTH, 550));
        this.setLayout(null);
        
        // --- ÜST BÖLÜM (Filtreleme) ---
        JPanel topPanel = new JPanel();
        topPanel.setBounds(0, 0, WIDTH, 60);
        topPanel.setLayout(null);
        add(topPanel);
        
        JLabel lblSelectRace = new JLabel("Yarış Seçin:");
        lblSelectRace.setFont(new Font("Verdana", Font.BOLD, 15));
        lblSelectRace.setBounds(20, 15, 120, 30);
        topPanel.add(lblSelectRace);
        
        cmbxRace = new JComboBox<>();
        cmbxRace.setFont(new Font("Verdana", Font.PLAIN, 15));
        cmbxRace.setBounds(130, 15, 250, 30);
        topPanel.add(cmbxRace);
        
        btnReturnMainMenu = new JButton("Ana Menüye Dön");
        btnReturnMainMenu.setFont(new Font("Century Gothic", Font.BOLD, 14));
        btnReturnMainMenu.setBounds(730, 10, 170, 40);
        topPanel.add(btnReturnMainMenu);
        
        // --- BAŞLIKLAR (Header) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 60, WIDTH, ROW_HEIGHT);
        headerPanel.setLayout(null);
        headerPanel.setBackground(Color.DARK_GRAY);
        add(headerPanel);
        
        String[] headers = {"İsim", "S", "Y", "Y.DT", "P", "SS", "SY", "S.DT", "Toplam"};
        int[] xPositions = {10, 200, 280, 360, 440, 520, 600, 680, 760};
        int[] widths =     {180, 70,  70,  70,  70,  70,  70,  70,  100};
        
        for (int i = 0; i < headers.length; i++) {
            JLabel lbl = new JLabel(headers[i]);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Verdana", Font.BOLD, 15));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setBounds(xPositions[i], 0, widths[i], ROW_HEIGHT);
            headerPanel.add(lbl);
        }
        
        // --- TABLO İÇERİĞİ (Scrollable) ---
        tableContainer = new JPanel();
        tableContainer.setLayout(null);
        
        JScrollPane scrollPane = new JScrollPane(tableContainer);
        scrollPane.setBounds(0, 60 + ROW_HEIGHT, WIDTH, 450 - ROW_HEIGHT);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        add(scrollPane);
        
        // Verileri Yükle
        loadComboData();
        
        // --- EVENT LISTENER'LAR ---
        cmbxRace.addActionListener(e -> refreshResults());
        
        btnReturnMainMenu.addActionListener(e -> {
            Main.mainMenuFrame.raceResultsFrame.dispose();
            Main.mainMenuFrame.setVisible(true);
        });
    }
    
    private void loadComboData() {
        List<Race> races = raceDAO.getAllRaces();
        for (Race r : races) {
            cmbxRace.addItem(r);
        }
        if (!races.isEmpty()) refreshResults();
    }
    
    private void refreshResults() {
        tableContainer.removeAll();
        Race selectedRace = (Race) cmbxRace.getSelectedItem();
        if (selectedRace == null || !selectedRace.isCompleted()) {
            tableContainer.revalidate();
            tableContainer.repaint();
            return;
        }
        
        List<User> users = userDAO.getAllUsers();
        int userCount = users.size();
        tableContainer.setPreferredSize(new Dimension(WIDTH, userCount * ROW_HEIGHT));
        
        // 1. ADIM: Maksimum Doğru Tahmin sayılarını bul (Mor renk ile vurgulamak için)
        int maxRaceRG = 0;
        int maxSprintRG = 0;
        Prediction[] tempPreds = new Prediction[userCount];
        
        for (int i = 0; i < userCount; i++) {
            Prediction pred = predictionDAO.getPrediction(users.get(i).getId(), selectedRace.getId());
            if (pred != null && !pred.isDidNotAttend()) {
                // Sırrı burada: Veritabanından gelen tahmini anlık olarak hesaplatıyoruz!
                scoreService.calculatePoints(pred, selectedRace);
                
                if (pred.getRaceRightGuess() > maxRaceRG) maxRaceRG = pred.getRaceRightGuess();
                if (pred.getSprintRightGuess() > maxSprintRG) maxSprintRG = pred.getSprintRightGuess();
            }
            tempPreds[i] = pred;
        }
        
        // 2. ADIM: Arayüzü Çiz
        int[] xPositions = {10, 200, 280, 360, 440, 520, 600, 680, 760};
        int[] widths =     {180, 70,  70,  70,  70,  70,  70,  70,  100};
        Color highlightColor = new Color(117, 49, 156); // Pandora Moru (En çok bilenler için)
        
        for (int i = 0; i < userCount; i++) {
            User u = users.get(i);
            Prediction p = tempPreds[i];
            
            // Satır Arkaplanı (Zebra deseni)
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(null);
            rowPanel.setBounds(0, i * ROW_HEIGHT, WIDTH, ROW_HEIGHT);
            rowPanel.setBackground(i % 2 == 0 ? new Color(240, 240, 240) : Color.WHITE);
            
            // 0: İsim
            JLabel lblName = createCell(u.getName(), xPositions[0], widths[0]);
            lblName.setHorizontalAlignment(SwingConstants.LEFT);
            rowPanel.add(lblName);
            
            if (p != null) {
                if (p.isDidNotAttend()) {
                    JLabel lblDNA = createCell("KATILMADI", xPositions[1], 760);
                    lblDNA.setForeground(Color.RED);
                    rowPanel.add(lblDNA);
                } else {
                    // 1: Sıralama Puanı
                    rowPanel.add(createCell(String.valueOf(p.getQualiPoints()), xPositions[1], widths[1]));
                    // 2: Yarış Puanı
                    rowPanel.add(createCell(String.valueOf(p.getRacePoints()), xPositions[2], widths[2]));
                    
                    // 3: Yarış Doğru Tahmin (Y.DT) -> Eğer maksimumsa Mor yap
                    JLabel lblRaceRG = createCell(String.valueOf(p.getRaceRightGuess()), xPositions[3], widths[3]);
                    if (p.getRaceRightGuess() == maxRaceRG && maxRaceRG > 0) {
                        lblRaceRG.setOpaque(true); lblRaceRG.setBackground(highlightColor); lblRaceRG.setForeground(Color.WHITE);
                    }
                    rowPanel.add(lblRaceRG);
                    
                    // 4: Podyum
                    JLabel lblPodium = createCell(p.isPodiumCorrect() ? "✔" : "✘", xPositions[4], widths[4]);
                    lblPodium.setForeground(p.isPodiumCorrect() ? new Color(0, 150, 0) : Color.RED);
                    rowPanel.add(lblPodium);
                    
                    if (selectedRace.hasSprint()) {
                        // 5: Sprint Q Puanı
                        rowPanel.add(createCell(String.valueOf(p.getSprintQPoints()), xPositions[5], widths[5]));
                        // 6: Sprint R Puanı
                        rowPanel.add(createCell(String.valueOf(p.getSprintRPoints()), xPositions[6], widths[6]));
                        
                        // 7: Sprint Doğru Tahmin (S.DT) -> Eğer maksimumsa Mor yap
                        JLabel lblSprintRG = createCell(String.valueOf(p.getSprintRightGuess()), xPositions[7], widths[7]);
                        if (p.getSprintRightGuess() == maxSprintRG && maxSprintRG > 0) {
                            lblSprintRG.setOpaque(true); lblSprintRG.setBackground(highlightColor); lblSprintRG.setForeground(Color.WHITE);
                        }
                        rowPanel.add(lblSprintRG);
                    } else {
                        rowPanel.add(createCell("-", xPositions[5], widths[5]));
                        rowPanel.add(createCell("-", xPositions[6], widths[6]));
                        rowPanel.add(createCell("-", xPositions[7], widths[7]));
                    }
                    
                    // 8: Toplam Puan
                    JLabel lblTotal = createCell(String.valueOf(p.getPointsEarned()), xPositions[8], widths[8]);
                    lblTotal.setFont(new Font("Verdana", Font.BOLD, 15));
                    rowPanel.add(lblTotal);
                }
            } else {
                JLabel lblNoPred = createCell("TAHMİN GİRİLMEDİ", xPositions[1], 760);
                lblNoPred.setForeground(Color.GRAY);
                rowPanel.add(lblNoPred);
            }
            
            tableContainer.add(rowPanel);
        }
        
        tableContainer.revalidate();
        tableContainer.repaint();
    }
    
    // Hücre (Label) oluşturmayı kolaylaştıran yardımcı metod
    private JLabel createCell(String text, int x, int width) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Verdana", Font.PLAIN, 14));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setBounds(x, 0, width, ROW_HEIGHT);
        return lbl;
    }
}