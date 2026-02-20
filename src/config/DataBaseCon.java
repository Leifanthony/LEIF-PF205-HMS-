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
    public static Connection connectDB() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(DB_URL);

        // WAL mode and busy timeout
        try (Statement stmt = con.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL");  // allows concurrent reads/writes
            stmt.execute("PRAGMA busy_timeout=5000"); // wait up to 5s if DB locked
        }

        // Ensure users table exists
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
    }

    // =========================
    // INSERT / UPDATE RECORD (Transaction Safe)
    // =========================
    public static boolean addRecord(String sql, Object... values) {
        try (Connection conn = connectDB()) {
            conn.setAutoCommit(false); // start transaction
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < values.length; i++) {
                    pstmt.setObject(i + 1, values[i]);
                }
                int rows = pstmt.executeUpdate();
                conn.commit();
                return rows > 0;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error adding record (rollback): " + e.getMessage());
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error adding record: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // LOGIN AUTHENTICATION
    // =========================
    public static boolean authenticate(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Login Error: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // DISPLAY DATA TO JTable (Safe, CachedRowSet)
    // =========================
    public static void displayData(String sql, JTable table) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Use CachedRowSet to disconnect ResultSet from DB
            CachedRowSet crs = new CachedRowSetImpl();
            crs.populate(rs);
            table.setModel(DbUtils.resultSetToTableModel(crs));

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error displaying data: " + e.getMessage());
        }
    }

    // =========================
    // FETCH SINGLE VALUE
    // =========================
    public static Object getSingleValue(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject(1);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error fetching value: " + e.getMessage());
        }
        return null;
    }

    // =========================
    // FETCH USER AS OBJECT
    // =========================
    public static User getUser(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("type"),
                            rs.getString("status"),
                            rs.getBytes("idpicture")
                    );
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }
        return null;
    }

    // =========================
    // USER OBJECT
    // =========================
    public static class User {
        public int id;
        public String firstName, lastName, email, password, type, status;
        public byte[] idPicture;

        public User(int id, String firstName, String lastName, String email, String password,
                    String type, String status, byte[] idPicture) {
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