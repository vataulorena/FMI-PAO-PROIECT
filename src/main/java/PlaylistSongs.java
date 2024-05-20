import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlaylistSongs {
    public static void createTable() {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     """
                     CREATE TABLE IF NOT EXISTS PlaylistSongs (
                         playlist_id INT,
                         song_id INT,
                         PRIMARY KEY (playlist_id, song_id),
                         FOREIGN KEY (playlist_id) REFERENCES Playlists(playlist_id),
                         FOREIGN KEY (song_id) REFERENCES Songs(song_id));
                     """)) {
            stmt.executeUpdate();
            System.out.println("PlaylistSongs table created successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        PlaylistSongs playlistsongs = new PlaylistSongs();
        playlistsongs.createTable();
    }
}
