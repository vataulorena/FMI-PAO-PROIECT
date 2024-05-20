import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuditLog {
    public void createTable() {
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

    public void logCommand(int userId, String command, boolean success) {
        String sql = "INSERT INTO AuditLog (user_id, command, success) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, command);
            stmt.setBoolean(3, success);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    public List<String> getUserAuditLogs(String username, int page, int itemsPerPage) {
        List<String> logs = new ArrayList<>();
        String sql ="SELECT Users.username, AuditLog.command, AuditLog.success, AuditLog.timestamp " +
                    "FROM AuditLog JOIN Users ON AuditLog.user_id = Users.user_id " +
                    "WHERE Users.username = ? " +
                    "ORDER BY AuditLog.timestamp DESC " +
                    "LIMIT ? OFFSET ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setInt(2, itemsPerPage);
            stmt.setInt(3, (page - 1) * itemsPerPage);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                logs.add(String.format("%s - %s - %s - %s",
                        rs.getString("username"),
                        rs.getString("command"),
                        rs.getBoolean("success") ? "SUCCESS" : "FAILURE",
                        rs.getTimestamp("timestamp")));
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return logs;
    }
    public static void main(String[] args) {
        AuditLog auditLog = new AuditLog();
        auditLog.createTable();
    }
}
