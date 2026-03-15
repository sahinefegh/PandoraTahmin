package com.pandoratahmin.database;

import com.pandoratahmin.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.pandoratahmin.model.Prediction;
import com.pandoratahmin.model.Race;
import com.pandoratahmin.service.ScoreCalculationService;

public class UserDAO {

    public void addUser(String name, String team) {
        String sql = "INSERT INTO users (name, team, total_points) VALUES (?, ?, 0)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, team);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Kullanıcı eklenemedi (" + name + "): " + e.getMessage());
        }
    }

    public void deleteUser(String name) {
        String sql = "DELETE FROM users WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Kullanıcı silinemedi (" + name + "): " + e.getMessage());
        }
    }

    public void updateUser(int id, String newName, String newTeam) {
        String sql = "UPDATE users SET name = ?, team = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newTeam);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Kullanıcı güncellenemedi: " + e.getMessage());
        }
    }

    public void updateUserTeam(String name, String newTeam) {
        String sql = "UPDATE users SET team = ? WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newTeam);
            pstmt.setString(2, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Kullanıcı takımı güncellenemedi (" + name + "): " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT id, name, team FROM users";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String team = rs.getString("team");

                User user = new User(id, name, team);
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Kullanıcı listesi çekilemedi: " + e.getMessage());
        }

        // --- DİNAMİK PUAN HESAPLAMA (Bonus Puanlar Dahil) ---
        RaceDAO raceDAO = new RaceDAO();
        PredictionDAO predDAO = new PredictionDAO();
        ScoreCalculationService scoreService = new ScoreCalculationService();

        List<Race> allRaces = raceDAO.getAllRaces();

        // 1. Kullanıcı bazında toplam puanları tutacağımız map
        Map<Integer, Integer> userPointsMap = new HashMap<>();
        for (User u : userList) {
            userPointsMap.put(u.getId(), 0);
        }

        // 2. Her yarış için teker teker tahminleri çekip hesapla
        for (Race race : allRaces) {
            if (!race.isCompleted())
                continue;

            int maxRaceRG = 0;
            int maxSprintRG = 0;
            Map<Integer, Prediction> racePreds = new HashMap<>();

            // A) Bu yarış için herkesin servisten güncel puanını hesaplat
            for (User u : userList) {
                Prediction p = predDAO.getPrediction(u.getId(), race.getId());
                if (p != null && !p.isDidNotAttend()) {
                    scoreService.calculatePoints(p, race);

                    if (p.getRaceRightGuess() > maxRaceRG)
                        maxRaceRG = p.getRaceRightGuess();
                    if (p.getSprintRightGuess() > maxSprintRG)
                        maxSprintRG = p.getSprintRightGuess();
                }
                racePreds.put(u.getId(), p);
            }

            // B) Taban puanları ve varsa Bonusları ekle
            for (User u : userList) {
                Prediction p = racePreds.get(u.getId());
                if (p != null && !p.isDidNotAttend()) {
                    int currentPts = userPointsMap.get(u.getId());
                    currentPts += p.getPointsEarned(); // Taban Puan (Podyum +3 vs. dahil)

                    // En çok bilene ekstra bonus
                    if (p.getRaceRightGuess() == maxRaceRG && maxRaceRG > 0) {
                        currentPts += maxRaceRG;
                    }
                    if (race.hasSprint() && p.getSprintRightGuess() == maxSprintRG && maxSprintRG > 0) {
                        currentPts += maxSprintRG;
                    }

                    userPointsMap.put(u.getId(), currentPts);
                }
            }
        }

        // 3. Hesaplanan puanları User nesnelerine yazıp listeyi sırala
        for (User u : userList) {
            u.setPoints(userPointsMap.get(u.getId()));
        }

        // Puana göre büyükten küçüğe sırala
        userList.sort((u1, u2) -> Integer.compare(u2.getPoints(), u1.getPoints()));

        return userList;
    }
}