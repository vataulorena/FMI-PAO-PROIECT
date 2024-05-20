import java.sql.SQLException;
import java.util.Scanner;

public class PromoteCommand implements ICommand {
    private final IAuthentication auth;
    private final Scanner scanner;

    public PromoteCommand(IAuthentication auth, Scanner scanner) {
        this.auth = auth;
        this.scanner = scanner;
    }

    @Override
    public boolean execute() throws SQLException {
        if (!auth.isLoggedInAsAdmin()) {
            System.out.println("Only administrators can perform this action.");
            return false;
        }
        System.out.println("Username to promote: ");
        String username = scanner.nextLine().trim();
        return auth.promote(username);
    }
}
