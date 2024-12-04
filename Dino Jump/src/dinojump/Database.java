package dinojump;
import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dino_jump";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    // Method untuk mendapatkan koneksi ke database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    // Method untuk memverifikasi login
    public static boolean login(String username, String password) {
        boolean success = false;
        String sql = "SELECT * FROM profile WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                success = true;  // Login berhasil
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return success;
    }

    // Method untuk mendaftarkan user baru
    public static boolean register(String username, String password) {
        boolean success = false;
        String sql = "INSERT INTO profile (username, password, best_score, karakter) VALUES (?, ?, ?,?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, 0);
            stmt.setString(4, "dino_kuning");

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                success = true; // Registrasi berhasil
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return success;
    }
    public static boolean isUsernameTaken(String username) {
        boolean exists = false;
        String sql = "SELECT * FROM profile WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                exists = true;
            }
    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return exists;
    }
    
}
