import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CSVExportManager {
    public void exportToCSV(List<String[]> songs, String playlistName, String username) throws IOException {
        String fileName = String.format("export_%s_%s_%s.csv", username, playlistName, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Title,Artist,Release Year\n");
            for (String[] song : songs) {
                writer.write(String.format("\"%s\",\"%s\",%s\n", song[0], song[1], song[2]));
            }
        }
    }
}
