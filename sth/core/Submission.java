package sth.core;

public class Submission implements java.io.Serializable {
    private static final long serialVersionUID = 201810051538L;
    private String _message;
    private int _studentId;

    public Submission (Student student, String message) {
        _studentId = student.getId();
        _message = message;
    }

    int getId() {
        return _studentId;
    }

    String getMessage() {
        return _message;
    }
}
