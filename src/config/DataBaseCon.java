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

        try (Statement stmt = con.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL");
            stmt.execute("PRAGMA busy_timeout=5000");
        }

        // Create users table if not exists
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
    // PREPARE STATEMENT
    // =========================
    public static PreparedStatement prepareStatement(String sql)
            throws SQLException, ClassNotFoundException {
        Connection conn = connectDB();
        return conn.prepareStatement(sql);
    }

    // =========================
    // INSERT RECORD
    // =========================
    public static boolean addRecord(String sql, Object... values) {
        try (Connection conn = connectDB()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < values.length; i++) {
                    pstmt.setObject(i + 1, values[i]);
                }

                int rows = pstmt.executeUpdate();
                conn.commit();
                return rows > 0;

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transaction rolled back: " + e.getMessage());
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error adding record: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // 🔥 UPDATE RECORD (NEW)
    // =========================
    public static boolean updateRecord(String sql, Object... values) {
        try (Connection conn = connectDB()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                for (int i = 0; i < values.length; i++) {
                    pstmt.setObject(i + 1, values[i]);
                }

                int rows = pstmt.executeUpdate();
                conn.commit();
                return rows > 0;

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Update rolled back: " + e.getMessage());
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Update Error: " + e.getMessage());
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
    // DISPLAY DATA TO JTable
    // =========================
    public static void displayData(String sql, JTable table) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

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
    // GET DATA (Safe CachedRowSet)
    // =========================
    public CachedRowSet getData(String sql) throws SQLException {
        try (Connection conn = connectDB();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            CachedRowSet crs = new CachedRowSetImpl();
            crs.populate(rs);
            return crs;

        } catch (Exception e) {
            throw new SQLException("Error fetching data: " + e.getMessage());
        }
    }

    // =========================
    // DELETE DATA
    // =========================
    public void deleteData(int id, String table, String column) {
        String sql = "DELETE FROM " + table + " WHERE " + column + " = ?";
        try (Connection conn = connectDB();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Record deleted successfully!");

        } catch (Exception e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }

    // =========================
    // GET USER BY SQL
    // =========================
    public User getUser(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pst = conn.prepareStatement(sql)) {

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

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error fetching user: " + e.getMessage());
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