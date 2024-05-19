import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Sessions {
    private void createTable() {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     """
                     CREATE TABLE IF NOT EXISTS Sessions (
                         session_id INT AUTO_INCREMENT PRIMARY KEY,
                         user_id INT NOT NULL,
                         token VARCHAR(255) UNIQUE NOT NULL,
                         expires_at DATETIME NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES Users(user_id));
                     """)) {
            stmt.executeUpdate();
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        Sessions sessions = new Sessions();
        sessions.createTable();
    }
}
