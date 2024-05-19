import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Authentication {
    public boolean login(String username, String password) {
        String sql = "SELECT password_hash FROM Users WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPasswordHash = rs.getString("password_hash");
                if (storedPasswordHash.equals(password)) {
                    System.out.println("You are now authenticated as " + username);
                    return true;
                }
            }
            System.out.println("Username or password is invalid. Please try again!");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return false;
    }
    public boolean register(String username, String password) {
        if (userExists(username)) {
            System.out.println("User with given username already exists! Please try again!");
            return false;
        }
        String sql = "INSERT INTO Users(username, password_hash, is_admin) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setBoolean(3, isFirstUser());
            stmt.executeUpdate();
            System.out.println("Registered account with user name " + username);
            return login(username, password);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return false;
    }
    private boolean userExists(String username) {
        String sql = "SELECT user_id FROM Users WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }
    private boolean isFirstUser() {
        String sql = "SELECT COUNT(user_id) AS count FROM Users";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") == 0;
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return false;
    }
    public void logout() {
        System.out.println("Successfully logged out.");
    }
}
