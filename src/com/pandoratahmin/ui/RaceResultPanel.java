package com.pandoratahmin.ui;

import com.pandoratahmin.database.PredictionDAO;
import com.pandoratahmin.database.RaceDAO;
import com.pandoratahmin.database.SettingsDAO;
import com.pandoratahmin.database.UserDAO;
import com.pandoratahmin.model.Prediction;
import com.pandoratahmin.model.Race;
import com.pandoratahmin.model.User;
import com.pandoratahmin.service.ScoreCalculationService;
import com.pandoratahmin.main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RaceResultPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private RaceDAO raceDAO = new RaceDAO();
    private UserDAO userDAO = new UserDAO();
    private PredictionDAO predictionDAO = new PredictionDAO();
    private SettingsDAO settingsDAO = new SettingsDAO();
    private ScoreCalculationService scoreService = new ScoreCalculationService();

    private int WIDTH = 925;
    private int rowHeight = 50;
    private int HEIGHT;
    private int userCount;

    private JLabel[] nameLblArr;
    private JLabel[] qPointsLblArr;
    private JLabel[] rPointsLblArr;
    private JLabel[] raceRGLblArr;
    private JLabel[] podiumLblArr;
    private JLabel[] sQPointsLblArr;
    private JLabel[] sRPointsLblArr;
    private JLabel[] sprintRGLblArr;
    private JLabel[] totalPointsLblArr;

    JPanel panelTable = new JPanel();
    JButton btnReturnMainMenu = new JButton("Ana Menüye Dön");
    JComboBox<Race> cmbxRace = new JComboBox<>();
    List<User> userList;

    public RaceResultPanel() {
        userList = userDAO.getAllUsers();
        userCount = userList.size();
        HEIGHT = rowHeight * (userCount + 2);

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        nameLblArr = new JLabel[userCount];
        qPointsLblArr = new JLabel[userCount];
        rPointsLblArr = new JLabel[userCount];
        raceRGLblArr = new JLabel[userCount];
        podiumLblArr = new JLabel[userCount];
        sQPointsLblArr = new JLabel[userCount];
        sRPointsLblArr = new JLabel[userCount];
        sprintRGLblArr = new JLabel[userCount];
        totalPointsLblArr = new JLabel[userCount];

        // --- PANELLER ---
        JPanel panelTop = new JPanel();
        panelTop.setBounds(0, 0, WIDTH, rowHeight);
        panelTop.setLayout(null);
        add(panelTop);

        panelTable.setBounds(0, rowHeight, WIDTH, HEIGHT - rowHeight);
        panelTable.setBackground(Color.BLACK);
        panelTable.setLayout(null);
        add(panelTable);

        // --- ÜST MENÜ ---
        JLabel lblRace = new JLabel("Yarış : ");
        lblRace.setFont(FontManager.getFont(Font.BOLD, 16));
        lblRace.setBounds(20, 10, 80, 30);
        panelTop.add(lblRace);

        cmbxRace.setBounds(100, 10, 200, 30);
        cmbxRace.setFont(FontManager.getFont(Font.PLAIN, 15));
        panelTop.add(cmbxRace);

        btnReturnMainMenu.setFont(FontManager.getFont(Font.BOLD, 14));
        btnReturnMainMenu.setBounds(WIDTH - 210, 10, 180, 30);
        panelTop.add(btnReturnMainMenu);

        // --- TABLO BAŞLIKLARI ---
        String[] headers = { "İsim", "Q", "R", "Y.DT", "P", "SQ", "SR", "S.DT", "Toplam" };
        int[] xPos = { 0, 301, 376, 451, 526, 601, 676, 751, 826 };
        int[] widths = { 300, 74, 74, 74, 74, 74, 74, 74, 99 };

        for (int i = 0; i < headers.length; i++) {
            JLabel lbl = new JLabel();
            lbl.setForeground(Color.WHITE);
            lbl.setFont(FontManager.getFont(Font.BOLD, 16));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setBounds(xPos[i], 0, widths[i], rowHeight);
            if (headers[i].equals("P")) {
                try {
                    ImageIcon icn = new ImageIcon("src/resources/images/podiumcolored.png");
                    Image img = icn.getImage();
                    Image newImg = img.getScaledInstance(widths[i] - 20, rowHeight, Image.SCALE_SMOOTH);
                    lbl.setIcon(new ImageIcon(newImg));
                } catch (Exception e) {
                    lbl.setText("P");
                    e.printStackTrace();
                }
            } else {
                lbl.setText(headers[i]);
            }
            panelTable.add(lbl);
        }

        // --- HÜCRELERİN OLUŞTURULMASI ---
        for (int i = 0; i < userCount; i++) {
            int y = rowHeight * (i + 1);

            nameLblArr[i] = new JLabel();
            nameLblArr[i].setBounds(xPos[0], y + 1, widths[0], rowHeight - 1);
            nameLblArr[i].setOpaque(true);
            nameLblArr[i].setFont(FontManager.getFont(Font.BOLD, 15));
            panelTable.add(nameLblArr[i]);

            qPointsLblArr[i] = createDataLabel(xPos[1], y, widths[1]);
            rPointsLblArr[i] = createDataLabel(xPos[2], y, widths[2]);
            raceRGLblArr[i] = createDataLabel(xPos[3], y, widths[3]);
            podiumLblArr[i] = createDataLabel(xPos[4], y, widths[4]);
            sQPointsLblArr[i] = createDataLabel(xPos[5], y, widths[5]);
            sRPointsLblArr[i] = createDataLabel(xPos[6], y, widths[6]);
            sprintRGLblArr[i] = createDataLabel(xPos[7], y, widths[7]);

            totalPointsLblArr[i] = createDataLabel(xPos[8], y, widths[8]);
            totalPointsLblArr[i].setFont(FontManager.getFont(Font.BOLD, 16));
        }

        // --- VERİ DOLDURMA ---
        List<Race> races = raceDAO.getAllRaces();
        for (Race r : races) {
            cmbxRace.addItem(r);
        }

        // --- EVENT LISTENER'LAR ---
        btnReturnMainMenu.addActionListener(e -> {
            MainMenuFrame.raceResultsFrame.dispose();
            Main.mainMenuFrame.setVisible(true);
        });

        cmbxRace.addActionListener(e -> refreshData());

        // Seçili yarış varsa, tabloyu ilk açılışta doldur
        if (cmbxRace.getItemCount() > 0) {
            refreshData();
        }
    }

    private JLabel createDataLabel(int x, int y, int width) {
        JLabel lbl = new JLabel();
        lbl.setBounds(x, y + 1, width, rowHeight - 1);
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        lbl.setForeground(Color.BLACK);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setFont(FontManager.getFont(Font.PLAIN, 15));
        panelTable.add(lbl);
        return lbl;
    }

    private void refreshData() {
        Race selectedRace = (Race) cmbxRace.getSelectedItem();
        if (selectedRace == null)
            return;

        int maxRaceRG = 0;
        int maxSprintRG = 0;
        Prediction[] preds = new Prediction[userCount];

        for (int i = 0; i < userCount; i++) {
            Prediction p = predictionDAO.getPrediction(userList.get(i).getId(), selectedRace.getId());
            if (p != null && !p.isDidNotAttend()) {
                scoreService.calculatePoints(p, selectedRace);
                if (p.getRaceRightGuess() > maxRaceRG)
                    maxRaceRG = p.getRaceRightGuess();
                if (p.getSprintRightGuess() > maxSprintRG)
                    maxSprintRG = p.getSprintRightGuess();
            }
            preds[i] = p;
        }

        for (int i = 0; i < userCount; i++) {
            User u = userList.get(i);
            Prediction pred = preds[i];

            Color teamColor = settingsDAO.getTeamColor(u.getTeam(), Color.DARK_GRAY);
            Color fontColor = getContrastColor(teamColor);

            nameLblArr[i].setText("  " + u.getName());
            nameLblArr[i].setBackground(teamColor);
            nameLblArr[i].setForeground(fontColor);

            if (pred != null) {
                if (pred.isDidNotAttend()) {
                    setLabel(qPointsLblArr[i], "DNA", Color.WHITE, Color.RED);
                    setLabel(rPointsLblArr[i], "DNA", Color.WHITE, Color.RED);
                    setLabel(raceRGLblArr[i], "0", Color.WHITE, Color.BLACK);
                    setLabel(podiumLblArr[i], "0", Color.WHITE, Color.RED);
                    if (selectedRace.hasSprint()) {
                        setLabel(sQPointsLblArr[i], "DNA", Color.WHITE, Color.RED);
                        setLabel(sRPointsLblArr[i], "DNA", Color.WHITE, Color.RED);
                        setLabel(sprintRGLblArr[i], "0", Color.WHITE, Color.BLACK);
                    } else {
                        setLabel(sQPointsLblArr[i], "-", Color.WHITE, Color.BLACK);
                        setLabel(sRPointsLblArr[i], "-", Color.WHITE, Color.BLACK);
                        setLabel(sprintRGLblArr[i], "-", Color.WHITE, Color.BLACK);
                    }
                    setLabel(totalPointsLblArr[i], "0", Color.WHITE, Color.BLACK);
                } else {
                    setLabel(qPointsLblArr[i], String.valueOf(pred.getQualiPoints()), Color.WHITE, Color.BLACK);
                    setLabel(rPointsLblArr[i], String.valueOf(pred.getRacePoints()), Color.WHITE, Color.BLACK);

                    if (pred.getRaceRightGuess() == maxRaceRG && maxRaceRG > 0) {
                        setLabel(raceRGLblArr[i], String.valueOf(pred.getRaceRightGuess()), new Color(117, 49, 156),
                                Color.WHITE);
                    } else {
                        setLabel(raceRGLblArr[i], String.valueOf(pred.getRaceRightGuess()), Color.WHITE, Color.BLACK);
                    }

                    if (pred.isPodiumCorrect()) {
                        setLabel(podiumLblArr[i], "3", new Color(117, 49, 156), Color.WHITE);
                        rPointsLblArr[i].setText(String.valueOf(Integer.parseInt(rPointsLblArr[i].getText()) - 3));
                    } else {
                        setLabel(podiumLblArr[i], "0", Color.WHITE, Color.BLACK);
                    }

                    if (selectedRace.hasSprint()) {
                        setLabel(sQPointsLblArr[i], String.valueOf(pred.getSprintQPoints()), Color.WHITE, Color.BLACK);
                        setLabel(sRPointsLblArr[i], String.valueOf(pred.getSprintRPoints()), Color.WHITE, Color.BLACK);

                        if (pred.getSprintRightGuess() == maxSprintRG && maxSprintRG > 0) {
                            setLabel(sprintRGLblArr[i], String.valueOf(pred.getSprintRightGuess()),
                                    new Color(117, 49, 156), Color.WHITE);
                        } else {
                            setLabel(sprintRGLblArr[i], String.valueOf(pred.getSprintRightGuess()), Color.WHITE,
                                    Color.BLACK);
                        }
                    } else {
                        setLabel(sQPointsLblArr[i], "-", Color.WHITE, Color.BLACK);
                        setLabel(sRPointsLblArr[i], "-", Color.WHITE, Color.BLACK);
                        setLabel(sprintRGLblArr[i], "-", Color.WHITE, Color.BLACK);
                    }

                    int totalPts = pred.getPointsEarned();
                    if (pred.getRaceRightGuess() == maxRaceRG && maxRaceRG > 0) {
                        totalPts += maxRaceRG;
                    }
                    if (selectedRace.hasSprint() && pred.getSprintRightGuess() == maxSprintRG && maxSprintRG > 0) {
                        totalPts += maxSprintRG;
                    }

                    setLabel(totalPointsLblArr[i], String.valueOf(totalPts), Color.WHITE, Color.BLACK);
                }
            } else {
                setLabel(qPointsLblArr[i], "-", Color.WHITE, Color.BLACK);
                setLabel(rPointsLblArr[i], "-", Color.WHITE, Color.BLACK);
                setLabel(raceRGLblArr[i], "-", Color.WHITE, Color.BLACK);
                setLabel(podiumLblArr[i], "-", Color.WHITE, Color.BLACK);
                if (selectedRace.hasSprint()) {
                    setLabel(sQPointsLblArr[i], "-", Color.WHITE, Color.BLACK);
                    setLabel(sRPointsLblArr[i], "-", Color.WHITE, Color.BLACK);
                    setLabel(sprintRGLblArr[i], "-", Color.WHITE, Color.BLACK);
                } else {
                    setLabel(sQPointsLblArr[i], "-", Color.WHITE, Color.BLACK);
                    setLabel(sRPointsLblArr[i], "-", Color.WHITE, Color.BLACK);
                    setLabel(sprintRGLblArr[i], "-", Color.WHITE, Color.BLACK);
                }
                setLabel(totalPointsLblArr[i], "-", Color.WHITE, Color.BLACK);
            }
        }
        panelTable.revalidate();
        panelTable.repaint();
    }

    private void setLabel(JLabel lbl, String text, Color bg, Color fg) {
        lbl.setText(text);
        lbl.setBackground(bg);
        lbl.setForeground(fg);
    }

    private Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }
}