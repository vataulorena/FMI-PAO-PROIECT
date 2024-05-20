public class LogoutCommand implements ICommand {
    private final IAuthentication auth;

    public LogoutCommand(IAuthentication auth) {
        this.auth = auth;
    }

    @Override
    public boolean execute() {
        auth.logout();
        return true;
    }
}
