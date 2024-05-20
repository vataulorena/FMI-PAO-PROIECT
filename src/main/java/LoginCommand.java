import java.sql.SQLException;
import java.util.Scanner;

public class LoginCommand implements ICommand {
    private final IAuthentication auth;
    private final Scanner scanner;

    public LoginCommand(IAuthentication auth, Scanner scanner) {
        this.auth = auth;
        this.scanner = scanner;
    }

    @Override
    public boolean execute() throws SQLException {
        System.out.println("Username: ");
        String username = scanner.nextLine().trim();
        System.out.println("Password: ");
        String password = scanner.nextLine().trim();
        return auth.login(username, password);
    }
}
