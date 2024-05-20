import java.sql.SQLException;

public interface ICommand {
    boolean execute() throws SQLException;
}
