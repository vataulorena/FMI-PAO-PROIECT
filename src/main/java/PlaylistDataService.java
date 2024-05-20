import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDataService {
    public List<String[]> getPlaylistSongs(String identifier, boolean isByName, int userId) throws SQLException {
        List<String[]> songs = new ArrayList<>();
        String sql = isByName ?
                "SELECT Songs.title, Songs.artist_name, Songs.release_year FROM Songs " +
                        "JOIN PlaylistSongs ON Songs.song_id = PlaylistSongs.song_id " +
                        "JOIN Playlists ON PlaylistSongs.playlist_id = Playlists.playlist_id " +
                        "WHERE Playlists.name = ? AND Playlists.user_id = ?" :
                "SELECT Songs.title, Songs.artist_name, Songs.release_year FROM Songs " +
                        "JOIN PlaylistSongs ON Songs.song_id = PlaylistSongs.song_id " +
                        "JOIN Playlists ON PlaylistSongs.playlist_id = Playlists.playlist_id " +
                        "WHERE Playlists.playlist_id = ? AND Playlists.user_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, identifier);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    songs.add(new String[] { rs.getString("title"), rs.getString("artist_name"), String.valueOf(rs.getInt("release_year")) });
                }
            }
        }
        return songs;
    }
}
