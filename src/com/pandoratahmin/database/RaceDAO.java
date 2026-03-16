package com.pandoratahmin.database;

import com.pandoratahmin.model.Race;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RaceDAO {

    public void addRace(String raceName, boolean hasSprint) {
        String sql = "INSERT INTO races (race_name, has_sprint, is_completed) VALUES (?, ?, 0)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, raceName);
            pstmt.setBoolean(2, hasSprint);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Yarış eklenemedi: " + e.getMessage());
        }
    }

    public void deleteRace(int id) {
        String sql = "DELETE FROM races WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Yarış silinemedi: " + e.getMessage());
        }
    }

    public void updateRaceResults(Race race) {
        String sql = "UPDATE races SET is_completed = ?, quali_result = ?, race_result = ?, " +
                     "sprint_q_result = ?, sprint_r_result = ?, dnfs = ?, sprint_dnfs = ? " +
                     "WHERE id = ?";
                     
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, race.isCompleted());
            pstmt.setString(2, joinArray(race.getQualiResult()));
            pstmt.setString(3, joinArray(race.getRaceResult()));
            pstmt.setString(4, joinArray(race.getSprintQualiResult()));
            pstmt.setString(5, joinArray(race.getSprintResult()));
            pstmt.setString(6, joinArray(race.getDnfs()));
            pstmt.setString(7, joinArray(race.getSprintDnfs()));
            pstmt.setInt(8, race.getId());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Yarış sonuçları güncellenemedi: " + e.getMessage());
        }
    }

    public List<Race> getAllRaces() {
        List<Race> raceList = new ArrayList<>();
        String sql = "SELECT * FROM races";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Race race = new Race(rs.getInt("id"), rs.getString("race_name"), rs.getBoolean("has_sprint"));
                race.setCompleted(rs.getBoolean("is_completed"));
                
                race.setQualiResult(splitString(rs.getString("quali_result")));
                race.setRaceResult(splitString(rs.getString("race_result")));
                race.setSprintQualiResult(splitString(rs.getString("sprint_q_result")));
                race.setSprintResult(splitString(rs.getString("sprint_r_result")));
                race.setDnfs(splitString(rs.getString("dnfs")));
                race.setSprintDnfs(splitString(rs.getString("sprint_dnfs")));
                
                raceList.add(race);
            }
            
        } catch (SQLException e) {
            System.out.println("Yarışlar çekilemedi: " + e.getMessage());
        }
        return raceList;
    }

    // --- YARDIMCI METODLAR ---
    
    // String[] dizisini "VER,HAM,LEC" formatına çevirir
    private String joinArray(String[] array) {
        if (array == null || array.length == 0) return null;
        return String.join(",", array);
    }

    // "VER,HAM,LEC" metnini String[] dizisine çevirir
    private String[] splitString(String str) {
        if (str == null || str.trim().isEmpty()) return null;
        return str.split(",");
    }
}