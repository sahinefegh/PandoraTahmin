package com.pandoratahmin.ui; // refresh

import com.pandoratahmin.database.SettingsDAO;
import com.pandoratahmin.main.Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Locale;
import java.util.Map;

import javax.swing.*;

public class SettingsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private SettingsDAO settingsDAO;

    JSpinner spinnerRight = new JSpinner();
    JSpinner spinnerDNF = new JSpinner();
    JSpinner spinnerAbove = new JSpinner();
    JSpinner spinnerBelow = new JSpinner();
    JSpinner spinnerDNQ = new JSpinner();
    JSpinner spinnerDNR = new JSpinner();

    JButton btnSavePoints = new JButton("Kaydet");
    JButton btnResetPoints = new JButton("Sıfırla");
    JButton btnReturnMainMenu = new JButton("Ana Menüye Dön");
    private JTextField txtDrivers;

    private JComboBox<String> cmbxTeams;
    private JLabel lblTeamPreview;
    private Map<String, Color> teamMap;

    public SettingsPanel() {
        this.settingsDAO = new SettingsDAO();

        int width = 600;
        int height = 510;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        btnReturnMainMenu.setFont(FontManager.getFont(Font.BOLD, 13));
        btnReturnMainMenu.setFocusable(false);
        btnReturnMainMenu.setBounds(width - 180 - 10, height - 40 - 10, 180, 40);
        add(btnReturnMainMenu);

        JPanel panelPoints = new JPanel();
        panelPoints.setBounds(0, 0, width, 175);
        panelPoints.setLayout(null);
        add(panelPoints);

        JPanel panelColor = new JPanel();
        panelColor.setBounds(0, 178, width, 165);
        panelColor.setLayout(null);
        add(panelColor);

        JPanel panelDrivers = new JPanel();
        panelDrivers.setBounds(0, 346, width, height - 346);
        panelDrivers.setLayout(null);
        add(panelDrivers);

        // --- PUANLAMA EKRANI ---
        JLabel lblPoints = new JLabel("Puanlama Sistemi");
        lblPoints.setFont(FontManager.getFont(Font.BOLD, 17));
        lblPoints.setHorizontalAlignment(SwingConstants.CENTER);
        lblPoints.setBounds(180, 2, 240, 30);
        panelPoints.add(lblPoints);

        JLabel lblRight = new JLabel("Doğru");
        lblRight.setFont(FontManager.getFont(Font.BOLD, 15));
        lblRight.setBounds(40, 50, 80, 25);
        panelPoints.add(lblRight);

        spinnerRight.setModel(new SpinnerNumberModel(0, 0, null, 1));
        spinnerRight.setFont(FontManager.getFont(Font.PLAIN, 14));
        spinnerRight.setBounds(110, 50, 60, 25);
        panelPoints.add(spinnerRight);

        JLabel lblDNF = new JLabel("DNF");
        lblDNF.setFont(FontManager.getFont(Font.BOLD, 15));
        lblDNF.setBounds(40, 100, 80, 25);
        panelPoints.add(lblDNF);

        spinnerDNF.setModel(new SpinnerNumberModel(0, null, 0, 1));
        spinnerDNF.setFont(FontManager.getFont(Font.PLAIN, 14));
        spinnerDNF.setBounds(110, 100, 60, 25);
        panelPoints.add(spinnerDNF);

        JLabel lblAbove = new JLabel("Daha iyi");
        lblAbove.setFont(FontManager.getFont(Font.BOLD, 15));
        lblAbove.setBounds(208, 50, 92, 25);
        panelPoints.add(lblAbove);

        spinnerAbove.setModel(new SpinnerNumberModel(0, 0, null, 1));
        spinnerAbove.setFont(FontManager.getFont(Font.PLAIN, 14));
        spinnerAbove.setBounds(305, 50, 60, 25);
        panelPoints.add(spinnerAbove);

        JLabel lblBelow = new JLabel("Daha kötü");
        lblBelow.setFont(FontManager.getFont(Font.BOLD, 15));
        lblBelow.setBounds(208, 100, 92, 25);
        panelPoints.add(lblBelow);

        spinnerBelow.setModel(new SpinnerNumberModel(0, null, 0, 1));
        spinnerBelow.setFont(FontManager.getFont(Font.PLAIN, 14));
        spinnerBelow.setBounds(305, 100, 60, 25);
        panelPoints.add(spinnerBelow);

        JLabel lblDNQ = new JLabel("Q Yazmadı");
        lblDNQ.setFont(FontManager.getFont(Font.BOLD, 15));
        lblDNQ.setBounds(395, 50, 100, 25);
        panelPoints.add(lblDNQ);

        spinnerDNQ.setModel(new SpinnerNumberModel(0, null, 0, 1));
        spinnerDNQ.setFont(FontManager.getFont(Font.PLAIN, 14));
        spinnerDNQ.setBounds(500, 50, 60, 25);
        panelPoints.add(spinnerDNQ);

        JLabel lblDNR = new JLabel("R Yazmadı");
        lblDNR.setFont(FontManager.getFont(Font.BOLD, 15));
        lblDNR.setBounds(395, 100, 100, 25);
        panelPoints.add(lblDNR);

        spinnerDNR.setModel(new SpinnerNumberModel(0, null, 0, 1));
        spinnerDNR.setFont(FontManager.getFont(Font.PLAIN, 14));
        spinnerDNR.setBounds(500, 100, 60, 25);
        panelPoints.add(spinnerDNR);

        btnResetPoints.setFont(FontManager.getFont(Font.BOLD, 13));
        btnResetPoints.setBounds(305, 140, 100, 25);
        panelPoints.add(btnResetPoints);

        btnSavePoints.setFont(FontManager.getFont(Font.BOLD, 13));
        btnSavePoints.setBounds(195, 140, 100, 25);
        panelPoints.add(btnSavePoints);

        // --- TAKIM RENKLERİ EKRANI ---
        JLabel lblColorTitle = new JLabel("Takım Yönetimi");
        lblColorTitle.setFont(FontManager.getFont(Font.BOLD, 17));
        lblColorTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblColorTitle.setBounds(180, 2, 240, 30);
        panelColor.add(lblColorTitle);

        // Sol: ComboBox
        cmbxTeams = new JComboBox<>();
        cmbxTeams.setFont(FontManager.getFont(Font.PLAIN, 15));
        cmbxTeams.setBounds(30, 65, 180, 35);
        panelColor.add(cmbxTeams);

        // Orta: Önizleme Kutusu (İsim + Renk)
        lblTeamPreview = new JLabel("Takım Seçin");
        lblTeamPreview.setFont(FontManager.getFont(Font.BOLD, 16));
        lblTeamPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblTeamPreview.setOpaque(true);
        lblTeamPreview.setBackground(Color.DARK_GRAY);
        lblTeamPreview.setForeground(Color.WHITE);
        lblTeamPreview.setBounds(240, 50, 170, 65);
        lblTeamPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        panelColor.add(lblTeamPreview);

        // Sağ: Kontrol Butonları
        JButton btnAddTeam = new JButton("Ekle");
        btnAddTeam.setFont(FontManager.getFont(Font.BOLD, 13));
        btnAddTeam.setBounds(450, 35, 110, 30);
        panelColor.add(btnAddTeam);

        JButton btnEditTeam = new JButton("Düzenle");
        btnEditTeam.setFont(FontManager.getFont(Font.BOLD, 13));
        btnEditTeam.setBounds(450, 70, 110, 30);
        panelColor.add(btnEditTeam);

        JButton btnDeleteTeam = new JButton("Sil");
        btnDeleteTeam.setFont(FontManager.getFont(Font.BOLD, 13));
        btnDeleteTeam.setBounds(450, 105, 110, 30);
        panelColor.add(btnDeleteTeam);

        // --- GEÇERLİ PİLOTLAR EKRANI ---
        JLabel lblDrivers = new JLabel("Geçerli Pilotlar");
        lblDrivers.setFont(FontManager.getFont(Font.BOLD, 17));
        lblDrivers.setHorizontalAlignment(SwingConstants.CENTER);
        lblDrivers.setBounds(180, 2, 240, 30);
        panelDrivers.add(lblDrivers);

        txtDrivers = new JTextField();
        txtDrivers.setBounds(30, 50, width - 60, 25);
        txtDrivers.setFont(FontManager.getFont(Font.PLAIN, 18));
        panelDrivers.add(txtDrivers);

        JButton btnSaveDrivers = new JButton("Kaydet");
        btnSaveDrivers.setBounds((width - 100) / 2, 85, 100, 25);
        btnSaveDrivers.setFont(FontManager.getFont(Font.BOLD, 13));
        panelDrivers.add(btnSaveDrivers);

        getPoints();
        getDrivers();
        refreshTeamUI();

        // --- EVENT LİSTENER'LAR ---

        cmbxTeams.addActionListener(e -> updateTeamPreview());

        btnAddTeam.addActionListener(e -> openTeamDialog(null));

        btnEditTeam.addActionListener(e -> {
            String selected = (String) cmbxTeams.getSelectedItem();
            if (selected != null)
                openTeamDialog(selected);
        });

        btnDeleteTeam.addActionListener(e -> {
            String selected = (String) cmbxTeams.getSelectedItem();
            if (selected != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        selected + " takımını silmek istediğinize emin misiniz?", "Takım Sil",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    settingsDAO.deleteTeam(selected);
                    refreshTeamUI();
                }
            }
        });

        btnSaveDrivers.addActionListener(e -> {
            String[] drivers = txtDrivers.getText().toUpperCase(Locale.ENGLISH).split(",");
            settingsDAO.saveValidDrivers(drivers);
            JOptionPane.showMessageDialog(this, "Geçerli pilotlar kaydedildi.", "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        btnSavePoints.addActionListener(e -> {
            settingsDAO.saveSetting("POINTS_RIGHT", String.valueOf(spinnerRight.getValue()));
            settingsDAO.saveSetting("POINTS_ABOVE", String.valueOf(spinnerAbove.getValue()));
            settingsDAO.saveSetting("POINTS_BELOW", String.valueOf(spinnerBelow.getValue()));
            settingsDAO.saveSetting("POINTS_DNF", String.valueOf(spinnerDNF.getValue()));
            settingsDAO.saveSetting("POINTS_DNQ", String.valueOf(spinnerDNQ.getValue()));
            settingsDAO.saveSetting("POINTS_DNR", String.valueOf(spinnerDNR.getValue()));
            JOptionPane.showMessageDialog(this, "Puanlama ayarları kaydedildi.", "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        btnResetPoints.addActionListener(e -> getPoints());

        btnReturnMainMenu.addActionListener(e -> {
            MainMenuFrame.settingsFrame.dispose();
            Main.mainMenuFrame.setVisible(true);
        });
    }

    private void refreshTeamUI() {
        teamMap = settingsDAO.getAllTeams();
        cmbxTeams.removeAllItems();
        for (String teamName : teamMap.keySet()) {
            cmbxTeams.addItem(teamName);
        }
        updateTeamPreview();
    }

    private void updateTeamPreview() {
        String selected = (String) cmbxTeams.getSelectedItem();
        if (selected != null && teamMap.containsKey(selected)) {
            lblTeamPreview.setText(selected);
            Color c = teamMap.get(selected);
            lblTeamPreview.setBackground(c);
            lblTeamPreview.setForeground(getContrastColor(c));
        } else {
            lblTeamPreview.setText("Takım Yok");
            lblTeamPreview.setBackground(Color.DARK_GRAY);
            lblTeamPreview.setForeground(Color.WHITE);
        }
    }

    private void openTeamDialog(String existingTeamName) {
        boolean isEdit = (existingTeamName != null);

        JTextField txtName = new JTextField(isEdit ? existingTeamName : "");
        JButton btnColorPicker = new JButton("Renk Seç");
        final Color[] chosenColor = { isEdit ? teamMap.get(existingTeamName) : Color.WHITE };
        btnColorPicker.setBackground(chosenColor[0]);
        btnColorPicker.setForeground(getContrastColor(chosenColor[0]));

        btnColorPicker.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Takım Rengi Seçin", chosenColor[0]);
            if (c != null) {
                chosenColor[0] = c;
                btnColorPicker.setBackground(c);
                btnColorPicker.setForeground(getContrastColor(c));
            }
        });

        Object[] message = {
                "Takım Adı:", txtName,
                "Takım Rengi:", btnColorPicker
        };

        int option = JOptionPane.showConfirmDialog(this, message, isEdit ? "Takımı Düzenle" : "Yeni Takım Ekle",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String newName = txtName.getText().trim();
            if (!newName.isEmpty()) {
                if (isEdit) {
                    settingsDAO.updateTeam(existingTeamName, newName, chosenColor[0]);
                } else {
                    settingsDAO.saveTeamColor(newName, chosenColor[0]); // Ekleme için aynı metodu kullanıyoruz
                }
                refreshTeamUI(); // Güncelleme bittikten sonra listeyi tazeleyin
            }
        }
    }

    // Veritabanından Puanları Çeker
    public void getPoints() {
        spinnerRight.setValue(settingsDAO.getSettingAsInt("POINTS_RIGHT", 5));
        spinnerDNF.setValue(settingsDAO.getSettingAsInt("POINTS_DNF", -2));
        spinnerAbove.setValue(settingsDAO.getSettingAsInt("POINTS_ABOVE", 1));
        spinnerBelow.setValue(settingsDAO.getSettingAsInt("POINTS_BELOW", -1));
        spinnerDNQ.setValue(settingsDAO.getSettingAsInt("POINTS_DNQ", -5));
        spinnerDNR.setValue(settingsDAO.getSettingAsInt("POINTS_DNR", -15));
    }

    // Veritabanından Pilotları Çeker
    public void getDrivers() {
        String[] drivers = settingsDAO.getValidDrivers();
        if (drivers != null && drivers.length > 0) {
            txtDrivers.setText(String.join(",", drivers));
        } else {
            txtDrivers.setText("");
        }
    }

    private Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }
}