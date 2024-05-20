import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Playlists {
    public void createTable() {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     """
                     CREATE TABLE IF NOT EXISTS Playlists (
                         playlist_id INT AUTO_INCREMENT PRIMARY KEY,
                         user_id INT NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         UNIQUE(name, user_id),
                         FOREIGN KEY (user_id) REFERENCES Users(user_id));
                     """)) {
            stmt.executeUpdate();
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    public void insertPlaylist(int userId, String name) {
        if (!playlistExists(userId, name)) {
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO Playlists(user_id, name) VALUES (?, ?)")) {
                stmt.setInt(1, userId);
                stmt.setString(2, name);
                stmt.executeUpdate();
                System.out.println("Playlist " + name + " created successfully.");
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        } else {
            System.out.println("You already have a playlist named " + name);
        }
    }
    private boolean playlistExists(int userId, String name) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT playlist_id FROM Playlists WHERE user_id = ? AND name = ?")) {
            stmt.setInt(1, userId);
            stmt.setString(2, name);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }
    public static void main(String[] args) {
        Playlists playlists = new Playlists();
        playlists.createTable();
        playlists.insertPlaylist(1, "Favs");
        playlists.insertPlaylist(2, "Workout");
        playlists.insertPlaylist(3, "Night Drive");
        playlists.insertPlaylist(4, "All Tears");
        playlists.insertPlaylist(5, "Study");
    }
}
