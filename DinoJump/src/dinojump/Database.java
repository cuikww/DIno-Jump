package dinojump;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "INSERT INTO profile (username, password, best_score) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, 0);  // Set skor awal ke 0

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                success = true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return success;
    }

    // Cek apakah username sudah ada
    public static boolean isUsernameTaken(String username) {
        boolean exists = false;
        String sql = "SELECT * FROM profile WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                exists = true; // Username sudah ada
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return exists;
    }
    // Method untuk mendapatkan best score dari database berdasarkan username
    public static int getBestScore(String username) {
        int bestScore = 0;
        String sql = "SELECT best_score FROM profile WHERE username = ?";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                bestScore = rs.getInt("best_score");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return bestScore;
    }

    // Method untuk memperbarui best score
    public static boolean updateBestScore(String username, int newBestScore) {
        boolean success = false;
        String sql = "UPDATE profile SET best_score = ? WHERE username = ?";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newBestScore);
            stmt.setString(2, username);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                success = true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return success;
    }
     // Method untuk mendapatkan Top 3 Best Scores
    public static List<String> getTop3BestScores() {
        List<String> topScores = new ArrayList<>();
        String sql = "SELECT username, best_score FROM profile ORDER BY best_score DESC LIMIT 3";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                int bestScore = rs.getInt("best_score");
                topScores.add(username +" - " + bestScore);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return topScores;
    }

}
