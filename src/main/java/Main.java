import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Authentication auth = new Authentication();
        SongManager songManager = new SongManager();
        Scanner scanner = new Scanner(System.in);
        Playlists playlists = new Playlists();

        while (true) {
            System.out.println("Enter command: ");
            String command = scanner.nextLine().trim();
            String username, password, title, artist;
            int releaseYear;
            Paginator paginator = new Paginator();

            switch (command.toLowerCase()) {
                case "login":
                    System.out.println("Username: ");
                    username = scanner.nextLine().trim();
                    System.out.println("Password: ");
                    password = scanner.nextLine().trim();
                    auth.login(username, password);
                    break;
                case "register":
                    System.out.println("Username: ");
                    username = scanner.nextLine().trim();
                    System.out.println("Password: ");
                    password = scanner.nextLine().trim();
                    auth.register(username, password);
                    break;
                case "logout":
                    auth.logout();
                    break;
                case "promote":
                    System.out.println("Username to promote: ");
                    username = scanner.nextLine().trim();
                    if (auth.isLoggedInAsAdmin()) {
                        if (auth.promote(username)) {
                            System.out.println(username + " is now an administrator!");
                        } else {
                            System.out.println("Specified user does not exist or could not be promoted.");
                        }
                    } else {
                        System.out.println("Only administrators can perform this action.");
                    }
                    break;
                case "create song":
                    if (!auth.isLoggedIn()) {
                        System.out.println("You must be logged in to create a song.");
                        break;
                    }
                    System.out.println("Song Title: ");
                    title = scanner.nextLine().trim();
                    System.out.println("Artist Name: ");
                    artist = scanner.nextLine().trim();
                    System.out.println("Release Year: ");
                    releaseYear = Integer.parseInt(scanner.nextLine().trim());
                    songManager.createSong(title, artist, releaseYear);
                    break;
                case "create playlist":
                    System.out.println("Enter playlist name:");
                    String playlistName = scanner.nextLine().trim();
                    if (auth.isLoggedIn()) {
                        playlists.insertPlaylist(auth.getCurrentUserId(), playlistName);
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
                    } else {
                        System.out.println("You must be logged in to view playlists.");
                    }
                    break;
                case "exit":
                    System.out.println("Exiting the application...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Unknown command! Please enter a valid command.");
                    break;
            }
        }
    }
}
