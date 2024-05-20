import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchManager {
    public void searchSongs(String type, String criterion, int page, int itemsPerPage) {
        String sql = buildSearchQuery(type, criterion);
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, criterion + "%");
            stmt.setInt(2, itemsPerPage);
            stmt.setInt(3, (page - 1) * itemsPerPage);
            ResultSet rs = stmt.executeQuery();
            List<String> results = new ArrayList<>();
            while (rs.next()) {
                String result = String.format("%s - %s (%d) [ID: %05d]",
                        rs.getString("artist_name"), rs.getString("title"),
                        rs.getInt("release_year"), rs.getInt("song_id"));
                results.add(result);
            }
            if (results.isEmpty()) {
                System.out.printf("Page 0 of 0 (%d items per page):\n", itemsPerPage);
            } else {
                int totalRows = getTotalRowCount(type, criterion);
                int totalPages = (totalRows + itemsPerPage - 1) / itemsPerPage;
                System.out.printf("Page %d of %d (%d items per page):\n", page, totalPages, itemsPerPage);
                results.forEach(System.out::println);
                System.out.println("To return the next page run the query as follow:");
                System.out.printf("`search %s \"%s\" %d`\n", type, criterion, page + 1);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    public void handleSearch(String searchParameters) {
        try {
            String[] params = searchParameters.split(" ", 2);
            String type = params[0];
            String criterion = params[1].replace("\"", "");
            int page = 1;
            int itemsPerPage = 5;
            if (criterion.matches(".* \\d+$")) {
                page = Integer.parseInt(criterion.substring(criterion.lastIndexOf(' ') + 1));
                criterion = criterion.substring(0, criterion.lastIndexOf(' '));
            }
            searchSongs(type, criterion, page, itemsPerPage);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please specify a search type and a criterion.");
        }
    }
    private int getTotalRowCount(String type, String criterion) {
        String field = type.equals("author") ? "artist_name" : "title";
        String sql = "SELECT COUNT(*) FROM Songs WHERE " + field + " LIKE ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, criterion + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return 0;
    }
    private String buildSearchQuery(String type, String criterion) {
        String field = type.equals("author") ? "artist_name" : "title";
        return "SELECT song_id, title, artist_name, release_year FROM Songs WHERE " + field + " LIKE ? ORDER BY " + field + " ASC LIMIT ? OFFSET ?";
    }
}
