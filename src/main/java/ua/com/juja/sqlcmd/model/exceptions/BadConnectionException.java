package ua.com.juja.sqlcmd.model.exceptions;

public class BadConnectionException extends Exception {

    public BadConnectionException() {
    }

    public BadConnectionException(String message) {
        super(message);
    }
}
