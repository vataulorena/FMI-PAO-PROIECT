import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Users {

    private void createTable() {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     """
                     CREATE TABLE IF NOT EXISTS Users (
                         user_id INT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(255) UNIQUE NOT NULL,
                         password_hash VARCHAR(255) NOT NULL,
                         is_admin BOOLEAN NOT NULL);
                     """)) {
            stmt.executeUpdate();
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private void insertUser(String username, String passwordHash, boolean isAdmin) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Users(username, password_hash, is_admin) VALUES (?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            stmt.setBoolean(3, isAdmin);
            stmt.executeUpdate();
            System.out.println("User " + username + " inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Users users = new Users();
        users.createTable();
        users.insertUser("alice123", "hash_alice_password", false);
        users.insertUser("bob456", "hash_bob_password", false);
        users.insertUser("charlie789", "hash_charlie_password", true);
        users.insertUser("david012", "hash_david_password", false);
        users.insertUser("eve345", "hash_eve_password", false);
    }
}
