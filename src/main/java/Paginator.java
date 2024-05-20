import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Paginator {
    public void printPage(int userId, int page, int itemsPerPage) {
        int offset = (page - 1) * itemsPerPage;
        List<String> results = new ArrayList<>();
        int totalRows = countRows(userId);
        int totalPages = (int) Math.ceil((double) totalRows / itemsPerPage);

        String query = "SELECT playlist_id, name FROM Playlists WHERE user_id = ? LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, itemsPerPage);
            stmt.setInt(3, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("playlist_id");
                String name = rs.getString("name");
                results.add(String.format("%d. %s [ID: %d]", results.size() + 1, name, id));
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return;
        }

        if (results.isEmpty()) {
            System.out.println("No playlists found.");
        } else {
            System.out.println("Page " + page + " of " + (totalPages > 0 ? totalPages : 1) + " (" + itemsPerPage + " items per page):");
            results.forEach(System.out::println);
            if (page < totalPages) {
                System.out.println("To return the next page run the query as follows:");
                System.out.println("`list playlists " + (page + 1) + "`");
            }
        }
    }

    private int countRows(int userId) {
        int count = 0;
        String countQuery = "SELECT COUNT(*) AS total FROM Playlists WHERE user_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(countQuery)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return count;
    }
}