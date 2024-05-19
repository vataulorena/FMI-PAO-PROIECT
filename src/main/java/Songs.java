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
                         artist_id INT NOT NULL,
                         release_year INT,
                         FOREIGN KEY (artist_id) REFERENCES Artists(artist_id));
                     """)) {
            stmt.executeUpdate();
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    private void insertSong(String title, int artistId, int releaseYear) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Songs(title, artist_id, release_year) VALUES (?, ?, ?)")) {
            stmt.setString(1, title);
            stmt.setInt(2, artistId);
            stmt.setInt(3, releaseYear);
            stmt.executeUpdate();
            System.out.println("Song " + title + " inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Songs songs = new Songs();
        songs.createTable();
        songs.insertSong("Beggin'", 1, 2021); // Assuming artist_id 1 is Maneskin
        songs.insertSong("Lose You to Love Me", 2, 2019); // Assuming artist_id 2 is Selena Gomez
        songs.insertSong("Diamonds", 3, 2012); // Assuming artist_id 3 is Rihanna
        songs.insertSong("Lover", 4, 2019); // Assuming artist_id 4 is Taylor Swift
        songs.insertSong("Treat You Better", 5, 2020); // Assuming artist_id 5 is Shawn Mendes
    }
}
