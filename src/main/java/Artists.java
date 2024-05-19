import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Artists {
    private void createTable() {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     """
                     CREATE TABLE IF NOT EXISTS Artists (
                         artist_id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) UNIQUE NOT NULL,
                         genre VARCHAR(255) NOT NULL);
                     """)) {
            stmt.executeUpdate();
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    private void insertArtist(String name, String genre) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Artists(name, genre) VALUES (?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, genre);
            stmt.executeUpdate();
            System.out.println("Artist " + name + " inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Artists artists = new Artists();
        artists.createTable();
        artists.insertArtist("Maneskin", "Rock");
        artists.insertArtist("Selena Gomez", "Pop");
        artists.insertArtist("Rihanna", "Pop");
        artists.insertArtist("Taylor Swift", "Pop");
        artists.insertArtist("Shawn Mendes", "Pop");
    }
}
