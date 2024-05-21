import exception.InvalidCommandException;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        IAuthentication auth = new Authentication();
        SongManager songManager = new SongManager();
        Scanner scanner = new Scanner(System.in);
        Playlists playlists = new Playlists();
        PlaylistManager playlistManager = new PlaylistManager();
        SearchManager searchManager = new SearchManager();
        CSVExportManager csvExportManager = new CSVExportManager();
        JSONExportManager jsonExportManager = new JSONExportManager();
        PlaylistDataService playlistDataService = new PlaylistDataService();
        AuditLog auditLog = new AuditLog();

        while (true) {
            System.out.println("Enter command: ");
            String command = scanner.nextLine().trim();
            Paginator paginator = new Paginator();
            boolean success = false;

            try {
                ICommand cmd = null;
                switch (command.toLowerCase()) {
                    case "login":
                        cmd = new LoginCommand(auth, scanner);
                        break;
                    case "register":
                        cmd = new RegisterCommand(auth, scanner);
                        break;
                    case "logout":
                        cmd = new LogoutCommand(auth);
                        break;
                    case "promote":
                        cmd = new PromoteCommand(auth, scanner);
                        break;
                    case "create song":
                        if (auth.isLoggedInAsAdmin()) {
                            System.out.println("Song Title: ");
                            String title = scanner.nextLine().trim();
                            System.out.println("Artist Name: ");
                            String artist = scanner.nextLine().trim();
                            System.out.println("Release Year: ");
                            int releaseYear = Integer.parseInt(scanner.nextLine().trim());
                            songManager.createSong(title, artist, releaseYear);
                            success = true;
                        } else {
                            System.out.println("Only administrators can create songs.");
                        }
                        break;
                    case "create playlist":
                        if (auth.isLoggedIn()) {
                            System.out.println("Enter playlist name:");
                            String plName = scanner.nextLine().trim();
                            playlists.insertPlaylist(auth.getCurrentUserId(), plName);
                            success = true;
                        } else {
                            System.out.println("You must be logged in to create playlists.");
                        }
                        break;
                    case "list playlists":
                        if (auth.isLoggedIn()) {
                            int page = 1;
                            System.out.println("Items per page:");
                            int itemsPerPage = Integer.parseInt(scanner.nextLine().trim());
                            paginator.printPage(auth.getCurrentUserId(), page, itemsPerPage);
                            success = true;
                        } else {
                            System.out.println("You must be logged in to view playlists.");
                        }
                        break;
                    case "add byname":
                        if (auth.isLoggedIn()) {
                            System.out.println("Enter playlist name:");
                            String plName = scanner.nextLine().trim();
                            System.out.println("Enter song IDs (separated by spaces):");
                            List<Integer> songIds = Arrays.stream(scanner.nextLine().trim().split(" "))
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList());
                            playlistManager.addSongToPlaylistByName(plName, auth.getCurrentUserId(), songIds);
                            success = true;
                        } else {
                            System.out.println("You must be logged in to add songs to playlists.");
                        }
                        break;
                    case "add byid":
                        if (auth.isLoggedIn()) {
                            System.out.println("Enter playlist ID:");
                            int playlistId = Integer.parseInt(scanner.nextLine().trim());
                            System.out.println("Enter song IDs (separated by spaces):");
                            List<Integer> songIds = Arrays.stream(scanner.nextLine().trim().split(" "))
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList());
                            playlistManager.addSongToPlaylistById(playlistId, songIds);
                            success = true;
                        } else {
                            System.out.println("You must be logged in to add songs to playlists.");
                        }
                        break;
                    case "search":
                        if (auth.isLoggedIn()) {
                            System.out.println("Specify the search criterion type (artist or name):");
                            String criterionType = scanner.nextLine().trim();
                            System.out.println("Enter search term:");
                            String searchTerm = scanner.nextLine().trim();
                            int searchPage = 1;
                            int searchItemsPerPage = 5;
                            searchManager.searchSongs(criterionType, searchTerm, searchPage, searchItemsPerPage);
                            success = true;
                        } else {
                            System.out.println("You must be logged in to perform searches.");
                        }
                        break;
                    case "export playlist":
                        if (auth.isLoggedIn()) {
                            System.out.println("Enter playlist identifier (name or ID):");
                            String identifier = scanner.nextLine().trim();
                            System.out.println("Enter export format (csv or json):");
                            String format = scanner.nextLine().trim();
                            ExportType exportType = "csv".equalsIgnoreCase(format) ? ExportType.CSV : ExportType.JSON;
                            boolean isByName = !identifier.matches("\\d+");
                            List<String[]> songs = playlistDataService.getPlaylistSongs(identifier, isByName, auth.getCurrentUserId());
                            if (songs.isEmpty()) {
                                System.out.printf("Playlist %s does not exist!\n", identifier);
                            } else if (exportType == ExportType.CSV) {
                                csvExportManager.exportToCSV(songs, identifier, auth.getCurrentUsername());
                            } else if (exportType == ExportType.JSON) {
                                jsonExportManager.exportToJSON(songs, identifier, auth.getCurrentUsername());
                            } else {
                                System.out.println("Unsupported format. Only 'csv' or 'json' are allowed.");
                            }
                            success = true;
                        } else {
                            System.out.println("You must be logged in to export playlists.");
                        }
                        break;
                    case "audit":
                        if (auth.isLoggedInAsAdmin()) {
                            System.out.println("Enter username to audit:");
                            String auditUsername = scanner.nextLine().trim();
                            System.out.println("Enter page number:");
                            int auditPage = Integer.parseInt(scanner.nextLine().trim());
                            int auditItemsPerPage = 5;
                            List<String> logs = auditLog.getUserAuditLogs(auditUsername, auditPage, auditItemsPerPage);
                            if (logs.isEmpty()) {
                                System.out.println("No audit logs found for user: " + auditUsername);
                            } else {
                                logs.forEach(System.out::println);
                            }
                            success = true;
                        } else {
                            System.out.println("Only administrators can perform this action.");
                        }
                        break;
                    case "exit":
                        System.out.println("Exiting the application...");
                        scanner.close();
                        return;
                    default:
                        throw new InvalidCommandException();
                }
                if (cmd != null) {
                    success = cmd.execute();
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            } finally {
                if (auth.isLoggedIn()) {
                    auditLog.logCommand(auth.getCurrentUserId(), command, success);
                }
            }
        }
    }
}