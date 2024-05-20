import java.sql.SQLException;

public interface IAuthentication {
    int getCurrentUserId();
    String getCurrentUsername();
    boolean login(String username, String password) throws SQLException;
    boolean register(String username, String password) throws SQLException;
    void logout();
    boolean promote(String username) throws SQLException;
    boolean isLoggedInAsAdmin();
    boolean isLoggedIn();
}
