package sth.core;

import sth.core.exception.BadEntryException;
import sth.core.exception.ImportFileException;
import sth.core.exception.NoSuchPersonIdException;
import sth.core.exception.NoSuchDisciplineIdException;
import sth.core.exception.NoSuchProjectIdException;
import sth.app.exception.NoSuchProjectException;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


/**
 * The façade class.
 */
public class SchoolManager {
    private Person _login;
    private School _school;
    private String _file;
    private transient FileInputStream _fileIn;
    private transient FileOutputStream _fileOut;
    private transient ObjectInputStream _objIn;
    private transient ObjectOutputStream _objOut;

    public SchoolManager (String name) {
        _school = new School(name);
        _file = null;
    }

    /**
    * @param datafile
    * @throws ImportFileException tries to read file
    */

    public void importFile(String datafile) throws ImportFileException {
        try {
            _school.importFile(datafile);
        } catch (IOException | BadEntryException e) {
            throw new ImportFileException(e);
        }
    }

    public void doOpen() throws NoSuchPersonIdException {
        try {
            _fileIn = new FileInputStream(_file);
            _objIn = new ObjectInputStream(_fileIn);
            _school = (School) _objIn.readObject();
            _objIn.close();
            _fileIn.close();

            login(_login.getId());

        } catch (ClassNotFoundException | IOException i) {
            i.printStackTrace();
        }
    }

    public void doSave() {
        try {
            _fileOut = new FileOutputStream(_file);
            _objOut = new ObjectOutputStream(_fileOut);
            _objOut.writeObject(_school);
            _objOut.close();
            _fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
    * Do the login of the user with the given identifier.

    * @param id identifier of the user to login
    * @throws NoSuchPersonIdException if there is no users with the given identifier
    */
    public void login(int id) throws NoSuchPersonIdException {
        _login = _school.getPerson(id);

        if (_login == null) {
            throw new NoSuchPersonIdException(id);
        }
    }

    public String getFile() {
        return _file;
    }

    public void updateFile(String name) {
        _file =name;
    }

    public Person getLogin() {
        return _login;
    }

    public School getSchool() {
        return _school;
    }

    public void changeSchool(School school) {
        _school = school;
    }

    /**
    * @return true when the currently logged in person is an administrative
    */
    boolean isLoggedUserAdministrative() {
        return _login instanceof Employee;
    }

    /**
    * @return true when the currently logged in person is a professor
    */
    public boolean isLoggedUserProfessor() {
        return _login instanceof Teacher;
    }

    /**
    * @return true when the currently logged in person is a student
    */
    public boolean isLoggedUserStudent() {
        return _login instanceof Student;
    }

    /**
    * @return true when the currently logged in person is a representative
    */
    public boolean isLoggedUserRepresentative() {
        if (_login instanceof Student) {
            return ((Student) _login).isRepresentative();
        }
        return false;
    }

    public List<String> getMessages() {
        return _login.showNotifications();
    }

    public List<String> showPerson(){
        List<String> l = new ArrayList<>();
        l.addAll(_login.getInfo());

        return l;
    }

    public List<String> changePhone(int number) {
        _login.changePhone(number);
        return showPerson();
    }

    public List<String> searchPerson(String name) {
        List<Person> pp = getSchool().searchPerson(name);
        List<String> l = new ArrayList<>();

        for (Person a: pp) {
            l.addAll(a.getInfo());
        }
        return l;
    }

    public List<String> showAllPersons() {
        ArrayList<Person> p = getSchool().getAllPeople();
        List<String> l = new ArrayList<>();

        for (Person a: p) {
            l.addAll(a.getInfo());
        }
        return l;
    }

    public List<String> getStudents(String dis) {
        Teacher t = (Teacher) _login;
        ArrayList<Person> p = t.getStudents(dis);
        List<String> l = new ArrayList<>();

        for (Person a: p) {
            l.addAll(a.getInfo());
        }

        return l;
    }

    public boolean inCourse (String dis) {
        Student s = (Student) _login;
        Course c = s.getCourse();
        return c.hasDiscipline(dis);
    }

    public boolean disciplineHasProject(String dis, String pro) {
        if (isLoggedUserStudent()) {
            Student s = (Student) _login;
            Course c = s.getCourse();
            return c.getDiscipline(dis).hasProject(pro);
        }
        
        Teacher t = (Teacher) _login;
        return t.hasProject(dis, pro);
    }

    public boolean hasDiscipline(String dis) {
        return _login.hasDiscipline(dis);
    }

    public boolean hasSubmitted(String dis, String pro) {
        Student s = (Student) _login;
        return s.getProject(dis, pro).hasSubmitted(s);
    }

    public Project getProject(String dis, String pro) {
        Student s = (Student) _login;
        return s.getProject(dis, pro);
    }

    public boolean isProjectOpen(String dis, String pro) {
        Student s = (Student) _login;
        return s.getCourse().getDiscipline(dis).getProject(pro).getStatus();
    }

    public void createProject(String dis, String pro) {
        Teacher t = (Teacher) _login;
        t.createProject(dis, pro);
    }

    public void closeProject(String dis, String pro) {
        Teacher t = (Teacher) _login;
        t.closeProject(dis, pro);
    }

    public void submitProject(String dis, String pro, String message) {
        Student s = (Student)_login;
        try {
            s.submitProject(dis, pro, message);    
        } catch (BadEntryException | NoSuchProjectIdException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSubmissions(String dis, String pro) {
        Teacher t = (Teacher) _login;
        try {
            return t.getSubmissions(dis, pro);
        } catch (NoSuchDisciplineIdException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Survey getSurvey(String dis, String pro) {
        Student s = (Student) _login;
        return s.getCourse().getDiscipline(dis).getProject(pro).getSurvey();
    }

     public Survey getSurveyTeacher(String dis, String pro) {
        Teacher t = (Teacher) _login;
        return t.getDiscipline(dis).getProject(pro).getSurvey();
    }

    public void submitSurvey(String dis, String pro, String message, int hours) throws NoSuchProjectException {
        Student s = (Student) _login;
        try {
            s.submitSurvey(dis, pro, message, hours);
        } catch (BadEntryException e) {
            throw new NoSuchProjectException(dis, pro);
        }
    }

    public List<String> getSurveyResults(String dis, String pro) {
        if (isLoggedUserProfessor()) {
            Teacher t = (Teacher) _login;
            try {
                return t.getSurveyResults(dis, pro);
            } catch (BadEntryException | NoSuchDisciplineIdException e) {
                e.printStackTrace();
            }
        }
        else if (isLoggedUserStudent()) {
            Student s = (Student) _login;
            try {
                return s.getSurveyResults(dis, pro);
            }catch (BadEntryException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<String> getSurveyResultsRepresentative(String dis)  {
        Student s = (Student) _login;

        try {
            return s.getSurveyResultsRepresentative(dis);
        } catch (BadEntryException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSurveyStatus(String dis, String pro) {
        Student s = (Student) _login;
        return s.getCourse().getDiscipline(dis).getProject(pro).getSurveyStatus();
    }

    public boolean hasReplySurvey(String dis, String pro) {
        Student s = (Student) _login;
        return s.getCourse().getDiscipline(dis).getProject(pro).getSurvey().getNumReply() > 0;
    }

    public void createSurvey(String dis, String pro) {
        Student s = (Student) _login;
        s.createSurvey(dis, pro);
    }

    public void openSurvey(String dis, String pro) {
        Student s = (Student) _login;
        s.openSurvey(dis, pro);
    }

    public void closeSurvey(String dis, String pro) {
        Student s = (Student) _login;
        s.closeSurvey(dis, pro);
    }

    public void cancelSurvey(String dis, String pro) {
        Student s = (Student) _login;
        s.cancelSurvey(dis, pro);
    }

    public void finishSurvey(String dis, String pro) {
        Student s = (Student) _login;
        s.finalCondition(dis, pro);
    }
}
