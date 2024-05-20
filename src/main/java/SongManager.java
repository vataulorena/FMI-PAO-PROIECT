import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SongManager {
    public void createSong(String title, String artist, int releaseYear) {
        if (songExists(title, artist)) {
            System.out.println("This song is already part of the library!");
            return;
        }

        String sql = "INSERT INTO Songs (title, artist_name, release_year) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, artist);
            stmt.setInt(3, releaseYear);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Added " + title + " by " + artist + " to the library.");
            } else {
                System.out.println("Failed to add the song to the library.");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private boolean songExists(String title, String artist) {
        String sql = "SELECT song_id FROM Songs WHERE title = ? AND artist_name = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, artist);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }
}
