import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Songs {
    private void createTable() {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     """
                     CREATE TABLE IF NOT EXISTS Songs (
                         song_id INT AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(255) NOT NULL,
                         artist_name VARCHAR(255) NOT NULL,
                         release_year INT);
                     """)) {
            stmt.executeUpdate();
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private void insertSong(String title, String artistName, int releaseYear) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Songs(title, artist_name, release_year) VALUES (?, ?, ?)")) {
            stmt.setString(1, title);
            stmt.setString(2, artistName);
            stmt.setInt(3, releaseYear);
            stmt.executeUpdate();
            System.out.println("Song " + title + " by " + artistName + " added to the library.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Songs songs = new Songs();
        songs.createTable();
        songs.insertSong("Beggin'", "Maneskin", 2021);
        songs.insertSong("Lose You to Love Me", "Selena Gomez", 2019);
        songs.insertSong("Diamonds", "Rihanna", 2012);
        songs.insertSong("Lover", "Taylor Swift", 2019);
        songs.insertSong("Treat You Better", "Shawn Mendes", 2020);
    }
}
