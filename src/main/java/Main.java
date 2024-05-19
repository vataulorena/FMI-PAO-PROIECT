import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Authentication auth = new Authentication();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter command (login, register, logout, exit): ");
            String command = scanner.nextLine().trim();
            String username, password;

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
                case "exit":
                    System.out.println("Exiting the application...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Unknown command! Please enter login, register, logout, or exit.");
                    break;
            }
        }
    }
}
