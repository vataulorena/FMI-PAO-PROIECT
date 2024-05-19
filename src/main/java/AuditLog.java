import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditLog {
    private void createTable() {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     """
                     CREATE TABLE IF NOT EXISTS AuditLog (
                         log_id INT AUTO_INCREMENT PRIMARY KEY,
                         user_id INT NOT NULL,
                         command TEXT NOT NULL,
                         success BOOLEAN NOT NULL,
                         timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES Users(user_id));
                     """)) {
            stmt.executeUpdate();
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        AuditLog auditLog = new AuditLog();
        auditLog.createTable();
    }
}
