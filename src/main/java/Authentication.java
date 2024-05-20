import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication extends DatabaseManager implements IAuthentication {
    private static int currentUserId = -1;
    private static String currentUsername = "";
    private static boolean isAdmin = false;

    @Override
    public int getCurrentUserId() {
        return currentUserId;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public boolean login(String username, String password) throws SQLException {
        String sql = "SELECT user_id, password_hash, is_admin FROM Users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPasswordHash = rs.getString("password_hash");
                if (storedPasswordHash.equals(password)) {
                    currentUserId = rs.getInt("user_id");
                    currentUsername = username;
                    isAdmin = rs.getBoolean("is_admin");
                    System.out.println("You are now authenticated as " + username);
                    return true;
                }
            }
            System.out.println("Username or password is invalid. Please try again!");
            closeResources(conn, stmt, rs);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            throw e;
        }
        return false;
    }

    @Override
    public boolean register(String username, String password) throws SQLException {
        if (userExists(username)) {
            System.out.println("User with given username already exists! Please try again!");
            return false;
        }
        String sql = "INSERT INTO Users(username, password_hash, is_admin) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setBoolean(3, isFirstUser());
            stmt.executeUpdate();
            System.out.println("Registered account with user name " + username);
            return login(username, password);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            throw e;
        }
    }

    private boolean userExists(String username) throws SQLException {
        String sql = "SELECT user_id FROM Users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next();
            closeResources(conn, stmt, rs);
            return exists;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            throw e;
        }
    }

    private boolean isFirstUser() throws SQLException {
        String sql = "SELECT COUNT(user_id) AS count FROM Users";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean isFirst = rs.getInt("count") == 0;
                closeResources(conn, stmt, rs);
                return isFirst;
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            throw e;
        }
        return false;
    }

    @Override
    public void logout() {
        currentUserId = -1;
        currentUsername = "";
        isAdmin = false;
        System.out.println("Successfully logged out.");
    }

    @Override
    public boolean promote(String username) throws SQLException {
        if (!isAdmin) {
            System.out.println("Only administrators can perform this action.");
            return false;
        }

        String sql = "UPDATE Users SET is_admin = TRUE WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            int affectedRows = stmt.executeUpdate();
            closeResources(conn, stmt, null);
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean isLoggedInAsAdmin() {
        return isAdmin && currentUserId != -1;
    }

    @Override
    public boolean isLoggedIn() {
        return currentUserId != -1;
    }
}
