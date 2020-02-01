package sth.core;

import sth.core.exception.BadEntryException;
import sth.core.exception.NoSuchDisciplineIdException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Teacher extends Person implements java.io.Serializable {
    private static final long serialVersionUID = 201810051538L;
    private int _id;
    private String _name;
    private int _phoneNumber;
    private ArrayList <Discipline> _disciplines = new ArrayList<>();
    private ArrayList <Notification> _notifications = new ArrayList<>();

    public Teacher(int number, int phone, String name) {
        _id = number;
        _phoneNumber = phone;
        _name = name;
    }

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public int getPhone() {
        return _phoneNumber;
    }

    @Override
    public void changePhone(int newPhone) {
        _phoneNumber = newPhone;
    }

    @Override
    public String header() {
        String s = "DOCENTE|" + Integer.toString(_id) + "|" + Integer.toString(_phoneNumber) + "|" + _name;
        return s; 
    }

    @Override
    public List<String> body() {
        List<String> l = new ArrayList<>();
       
        for (Discipline d: _disciplines) {
            String disciplines = "* " + d.getCourse().getName() + " - " + d.getName();
            l.add(disciplines);
        }
        return l;
    }

    private static Comparator<Discipline> _disCourseCompare = new Comparator<Discipline>() {
        @Override
        public int compare(Discipline d1, Discipline d2) {
            return d1.getCourse().getName().compareTo(d2.getCourse().getName());
        }
    };

    private static Comparator<Discipline> _disNameCompare = new Comparator<Discipline>() {
        @Override
        public int compare(Discipline d1, Discipline d2) {
            return d1.getName().compareTo(d2.getName());
        }
    };

    ArrayList<Person> getStudents(String discipline) {
        for (Discipline d: _disciplines) {
            if (d.getName().equals(discipline)) {
                return d.getStudents();
            }
        }
        return null;
    }

    void addDiscipline(Discipline d) throws BadEntryException {
        for (Discipline dis: _disciplines ) {
            if (dis.getName().equals(d.getName())) {
                if (dis.getCourse().getName().equals(d.getCourse().getName())) {
                    throw new BadEntryException("This Teacher already teaches that discipline");
                }
            }
        }
        _disciplines.add(d);
        _disciplines.sort(_disNameCompare);
        _disciplines.sort(_disCourseCompare);
    }

    @Override
    public boolean hasDiscipline(String discipline) {
        for (Discipline dis: _disciplines) {
            if (dis.getName().equals(discipline)) {
                return true;
            }
        }
        return false;
    }

    Discipline getDiscipline(String dis) {
        for (Discipline d: _disciplines) {
            if (d.getName().equals(dis)) {
                return d;
            }
        }
        return null;
    }

    @Override
    public boolean hasProject(String discipline, String project) {
        for (Discipline d: _disciplines) {
            if (d.getName().equals(discipline)) {
                if (d.hasProject(project)) {
                    return true;
                }
            }
        }
        return false;
    }

    void createProject(String disName, String pro) {
        for (Discipline dis: _disciplines) {
            if (dis.getName().equals(disName)) {
                Project proj = new Project(pro, dis);
                dis.addProject(proj);
                break;
            }
        }
    }

    boolean isProjectOpen(String discipline, String project) {
        Project p = null;

        for (Discipline d: _disciplines) {
            if (discipline.equals(d.getName())) {
                p = d.getProject(project);
                return p.getStatus();
            }
        }
        return false;
    }

    void closeProject(String dis, String p) {
        for (Discipline d: _disciplines) {
            if (d.getName().equals(dis)) {
                if (d.hasProject(p) && isProjectOpen(dis, p)) {
                    d.getProject(p).close();
                    break;
                }
            }
        }
    }

    List <String> getSubmissions(String dis, String pro) throws NoSuchDisciplineIdException {
        for (Discipline d: _disciplines) {
            if (d.getName().equals(dis)) {
                return d.getSubmissions(pro);
            }
        }
        throw new NoSuchDisciplineIdException(dis);
    }

    List<String> getSurveyResults(String dis, String pro) throws BadEntryException, NoSuchDisciplineIdException {
        for (Discipline d: _disciplines) {
            if (d.getName().equals(dis)) {
                return d.getSurveyResultsTeacher(pro);
            }
        }
        throw new NoSuchDisciplineIdException(dis);
    }

    @Override
    void parseContext(String lineContext, School school) throws BadEntryException {
        String components[] =  lineContext.split("\\|");

        if (components.length != 2) {
            throw new BadEntryException("Invalid line context " + lineContext);
        }

        Course course = school.parseCourse(components[0]);
        Discipline discipline = course.parseDiscipline(components[1]);
        this.addDiscipline(discipline);

        discipline.addTeacher(this);
    }
}
