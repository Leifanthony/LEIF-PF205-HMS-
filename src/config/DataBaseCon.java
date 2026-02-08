package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.proteanit.sql.DbUtils;

public class DataBaseCon {

    // =========================
    // DATABASE CONNECTION
    // =========================
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:HMS.db");
            System.out.println("Connection Successful");

            // Create users table (with idpicture BLOB)
            String createUsers = "CREATE TABLE IF NOT EXISTS tbl_users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "first_name TEXT NOT NULL,"
                    + "last_name TEXT NOT NULL,"
                    + "email TEXT NOT NULL UNIQUE,"
                    + "password TEXT NOT NULL,"
                    + "type TEXT NOT NULL,"
                    + "status TEXT NOT NULL,"
                    + "idpicture BLOB"
                    + ")";
            con.createStatement().execute(createUsers);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed: " + e.getMessage());
        }
        return con;
    }

    // =========================
    // INSERT / UPDATE RECORD
    // =========================
    public void addRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Record added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

    // =========================
    // LOGIN AUTHENTICATION
    // =========================
    public boolean authenticate(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return false;
    }

    // =========================
    // DISPLAY DATA TO JTable
    // =========================
    public void displayData(String sql, javax.swing.JTable table) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            table.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            System.out.println("Error displaying data: " + e.getMessage());
        }
    }

    // =========================
    // FETCH SINGLE USER / DATA
    // =========================
    public ResultSet getUser(String sql, Object... values) {
        try {
            Connection conn = connectDB();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof String) {
                    pstmt.setString(i + 1, values[i].toString().trim());
                } else {
                    pstmt.setObject(i + 1, values[i]);
                }
            }
            return pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }
        return null;
    }
}
