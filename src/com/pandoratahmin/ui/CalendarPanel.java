package com.pandoratahmin.ui; // refresh

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
    JButton btnEditRace = new JButton("Yarış Sonucu Düzenle");

    JLabel lblQualiResult = new JLabel("1-    2-    3-   ");
    GHTextField[] txtQ = new GHTextField[3];
    JLabel lblRaceResultLine1 = new JLabel("1-    2-    3-    4-    5-    ");
    JLabel lblRaceResultLine2 = new JLabel("6-    7-    8-    9-    10-  ");
    GHTextField[] txtRace = new GHTextField[10];
    JTextField txtDnfs = new JTextField();
    JTextField txtSprintDnfs = new JTextField();

    JLabel lblSprintQTitle = new JLabel("Sprint Sıralama");
    JLabel lblSprintRTitle = new JLabel("Sprint Yarış");
    JLabel lblSprintQTitleEdit = new JLabel("Sprint Sıralama");
    JLabel lblSprintRTitleEdit = new JLabel("Sprint Yarış");
    JLabel lblSprintQResult = new JLabel("1-    2-    3-   ");
    JLabel lblSprintResultLine1 = new JLabel("1-    2-    3-    4-    ");
    JLabel lblSprintResultLine2 = new JLabel("5-    6-    7-    8-    ");
    GHTextField[] txtSprintQ = new GHTextField[3];
    GHTextField[] txtSprintR = new GHTextField[8];
    JLabel lblSprintDNFs = new JLabel("DNF");
    JPanel cardPanel = new JPanel(new CardLayout());

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

    public CalendarPanel() {
        this.raceDAO = new RaceDAO();
        initTextFieldArrays();
        this.setPreferredSize(new Dimension(800, 640));
        setLayout(null);

        list.setModel(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBounds(0, 30, 250, 560);
        list.setFont(FontManager.getFont(Font.PLAIN, 16));
        add(list);

        JPanel panelTop = new JPanel();
        panelTop.setBounds(0, 0, 800, 30);
        panelTop.setLayout(null);
        add(panelTop);

        lblTopLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblTopLabel.setFont(FontManager.getFont(Font.BOLD, 18));
        lblTopLabel.setBounds(0, 0, 250, 30);
        panelTop.add(lblTopLabel);

        lblRaceName.setHorizontalAlignment(SwingConstants.CENTER);
        lblRaceName.setFont(FontManager.getFont(Font.BOLD, 20));
        lblRaceName.setBounds(250, 0, 550, 30);
        panelTop.add(lblRaceName);

        btnAddRace.setFont(FontManager.getFont(Font.BOLD, 15));
        btnAddRace.setBounds(0, 590, 110, 50);
        add(btnAddRace);

        btnRemoveRace.setFont(FontManager.getFont(Font.BOLD, 15));
        btnRemoveRace.setBounds(110, 590, 140, 50);
        add(btnRemoveRace);

        btnReturnMainMenu.setFont(FontManager.getFont(Font.BOLD, 15));
        btnReturnMainMenu.setBounds(530, 590, 220, 50);
        add(btnReturnMainMenu);

        btnEditRace.setFont(FontManager.getFont(Font.BOLD, 14));
        btnEditRace.setBounds(300, 590, 220, 50);
        add(btnEditRace);

        cardPanel.setBounds(250, 0, 550, 590);
        add(cardPanel);

        JPanel panelRaceInfo = new JPanel();
        panelRaceInfo.setLayout(null);
        cardPanel.add(panelRaceInfo, "INFO");

        setupInfoPanelComponents(panelRaceInfo);

        JPanel panelEditRace = new JPanel();
        panelEditRace.setLayout(null);
        cardPanel.add(panelEditRace, "EDIT");

        setupEditPanelComponents(panelEditRace);

        updateList();

        // --- EVENT LİSTENER'LAR ---

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && list.getSelectedValue() != null) {
                selectedRace = list.getSelectedValue();
                updateInfoPanel();
                updateEditPanel();
            }
        });

        btnAddRace.addActionListener(e -> openAddRaceDialog());

        btnRemoveRace.addActionListener(e -> {
            if (selectedRace != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        selectedRace.getRaceName() + " silinecek, emin misiniz?", "Yarış Sil",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    raceDAO.deleteRace(selectedRace.getId());
                    updateList();
                }
            }
        });

        btnReturnMainMenu.addActionListener(e -> {
            MainMenuFrame.calendarFrame.dispose();
            Main.mainMenuFrame.setVisible(true);
        });

        btnEditRace.addActionListener(e -> {
            if (selectedRace != null) {
                updateEditPanel();
                CardLayout cl = (CardLayout) (cardPanel.getLayout());
                cl.show(cardPanel, "EDIT");
                toggleMainButtons(false);
            }
        });
    }

    // --- YARDIMCI METODLAR ---

    private void openAddRaceDialog() {
        JDialog dialog = new JDialog();
        dialog.setSize(400, 220);
        dialog.setTitle("Pandora Tahmin - Yarış Ekle");
        dialog.getContentPane().setLayout(null);
        dialog.setResizable(false);

        JLabel lblName = new JLabel("Yarış Adı");
        lblName.setFont(FontManager.getFont(Font.BOLD, 16));
        lblName.setBounds(20, 30, 100, 30);
        dialog.getContentPane().add(lblName);

        JTextField txtName = new JTextField();
        txtName.setBounds(140, 30, 200, 30);
        txtName.setFont(FontManager.getFont(Font.PLAIN, 18));
        dialog.getContentPane().add(txtName);

        JLabel lblSprint = new JLabel("Sprint ?");
        lblSprint.setFont(FontManager.getFont(Font.BOLD, 16));
        lblSprint.setBounds(20, 90, 100, 30);
        dialog.getContentPane().add(lblSprint);

        JComboBox<String> cmbxSprint = new JComboBox<>(new String[] { "Var", "Yok" });
        cmbxSprint.setBounds(140, 90, 100, 30);
        dialog.getContentPane().add(cmbxSprint);

        JButton okButton = new JButton("Ekle");
        okButton.setBounds(250, 145, 60, 30);
        dialog.getContentPane().add(okButton);

        JButton cancelButton = new JButton("İptal");
        cancelButton.setBounds(315, 145, 60, 30);
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
        for (Race r : races) {
            listModel.addElement(r);
        }
        if (!listModel.isEmpty()) {
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
        lblQTitle.setFont(FontManager.getFont(Font.BOLD, 16));
        lblQTitle.setBounds(12, 40, 523, 20);
        panel.add(lblQTitle);

        lblQualiResult.setHorizontalAlignment(SwingConstants.CENTER);
        lblQualiResult.setFont(FontManager.getFont(Font.BOLD, 18));
        lblQualiResult.setBounds(12, 75, 538, 25);
        panel.add(lblQualiResult);

        JLabel lblRTitle = new JLabel("Yarış");
        lblRTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblRTitle.setFont(FontManager.getFont(Font.BOLD, 16));
        lblRTitle.setBounds(12, 125, 523, 20);
        panel.add(lblRTitle);

        lblRaceResultLine1.setHorizontalAlignment(SwingConstants.CENTER);
        lblRaceResultLine1.setFont(FontManager.getFont(Font.BOLD, 18));
        lblRaceResultLine1.setBounds(12, 161, 538, 25);
        panel.add(lblRaceResultLine1);
        lblRaceResultLine2.setHorizontalAlignment(SwingConstants.CENTER);
        lblRaceResultLine2.setFont(FontManager.getFont(Font.BOLD, 18));
        lblRaceResultLine2.setBounds(12, 201, 538, 25);
        panel.add(lblRaceResultLine2);

        lblSprintQTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintQTitle.setFont(FontManager.getFont(Font.BOLD, 16));
        lblSprintQTitle.setBounds(12, 257, 523, 20);
        panel.add(lblSprintQTitle);

        lblSprintQResult.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintQResult.setFont(FontManager.getFont(Font.BOLD, 18));
        lblSprintQResult.setBounds(12, 293, 538, 25);
        panel.add(lblSprintQResult);

        lblSprintRTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintRTitle.setFont(FontManager.getFont(Font.BOLD, 16));
        lblSprintRTitle.setBounds(12, 349, 523, 20);
        panel.add(lblSprintRTitle);

        lblSprintResultLine1.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintResultLine1.setFont(FontManager.getFont(Font.BOLD, 18));
        lblSprintResultLine1.setBounds(12, 385, 538, 25);
        panel.add(lblSprintResultLine1);
        lblSprintResultLine2.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintResultLine2.setFont(FontManager.getFont(Font.BOLD, 18));
        lblSprintResultLine2.setBounds(12, 425, 538, 25);
        panel.add(lblSprintResultLine2);
    }

    // --- DÜZENLEME SEKMESİ İÇERİĞİ ---
    private void setupEditPanelComponents(JPanel panel) {
        JLabel lblQTitleE = new JLabel("Sıralama");
        lblQTitleE.setHorizontalAlignment(SwingConstants.CENTER);
        lblQTitleE.setFont(FontManager.getFont(Font.BOLD, 16));
        lblQTitleE.setBounds(12, 33, 523, 20);
        panel.add(lblQTitleE);

        for (int i = 0; i < 3; i++) {
            txtQ[i].setGHLocation(150 + (i * 90), 70);
            panel.add(txtQ[i]);
            panel.add(txtQ[i].getLabel());
        }

        JLabel lblRTitleE = new JLabel("Yarış");
        lblRTitleE.setHorizontalAlignment(SwingConstants.CENTER);
        lblRTitleE.setFont(FontManager.getFont(Font.BOLD, 16));
        lblRTitleE.setBounds(12, 115, 523, 20);
        panel.add(lblRTitleE);

        for (int i = 0; i < 10; i++) {
            int x = 60 + ((i % 5) * 90);
            int y = 150 + ((i / 5) * 40);
            txtRace[i].setGHLocation(x, y);
            panel.add(txtRace[i]);
            panel.add(txtRace[i].getLabel());
        }

        txtDnfs.setBounds(120, 230, 300, 25);
        txtDnfs.setFont(GHTextField.textFieldFont);
        panel.add(txtDnfs);

        JLabel lblDNFs = new JLabel("DNF");
        lblDNFs.setBounds(70, 229, 50, 25);
        lblDNFs.setFont(GHTextField.numaraFont);
        panel.add(lblDNFs);

        lblSprintQTitleEdit.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintQTitleEdit.setFont(FontManager.getFont(Font.BOLD, 16));
        lblSprintQTitleEdit.setBounds(12, 275, 523, 20);
        panel.add(lblSprintQTitleEdit);

        for (int i = 0; i < 3; i++) {
            txtSprintQ[i].setGHLocation(150 + (i * 90), 310);
            panel.add(txtSprintQ[i]);
            panel.add(txtSprintQ[i].getLabel());
        }

        lblSprintRTitleEdit.setHorizontalAlignment(SwingConstants.CENTER);
        lblSprintRTitleEdit.setFont(FontManager.getFont(Font.BOLD, 16));
        lblSprintRTitleEdit.setBounds(12, 355, 523, 20);
        panel.add(lblSprintRTitleEdit);

        for (int i = 0; i < 8; i++) {
            int x = 100 + ((i % 4) * 90);
            int y = 390 + ((i / 4) * 40);
            txtSprintR[i].setGHLocation(x, y);
            panel.add(txtSprintR[i]);
            panel.add(txtSprintR[i].getLabel());
        }

        txtSprintDnfs.setBounds(130, 470, 295, 25);
        txtSprintDnfs.setFont(GHTextField.textFieldFont);
        panel.add(txtSprintDnfs);

        lblSprintDNFs.setBounds(80, 469, 50, 25);
        lblSprintDNFs.setFont(GHTextField.numaraFont);
        panel.add(lblSprintDNFs);

        KeyAdapter noSpaceAdapter = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == ' ')
                    e.consume();
            }
        };
        txtDnfs.addKeyListener(noSpaceAdapter);
        txtSprintDnfs.addKeyListener(noSpaceAdapter);

        JButton btnCancelEditing = new JButton("İptal");
        btnCancelEditing.setFont(FontManager.getFont(Font.BOLD, 15));
        btnCancelEditing.setBounds(270, 520, 100, 30);
        panel.add(btnCancelEditing);

        JButton btnSaveEdit = new JButton("Kaydet");
        btnSaveEdit.setFont(FontManager.getFont(Font.BOLD, 15));
        btnSaveEdit.setBounds(160, 520, 100, 30);
        panel.add(btnSaveEdit);

        JButton btnClearEdit = new JButton("Temizle");
        btnClearEdit.setFont(FontManager.getFont(Font.BOLD, 15));
        btnClearEdit.setBounds(440, 520, 100, 30);
        panel.add(btnClearEdit);

        btnCancelEditing.addActionListener(e -> {
            CardLayout cl = (CardLayout) (cardPanel.getLayout());
            cl.show(cardPanel, "INFO"); // İptal edilince INFO paneline dön
            toggleMainButtons(true);
        });

        btnClearEdit.addActionListener(e -> {
            selectedRace.setDnfs(null);
            selectedRace.setQualiResult(null);
            selectedRace.setRaceResult(null);
            selectedRace.setSprintDnfs(null);
            selectedRace.setSprintQualiResult(null);
            selectedRace.setSprintResult(null);
            selectedRace.setCompleted(false);

            raceDAO.updateRaceResults(selectedRace);
            updateEditPanel();
            updateInfoPanel();

            CardLayout cl = (CardLayout) (cardPanel.getLayout());
            cl.show(cardPanel, "INFO");
            toggleMainButtons(true);
        });

        btnSaveEdit.addActionListener(e -> {
            boolean valid = true;
            for (GHTextField txt : txtQ) {
                if (txt.getText().isEmpty()) {
                    valid = false;
                    break;
                }
            }
            for (GHTextField txt : txtRace) {
                if (txt.getText().isEmpty()) {
                    valid = false;
                    break;
                }
            }
            if (selectedRace.hasSprint()) {
                for (GHTextField txt : txtSprintQ) {
                    if (txt.getText().isEmpty()) {
                        valid = false;
                        break;
                    }
                }
                for (GHTextField txt : txtSprintR) {
                    if (txt.getText().isEmpty()) {
                        valid = false;
                        break;
                    }
                }
            }

            if (valid) {
                String[] quali = new String[3];
                for (int i = 0; i < 3; i++)
                    quali[i] = txtQ[i].getText();
                selectedRace.setQualiResult(quali);

                String[] race = new String[10];
                for (int i = 0; i < 10; i++)
                    race[i] = txtRace[i].getText();
                selectedRace.setRaceResult(race);

                String[] dnfs = txtDnfs.getText().toUpperCase(Locale.ENGLISH).split(",");
                for (int i = 0; i < dnfs.length; i++)
                    dnfs[i] = dnfs[i].trim();
                selectedRace.setDnfs(dnfs.length == 1 && dnfs[0].isEmpty() ? null : dnfs);

                if (selectedRace.hasSprint()) {
                    String[] sprintQ = new String[3];
                    for (int i = 0; i < 3; i++)
                        sprintQ[i] = txtSprintQ[i].getText();
                    selectedRace.setSprintQualiResult(sprintQ);

                    String[] sprintR = new String[8];
                    for (int i = 0; i < 8; i++)
                        sprintR[i] = txtSprintR[i].getText();
                    selectedRace.setSprintResult(sprintR);

                    String[] sDnfs = txtSprintDnfs.getText().toUpperCase(Locale.ENGLISH).split(",");
                    for (int i = 0; i < sDnfs.length; i++)
                        sDnfs[i] = sDnfs[i].trim();
                    selectedRace.setSprintDnfs(sDnfs.length == 1 && sDnfs[0].isEmpty() ? null : sDnfs);
                }

                selectedRace.setCompleted(true);
                raceDAO.updateRaceResults(selectedRace);

                CardLayout cl = (CardLayout) (cardPanel.getLayout());
                cl.show(cardPanel, "INFO");
                toggleMainButtons(true);
                updateInfoPanel();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Lütfen alanları eksiksiz doldurduğunuza emin olun.", "Uyarı",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    // --- PANEL GÜNCELLEME METODLARI ---

    public void updateInfoPanel() {
        if (selectedRace != null) {
            lblRaceName.setText(selectedRace.getRaceName());
            if (selectedRace.getQualiResult() != null) {
                lblQualiResult.setText("1-" + selectedRace.getQualiResult()[0] + " 2-"
                        + selectedRace.getQualiResult()[1] + " 3-" + selectedRace.getQualiResult()[2]);
            } else {
                lblQualiResult.setText("1-    2-    3- ");
            }
            if (selectedRace.getRaceResult() != null) {
                for (int i = 0; i < 2; i++) {
                    StringBuilder line = new StringBuilder();
                    String space = i < 2 ? "  " : " ";
                    for (int j = 0; j < 5; j++) {
                        line.append((i * 5) + j + 1).append("-").append(selectedRace.getRaceResult()[(i * 5) + j])
                                .append(space);
                    }
                    if (i == 0)
                        lblRaceResultLine1.setText(line.toString());
                    else
                        lblRaceResultLine2.setText(line.toString());
                }
            } else {
                lblRaceResultLine1.setText("1-    2-    3-    4-    5-    ");
                lblRaceResultLine2.setText("6-    7-    8-    9-    10-  ");
            }

            if (selectedRace.hasSprint()) {
                setSprintInfoVisible(true);
                if (selectedRace.getSprintQualiResult() != null) {
                    lblSprintQResult.setText("1-" + selectedRace.getSprintQualiResult()[0] + " 2-"
                            + selectedRace.getSprintQualiResult()[1] + " 3-" + selectedRace.getSprintQualiResult()[2]);
                } else {
                    lblSprintQResult.setText("1-    2-    3- ");
                }

                if (selectedRace.getSprintResult() != null) {
                    for (int i = 0; i < 2; i++) {
                        StringBuilder line = new StringBuilder();
                        for (int j = 0; j < 4; j++) {
                            line.append((i * 4) + j + 1).append("-").append(selectedRace.getSprintResult()[(i * 4) + j])
                                    .append("  ");
                        }
                        if (i == 0)
                            lblSprintResultLine1.setText(line.toString());
                        else
                            lblSprintResultLine2.setText(line.toString());
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
        if (selectedRace == null)
            return;

        String[] qualiResult = selectedRace.getQualiResult();
        String[] raceResult = selectedRace.getRaceResult();

        if (qualiResult != null && qualiResult.length >= 3) {
            for (int i = 0; i < 3; i++)
                txtQ[i].setText(qualiResult[i]);
        } else {
            for (int i = 0; i < 3; i++)
                txtQ[i].setText("");
        }
        if (raceResult != null && raceResult.length >= 10) {
            for (int i = 0; i < 10; i++)
                txtRace[i].setText(raceResult[i]);
        } else {
            for (int i = 0; i < 10; i++)
                txtRace[i].setText("");
        }

        String[] dnfs = selectedRace.getDnfs();
        txtDnfs.setText(dnfs != null ? String.join(",", dnfs) : "");
        if (selectedRace.hasSprint()) {
            setSprintEditVisible(true);
            String[] sprintResult = selectedRace.getSprintResult();
            String[] sprintQResult = selectedRace.getSprintQualiResult();

            if (sprintQResult != null && sprintQResult.length >= 3) {
                for (int i = 0; i < 3; i++)
                    txtSprintQ[i].setText(sprintQResult[i]);
            } else {
                for (int i = 0; i < 3; i++)
                    txtSprintQ[i].setText("");
            }
            if (sprintResult != null && sprintResult.length >= 8) {
                for (int i = 0; i < 8; i++)
                    txtSprintR[i].setText(sprintResult[i]);
            } else {
                for (int i = 0; i < 8; i++)
                    txtSprintR[i].setText("");
            }

            String[] sprintDNFs = selectedRace.getSprintDnfs();
            txtSprintDnfs.setText(sprintDNFs != null ? String.join(",", sprintDNFs) : "");
        } else {
            setSprintEditVisible(false);
        }
    }

    private void setSprintEditVisible(boolean b) {
        lblSprintQTitleEdit.setVisible(b);
        lblSprintRTitleEdit.setVisible(b);
        for (int i = 0; i < 3; i++)
            txtSprintQ[i].setVisible(b);
        for (int i = 0; i < 8; i++)
            txtSprintR[i].setVisible(b);
        txtSprintDnfs.setVisible(b);
        lblSprintDNFs.setVisible(b);
    }
}