package com.pandoratahmin.ui;

import com.pandoratahmin.database.RaceDAO;
import com.pandoratahmin.main.Main;
import com.pandoratahmin.model.Race;

import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Locale;

public class CalendarPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private RaceDAO raceDAO;
    private Race selectedRace;
    
    JList<Race> list = new JList<>();
    DefaultListModel<Race> listModel = new DefaultListModel<>();
    
    JLabel lblTopLabel = new JLabel("Yarışlar");
    JLabel lblRaceName = new JLabel("");
    JButton btnAddRace = new JButton("Ekle");
    JButton btnRemoveRace = new JButton("Kaldır");
    JButton btnReturnMainMenu = new JButton("Ana Menüye Dön");
    JButton btnEditRace = new JButton("Yarış Sonucunu Düzenle");
    
    JLabel lblQualiResult = new JLabel("1-    2-    3-   ");
    GHTextField txtQualiFirst = new GHTextField("1");
    GHTextField txtQualiSecond = new GHTextField("2");
    GHTextField txtQualiThird = new GHTextField("3");
    JLabel lblRaceResultLine1 = new JLabel("1-    2-    3-    4-    5-    ");
    JLabel lblRaceResultLine2 = new JLabel("6-    7-    8-    9-    10-  ");
    GHTextField txtRace1st = new GHTextField("1");
    GHTextField txtRace2nd = new GHTextField("2");
    GHTextField txtRace3rd = new GHTextField("3");
    GHTextField txtRace4th = new GHTextField("4");
    GHTextField txtRace5th = new GHTextField("5");
    GHTextField txtRace6th = new GHTextField("6");
    GHTextField txtRace7th = new GHTextField("7");
    GHTextField txtRace8th = new GHTextField("8");
    GHTextField txtRace9th = new GHTextField("9");
    GHTextField txtRace10th = new GHTextField("10");
    GHTextField txtFastestLap = new GHTextField("EHT");
    JTextField txtDnfs = new JTextField();
    JTextField txtSprintDnfs = new JTextField();
    
    JLabel lblSprintQTitle = new JLabel("Sprint Sıralama");
    JLabel lblSprintRTitle = new JLabel("Sprint Yarış");
    JLabel lblSprintQTitleEdit = new JLabel("Sprint Sıralama");
    JLabel lblSprintRTitleEdit = new JLabel("Sprint Yarış");
    JLabel lblSprintQResult = new JLabel("1-    2-    3-   ");
    JLabel lblSprintResultLine1 = new JLabel("1-    2-    3-    4-    ");
    JLabel lblSprintResultLine2 = new JLabel("5-    6-    7-    8-    ");
    GHTextField txtSprintQ1st = new GHTextField("1");
    GHTextField txtSprintQ2nd = new GHTextField("2");
    GHTextField txtSprintQ3rd = new GHTextField("3");
    GHTextField txtSprintR1st = new GHTextField("1");
    GHTextField txtSprintR2nd = new GHTextField("2");
    GHTextField txtSprintR3rd = new GHTextField("3");
    GHTextField txtSprintR4th = new GHTextField("4");
    GHTextField txtSprintR5th = new GHTextField("5");
    GHTextField txtSprintR6th = new GHTextField("6");
    GHTextField txtSprintR7th = new GHTextField("7");
    GHTextField txtSprintR8th = new GHTextField("8");
    JLabel lblSprintDNFs = new JLabel("DNF");
    
    // JTabbedPane YERİNE CARDLAYOUT KULLANIYORUZ
    JPanel cardPanel = new JPanel(new CardLayout());
    
    public CalendarPanel() {
        this.raceDAO = new RaceDAO();
        this.setPreferredSize(new Dimension(800, 640));
        setLayout(null);                
        
        list.setModel(listModel);
        list.setVisibleRowCount(23);
        list.setBounds(0, 30, 250, 560);
        list.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        add(list);
        
        JPanel panelTop = new JPanel();
        panelTop.setBounds(0,0,800,30);
        panelTop.setLayout(null);
        add(panelTop);
        
        lblTopLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblTopLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        lblTopLabel.setBounds(0, 0, 250, 30);
        panelTop.add(lblTopLabel);
        
        lblRaceName.setHorizontalAlignment(SwingConstants.CENTER);
        lblRaceName.setFont(new Font("Verdana", Font.BOLD, 20));
        lblRaceName.setBounds(250, 0, 550, 30);
        panelTop.add(lblRaceName);
                
        btnAddRace.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnAddRace.setBounds(0, 590, 110, 50);
        add(btnAddRace);
                
        btnRemoveRace.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnRemoveRace.setBounds(110, 590, 140, 50); 
        add(btnRemoveRace);
            
        btnReturnMainMenu.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnReturnMainMenu.setBounds(530, 590, 210, 50);        
        add(btnReturnMainMenu);
        
        btnEditRace.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnEditRace.setBounds(310, 590, 210, 50);
        add(btnEditRace);

        // KART PANELİ AYARLARI
        cardPanel.setBounds(250, 0, 550, 590);
        add(cardPanel);
        
        // --- 1. KART: BİLGİ GÖRÜNTÜLEME ---
        JPanel panelRaceInfo = new JPanel();
        panelRaceInfo.setLayout(null);
        cardPanel.add(panelRaceInfo, "INFO"); // Panele bir isim ("INFO") vererek ekliyoruz
        
        setupInfoPanelComponents(panelRaceInfo);
        
        // --- 2. KART: SONUÇ DÜZENLEME ---
        JPanel panelEditRace = new JPanel();
        panelEditRace.setLayout(null);
        cardPanel.add(panelEditRace, "EDIT"); // Panele bir isim ("EDIT") vererek ekliyoruz
        
        setupEditPanelComponents(panelEditRace);
        
        // Veritabanından listeyi doldur
        updateList();
        
        // --- EVENT LİSTENER'LAR ---

        list.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting() && list.getSelectedValue() != null) {
                selectedRace = list.getSelectedValue();
                updateInfoPanel();
                updateEditPanel();
            }
        });
        
        btnAddRace.addActionListener(e -> openAddRaceDialog());
        
        btnRemoveRace.addActionListener(e -> {
            if(selectedRace != null) {
                int confirm = JOptionPane.showConfirmDialog(this, selectedRace.getRaceName() + " silinecek, emin misiniz?", "Yarış Sil", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION) {
                    raceDAO.deleteRace(selectedRace.getId());
                    updateList();
                }
            }
        });
        
        btnReturnMainMenu.addActionListener(e -> {
            Main.mainMenuFrame.calendarFrame.dispose();
            Main.mainMenuFrame.setVisible(true);
        });
        
        btnEditRace.addActionListener(e -> {
            if(selectedRace != null) {
                updateEditPanel();
                CardLayout cl = (CardLayout)(cardPanel.getLayout());
                cl.show(cardPanel, "EDIT");
                toggleMainButtons(false);
            }
        });
    }

    // --- YARDIMCI METODLAR ---
    
    private void openAddRaceDialog() {
        JDialog dialog = new JDialog();
        dialog.setSize(400,220);
        dialog.setTitle("Pandora Tahmin - Yarış Ekle");
        dialog.getContentPane().setLayout(null);
        dialog.setResizable(false);
        
        JLabel lblName = new JLabel("Yarış Adı");
        lblName.setFont(new Font("Verdana", Font.BOLD, 16));
        lblName.setBounds(20, 30, 100, 30);
        dialog.getContentPane().add(lblName);
        
        JTextField txtName = new JTextField();
        txtName.setBounds(140, 30, 200, 30);
        txtName.setFont(new Font("Verdana", Font.PLAIN, 18));
        dialog.getContentPane().add(txtName);
        
        JLabel lblSprint = new JLabel("Sprint ?");
        lblSprint.setFont(new Font("Verdana", Font.BOLD, 16));
        lblSprint.setBounds(20, 90, 100, 30);
        dialog.getContentPane().add(lblSprint);
        
        JComboBox<String> cmbxSprint = new JComboBox<>(new String[]{"Var", "Yok"});
        cmbxSprint.setBounds(140, 90, 100, 30);
        dialog.getContentPane().add(cmbxSprint);
        
        JButton okButton = new JButton("Ekle");
        okButton.setBounds(250,145,60,30);
        dialog.getContentPane().add(okButton);
        
        JButton cancelButton = new JButton("İptal");
        cancelButton.setBounds(315,145,60,30);
        dialog.getContentPane().add(cancelButton);
                        
        okButton.addActionListener(e -> {
            String raceName = txtName.getText().trim();
            boolean hasSprint = cmbxSprint.getSelectedIndex() == 0;
            if (!raceName.isEmpty()) {
                raceDAO.addRace(raceName, hasSprint); 
                dialog.dispose();
                updateList();
            }
        });
                        
        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);                
    }

    public void updateList() {
        listModel.clear();
        List<Race> races = raceDAO.getAllRaces();
        for(Race r : races) {
            listModel.addElement(r);
        }
        if(!listModel.isEmpty()) {
            list.setSelectedIndex(0);
            selectedRace = list.getSelectedValue();
        } else {
            selectedRace = null;
        }
        updateInfoPanel();
    }
    
    private void toggleMainButtons(boolean enable) {
        btnEditRace.setEnabled(enable);
        btnReturnMainMenu.setEnabled(enable);
        btnAddRace.setEnabled(enable);
        btnRemoveRace.setEnabled(enable);
        list.setEnabled(enable);
    }
    
    // --- BİLGİ SEKMESİ (SEKME 1) İÇERİĞİ ---
    private void setupInfoPanelComponents(JPanel panel) {
        JLabel lblQTitle = new JLabel("Sıralama");
        lblQTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblQTitle.setFont(new Font("Verdana", Font.BOLD, 16));
        lblQTitle.setBounds(12, 33, 523, 20);
        panel.add(lblQTitle);
        
        lblQualiResult.setHorizontalAlignment(SwingConstants.CENTER);
        lblQualiResult.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblQualiResult.setBounds(12, 69, 538, 25);
        panel.add(lblQualiResult);

        JLabel lblRTitle = new JLabel("Yarış");
        lblRTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblRTitle.setFont(new Font("Verdana", Font.BOLD, 16));
        lblRTitle.setBounds(12, 125, 523, 20);
        panel.add(lblRTitle);

        lblRaceResultLine1.setHorizontalAlignment(SwingConstants.CENTER);
        lblRaceResultLine1.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblRaceResultLine1.setBounds(12, 161, 538, 25);
        panel.add(lblRaceResultLine1);    
        lblRaceResultLine2.setHorizontalAlignment(SwingConstants.CENTER);
        lblRaceResultLine2.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblRaceResultLine2.setBounds(12, 201, 538, 25);
        panel.add(lblRaceResultLine2);
        
        lblSprintQTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintQTitle.setFont(new Font("Verdana", Font.BOLD, 16));
        lblSprintQTitle.setBounds(12, 257, 523, 20);
        panel.add(lblSprintQTitle);
        
        lblSprintQResult.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintQResult.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblSprintQResult.setBounds(12, 293, 538, 25);
        panel.add(lblSprintQResult);
        
        lblSprintRTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintRTitle.setFont(new Font("Verdana", Font.BOLD, 16));
        lblSprintRTitle.setBounds(12, 349, 523, 20);
        panel.add(lblSprintRTitle);
        
        lblSprintResultLine1.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintResultLine1.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblSprintResultLine1.setBounds(12, 385, 538, 25);
        panel.add(lblSprintResultLine1);
        lblSprintResultLine2.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintResultLine2.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblSprintResultLine2.setBounds(12, 425, 538, 25);
        panel.add(lblSprintResultLine2);
    }

    // --- DÜZENLEME SEKMESİ (SEKME 2) İÇERİĞİ ---
    private void setupEditPanelComponents(JPanel panel) {
        JLabel lblQTitleE = new JLabel("Sıralama");
        lblQTitleE.setHorizontalAlignment(SwingConstants.CENTER);
        lblQTitleE.setFont(new Font("Verdana", Font.BOLD, 16));
        lblQTitleE.setBounds(12, 33, 523, 20);
        panel.add(lblQTitleE);
        
        txtQualiFirst.setGHLocation(150, 70);        
        panel.add(txtQualiFirst); panel.add(txtQualiFirst.getLabel());
        txtQualiSecond.setGHLocation(240, 70);
        panel.add(txtQualiSecond); panel.add(txtQualiSecond.getLabel());
        txtQualiThird.setGHLocation(330, 70);
        panel.add(txtQualiThird); panel.add(txtQualiThird.getLabel());
        
        JLabel lblRTitleE = new JLabel("Yarış");
        lblRTitleE.setHorizontalAlignment(SwingConstants.CENTER);
        lblRTitleE.setFont(new Font("Verdana", Font.BOLD, 16));
        lblRTitleE.setBounds(12, 115, 523, 20);
        panel.add(lblRTitleE);
        
        txtRace1st.setGHLocation(60, 150);        
        panel.add(txtRace1st); panel.add(txtRace1st.getLabel());
        txtRace2nd.setGHLocation(150, 150);
        panel.add(txtRace2nd); panel.add(txtRace2nd.getLabel());
        txtRace3rd.setGHLocation(240, 150);
        panel.add(txtRace3rd); panel.add(txtRace3rd.getLabel());
        txtRace4th.setGHLocation(330, 150);
        panel.add(txtRace4th); panel.add(txtRace4th.getLabel());
        txtRace5th.setGHLocation(420, 150);
        panel.add(txtRace5th); panel.add(txtRace5th.getLabel());
        txtRace6th.setGHLocation(60, 190);
        panel.add(txtRace6th); panel.add(txtRace6th.getLabel());
        txtRace7th.setGHLocation(150, 190);
        panel.add(txtRace7th); panel.add(txtRace7th.getLabel());
        txtRace8th.setGHLocation(240, 190);
        panel.add(txtRace8th); panel.add(txtRace8th.getLabel());
        txtRace9th.setGHLocation(330, 190);
        panel.add(txtRace9th); panel.add(txtRace9th.getLabel());
        txtRace10th.setGHLocation(420, 190);
        panel.add(txtRace10th); panel.add(txtRace10th.getLabel());
        
        txtDnfs.setBounds(90, 230, 260, 25);
        txtDnfs.setFont(GHTextField.textFieldFont);
        panel.add(txtDnfs);
        
        JLabel lblDNFs = new JLabel("DNF");
        lblDNFs.setBounds(42, 229, 50, 25);
        lblDNFs.setFont(GHTextField.numaraFont);
        panel.add(lblDNFs);
        
        txtFastestLap.setGHLocation(420, 230);
        panel.add(txtFastestLap);
        txtFastestLap.getLabel().setBounds(373, 229, 50, 25);
        panel.add(txtFastestLap.getLabel());
        
        lblSprintQTitleEdit.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintQTitleEdit.setFont(new Font("Verdana", Font.BOLD, 16));
        lblSprintQTitleEdit.setBounds(12, 275, 523, 20);
        panel.add(lblSprintQTitleEdit);
        
        txtSprintQ1st.setGHLocation(150, 310);        
        panel.add(txtSprintQ1st); panel.add(txtSprintQ1st.getLabel());
        txtSprintQ2nd.setGHLocation(240, 310);
        panel.add(txtSprintQ2nd); panel.add(txtSprintQ2nd.getLabel());
        txtSprintQ3rd.setGHLocation(330, 310);
        panel.add(txtSprintQ3rd); panel.add(txtSprintQ3rd.getLabel());
        
        lblSprintRTitleEdit.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintRTitleEdit.setFont(new Font("Verdana", Font.BOLD, 16));
        lblSprintRTitleEdit.setBounds(12, 355, 523, 20);
        panel.add(lblSprintRTitleEdit);
        
        txtSprintR1st.setGHLocation(100, 390);        
        panel.add(txtSprintR1st); panel.add(txtSprintR1st.getLabel());
        txtSprintR2nd.setGHLocation(190, 390);
        panel.add(txtSprintR2nd); panel.add(txtSprintR2nd.getLabel());
        txtSprintR3rd.setGHLocation(280, 390);
        panel.add(txtSprintR3rd); panel.add(txtSprintR3rd.getLabel());
        txtSprintR4th.setGHLocation(370, 390);
        panel.add(txtSprintR4th); panel.add(txtSprintR4th.getLabel());
        txtSprintR5th.setGHLocation(100, 430);
        panel.add(txtSprintR5th); panel.add(txtSprintR5th.getLabel());
        txtSprintR6th.setGHLocation(190, 430);
        panel.add(txtSprintR6th); panel.add(txtSprintR6th.getLabel());
        txtSprintR7th.setGHLocation(280, 430);
        panel.add(txtSprintR7th); panel.add(txtSprintR7th.getLabel());
        txtSprintR8th.setGHLocation(370, 430);
        panel.add(txtSprintR8th); panel.add(txtSprintR8th.getLabel());
        
        txtSprintDnfs.setBounds(130, 470, 295, 25);
        txtSprintDnfs.setFont(GHTextField.textFieldFont);
        panel.add(txtSprintDnfs);
        
        lblSprintDNFs.setBounds(82, 469, 50, 25);
        lblSprintDNFs.setFont(GHTextField.numaraFont);
        panel.add(lblSprintDNFs);

        KeyAdapter noSpaceAdapter = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { if (e.getKeyChar() == ' ') e.consume(); }
        };
        txtDnfs.addKeyListener(noSpaceAdapter);
        txtSprintDnfs.addKeyListener(noSpaceAdapter);
        
        JButton btnCancelEditing = new JButton("İptal");
        btnCancelEditing.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnCancelEditing.setBounds(270, 520, 100, 30);
        panel.add(btnCancelEditing);
        
        JButton btnSaveEdit = new JButton("Kaydet");
        btnSaveEdit.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnSaveEdit.setBounds(160, 520, 100, 30);
        panel.add(btnSaveEdit);
        
        JButton btnClearEdit = new JButton("Temizle");
        btnClearEdit.setFont(new Font("Century Gothic", Font.BOLD, 15));
        btnClearEdit.setBounds(450, 520, 90, 30);
        panel.add(btnClearEdit);
        
        btnCancelEditing.addActionListener(e -> {
            CardLayout cl = (CardLayout)(cardPanel.getLayout());
            cl.show(cardPanel, "INFO"); // İptal edilince INFO paneline dön
            toggleMainButtons(true);
        });
        
        btnClearEdit.addActionListener(e -> {    
            selectedRace.setDnfs(null);
            selectedRace.setFastestLap(null);
            selectedRace.setQualiResult(null);
            selectedRace.setRaceResult(null);
            selectedRace.setSprintDnfs(null);
            selectedRace.setSprintQualiResult(null);
            selectedRace.setSprintResult(null);
            selectedRace.setCompleted(false);
            
            raceDAO.updateRaceResults(selectedRace); 
            updateEditPanel();
            updateInfoPanel();
            
            CardLayout cl = (CardLayout)(cardPanel.getLayout());
            cl.show(cardPanel, "INFO");
            toggleMainButtons(true);
        });

        btnSaveEdit.addActionListener(e -> {
            boolean valid = !txtQualiFirst.getText().isEmpty() && !txtRace1st.getText().isEmpty() && !txtFastestLap.getText().isEmpty();
            
            if(valid) {
                String[] quali = {txtQualiFirst.getText(), txtQualiSecond.getText(), txtQualiThird.getText()};
                selectedRace.setQualiResult(quali);
                
                String[] race = {txtRace1st.getText(), txtRace2nd.getText(), txtRace3rd.getText(), txtRace4th.getText(), txtRace5th.getText(),
                        txtRace6th.getText(), txtRace7th.getText(), txtRace8th.getText(), txtRace9th.getText(), txtRace10th.getText()};
                selectedRace.setRaceResult(race);
                
                selectedRace.setFastestLap(txtFastestLap.getText());
                
                String[] dnfs = txtDnfs.getText().toUpperCase(Locale.ENGLISH).split(",");
                for (int i = 0; i < dnfs.length; i++) dnfs[i] = dnfs[i].trim();
                selectedRace.setDnfs(dnfs.length == 1 && dnfs[0].isEmpty() ? null : dnfs);
                
                if(selectedRace.hasSprint()) {
                    String[] sprintQ = {txtSprintQ1st.getText(), txtSprintQ2nd.getText(), txtSprintQ3rd.getText()};
                    selectedRace.setSprintQualiResult(sprintQ);
                    
                    String[] sprintR = {txtSprintR1st.getText(), txtSprintR2nd.getText(), txtSprintR3rd.getText(), txtSprintR4th.getText(), txtSprintR5th.getText(),
                            txtSprintR6th.getText(), txtSprintR7th.getText(), txtSprintR8th.getText()};
                    selectedRace.setSprintResult(sprintR);
                    
                    String[] sDnfs = txtSprintDnfs.getText().toUpperCase(Locale.ENGLISH).split(",");
                    for (int i = 0; i < sDnfs.length; i++) sDnfs[i] = sDnfs[i].trim();
                    selectedRace.setSprintDnfs(sDnfs.length == 1 && sDnfs[0].isEmpty() ? null : sDnfs);
                }
                
                selectedRace.setCompleted(true);
                raceDAO.updateRaceResults(selectedRace); 
                
                CardLayout cl = (CardLayout)(cardPanel.getLayout());
                cl.show(cardPanel, "INFO"); // Kaydedince INFO paneline dön
                toggleMainButtons(true);
                updateInfoPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen en azından Q1, R1 ve En Hızlı Tur (EHT) alanlarını doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    // --- PANEL GÜNCELLEME METODLARI ---
    
    public void updateInfoPanel() {    
        if(selectedRace != null) {
            lblRaceName.setText(selectedRace.getRaceName());
            if (selectedRace.getQualiResult() != null) {
                lblQualiResult.setText("1-" +selectedRace.getQualiResult()[0] + " 2-" +selectedRace.getQualiResult()[1] +" 3-" +selectedRace.getQualiResult()[2]);
            } else {
                lblQualiResult.setText("1-    2-    3- ");
            }
            if (selectedRace.getRaceResult() != null) {
                for(int i=0; i<2; i++) {
                    StringBuilder line = new StringBuilder();
                    String space = i < 2 ? "  " : " ";
                    for(int j = 0; j < 5; j++) {
                        line.append((i*5)+j+1).append("-").append(selectedRace.getRaceResult()[(i*5)+j]).append(space);
                    }
                    if(i==0) lblRaceResultLine1.setText(line.toString());
                    else lblRaceResultLine2.setText(line.toString());
                }
            } else {
                lblRaceResultLine1.setText("1-    2-    3-    4-    5-    ");  
                lblRaceResultLine2.setText("6-    7-    8-    9-    10-  ");
            }
            
            if (selectedRace.hasSprint()) {
                setSprintInfoVisible(true);
                if (selectedRace.getSprintQualiResult() != null) {
                    lblSprintQResult.setText("1-" +selectedRace.getSprintQualiResult()[0] + " 2-" +selectedRace.getSprintQualiResult()[1] +" 3-" +selectedRace.getSprintQualiResult()[2]);
                } else {
                    lblSprintQResult.setText("1-    2-    3- ");
                }
                
                if (selectedRace.getSprintResult() != null) {
                    for(int i=0; i<2; i++) {
                        StringBuilder line = new StringBuilder();
                        for(int j = 0; j < 4; j++) {
                            line.append((i*4)+j+1).append("-").append(selectedRace.getSprintResult()[(i*4)+j]).append("  ");
                        }
                        if(i==0) lblSprintResultLine1.setText(line.toString());
                        else lblSprintResultLine2.setText(line.toString());
                    }
                } else {
                    lblSprintResultLine1.setText("1-    2-    3-    4-    ");  
                    lblSprintResultLine2.setText("5-    6-    7-    8-    ");
                }
            } else {
                setSprintInfoVisible(false);
            }
        } else {
            lblRaceName.setText("");
            setSprintInfoVisible(false);
        }
    }
    
    private void setSprintInfoVisible(boolean v) {
        lblSprintQTitle.setVisible(v);
        lblSprintRTitle.setVisible(v);
        lblSprintQResult.setVisible(v);
        lblSprintResultLine1.setVisible(v);
        lblSprintResultLine2.setVisible(v);
    }

    public void updateEditPanel() {
        if(selectedRace == null) return;
        
        String[] qualiResult = selectedRace.getQualiResult();
        String[] raceResult = selectedRace.getRaceResult();
        
        if (qualiResult != null) {
            txtQualiFirst.setText(qualiResult[0]); txtQualiSecond.setText(qualiResult[1]); txtQualiThird.setText(qualiResult[2]);
        } else {
            txtQualiFirst.setText(""); txtQualiSecond.setText(""); txtQualiThird.setText("");
        }        
        if (raceResult != null) {
            txtRace1st.setText(raceResult[0]); txtRace2nd.setText(raceResult[1]); txtRace3rd.setText(raceResult[2]);
            txtRace4th.setText(raceResult[3]); txtRace5th.setText(raceResult[4]); txtRace6th.setText(raceResult[5]);
            txtRace7th.setText(raceResult[6]); txtRace8th.setText(raceResult[7]); txtRace9th.setText(raceResult[8]);
            txtRace10th.setText(raceResult[9]);
        } else {
            txtRace1st.setText(""); txtRace2nd.setText(""); txtRace3rd.setText(""); txtRace4th.setText(""); txtRace5th.setText("");
            txtRace6th.setText(""); txtRace7th.setText(""); txtRace8th.setText(""); txtRace9th.setText(""); txtRace10th.setText("");
        }
        
        String[] dnfs = selectedRace.getDnfs();
        txtDnfs.setText(dnfs != null ? String.join(",", dnfs) : "");
        txtFastestLap.setText(selectedRace.getFastestLap() != null ? selectedRace.getFastestLap() : "");
        
        if(selectedRace.hasSprint()) {
            setSprintEditVisible(true);
            String[] sprintResult = selectedRace.getSprintResult();
            String[] sprintQResult = selectedRace.getSprintQualiResult();
            
            if(sprintQResult != null) {
                txtSprintQ1st.setText(sprintQResult[0]); txtSprintQ2nd.setText(sprintQResult[1]); txtSprintQ3rd.setText(sprintQResult[2]);
            } else {
                txtSprintQ1st.setText(""); txtSprintQ2nd.setText(""); txtSprintQ3rd.setText("");
            }
            if(sprintResult != null) {
                txtSprintR1st.setText(sprintResult[0]); txtSprintR2nd.setText(sprintResult[1]); txtSprintR3rd.setText(sprintResult[2]);
                txtSprintR4th.setText(sprintResult[3]); txtSprintR5th.setText(sprintResult[4]); txtSprintR6th.setText(sprintResult[5]);
                txtSprintR7th.setText(sprintResult[6]); txtSprintR8th.setText(sprintResult[7]);
            } else {
                txtSprintR1st.setText(""); txtSprintR2nd.setText(""); txtSprintR3rd.setText(""); txtSprintR4th.setText("");
                txtSprintR5th.setText(""); txtSprintR6th.setText(""); txtSprintR7th.setText(""); txtSprintR8th.setText("");
            }
            
            String[] sprintDNFs = selectedRace.getSprintDnfs();
            txtSprintDnfs.setText(sprintDNFs != null ? String.join(",", sprintDNFs) : "");
        } else {
            setSprintEditVisible(false);
        }
    }
    
    private void setSprintEditVisible(boolean b) {
        lblSprintQTitleEdit.setVisible(b); lblSprintRTitleEdit.setVisible(b);
        txtSprintQ1st.setVisible(b); txtSprintQ2nd.setVisible(b); txtSprintQ3rd.setVisible(b);
        txtSprintR1st.setVisible(b); txtSprintR2nd.setVisible(b); txtSprintR3rd.setVisible(b);
        txtSprintR4th.setVisible(b); txtSprintR5th.setVisible(b); txtSprintR6th.setVisible(b);
        txtSprintR7th.setVisible(b); txtSprintR8th.setVisible(b);
        txtSprintDnfs.setVisible(b); lblSprintDNFs.setVisible(b);
    }
}