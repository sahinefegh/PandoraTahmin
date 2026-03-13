package com.pandoratahmin.database;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SettingsDAO {

    // --- 1. PUANLAMA SİSTEMİ İŞLEMLERİ ---

    public void saveSetting(String key, String value) {
        String sql = "INSERT OR REPLACE INTO settings (setting_key, setting_value) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ayar kaydedilemedi (" + key + "): " + e.getMessage());
        }
    }

    public int getSettingAsInt(String key, int defaultValue) {
        String sql = "SELECT setting_value FROM settings WHERE setting_key = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Integer.parseInt(rs.getString("setting_value"));
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Ayar okunamadı (" + key + "): " + e.getMessage());
        }
        return defaultValue;
    }

    // --- 2. TAKIM RENKLERİ İŞLEMLERİ ---

    public void saveTeamColor(String teamName, Color color) {
        String hexColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        String sql = "INSERT OR REPLACE INTO teams (name, color_hex) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, teamName);
            pstmt.setString(2, hexColor);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Takım rengi kaydedilemedi (" + teamName + "): " + e.getMessage());
        }
    }

    public Color getTeamColor(String teamName, Color defaultColor) {
        String sql = "SELECT color_hex FROM teams WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, teamName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Color.decode(rs.getString("color_hex"));
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Takım rengi okunamadı (" + teamName + "): " + e.getMessage());
        }
        return defaultColor;
    }

    public java.util.Map<String, Color> getAllTeams() {
        java.util.Map<String, Color> teams = new java.util.LinkedHashMap<>();
        String sql = "SELECT name, color_hex FROM teams ORDER BY name ASC";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                teams.put(rs.getString("name"), Color.decode(rs.getString("color_hex")));
            }
        } catch (SQLException e) {
            System.out.println("Takımlar çekilemedi: " + e.getMessage());
        }
        return teams;
    }

    public void deleteTeam(String teamName) {
        String sql = "DELETE FROM teams WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, teamName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Takım silinemedi: " + e.getMessage());
        }
    }

    public void updateTeam(String oldName, String newName, Color newColor) {
        String hexColor = String.format("#%02x%02x%02x", newColor.getRed(), newColor.getGreen(), newColor.getBlue());
        String sql = "UPDATE teams SET name = ?, color_hex = ? WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, hexColor);
            pstmt.setString(3, oldName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Takım güncellenemedi: " + e.getMessage());
        }
    }

    // --- 3. GEÇERLİ PİLOTLAR İŞLEMLERİ ---

    public void saveValidDrivers(String[] drivers) {
        String deleteSql = "DELETE FROM valid_drivers";
        String insertSql = "INSERT INTO valid_drivers (abbreviation) VALUES (?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            deleteStmt.executeUpdate();

            for (String driver : drivers) {
                if (driver != null && !driver.trim().isEmpty()) {
                    insertStmt.setString(1, driver.trim().toUpperCase());
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Pilotlar kaydedilemedi: " + e.getMessage());
        }
    }

    public String[] getValidDrivers() {
        String sql = "SELECT abbreviation FROM valid_drivers";
        List<String> driversList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                driversList.add(rs.getString("abbreviation"));
            }
        } catch (SQLException e) {
            System.out.println("Pilotlar okunamadı: " + e.getMessage());
        }
        return driversList.toArray(new String[0]);
    }
}