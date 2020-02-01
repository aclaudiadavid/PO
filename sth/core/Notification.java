package sth.core;

public class Notification implements java.io.Serializable {
    private static final long serialVersionUID = 201810051538L;
    private String _message;

    public Notification(String message) {
        _message = message;
    }

    String getMessage() {
        return _message;
    }
}
