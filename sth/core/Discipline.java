package sth.core;

import sth.core.exception.BadEntryException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Discipline implements java.io.Serializable, Observer {
    private static final long serialVersionUID = 201810051538L;
    private String _name;
    private Course _course;
    private int _capacity;
    private Collection<Person> _teachers = new ArrayList <>();
    private ArrayList<Person> _students = new ArrayList <>();
    private Collection<Project> _projects = new ArrayList <>();
    private List<Person> _toNotify = new ArrayList<>();

    public Discipline(String name, Course course) {
        _name = name;
        _course = course;
        _capacity = 200;
    }

    String getName() {
        return _name;
    }

    Course getCourse() {
        return _course;
    }

    void addTeacher(Teacher t) throws BadEntryException {
        for (Person p: _teachers) {
            if (p.getId() == t.getId()) {
                throw new BadEntryException("This Teacher already teaches this class");
            }
        }
        _teachers.add(t);
        this.addToList(t);
    }

    void enrollStudent(Student s) throws BadEntryException {
        for (Person p: _students) {
            if (p.getId() == s.getId()) {
                throw new BadEntryException("This Student is already enrolled in this class");
            }
        }
        if (_capacity > 0) {
            _capacity--;
            _students.add(s);
            _students.sort(_personHandler);
        }
        else {
            throw new BadEntryException("This Discipline is full");
        }
    }

    /**
    * Comparator override to sort Person by name
    */
    private static Comparator <Person> _personHandler = new Comparator<Person>() {
        @Override
        public int compare(Person p1, Person p2) {
            return Integer.compare(p1.getId(), p2.getId());
        }
    };

    ArrayList<Person> getStudents() {
        return _students;
    }

    void addProject(Project proj) {
        _projects.add(proj);
    }

    Project getProject(String pro) {
        for (Project p: _projects) {
            if (p.getName().equals(pro)) {
                return p;
            }
        }
        return null;
    }

    Boolean hasProject(String pro) {
        for (Project p: _projects) {
            if (p.getName().equals(pro)) {
                return true;
            }
        }
        return false;
    }

    List<String> getSubmissions(String pro) {
        for (Project p: _projects) {
            if (p.getName().equals(pro)) {
                return p.getSubmissions();
            }
        }
        return null;
    }

    public void sendNotification(String message) {
        for (Person p: _toNotify) {
            p.receiveNotification(message);
        }
    }

    public void addToList(Person p) {
        if (!_toNotify.contains(p)) {
            _toNotify.add(p);
        }
    }

    public void removeFromList(Person p) {
        _toNotify.remove(p);
    }

    List<String> getSurveyResults() throws BadEntryException{
        List<String> results = new ArrayList<>();

        for (Project p: _projects) {
            List<String> info = p.getInfoRepresentative();

            results.addAll(info);
        }

        return results;
    }

    List<String> getSurveyResultsTeacher(String proj) throws BadEntryException{
        List<String> info = new ArrayList<>();

        for (Project p: _projects) {
            if (p.getName().equals(proj)) {
                info = p.getInfoTeacher();
            }
        }

        return info;
    }

}
