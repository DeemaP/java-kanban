package tasktracker.exceptions;

public class ManagerException extends RuntimeException {
    private static final String MSG_SAVE = "An error occurred during the saving process.";
    private static final String MSG_LOAD = "An error occurred during the loading process.";
    public static final String MSG_FILE_NOT_FOUND = "An error occurred during the file creating process";

    private ManagerException(String msg, Exception e) {
        super(msg, e);
    }

    public static ManagerException saveException(Exception e) {
        return new ManagerException(MSG_SAVE, e);
    }

    public static ManagerException loadException(Exception e) {
        return new ManagerException(MSG_LOAD, e);
    }

    public static ManagerException fileCreateException(Exception e) {
        return new ManagerException(MSG_FILE_NOT_FOUND, e);
    }
}
