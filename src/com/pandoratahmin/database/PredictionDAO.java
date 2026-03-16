package com.pandoratahmin.database;

import com.pandoratahmin.model.Prediction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PredictionDAO {

    // Tahmini veritabanına kaydeder veya günceller
    public void savePrediction(Prediction pred) {
        Prediction existing = getPrediction(pred.getUserId(), pred.getRaceId());

        if (existing == null) {
            String sql = "INSERT INTO predictions (user_id, race_id, did_not_attend, quali_pred, race_pred, sprint_q_pred, sprint_r_pred, points_earned) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            executeSaveOrUpdate(sql, pred, false);
        } else {
            String sql = "UPDATE predictions SET did_not_attend = ?, quali_pred = ?, race_pred = ?, sprint_q_pred = ?, sprint_r_pred = ?, points_earned = ? " +
                         "WHERE user_id = ? AND race_id = ?";
            executeSaveOrUpdate(sql, pred, true);
        }
    }

    // Belirli bir kullanıcı ve yarış için yapılan tahmini çeker
    public Prediction getPrediction(int userId, int raceId) {
        String sql = "SELECT * FROM predictions WHERE user_id = ? AND race_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, raceId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Prediction pred = new Prediction(userId, raceId);
                pred.setId(rs.getInt("id"));
                pred.setDidNotAttend(rs.getBoolean("did_not_attend"));
                
                pred.setQualiPred(splitString(rs.getString("quali_pred")));
                pred.setRacePred(splitString(rs.getString("race_pred")));
                pred.setSprintQPred(splitString(rs.getString("sprint_q_pred")));
                pred.setSprintRPred(splitString(rs.getString("sprint_r_pred")));
                pred.setPointsEarned(rs.getInt("points_earned"));
                
                return pred;
            }
            
        } catch (SQLException e) {
            System.out.println("Tahmin çekilemedi: " + e.getMessage());
        }
        return null;
    }

    // --- YARDIMCI METODLAR ---

    // INSERT veya UPDATE sorgusunu çalıştıran ortak metod
    private void executeSaveOrUpdate(String sql, Prediction pred, boolean isUpdate) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (!isUpdate) { // INSERT
                pstmt.setInt(1, pred.getUserId());
                pstmt.setInt(2, pred.getRaceId());
                pstmt.setBoolean(3, pred.isDidNotAttend());
                pstmt.setString(4, joinArray(pred.getQualiPred()));
                pstmt.setString(5, joinArray(pred.getRacePred()));
                pstmt.setString(6, joinArray(pred.getSprintQPred()));
                pstmt.setString(7, joinArray(pred.getSprintRPred()));
                pstmt.setInt(8, pred.getPointsEarned());
            } else { // UPDATE
                pstmt.setBoolean(1, pred.isDidNotAttend());
                pstmt.setString(2, joinArray(pred.getQualiPred()));
                pstmt.setString(3, joinArray(pred.getRacePred()));
                pstmt.setString(4, joinArray(pred.getSprintQPred()));
                pstmt.setString(5, joinArray(pred.getSprintRPred()));
                pstmt.setInt(6, pred.getPointsEarned());
                pstmt.setInt(7, pred.getUserId());
                pstmt.setInt(8, pred.getRaceId());
            }
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Tahmin kaydedilemedi/güncellenemedi: " + e.getMessage());
        }
    }

    private String joinArray(String[] array) {
        if (array == null || array.length == 0) return null;
        return String.join(",", array);
    }

    private String[] splitString(String str) {
        if (str == null || str.trim().isEmpty()) return null;
        return str.split(",");
    }
}