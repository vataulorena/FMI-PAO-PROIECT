import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PlaylistManager {
    public void addSongToPlaylistByName(String playlistName, int userId, List<Integer> songIds) {
        int playlistId = findPlaylistIdByNameAndUserId(playlistName, userId);
        if (playlistId == -1) {
            System.out.println("The desired playlist does not exist.");
            return;
        }
        addSongsToPlaylist(playlistId, songIds);
    }
    public void addSongToPlaylistById(int playlistId, List<Integer> songIds) {
        if (!playlistExistsById(playlistId)) {
            System.out.println("The desired playlist does not exist.");
            return;
        }
        addSongsToPlaylist(playlistId, songIds);
    }
    private int findPlaylistIdByNameAndUserId(String name, int userId) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT playlist_id FROM Playlists WHERE name = ? AND user_id = ?")) {
            stmt.setString(1, name);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("playlist_id");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return -1;
    }
    private boolean playlistExistsById(int playlistId) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT 1 FROM Playlists WHERE playlist_id = ?")) {
            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }

    private void addSongsToPlaylist(int playlistId, List<Integer> songIds) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT IGNORE INTO PlaylistSongs(playlist_id, song_id) VALUES (?, ?)")) {
            for (int songId : songIds) {
                stmt.setInt(1, playlistId);
                stmt.setInt(2, songId);
                stmt.executeUpdate();
            }
            System.out.println("Songs added to the playlist successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
