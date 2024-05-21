package exception;

public class InvalidCommandException  extends RuntimeException {
    public InvalidCommandException() {
        super("Unknown command! Please enter a valid command.");
    }
}
