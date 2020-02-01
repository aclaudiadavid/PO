package sth.core;

import sth.core.exception.BadEntryException;
import sth.core.exception.NoSuchProjectIdException;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class Student extends Person implements java.io.Serializable {
    private static final long serialVersionUID = 201810051538L;
    private int _id;
    private String _name;
    private int _phoneNumber;
    private boolean _isRepresentative;
    private ArrayList <Discipline> _disciplines = new ArrayList<>();
    private Course _course;
    private ArrayList <Notification> _notifications = new ArrayList<>();

    public Student(int number, int phone, String name, boolean representative) {
        _name = name;
        _id = number;
        _phoneNumber = phone;
        _isRepresentative = representative;
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
        String s;
        if (isRepresentative()) {
            s = "DELEGADO|" + Integer.toString(_id) + "|" + Integer.toString(_phoneNumber) + "|" + _name;
        }
        else {
            s = "ALUNO|" + Integer.toString(_id) + "|" + Integer.toString(_phoneNumber) + "|" + _name;
        }

        return s;
    }

    @Override
    public List<String> body() {
        List<String> l = new ArrayList<>();
        String s;

        for (Discipline d: _disciplines) {
            s = "* " + _course.getName() + " - " + d.getName();
            l.add(s);
        }

        return l;
    }

    void unsetRepresentative() {
        _isRepresentative = false;
    }

    boolean isRepresentative() {
        return _isRepresentative;
    }


    Course getCourse() {
        return _course;
    }


    private static Comparator <Discipline> _disCompare = new Comparator<Discipline>() {
        @Override
        public int compare(Discipline d1, Discipline d2) {
            return d1.getName().compareTo(d2.getName());
        }
    };

    void addDiscipline (Discipline d) throws BadEntryException {
        for (Discipline dis: _disciplines) {
            if (dis.getName().equals(d.getName())) {
                throw new BadEntryException("Student is already enrolled");
            }
        }

        if (_disciplines.size() < 6) {
            _disciplines.add(d);
            _disciplines.sort(_disCompare);
            d.addToList(this);
        } else {
            throw new BadEntryException("Student has too many disciplines");
        }
    }

    public boolean hasDiscipline(String discipline) {
        for (Discipline dis: _disciplines) {
            if (dis.getName().equals(discipline)) {
                return true;
            }
        }
        return false;
    }

    Project getProject(String dis, String pro) {
        for (Discipline discipline: _disciplines) {
            if (discipline.getName().equals(dis)) {
                return discipline.getProject(pro);
            }
        }
        return null;
    }

    void submitProject(String dis, String pro, String submission) throws BadEntryException, NoSuchProjectIdException {
        boolean studies = false;

        for (Discipline discipline: _disciplines) {
            if (discipline.getName().equals(dis)) {
                studies = true;
                if (discipline.getProject(pro) != null) {
                    discipline.getProject(pro).addSubmission(this, submission);
                }
                else {
                    throw new NoSuchProjectIdException(pro);
                }
            }
        }
        if (!studies) {
            throw new BadEntryException("Student ins't enrolled in that discipline");
        }
    }

    void submitSurvey(String discipline, String project, String message, int hours) throws BadEntryException{
        boolean hasDiscipline = false;
        Project p = null;

        for (Discipline d: _disciplines) {
            if (discipline.equals(d.getName())) {
                p = d.getProject(project);
                hasDiscipline = true;
            }
        }

        if (hasDiscipline){
            if(p.hasSubmitted(this)) {
                Answer answer = new Answer(message, hours);
                p.submitSurvey(this, answer);
            }
            else {
                throw new BadEntryException("This student hasn't submitted the project yet");
            }
        }
        else {
            throw new BadEntryException("Student isn't enrolled in this Discipline");
        }
    }

    void createSurvey(String dis, String pro) {
        if (_isRepresentative) {
            if (_course.hasDiscipline(dis)) {
                Discipline d = _course.getDiscipline(dis);
                Project p = d.getProject(pro);
                p.createSurvey();

            }
        }
    }

    void openSurvey(String dis, String pro) {
        if (_isRepresentative) {
            if (_course.hasDiscipline(dis)) {
                Discipline d = _course.getDiscipline(dis);
                Project p = d.getProject(pro);
                Survey s = p.getSurvey();
                Open open = new Open();
                open.doAction(s);

                d.sendNotification("Pode preencher inquérito do projecto " + pro + " da disciplina " + dis);
            }
        }
    }

    void closeSurvey(String dis, String pro) {
        if (_isRepresentative) {
            if (_course.hasDiscipline(dis)) {
                Discipline d = _course.getDiscipline(dis);
                Project p = d.getProject(pro);
                Survey s = p.getSurvey();
                Close close = new Close();
                close.doAction(s);
            }
        }
    }

    void cancelSurvey(String dis, String pro) {
        if (_isRepresentative) {
            if (_course.hasDiscipline(dis)) {
                Discipline d = _course.getDiscipline(dis);
                Project p = d.getProject(pro);
                p.cancelSurvey();

            }
        }
    }

    void finalCondition(String dis, String pro) {
        if (_isRepresentative) {
            if (_course.hasDiscipline(dis)) {
                Discipline d = _course.getDiscipline(dis);
                Project p = d.getProject(pro);
                Survey s = p.getSurvey();
                Finalize finalCondition = new Finalize(); 
                finalCondition.doAction(s);
                
                d.sendNotification("Resultados do inquérito do projecto " + pro + " da disciplina " + dis);
            }
        }
    }

    List<String> getSurveyResults(String discipline, String project) throws BadEntryException {
        if (hasDiscipline(discipline)) {
            Project p = getProject(discipline, project);
            return p.parseSurveyStudent();
        }
        else {
            throw new BadEntryException("Student is not enrolled in this Discipline");
        }
    }

    List<String> getSurveyResultsRepresentative(String discipline) throws BadEntryException {
        if (_course.hasDiscipline(discipline)) {
            return _course.getDiscipline(discipline).getSurveyResults();
        }
        else {
            throw new BadEntryException("Representative doesn't have access");
        }
    }

    @Override
    void parseContext(String lineContext, School school) throws BadEntryException {
        String components[] =  lineContext.split("\\|");

        if (components.length != 2) {
            throw new BadEntryException("Invalid line context " + lineContext);
        }
        if (_course == null) {
            _course = school.parseCourse(components[0]);
            _course.addStudent(this);
        }

        Discipline dis = _course.parseDiscipline(components[1]);
        this.addDiscipline(dis);

        dis.enrollStudent(this);
    }
}
