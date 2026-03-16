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

    private UserDAO userDAO;
    private RaceDAO raceDAO;
    private PredictionDAO predictionDAO;
    private SettingsDAO settingsDAO;
    private ScoreCalculationService scoreService;

    private Race selectedRace;
    private User selectedUser;
    private Prediction currentPrediction;

    private JPanel panelRace;
    private JPanel panelSprint;

    JButton btnReturnMainMenu = new JButton("Ana Menüye Dön");
    JButton btnCalculatePoints = new JButton("Hesapla");
    JComboBox<Race> cmbxRace = new JComboBox<>();
    JComboBox<User> cmbxUser = new JComboBox<>();
    JLabel lblCalcStatus = new JLabel("");

    GHTextField[] txtQ = new GHTextField[3];
    GHTextField[] txtSprintQ = new GHTextField[3];
    GHTextField[] txtRace = new GHTextField[10];
    GHTextField[] txtSprintR = new GHTextField[8];

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

        this.setPreferredSize(new Dimension(900, 500));
        this.setLayout(null);

        initTextFieldArrays();
        setupUI();
        loadComboData();
        setupListeners();
    }

    private void initTextFieldArrays() {
        for (int i = 0; i < 3; i++) {
            txtQ[i] = new GHTextField(String.valueOf(i + 1));
            txtSprintQ[i] = new GHTextField(String.valueOf(i + 1));
        }
        for (int i = 0; i < 10; i++) {
            txtRace[i] = new GHTextField(String.valueOf(i + 1));
        }
        for (int i = 0; i < 8; i++) {
            txtSprintR[i] = new GHTextField(String.valueOf(i + 1));
        }
    }

    private void setupUI() {
        btnReturnMainMenu.setFont(FontManager.getFont(Font.BOLD, 13));
        btnReturnMainMenu.setBounds(730, 450, 160, 40);
        btnReturnMainMenu.setFocusable(false);
        add(btnReturnMainMenu);

        btnInsert.setFont(FontManager.getFont(Font.BOLD, 13));
        btnInsert.setBounds(600, 455, 100, 30);
        btnInsert.setFocusable(false);
        add(btnInsert);

        btnCalculatePoints.setFont(FontManager.getFont(Font.BOLD, 14));
        btnCalculatePoints.setBounds(375, 450, 150, 40);
        btnCalculatePoints.setFocusable(false);
        add(btnCalculatePoints);

        JPanel panelTop = new JPanel();
        panelTop.setBounds(0, 0, 900, 62);
        panelTop.setLayout(null);
        add(panelTop);

        JPanel panelLine = new JPanel();
        panelLine.setBounds(0, 62, 900, 3);
        panelLine.setBackground(Color.BLACK);
        panelLine.setLayout(null);
        add(panelLine);

        panelRace = new JPanel();
        panelRace.setBounds(0, 65, 500, 433);
        panelRace.setLayout(null);
        add(panelRace);

        panelSprint = new JPanel();
        panelSprint.setBounds(500, 65, 400, 433);
        panelSprint.setLayout(null);
        add(panelSprint);

        lblCalcStatus.setFont(FontManager.getFont(Font.PLAIN, 14));
        lblCalcStatus.setBounds(0, 447, 375, 40);
        lblCalcStatus.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblCalcStatus);
        setComponentZOrder(lblCalcStatus, 0);

        JLabel lblUser = new JLabel("Kişi :");
        lblUser.setFont(FontManager.getFont(Font.BOLD, 14));
        lblUser.setBounds(60, 20, 80, 20);
        panelTop.add(lblUser);

        cmbxUser.setFont(FontManager.getFont(Font.PLAIN, 14));
        cmbxUser.setBounds(140, 18, 180, 25);
        cmbxUser.setFocusable(false);
        panelTop.add(cmbxUser);

        JLabel lblRace = new JLabel("Yarış :");
        lblRace.setFont(FontManager.getFont(Font.BOLD, 14));
        lblRace.setBounds(570, 20, 80, 20);
        panelTop.add(lblRace);

        cmbxRace.setFont(FontManager.getFont(Font.PLAIN, 14));
        cmbxRace.setBounds(650, 18, 180, 25);
        cmbxRace.setFocusable(false);
        panelTop.add(cmbxRace);

        // --- Sıralama Bölümü ---
        JLabel lblQuali = new JLabel("Sıralama");
        lblQuali.setHorizontalAlignment(SwingConstants.CENTER);
        lblQuali.setFont(FontManager.getFont(Font.BOLD, 18));
        lblQuali.setBounds(0, 0, 475, 40);
        panelRace.add(lblQuali);

        for (int i = 0; i < 3; i++) {
            txtQ[i].setGHLocation(125 + (i * 90), 50);
            panelRace.add(txtQ[i]);
            panelRace.add(txtQ[i].getLabel());
        }

        chckbxQuali.setFont(FontManager.getFont(Font.PLAIN, 13));
        chckbxQuali.setBounds(110, 87, 160, 20);
        chckbxQuali.setFocusable(false);
        panelRace.add(chckbxQuali);

        txtQualiFull.setBounds(272, 85, 90, 25);
        txtQualiFull.setFont(GHTextField.textFieldFont);
        panelRace.add(txtQualiFull);

        // --- Sprint Sıralama Bölümü ---
        JLabel lblSprintQ = new JLabel("Sprint Sıralama");
        lblSprintQ.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintQ.setFont(FontManager.getFont(Font.BOLD, 18));
        lblSprintQ.setBounds(0, 0, 400, 40);
        panelSprint.add(lblSprintQ);

        for (int i = 0; i < 3; i++) {
            txtSprintQ[i].setGHLocation(85 + (i * 90), 50);
            panelSprint.add(txtSprintQ[i]);
            panelSprint.add(txtSprintQ[i].getLabel());
        }

        chckbxSprintQ.setFont(FontManager.getFont(Font.PLAIN, 13));
        chckbxSprintQ.setBounds(70, 87, 160, 20);
        chckbxSprintQ.setFocusable(false);
        panelSprint.add(chckbxSprintQ);

        txtSprintQFull.setBounds(232, 85, 90, 25);
        txtSprintQFull.setFont(GHTextField.textFieldFont);
        panelSprint.add(txtSprintQFull);

        // --- Yarış Bölümü ---
        JLabel lblRaceT = new JLabel("Yarış");
        lblRaceT.setHorizontalAlignment(SwingConstants.CENTER);
        lblRaceT.setFont(FontManager.getFont(Font.BOLD, 18));
        lblRaceT.setBounds(0, 120, 475, 40);
        panelRace.add(lblRaceT);

        for (int i = 0; i < 10; i++) {
            int x = 35 + ((i % 5) * 90);
            int y = 170 + ((i / 5) * 40);
            txtRace[i].setGHLocation(x, y);
            panelRace.add(txtRace[i]);
            panelRace.add(txtRace[i].getLabel());
        }

        chckbxRace.setFont(FontManager.getFont(Font.PLAIN, 13));
        chckbxRace.setBounds(170, 252, 160, 20);
        chckbxRace.setFocusable(false);
        panelRace.add(chckbxRace);

        txtRaceFull.setBounds(125, 285, 250, 25);
        txtRaceFull.setFont(GHTextField.textFieldFont);
        panelRace.add(txtRaceFull);

        // --- Sprint Yarış Bölümü ---
        JLabel lblSprintR = new JLabel("Sprint Yarış");
        lblSprintR.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintR.setFont(FontManager.getFont(Font.BOLD, 18));
        lblSprintR.setBounds(0, 120, 400, 40);
        panelSprint.add(lblSprintR);

        for (int i = 0; i < 8; i++) {
            int x = 35 + ((i % 4) * 90);
            int y = 170 + ((i / 4) * 40);
            txtSprintR[i].setGHLocation(x, y);
            panelSprint.add(txtSprintR[i]);
            panelSprint.add(txtSprintR[i].getLabel());
        }

        chckbxSprintR.setFont(FontManager.getFont(Font.PLAIN, 13));
        chckbxSprintR.setBounds(130, 252, 160, 20);
        chckbxSprintR.setFocusable(false);
        panelSprint.add(chckbxSprintR);

        txtSprintRFull.setBounds(125, 285, 180, 25);
        txtSprintRFull.setFont(GHTextField.textFieldFont);
        panelSprint.add(txtSprintRFull);

        // --- Genel Katılım ---
        chckbxDNA.setHorizontalAlignment(SwingConstants.CENTER);
        chckbxDNA.setFont(FontManager.getFont(Font.PLAIN, 13));
        chckbxDNA.setBounds(325, 330, 150, 20);
        chckbxDNA.setFocusable(false);
        panelRace.add(chckbxDNA);
    }

    private void loadComboData() {
        List<User> users = userDAO.getAllUsers();
        for (User u : users)
            cmbxUser.addItem(u);

        List<Race> races = raceDAO.getAllRaces();
        for (Race r : races)
            cmbxRace.addItem(r);
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
            chckbxQuali.setSelected(dna);
            chckbxQuali.setEnabled(!dna);
            setQualiEnabled(!dna);
            chckbxRace.setSelected(dna);
            chckbxRace.setEnabled(!dna);
            setRaceEnabled(!dna);
            if (selectedRace != null && selectedRace.hasSprint()) {
                chckbxSprintQ.setSelected(dna);
                chckbxSprintQ.setEnabled(!dna);
                setSprintQEnabled(!dna);
                chckbxSprintR.setSelected(dna);
                chckbxSprintR.setEnabled(!dna);
                setSprintREnabled(!dna);
            }
        });

        btnReturnMainMenu.addActionListener(e -> {
            MainMenuFrame.calculatePointsFrame.dispose();
            Main.mainMenuFrame.setVisible(true);
        });

        btnInsert.addActionListener(e -> {
            insertDrivers();
            txtQualiFull.setText("");
            txtRaceFull.setText("");
            txtSprintQFull.setText("");
            txtSprintRFull.setText("");
        });

        btnCalculatePoints.addActionListener(e -> {
            if (selectedUser == null || selectedRace == null)
                return;

            if (!chckbxDNA.isSelected() && !checkDrivers()) {
                lblCalcStatus.setText("HATA: Geçersiz pilot kısaltması!");
                lblCalcStatus.setForeground(Color.RED);
                return;
            }

            Prediction pred = new Prediction(selectedUser.getId(), selectedRace.getId());
            pred.setDidNotAttend(chckbxDNA.isSelected());

            if (!chckbxQuali.isSelected()) {
                String[] qArr = new String[3];
                for (int i = 0; i < 3; i++)
                    qArr[i] = txtQ[i].getText();
                pred.setQualiPred(qArr);
            }
            if (!chckbxRace.isSelected()) {
                String[] rArr = new String[10];
                for (int i = 0; i < 10; i++)
                    rArr[i] = txtRace[i].getText();
                pred.setRacePred(rArr);
            }
            if (selectedRace.hasSprint()) {
                if (!chckbxSprintQ.isSelected()) {
                    String[] sqArr = new String[3];
                    for (int i = 0; i < 3; i++)
                        sqArr[i] = txtSprintQ[i].getText();
                    pred.setSprintQPred(sqArr);
                }
                if (!chckbxSprintR.isSelected()) {
                    String[] srArr = new String[8];
                    for (int i = 0; i < 8; i++)
                        srArr[i] = txtSprintR[i].getText();
                    pred.setSprintRPred(srArr);
                }
            }

            scoreService.calculatePoints(pred, selectedRace);
            predictionDAO.savePrediction(pred);

            lblCalcStatus.setText("Tahmin Kaydedildi!");
            lblCalcStatus.setForeground(new Color(0, 150, 0));
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

            if (!selectedRace.isCompleted()) {
                lblCalcStatus.setText("Yarış sonucu henüz girilmemiş.");
                lblCalcStatus.setForeground(Color.ORANGE);
            }

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
        if (currentPrediction.isDidNotAttend()) {
            chckbxQuali.setSelected(true);
            chckbxRace.setSelected(true);
            chckbxSprintQ.setSelected(true);
            chckbxSprintR.setSelected(true);
            return;
        }

        String[] qPred = currentPrediction.getQualiPred();
        if (qPred != null && qPred.length >= 3) {
            for (int i = 0; i < 3; i++)
                txtQ[i].setText(qPred[i]);
            chckbxQuali.setSelected(false);
        } else {
            chckbxQuali.setSelected(true);
        }

        String[] rPred = currentPrediction.getRacePred();
        if (rPred != null && rPred.length >= 10) {
            for (int i = 0; i < 10; i++)
                txtRace[i].setText(rPred[i]);
            chckbxRace.setSelected(false);
        } else {
            chckbxRace.setSelected(true);
        }

        if (selectedRace.hasSprint()) {
            String[] sqPred = currentPrediction.getSprintQPred();
            if (sqPred != null && sqPred.length >= 3) {
                for (int i = 0; i < 3; i++)
                    txtSprintQ[i].setText(sqPred[i]);
                chckbxSprintQ.setSelected(false);
            } else {
                chckbxSprintQ.setSelected(true);
            }

            String[] srPred = currentPrediction.getSprintRPred();
            if (srPred != null && srPred.length >= 8) {
                for (int i = 0; i < 8; i++)
                    txtSprintR[i].setText(srPred[i]);
                chckbxSprintR.setSelected(false);
            } else {
                chckbxSprintR.setSelected(true);
            }
        }

        setQualiEnabled(!chckbxQuali.isSelected());
        setRaceEnabled(!chckbxRace.isSelected());
        setSprintQEnabled(!chckbxSprintQ.isSelected() && selectedRace.hasSprint());
        setSprintREnabled(!chckbxSprintR.isSelected() && selectedRace.hasSprint());
    }

    private void updateSprintVisibility() {
        boolean hasSprint = selectedRace != null && selectedRace.hasSprint();

        panelSprint.setVisible(hasSprint);
        if (hasSprint) {
            panelRace.setLocation(0, 65);
        } else {
            panelRace.setLocation(200, 65);
        }

        if (!hasSprint) {
            chckbxSprintQ.setSelected(false);
            chckbxSprintR.setSelected(false);
            setSprintQEnabled(false);
            setSprintREnabled(false);
        } else {
            setSprintQEnabled(!chckbxSprintQ.isSelected());
            setSprintREnabled(!chckbxSprintR.isSelected());
        }
    }

    private boolean checkDrivers() {
        String[] validDrivers = settingsDAO.getValidDrivers();
        List<String> validList = Arrays.asList(validDrivers);

        if (!chckbxQuali.isSelected() && !areValid(validList, txtQ))
            return false;

        if (!chckbxRace.isSelected() && !areValid(validList, txtRace))
            return false;

        if (selectedRace.hasSprint()) {
            if (!chckbxSprintQ.isSelected() && !areValid(validList, txtSprintQ))
                return false;
            if (!chckbxSprintR.isSelected() && !areValid(validList, txtSprintR))
                return false;
        }
        return true;
    }

    private boolean areValid(List<String> validList, GHTextField[] fields) {
        for (GHTextField field : fields) {
            String val = field.getText().trim();
            if (!val.equals("X") && !val.isEmpty() && !validList.contains(val))
                return false;
        }
        return true;
    }

    public void insertDrivers() {
        String[] drivers;
        if (!txtQualiFull.getText().isEmpty()) {
            drivers = txtQualiFull.getText().split("-");
            if (drivers.length > 3) {
                for (int i = 0; i < 3; i++)
                    txtQ[i].setText(drivers[i + 1].trim().substring(0, 3));
            }
        }
        if (!txtRaceFull.getText().isEmpty()) {
            drivers = txtRaceFull.getText().split("-");
            if (drivers.length > 1) {
                for (int i = 0; i < 10; i++)
                    txtRace[i].setText(drivers[i + 1].trim().substring(0, 3));
            }
        }
        if (selectedRace != null && selectedRace.hasSprint()) {
            if (!txtSprintQFull.getText().isEmpty()) {
                drivers = txtSprintQFull.getText().split("-");
                if (drivers.length > 3) {
                    for (int i = 0; i < 3; i++)
                        txtSprintQ[i].setText(drivers[i + 1].trim().substring(0, 3));
                }
            }
            if (!txtSprintRFull.getText().isEmpty()) {
                drivers = txtSprintRFull.getText().split("-");
                if (drivers.length > 8) {
                    for (int i = 0; i < 8; i++)
                        txtSprintR[i].setText(drivers[i + 1].trim().substring(0, 3));
                }
            }
        }
    }

    private void resetAllFields() {
        for (int i = 0; i < 3; i++) {
            txtQ[i].setText("");
            txtSprintQ[i].setText("");
        }
        for (int i = 0; i < 10; i++) {
            txtRace[i].setText("");
        }
        for (int i = 0; i < 8; i++) {
            txtSprintR[i].setText("");
        }
        chckbxQuali.setSelected(false);
        chckbxRace.setSelected(false);
        chckbxSprintQ.setSelected(false);
        chckbxSprintR.setSelected(false);
    }

    private void setQualiEnabled(boolean b) {
        for (int i = 0; i < 3; i++)
            txtQ[i].setEnabled(b);
        txtQualiFull.setEnabled(b);
    }

    private void setRaceEnabled(boolean b) {
        for (int i = 0; i < 10; i++)
            txtRace[i].setEnabled(b);
        txtRaceFull.setEnabled(b);
    }

    private void setSprintQEnabled(boolean b) {
        for (int i = 0; i < 3; i++)
            txtSprintQ[i].setEnabled(b);
        txtSprintQFull.setEnabled(b);
    }

    private void setSprintREnabled(boolean b) {
        for (int i = 0; i < 8; i++)
            txtSprintR[i].setEnabled(b);
        txtSprintRFull.setEnabled(b);
    }
}