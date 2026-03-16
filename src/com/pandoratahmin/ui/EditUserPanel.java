package com.pandoratahmin.ui;

import com.pandoratahmin.database.SettingsDAO;
import com.pandoratahmin.database.UserDAO;
import com.pandoratahmin.main.Main;
import com.pandoratahmin.model.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EditUserPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;
    private User selectedUser;
    private boolean editing;

    JList<User> list = new JList<>();
    DefaultListModel<User> listModel = new DefaultListModel<>();
    JLabel lblUserInfo = new JLabel("Kişi Bilgileri");
    JLabel lblUserList = new JLabel("Kişiler");
    JButton btnAddUser = new JButton("Ekle");
    JButton btnRemoveUser = new JButton("Kaldır");
    JButton btnReturnMainMenu = new JButton("Ana Menüye Dön");
    JButton btnEditUser = new JButton("Kişiyi Düzenle");
    JLabel lblUserName = new JLabel("İsim : ");
    JLabel lblUserTeam = new JLabel("Takım : ");
    JLabel lblUserPoints = new JLabel("Puan : ");
    JTextField txtUserName = new JTextField();
    JComboBox<String> cmbxUserTeam = new JComboBox<>();
    JButton btnSaveUser = new JButton("Kaydet");
    JButton btnCancelEditing = new JButton("İptal");

    public EditUserPanel() {
        this.userDAO = new UserDAO();
        this.setPreferredSize(new Dimension(700, 500));
        this.setLayout(null);
        editing = false;

        list.setModel(listModel);
        list.setBounds(0, 30, 250, 430);
        list.setFont(FontManager.getFont(Font.PLAIN, 16));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(list);

        btnReturnMainMenu.setFont(FontManager.getFont(Font.BOLD, 14));
        btnReturnMainMenu.setBounds(375, 460, 200, 40);
        add(btnReturnMainMenu);

        JPanel topPanel = new JPanel();
        topPanel.setBounds(0, 0, 700, 30);
        add(topPanel);
        topPanel.setLayout(null);

        lblUserInfo.setFont(FontManager.getFont(Font.BOLD, 16));
        lblUserInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblUserInfo.setBounds(250, 0, 450, 30);
        topPanel.add(lblUserInfo);
        lblUserList.setHorizontalAlignment(SwingConstants.CENTER);
        lblUserList.setFont(FontManager.getFont(Font.BOLD, 16));
        lblUserList.setBounds(0, 0, 250, 30);
        topPanel.add(lblUserList);

        btnAddUser.setFont(FontManager.getFont(Font.BOLD, 15));
        btnAddUser.setBounds(0, 460, 125, 40);
        add(btnAddUser);
        btnRemoveUser.setFont(FontManager.getFont(Font.BOLD, 15));
        btnRemoveUser.setBounds(125, 460, 125, 40);
        add(btnRemoveUser);

        JPanel panelInfo = new JPanel();
        panelInfo.setBounds(250, 30, 450, 430);
        panelInfo.setLayout(null);
        add(panelInfo);

        btnEditUser.setFont(FontManager.getFont(Font.BOLD, 14));
        btnEditUser.setBounds(150, 380, 150, 30);
        panelInfo.add(btnEditUser);

        lblUserName.setFont(FontManager.getFont(Font.BOLD, 18));
        lblUserName.setBounds(40, 30, 300, 30);
        panelInfo.add(lblUserName);
        lblUserTeam.setFont(FontManager.getFont(Font.BOLD, 18));
        lblUserTeam.setBounds(40, 70, 300, 30);
        panelInfo.add(lblUserTeam);
        lblUserPoints.setFont(FontManager.getFont(Font.BOLD, 18));
        lblUserPoints.setBounds(40, 110, 300, 30);
        panelInfo.add(lblUserPoints);

        // Düzenleme Bileşenleri
        txtUserName.setFont(FontManager.getFont(Font.BOLD, 17));
        txtUserName.setBounds(120, 30, 250, 30);
        panelInfo.add(txtUserName);

        cmbxUserTeam.setFont(FontManager.getFont(Font.BOLD, 17));
        cmbxUserTeam.setBounds(120, 70, 250, 30);
        SettingsDAO tempSetDao = new SettingsDAO();
        for (String teamName : tempSetDao.getAllTeams().keySet()) {
            cmbxUserTeam.addItem(teamName);
        }
        panelInfo.add(cmbxUserTeam);

        btnSaveUser.setFont(FontManager.getFont(Font.BOLD, 15));
        btnSaveUser.setBounds(115, 380, 100, 30);
        panelInfo.add(btnSaveUser);

        btnCancelEditing.setFont(FontManager.getFont(Font.BOLD, 15));
        btnCancelEditing.setBounds(235, 380, 100, 30);
        panelInfo.add(btnCancelEditing);

        // Veritabanından listeyi çekip doldur
        updateList();

        // --- EVENT LİSTENER'LAR ---

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && list.getSelectedValue() != null) {
                    selectedUser = list.getSelectedValue();
                    updatePanel();
                }
            }
        });

        btnSaveUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedUser != null) {
                    String newName = txtUserName.getText();
                    String newTeam = (String) cmbxUserTeam.getSelectedItem();

                    userDAO.updateUser(selectedUser.getId(), newName, newTeam);

                    editing = false;
                    updateList();
                }
            }
        });

        btnCancelEditing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editing = false;
                updatePanel();
            }
        });

        btnEditUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (list.getSelectedValue() != null) {
                    editing = true;
                    updatePanel();
                }
            }
        });

        btnRemoveUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedUser != null) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            selectedUser.getName() + " silinecek, emin misiniz?", "Kişi Sil",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        userDAO.deleteUser(selectedUser.getName());
                        updateList();
                    }
                }
            }
        });

        btnAddUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddUserDialog();
            }
        });

        btnReturnMainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuFrame.editUserFrame.dispose();
                Main.mainMenuFrame.setVisible(true);
            }
        });
    }

    private void openAddUserDialog() {
        JDialog dialog = new JDialog();
        dialog.setSize(400, 220);
        dialog.setIconImage(MainMenuFrame.pandoraIcon.getImage());
        dialog.setTitle("Pandora Tahmin - Kişi Ekle");
        dialog.getContentPane().setLayout(null);
        dialog.setResizable(false);

        JLabel lblNewUserName = new JLabel("Kişi Adı");
        lblNewUserName.setFont(FontManager.getFont(Font.BOLD, 16));
        lblNewUserName.setBounds(20, 30, 100, 30);
        dialog.getContentPane().add(lblNewUserName);

        JTextField txtNewUserName = new JTextField();
        txtNewUserName.setBounds(140, 30, 200, 30);
        txtNewUserName.setFont(FontManager.getFont(Font.PLAIN, 18));
        dialog.getContentPane().add(txtNewUserName);

        JLabel lblNewUserTeam = new JLabel("Takım");
        lblNewUserTeam.setFont(FontManager.getFont(Font.BOLD, 16));
        lblNewUserTeam.setBounds(20, 90, 100, 30);
        dialog.getContentPane().add(lblNewUserTeam);

        JComboBox<String> cmbxNewUserTeam = new JComboBox<String>();
        cmbxNewUserTeam.setBounds(140, 90, 200, 30);
        SettingsDAO tempSetDao = new SettingsDAO();
        for (String teamName : tempSetDao.getAllTeams().keySet()) {
            cmbxNewUserTeam.addItem(teamName);
        }
        dialog.getContentPane().add(cmbxNewUserTeam);

        JButton okButton = new JButton("Ekle");
        okButton.setBounds(250, 145, 60, 30);
        dialog.getContentPane().add(okButton);

        JButton cancelButton = new JButton("İptal");
        cancelButton.setBounds(315, 145, 60, 30);
        dialog.getContentPane().add(cancelButton);

        okButton.addActionListener(e -> {
            String userName = txtNewUserName.getText().trim();
            String userTeam = (String) cmbxNewUserTeam.getSelectedItem();
            if (!userName.isEmpty()) {
                userDAO.addUser(userName, userTeam);
                dialog.dispose();
                updateList();
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public void updatePanel() {
        if (selectedUser != null) {
            lblUserName.setText("İsim : " + selectedUser.getName());
            lblUserTeam.setText("Takım : " + selectedUser.getTeam());
            lblUserPoints.setText("Puan : " + selectedUser.getPoints());
        } else {
            lblUserName.setText("İsim : ");
            lblUserTeam.setText("Takım : ");
            lblUserPoints.setText("Puan : ");
            btnEditUser.setEnabled(false);
        }

        if (editing && selectedUser != null) {
            lblUserName.setText("İsim : ");
            lblUserTeam.setText("Takım : ");
            lblUserPoints.setText("");
            txtUserName.setText(selectedUser.getName());
            cmbxUserTeam.setSelectedItem(selectedUser.getTeam());
        }

        btnEditUser.setVisible(!editing);
        txtUserName.setVisible(editing);
        cmbxUserTeam.setVisible(editing);
        btnCancelEditing.setVisible(editing);
        btnSaveUser.setVisible(editing);

        list.setEnabled(!editing);
        btnAddUser.setEnabled(!editing);
        btnRemoveUser.setEnabled(!editing);
        btnReturnMainMenu.setEnabled(!editing);
    }

    public void updateList() {
        listModel.clear();
        List<User> usersFromDB = userDAO.getAllUsers();

        for (User u : usersFromDB) {
            listModel.addElement(u);
        }

        if (!listModel.isEmpty()) {
            list.setSelectedIndex(0);
            selectedUser = list.getSelectedValue();
            btnEditUser.setEnabled(true);
        } else {
            selectedUser = null;
        }
        updatePanel();
    }
}