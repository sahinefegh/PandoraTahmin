package com.pandoratahmin.database;

import com.pandoratahmin.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "SELECT id, name, team, total_points FROM users ORDER BY total_points DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String team = rs.getString("team");
                int totalPoints = rs.getInt("total_points");
                
                User user = new User(id, name, team);
                user.setPoints(totalPoints);
                userList.add(user);
            }
            
        } catch (SQLException e) {
            System.out.println("Kullanıcı listesi çekilemedi: " + e.getMessage());
        }
        return userList;
    }
}