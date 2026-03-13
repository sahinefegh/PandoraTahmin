package com.pandoratahmin.ui;

import com.pandoratahmin.database.PredictionDAO;
import com.pandoratahmin.database.RaceDAO;
import com.pandoratahmin.database.SettingsDAO;
import com.pandoratahmin.database.UserDAO;
import com.pandoratahmin.main.Main;
import com.pandoratahmin.model.Prediction;
import com.pandoratahmin.model.Race;
import com.pandoratahmin.model.User;
import com.pandoratahmin.service.ScoreCalculationService;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class CalculatePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    // --- Veritabanı ve Servis Bağlantıları ---
    private UserDAO userDAO;
    private RaceDAO raceDAO;
    private PredictionDAO predictionDAO;
    private SettingsDAO settingsDAO;
    private ScoreCalculationService scoreService;
    
    private Race selectedRace;
    private User selectedUser;
    private Prediction currentPrediction;

    JButton btnReturnMainMenu = new JButton("Ana Menüye Dön");
    JButton btnCalculatePoints = new JButton("Hesapla");
    JComboBox<Race> cmbxRace = new JComboBox<>();
    JComboBox<User> cmbxUser = new JComboBox<>();
    JLabel lblCalcStatus = new JLabel("");
    
    GHTextField txtQ1st = new GHTextField("1"); GHTextField txtQ2nd = new GHTextField("2"); GHTextField txtQ3rd = new GHTextField("3");
    GHTextField txtSprintQ1st = new GHTextField("1"); GHTextField txtSprintQ2nd = new GHTextField("2"); GHTextField txtSprintQ3rd = new GHTextField("3");
    GHTextField txtR1st = new GHTextField("1"); GHTextField txtR2nd = new GHTextField("2"); GHTextField txtR3rd = new GHTextField("3");
    GHTextField txtR4th = new GHTextField("4"); GHTextField txtR5th = new GHTextField("5"); GHTextField txtR6th = new GHTextField("6");
    GHTextField txtR7th = new GHTextField("7"); GHTextField txtR8th = new GHTextField("8"); GHTextField txtR9th = new GHTextField("9");
    GHTextField txtR10th = new GHTextField("10"); GHTextField txtFastestLap = new GHTextField("EHT");
    
    GHTextField txtSprintR1st = new GHTextField("1"); GHTextField txtSprintR2nd = new GHTextField("2"); GHTextField txtSprintR3rd = new GHTextField("3");
    GHTextField txtSprintR4th = new GHTextField("4"); GHTextField txtSprintR5th = new GHTextField("5"); GHTextField txtSprintR6th = new GHTextField("6");
    GHTextField txtSprintR7th = new GHTextField("7"); GHTextField txtSprintR8th = new GHTextField("8");
    
    JTextField txtQualiFull = new JTextField();
    JTextField txtRaceFull = new JTextField();
    JTextField txtSprintQFull = new JTextField();
    JTextField txtSprintRFull = new JTextField();
    JButton btnInsert = new JButton("Yerleştir");
    
    JCheckBox chckbxQuali = new JCheckBox("Tahmin yapmadı");
    JCheckBox chckbxRace = new JCheckBox("Tahmin yapmadı");
    JCheckBox chckbxSprintQ = new JCheckBox("Tahmin yapmadı");
    JCheckBox chckbxSprintR = new JCheckBox("Tahmin yapmadı");
    JCheckBox chckbxDNA = new JCheckBox("Katılmadı");
    
    public CalculatePanel() {
        this.userDAO = new UserDAO();
        this.raceDAO = new RaceDAO();
        this.predictionDAO = new PredictionDAO();
        this.settingsDAO = new SettingsDAO();
        this.scoreService = new ScoreCalculationService();
        
        this.setPreferredSize(new Dimension(900,500));
        this.setLayout(null);
        
        setupUI();
        loadComboData();
        setupListeners();
    }
    
    private void setupUI() {
        btnReturnMainMenu.setFont(new Font("Century Gothic", Font.BOLD, 13));
        btnReturnMainMenu.setBounds(740, 450, 150, 40);
        btnReturnMainMenu.setFocusable(false);
        add(btnReturnMainMenu);
        
        btnInsert.setFont(new Font("Century Gothic", Font.BOLD, 13));
        btnInsert.setBounds(620, 455, 90, 30);
        btnInsert.setFocusable(false);
        add(btnInsert);
        
        JPanel panelTop = new JPanel();
        panelTop.setBounds(0, 0, 900, 60);
        panelTop.setLayout(null);
        add(panelTop);
        
        JPanel panelPrediction = new JPanel();
        panelPrediction.setBounds(0, 65, 900, 435);
        panelPrediction.setLayout(null);
        add(panelPrediction);

        btnCalculatePoints.setFont(new Font("Century Gothic", Font.BOLD, 14));
        btnCalculatePoints.setBounds(325, 385, 150, 40);
        btnCalculatePoints.setFocusable(false);
        panelPrediction.add(btnCalculatePoints);
        
        lblCalcStatus.setFont(new Font("Verdana", Font.PLAIN, 14));
        lblCalcStatus.setBounds(105, 385, 200, 40);
        lblCalcStatus.setHorizontalAlignment(SwingConstants.RIGHT);
        panelPrediction.add(lblCalcStatus);
        
        JLabel lblUser = new JLabel("Kişi :");
        lblUser.setFont(new Font("Verdana", Font.BOLD, 14));
        lblUser.setBounds(60, 20, 80, 20);
        panelTop.add(lblUser);
        
        cmbxUser.setFont(new Font("Verdana", Font.PLAIN, 14));
        cmbxUser.setBounds(140, 18, 180, 25);
        cmbxUser.setFocusable(false);
        panelTop.add(cmbxUser);
        
        JLabel lblRace = new JLabel("Yarış :");
        lblRace.setFont(new Font("Verdana", Font.BOLD, 14));
        lblRace.setBounds(570, 20, 80, 20);
        panelTop.add(lblRace);
        
        cmbxRace.setFont(new Font("Verdana", Font.PLAIN, 14));
        cmbxRace.setBounds(650, 18, 180, 25);
        cmbxRace.setFocusable(false);
        panelTop.add(cmbxRace);
        
        // --- Sıralama Bölümü ---
        JLabel lblQuali = new JLabel("Sıralama");
        lblQuali.setHorizontalAlignment(SwingConstants.CENTER);
        lblQuali.setFont(new Font("Verdana", Font.BOLD, 18));
        lblQuali.setBounds(0, 0, 475, 40);
        panelPrediction.add(lblQuali);
        
        txtQ1st.setGHLocation(125, 50); panelPrediction.add(txtQ1st); panelPrediction.add(txtQ1st.getLabel());
        txtQ2nd.setGHLocation(215, 50); panelPrediction.add(txtQ2nd); panelPrediction.add(txtQ2nd.getLabel());
        txtQ3rd.setGHLocation(305, 50); panelPrediction.add(txtQ3rd); panelPrediction.add(txtQ3rd.getLabel());
        
        chckbxQuali.setFont(new Font("Verdana", Font.PLAIN, 13));
        chckbxQuali.setBounds(122, 85, 140, 20);
        chckbxQuali.setFocusable(false);
        panelPrediction.add(chckbxQuali);
        
        txtQualiFull.setBounds(272, 85, 90, 25);
        txtQualiFull.setFont(GHTextField.textFieldFont);
        panelPrediction.add(txtQualiFull);
        
        // --- Sprint Sıralama Bölümü ---
        JLabel lblSprintQ = new JLabel("Sprint Sıralama");
        lblSprintQ.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintQ.setFont(new Font("Verdana", Font.BOLD, 18));
        lblSprintQ.setBounds(500, 0, 400, 40);
        panelPrediction.add(lblSprintQ);
        
        txtSprintQ1st.setGHLocation(585, 50); panelPrediction.add(txtSprintQ1st); panelPrediction.add(txtSprintQ1st.getLabel());
        txtSprintQ2nd.setGHLocation(675, 50); panelPrediction.add(txtSprintQ2nd); panelPrediction.add(txtSprintQ2nd.getLabel());
        txtSprintQ3rd.setGHLocation(765, 50); panelPrediction.add(txtSprintQ3rd); panelPrediction.add(txtSprintQ3rd.getLabel());
        
        chckbxSprintQ.setFont(new Font("Verdana", Font.PLAIN, 13));
        chckbxSprintQ.setBounds(582, 85, 140, 20);
        chckbxSprintQ.setFocusable(false);
        panelPrediction.add(chckbxSprintQ);
        
        txtSprintQFull.setBounds(732, 85, 90, 25);
        txtSprintQFull.setFont(GHTextField.textFieldFont);
        panelPrediction.add(txtSprintQFull);
        
        // --- Yarış Bölümü ---
        JLabel lblRaceT = new JLabel("Yarış");
        lblRaceT.setHorizontalAlignment(SwingConstants.CENTER);
        lblRaceT.setFont(new Font("Verdana", Font.BOLD, 18));
        lblRaceT.setBounds(0, 120, 475, 40);
        panelPrediction.add(lblRaceT);
        
        txtR1st.setGHLocation(35, 170); panelPrediction.add(txtR1st); panelPrediction.add(txtR1st.getLabel());
        txtR2nd.setGHLocation(125, 170); panelPrediction.add(txtR2nd); panelPrediction.add(txtR2nd.getLabel());
        txtR3rd.setGHLocation(215, 170); panelPrediction.add(txtR3rd); panelPrediction.add(txtR3rd.getLabel());
        txtR4th.setGHLocation(305, 170); panelPrediction.add(txtR4th); panelPrediction.add(txtR4th.getLabel());
        txtR5th.setGHLocation(395, 170); panelPrediction.add(txtR5th); panelPrediction.add(txtR5th.getLabel());
        txtR6th.setGHLocation(35, 210); panelPrediction.add(txtR6th); panelPrediction.add(txtR6th.getLabel());
        txtR7th.setGHLocation(125, 210); panelPrediction.add(txtR7th); panelPrediction.add(txtR7th.getLabel());
        txtR8th.setGHLocation(215, 210); panelPrediction.add(txtR8th); panelPrediction.add(txtR8th.getLabel());
        txtR9th.setGHLocation(305, 210); panelPrediction.add(txtR9th); panelPrediction.add(txtR9th.getLabel());
        txtR10th.setGHLocation(395, 210); panelPrediction.add(txtR10th); panelPrediction.add(txtR10th.getLabel());
        txtFastestLap.setGHLocation(125, 250); panelPrediction.add(txtFastestLap);
        txtFastestLap.getLabel().setLocation(82, 249); panelPrediction.add(txtFastestLap.getLabel());
        
        chckbxRace.setFont(new Font("Verdana", Font.PLAIN, 13));
        chckbxRace.setBounds(230, 252, 140, 20);
        chckbxRace.setFocusable(false);
        panelPrediction.add(chckbxRace);
        
        txtRaceFull.setBounds(125, 285, 260, 25);
        txtRaceFull.setFont(GHTextField.textFieldFont);
        panelPrediction.add(txtRaceFull);
        
        // --- Sprint Yarış Bölümü ---
        JLabel lblSprintR = new JLabel("Sprint Yarış");
        lblSprintR.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintR.setFont(new Font("Verdana", Font.BOLD, 18));
        lblSprintR.setBounds(500, 120, 400, 40);
        panelPrediction.add(lblSprintR);
        
        txtSprintR1st.setGHLocation(535, 170); panelPrediction.add(txtSprintR1st); panelPrediction.add(txtSprintR1st.getLabel());
        txtSprintR2nd.setGHLocation(625, 170); panelPrediction.add(txtSprintR2nd); panelPrediction.add(txtSprintR2nd.getLabel());
        txtSprintR3rd.setGHLocation(715, 170); panelPrediction.add(txtSprintR3rd); panelPrediction.add(txtSprintR3rd.getLabel());
        txtSprintR4th.setGHLocation(805, 170); panelPrediction.add(txtSprintR4th); panelPrediction.add(txtSprintR4th.getLabel());
        txtSprintR5th.setGHLocation(535, 210); panelPrediction.add(txtSprintR5th); panelPrediction.add(txtSprintR5th.getLabel());
        txtSprintR6th.setGHLocation(625, 210); panelPrediction.add(txtSprintR6th); panelPrediction.add(txtSprintR6th.getLabel());
        txtSprintR7th.setGHLocation(715, 210); panelPrediction.add(txtSprintR7th); panelPrediction.add(txtSprintR7th.getLabel());
        txtSprintR8th.setGHLocation(805, 210); panelPrediction.add(txtSprintR8th); panelPrediction.add(txtSprintR8th.getLabel());
        
        chckbxSprintR.setFont(new Font("Verdana", Font.PLAIN, 13));
        chckbxSprintR.setBounds(630, 252, 140, 20);
        chckbxSprintR.setFocusable(false);
        panelPrediction.add(chckbxSprintR);
        
        txtSprintRFull.setBounds(625, 285, 180, 25);
        txtSprintRFull.setFont(GHTextField.textFieldFont);
        panelPrediction.add(txtSprintRFull);
        
        // --- Genel Katılım ---
        chckbxDNA.setHorizontalAlignment(SwingConstants.CENTER);
        chckbxDNA.setFont(new Font("Verdana", Font.PLAIN, 13));
        chckbxDNA.setBounds(325, 330, 150, 20);
        chckbxDNA.setFocusable(false);
        panelPrediction.add(chckbxDNA);
    }
    
    private void loadComboData() {
        List<User> users = userDAO.getAllUsers();
        for (User u : users) cmbxUser.addItem(u);
        
        List<Race> races = raceDAO.getAllRaces();
        for (Race r : races) cmbxRace.addItem(r);
    }

    private void setupListeners() {
        ActionListener updateComboAction = e -> updatePanel();
        cmbxUser.addActionListener(updateComboAction);
        cmbxRace.addActionListener(updateComboAction);
        
        chckbxQuali.addActionListener(e -> setQualiEnabled(!chckbxQuali.isSelected()));
        chckbxRace.addActionListener(e -> setRaceEnabled(!chckbxRace.isSelected()));
        chckbxSprintQ.addActionListener(e -> setSprintQEnabled(!chckbxSprintQ.isSelected()));
        chckbxSprintR.addActionListener(e -> setSprintREnabled(!chckbxSprintR.isSelected()));
        
        chckbxDNA.addActionListener(e -> {
            boolean dna = chckbxDNA.isSelected();
            chckbxQuali.setSelected(dna); chckbxQuali.setEnabled(!dna); setQualiEnabled(!dna);
            chckbxRace.setSelected(dna); chckbxRace.setEnabled(!dna); setRaceEnabled(!dna);
            if(selectedRace != null && selectedRace.hasSprint()) {
                chckbxSprintQ.setSelected(dna); chckbxSprintQ.setEnabled(!dna); setSprintQEnabled(!dna);
                chckbxSprintR.setSelected(dna); chckbxSprintR.setEnabled(!dna); setSprintREnabled(!dna);
            }
        });
        
        btnReturnMainMenu.addActionListener(e -> {
            Main.mainMenuFrame.calculatePointsFrame.dispose();
            Main.mainMenuFrame.setVisible(true);
        });
        
        btnInsert.addActionListener(e -> {
            insertDrivers();
            txtQualiFull.setText(""); txtRaceFull.setText(""); txtSprintQFull.setText(""); txtSprintRFull.setText("");
        });
        
        btnCalculatePoints.addActionListener(e -> {
            if(selectedUser == null || selectedRace == null) return;
            
            if(!chckbxDNA.isSelected() && !checkDrivers()) {
                lblCalcStatus.setText("HATA: Geçersiz pilot kısaltması!");
                lblCalcStatus.setForeground(Color.RED);
                return;
            }
            
            // Yeni Prediction nesnesini arayüzdeki verilerle doldur
            Prediction pred = new Prediction(selectedUser.getId(), selectedRace.getId());
            pred.setDidNotAttend(chckbxDNA.isSelected());
            
            if(!chckbxQuali.isSelected()) {
                pred.setQualiPred(new String[]{txtQ1st.getText(), txtQ2nd.getText(), txtQ3rd.getText()});
            }
            if(!chckbxRace.isSelected()) {
                pred.setRacePred(new String[]{txtR1st.getText(), txtR2nd.getText(), txtR3rd.getText(), txtR4th.getText(), txtR5th.getText(),
                                               txtR6th.getText(), txtR7th.getText(), txtR8th.getText(), txtR9th.getText(), txtR10th.getText()});
                pred.setFastestLapPred(txtFastestLap.getText());
            }
            if(selectedRace.hasSprint()) {
                if(!chckbxSprintQ.isSelected()) {
                    pred.setSprintQPred(new String[]{txtSprintQ1st.getText(), txtSprintQ2nd.getText(), txtSprintQ3rd.getText()});
                }
                if(!chckbxSprintR.isSelected()) {
                    pred.setSprintRPred(new String[]{txtSprintR1st.getText(), txtSprintR2nd.getText(), txtSprintR3rd.getText(), txtSprintR4th.getText(),
                                                      txtSprintR5th.getText(), txtSprintR6th.getText(), txtSprintR7th.getText(), txtSprintR8th.getText()});
                }
            }
            
            // Servis üzerinden puanı hesapla
            scoreService.calculatePoints(pred, selectedRace);
            
            // Veritabanına kaydet
            predictionDAO.savePrediction(pred);
            
            lblCalcStatus.setText("Tahmin Kaydedildi! Puan: " + pred.getPointsEarned());
            lblCalcStatus.setForeground(new Color(0, 150, 0)); // Yeşil
        });
        
        // Uygulama açılışında ilk paneli güncelle
        updatePanel();
    }

    public void updatePanel() {
        selectedRace = (Race) cmbxRace.getSelectedItem();
        selectedUser = (User) cmbxUser.getSelectedItem();
        lblCalcStatus.setText("");
        
        if (selectedRace != null && selectedUser != null) {
            btnCalculatePoints.setEnabled(selectedRace.isCompleted());
            
            if(!selectedRace.isCompleted()) {
                lblCalcStatus.setText("Yarış sonucu henüz girilmemiş.");
                lblCalcStatus.setForeground(Color.ORANGE);
            }
            
            // Veritabanından bu yarış ve kullanıcı için olan eski tahmini çek
            currentPrediction = predictionDAO.getPrediction(selectedUser.getId(), selectedRace.getId());
            
            fillFieldsFromPrediction();
            updateSprintVisibility();
            
        } else {
            btnCalculatePoints.setEnabled(false);
            resetAllFields();
        }
    }
    
    private void fillFieldsFromPrediction() {
        resetAllFields();
        
        if (currentPrediction == null) {
            chckbxDNA.setSelected(false);
            return;
        }
        
        chckbxDNA.setSelected(currentPrediction.isDidNotAttend());
        if(currentPrediction.isDidNotAttend()) {
            chckbxQuali.setSelected(true); chckbxRace.setSelected(true);
            chckbxSprintQ.setSelected(true); chckbxSprintR.setSelected(true);
            return;
        }

        String[] qPred = currentPrediction.getQualiPred();
        if(qPred != null && qPred.length >= 3) {
            txtQ1st.setText(qPred[0]); txtQ2nd.setText(qPred[1]); txtQ3rd.setText(qPred[2]);
            chckbxQuali.setSelected(false);
        } else { chckbxQuali.setSelected(true); }

        String[] rPred = currentPrediction.getRacePred();
        if(rPred != null && rPred.length >= 10) {
            txtR1st.setText(rPred[0]); txtR2nd.setText(rPred[1]); txtR3rd.setText(rPred[2]);
            txtR4th.setText(rPred[3]); txtR5th.setText(rPred[4]); txtR6th.setText(rPred[5]);
            txtR7th.setText(rPred[6]); txtR8th.setText(rPred[7]); txtR9th.setText(rPred[8]); txtR10th.setText(rPred[9]);
            txtFastestLap.setText(currentPrediction.getFastestLapPred());
            chckbxRace.setSelected(false);
        } else { chckbxRace.setSelected(true); }
        
        if(selectedRace.hasSprint()) {
            String[] sqPred = currentPrediction.getSprintQPred();
            if(sqPred != null && sqPred.length >= 3) {
                txtSprintQ1st.setText(sqPred[0]); txtSprintQ2nd.setText(sqPred[1]); txtSprintQ3rd.setText(sqPred[2]);
                chckbxSprintQ.setSelected(false);
            } else { chckbxSprintQ.setSelected(true); }
            
            String[] srPred = currentPrediction.getSprintRPred();
            if(srPred != null && srPred.length >= 8) {
                txtSprintR1st.setText(srPred[0]); txtSprintR2nd.setText(srPred[1]); txtSprintR3rd.setText(srPred[2]);
                txtSprintR4th.setText(srPred[3]); txtSprintR5th.setText(srPred[4]); txtSprintR6th.setText(srPred[5]);
                txtSprintR7th.setText(srPred[6]); txtSprintR8th.setText(srPred[7]);
                chckbxSprintR.setSelected(false);
            } else { chckbxSprintR.setSelected(true); }
        }
        
        setQualiEnabled(!chckbxQuali.isSelected());
        setRaceEnabled(!chckbxRace.isSelected());
        setSprintQEnabled(!chckbxSprintQ.isSelected() && selectedRace.hasSprint());
        setSprintREnabled(!chckbxSprintR.isSelected() && selectedRace.hasSprint());
    }

    private void updateSprintVisibility() {
        boolean hasSprint = selectedRace != null && selectedRace.hasSprint();
        chckbxSprintQ.setEnabled(hasSprint);
        chckbxSprintR.setEnabled(hasSprint);
        if(!hasSprint) {
            chckbxSprintQ.setSelected(false);
            chckbxSprintR.setSelected(false);
            setSprintQEnabled(false);
            setSprintREnabled(false);
        } else {
            setSprintQEnabled(!chckbxSprintQ.isSelected());
            setSprintREnabled(!chckbxSprintR.isSelected());
        }
    }
    
    // Geçerli pilot isimlerini SettingsDAO'dan çekip kontrol eder
    private boolean checkDrivers() {
        String[] validDrivers = settingsDAO.getValidDrivers();
        List<String> validList = Arrays.asList(validDrivers);
        
        if (!chckbxQuali.isSelected() && !areValid(validList, txtQ1st, txtQ2nd, txtQ3rd)) return false;
        if (!chckbxRace.isSelected() && !areValid(validList, txtR1st, txtR2nd, txtR3rd, txtR4th, txtR5th, txtR6th, txtR7th, txtR8th, txtR9th, txtR10th, txtFastestLap)) return false;
        
        if (selectedRace.hasSprint()) {
            if (!chckbxSprintQ.isSelected() && !areValid(validList, txtSprintQ1st, txtSprintQ2nd, txtSprintQ3rd)) return false;
            if (!chckbxSprintR.isSelected() && !areValid(validList, txtSprintR1st, txtSprintR2nd, txtSprintR3rd, txtSprintR4th, txtSprintR5th, txtSprintR6th, txtSprintR7th, txtSprintR8th)) return false;
        }
        return true;
    }
    
    private boolean areValid(List<String> validList, GHTextField... fields) {
        for(GHTextField field : fields) {
            String val = field.getText().trim();
            if(!val.equals("X") && !val.isEmpty() && !validList.contains(val)) return false;
        }
        return true;
    }

    public void insertDrivers() {
        String text; String[] drivers;
        if (!txtQualiFull.getText().isEmpty()) {
            drivers = txtQualiFull.getText().split("-");
            if(drivers.length > 3) { txtQ1st.setText(drivers[1].trim().substring(0, 3)); txtQ2nd.setText(drivers[2].trim().substring(0, 3)); txtQ3rd.setText(drivers[3].trim().substring(0, 3)); }
        }
        if (!txtRaceFull.getText().isEmpty()) {
            drivers = txtRaceFull.getText().split("-");
            if(drivers.length > 10) {
                txtR1st.setText(drivers[1].trim().substring(0, 3)); txtR2nd.setText(drivers[2].trim().substring(0, 3)); txtR3rd.setText(drivers[3].trim().substring(0, 3));
                txtR4th.setText(drivers[4].trim().substring(0, 3)); txtR5th.setText(drivers[5].trim().substring(0, 3)); txtR6th.setText(drivers[6].trim().substring(0, 3));
                txtR7th.setText(drivers[7].trim().substring(0, 3)); txtR8th.setText(drivers[8].trim().substring(0, 3)); txtR9th.setText(drivers[9].trim().substring(0, 3));
                txtR10th.setText(drivers[10].trim().substring(0, 3));
                if(drivers.length > 12) txtFastestLap.setText(drivers[12].trim().substring(1, 4));
            }
        }
        if (selectedRace != null && selectedRace.hasSprint()) {
            if (!txtSprintQFull.getText().isEmpty()) {
                drivers = txtSprintQFull.getText().split("-");
                if(drivers.length > 3) { txtSprintQ1st.setText(drivers[1].trim().substring(0, 3)); txtSprintQ2nd.setText(drivers[2].trim().substring(0, 3)); txtSprintQ3rd.setText(drivers[3].trim().substring(0, 3)); }
            }
            if (!txtSprintRFull.getText().isEmpty()) {
                drivers = txtSprintRFull.getText().split("-");
                if(drivers.length > 8) {
                    txtSprintR1st.setText(drivers[1].trim().substring(0, 3)); txtSprintR2nd.setText(drivers[2].trim().substring(0, 3)); txtSprintR3rd.setText(drivers[3].trim().substring(0, 3));
                    txtSprintR4th.setText(drivers[4].trim().substring(0, 3)); txtSprintR5th.setText(drivers[5].trim().substring(0, 3)); txtSprintR6th.setText(drivers[6].trim().substring(0, 3));
                    txtSprintR7th.setText(drivers[7].trim().substring(0, 3)); txtSprintR8th.setText(drivers[8].trim().substring(0, 3));
                }
            }
        }
    }

    private void resetAllFields() {
        txtQ1st.setText(""); txtQ2nd.setText(""); txtQ3rd.setText("");
        txtR1st.setText(""); txtR2nd.setText(""); txtR3rd.setText(""); txtR4th.setText(""); txtR5th.setText("");
        txtR6th.setText(""); txtR7th.setText(""); txtR8th.setText(""); txtR9th.setText(""); txtR10th.setText(""); txtFastestLap.setText("");
        txtSprintQ1st.setText(""); txtSprintQ2nd.setText(""); txtSprintQ3rd.setText("");
        txtSprintR1st.setText(""); txtSprintR2nd.setText(""); txtSprintR3rd.setText(""); txtSprintR4th.setText(""); 
        txtSprintR5th.setText(""); txtSprintR6th.setText(""); txtSprintR7th.setText(""); txtSprintR8th.setText("");
        chckbxQuali.setSelected(false); chckbxRace.setSelected(false);
        chckbxSprintQ.setSelected(false); chckbxSprintR.setSelected(false);
    }
    
    private void setQualiEnabled(boolean b) { txtQ1st.setEnabled(b); txtQ2nd.setEnabled(b); txtQ3rd.setEnabled(b); txtQualiFull.setEnabled(b); }
    private void setRaceEnabled(boolean b) { txtR1st.setEnabled(b); txtR2nd.setEnabled(b); txtR3rd.setEnabled(b); txtR4th.setEnabled(b); txtR5th.setEnabled(b); txtR6th.setEnabled(b); txtR7th.setEnabled(b); txtR8th.setEnabled(b); txtR9th.setEnabled(b); txtR10th.setEnabled(b); txtFastestLap.setEnabled(b); txtRaceFull.setEnabled(b); }
    private void setSprintQEnabled(boolean b) { txtSprintQ1st.setEnabled(b); txtSprintQ2nd.setEnabled(b); txtSprintQ3rd.setEnabled(b); txtSprintQFull.setEnabled(b); }
    private void setSprintREnabled(boolean b) { txtSprintR1st.setEnabled(b); txtSprintR2nd.setEnabled(b); txtSprintR3rd.setEnabled(b); txtSprintR4th.setEnabled(b); txtSprintR5th.setEnabled(b); txtSprintR6th.setEnabled(b); txtSprintR7th.setEnabled(b); txtSprintR8th.setEnabled(b); txtSprintRFull.setEnabled(b); }
}