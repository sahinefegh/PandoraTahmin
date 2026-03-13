package com.pandoratahmin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
	private static final String URL = "jdbc:sqlite:pandoratahmin.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL UNIQUE,"
                + "team TEXT,"
                + "total_points INTEGER DEFAULT 0"
                + ");";

        String createRacesTable = "CREATE TABLE IF NOT EXISTS races ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "race_name TEXT NOT NULL,"
                + "has_sprint BOOLEAN DEFAULT 0,"
                + "is_completed BOOLEAN DEFAULT 0,"
                + "quali_result TEXT,"
                + "race_result TEXT,"
                + "sprint_q_result TEXT,"
                + "sprint_r_result TEXT,"
                + "fastest_lap TEXT,"
                + "dnfs TEXT,"
                + "sprint_dnfs TEXT"
                + ");";

        String createPredictionsTable = "CREATE TABLE IF NOT EXISTS predictions ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER,"
                + "race_id INTEGER,"
                + "did_not_attend BOOLEAN DEFAULT 0,"
                + "quali_pred TEXT,"
                + "race_pred TEXT,"
                + "sprint_q_pred TEXT,"
                + "sprint_r_pred TEXT,"
                + "fastest_lap_pred TEXT,"
                + "points_earned INTEGER DEFAULT 0,"
                + "FOREIGN KEY(user_id) REFERENCES users(id),"
                + "FOREIGN KEY(race_id) REFERENCES races(id)"
                + ");";
        
        String createTeamsTable = "CREATE TABLE IF NOT EXISTS teams ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL UNIQUE,"
                + "color_hex TEXT NOT NULL"
                + ");";
        
        String createDriversTable = "CREATE TABLE IF NOT EXISTS valid_drivers ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "abbreviation TEXT NOT NULL UNIQUE"
                + ");";

        String createSettingsTable = "CREATE TABLE IF NOT EXISTS settings ("
                + "setting_key TEXT PRIMARY KEY,"
                + "setting_value TEXT"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createUsersTable);
            stmt.execute(createRacesTable);
            stmt.execute(createPredictionsTable);
            stmt.execute(createTeamsTable);
            stmt.execute(createDriversTable);
            stmt.execute(createSettingsTable);
            System.out.println("Veritabanı tabloları kontrol edildi/oluşturuldu.");
            
        } catch (SQLException e) {
            System.out.println("Tablo oluşturma hatası: " + e.getMessage());
        }
    }
}