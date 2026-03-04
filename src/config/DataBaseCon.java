package config;

import java.sql.*;
import javax.swing.JTable;
import javax.sql.rowset.CachedRowSet;
import com.sun.rowset.CachedRowSetImpl;
import net.proteanit.sql.DbUtils;

public class DataBaseCon {

    private static final String DB_URL = "jdbc:sqlite:HMS.db";

    // =========================
    // DATABASE CONNECTION
    // =========================
    public static Connection connectDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection(DB_URL);

            // PRAGMA settings
            try (Statement stmt = con.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL");
                stmt.execute("PRAGMA busy_timeout=5000");
            }

            // Create users table if it doesn't exist
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
            try (Statement stmt = con.createStatement()) {
                stmt.execute(createUsers);
            }

            return con;

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            return null;
        }
    }

    // =========================
    // PREPARE STATEMENT
    // =========================
    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        return connectDB().prepareStatement(sql);
    }

    // =========================
    // INSERT RECORD
    // =========================
    public static boolean addRecord(String sql, Object... values) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connectDB();
            if (conn == null) return false;

            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            int rows = pstmt.executeUpdate();
            conn.commit();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Add record error: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.out.println("Rollback failed: " + ex.getMessage()); }
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Closing connection failed: " + e.getMessage());
            }
        }
    }

    // =========================
    // UPDATE RECORD
    // =========================
    public static boolean updateRecord(String sql, Object... values) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connectDB();
            if (conn == null) return false;

            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            int rows = pstmt.executeUpdate();
            conn.commit();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Update record error: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.out.println("Rollback failed: " + ex.getMessage()); }
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Closing connection failed: " + e.getMessage());
            }
        }
    }

    // =========================
    // DELETE RECORD
    // =========================
    public static boolean deleteData(String table, String column, Object value) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM " + table + " WHERE " + column + " = ?";
        try {
            conn = connectDB();
            if (conn == null) return false;

            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, value);

            int rows = pstmt.executeUpdate();
            conn.commit();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Delete record error: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.out.println("Rollback failed: " + ex.getMessage()); }
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Closing connection failed: " + e.getMessage());
            }
        }
    }

    // =========================
    // LOGIN AUTHENTICATION
    // =========================
    public static boolean authenticate(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // DISPLAY DATA TO JTable
    // =========================
    public static void displayData(String sql, JTable table) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (conn == null) return;

            CachedRowSet crs = new CachedRowSetImpl();
            crs.populate(rs);
            table.setModel(DbUtils.resultSetToTableModel(crs));

        } catch (SQLException e) {
            System.out.println("Display data error: " + e.getMessage());
        }
    }

    // =========================
    // FETCH SINGLE VALUE
    // =========================
    public static Object getSingleValue(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return null;

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getObject(1);
            }

        } catch (SQLException e) {
            System.out.println("Get single value error: " + e.getMessage());
        }
        return null;
    }

    // =========================
    // GET DATA (CachedRowSet)
    // =========================
    public static CachedRowSet getData(String sql) {
        try (Connection conn = connectDB();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (conn == null) return null;

            CachedRowSet crs = new CachedRowSetImpl();
            crs.populate(rs);
            return crs;

        } catch (SQLException e) {
            System.out.println("Get data error: " + e.getMessage());
            return null;
        }
    }

    // =========================
    // GET USER BY SQL
    // =========================
    public static User getUser(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            if (conn == null) return null;

            for (int i = 0; i < values.length; i++) {
                pst.setObject(i + 1, values[i]);
            }

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    byte[] picture = rs.getBytes("idpicture");
                    return new User(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("type"),
                            rs.getString("status"),
                            picture
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Get user error: " + e.getMessage());
        }
        return null;
    }

    // =========================
    // USER MODEL
    // =========================
    public static class User {
        public int id;
        public String firstName, lastName, email, password, type, status;
        public byte[] idPicture;

        public User(int id, String firstName, String lastName,
                    String email, String password,
                    String type, String status,
                    byte[] idPicture) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.type = type;
            this.status = status;
            this.idPicture = idPicture;
        }
    }
}