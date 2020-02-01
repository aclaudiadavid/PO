package sth.core;

import sth.core.exception.BadEntryException;

import java.util.ArrayList;
import java.util.List;

public abstract class Person extends Template implements java.io.Serializable {
    private static final long serialVersionUID = 201810051538L;
    private int _id;
    private String _name;
    private int _phoneNumber;
    private ArrayList <Notification> _notifications = new ArrayList<>();

    public abstract String getName();

    public abstract int getPhone();

    public abstract void changePhone(int newPhone);

    public abstract int getId();

    public void receiveNotification(String message) {
        Notification note = new Notification(message);
        _notifications.add(note);
    }

    public List<String> showNotifications() {
        List<String> l = new ArrayList<>();
        
        for(Notification note: _notifications) {
            l.add(note.getMessage());
        }

        _notifications = new ArrayList<>();

        return l;
    }

    @Override
    public String header() {
        String s = Integer.toString(_id) + "|" + Integer.toString(_phoneNumber) + "|" + _name;
        return s;
    }

    @Override
    public List<String> body() {
        List<String> l = new ArrayList<>();
        return l;
    }

    /**
    * Parses the context information for a person from the import file.
    * This method defines the default behavior: no extra information is needed
    * thus it throws the exception.
    **/
    void parseContext(String context, School school) throws BadEntryException {
        throw new BadEntryException("Should not have extra context: " + context);
    }

    public boolean hasDiscipline(String discipline) {
        return false;
    }
    public boolean hasProject(String discipline, String project) {
        return false;
    }

}
