package sth.core;

public class Answer implements java.io.Serializable {
    private static final long serialVersionUID = 201810051538L;
    private String _message;
    private int _hours;

    Answer(String message, int hours) {
        _message = message;
        _hours = hours;
    }

    String getMessage() {
        return _message;
    }

    int getHours() {
        return _hours;
    }
}
