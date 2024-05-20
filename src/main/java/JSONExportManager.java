import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONExportManager {
    public void exportToJSON(List<String[]> songs, String playlistName, String username) throws IOException {
        String fileName = String.format("export_%s_%s_%s.json", username, playlistName, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        JSONArray jsonArray = new JSONArray();
        for (String[] song : songs) {
            JSONObject obj = new JSONObject();
            obj.put("title", song[0]);
            obj.put("artist", song[1]);
            obj.put("release_year", song[2]);
            jsonArray.add(obj);
        }
        Files.write(Path.of(fileName), jsonArray.toJSONString().getBytes());
    }
}
