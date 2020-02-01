package sth.core;

import sth.core.exception.BadEntryException;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class Project implements java.io.Serializable {
    private static final long serialVersionUID = 201810051538L;
    private String _name;
    private String _description;
    private boolean _isOpen;
    private Survey _survey = null;
    private Discipline _discipline;
    private List<Submission> _submissions = new ArrayList<>();
    private int _numSubmissions = 0;

    public Project(String name, Discipline dis) {
        _name = name;
        _isOpen = true;
        _discipline = dis;
    }

    Discipline getDiscipline() {
        return _discipline;
    }

    String getName() {
        return _name;
    }

    boolean getStatus() {
        return _isOpen;
    }

    void close() {
        if (_isOpen) {
            _isOpen = false;
            if (_survey != null) {
                Open aberto = new Open();
                aberto.doAction(_survey);
                _discipline.sendNotification("Pode preencher inquérito do projecto " + _name + " da disciplina " + _discipline.getName());
            }
        }
    }


    void addSubmission(Student s, String message) {
        Submission submission = new Submission(s, message);
        boolean newVal = true;

        for (Submission sub: _submissions) {
            if (sub.getId() == s.getId()) {
                _submissions.remove(sub);
                _submissions.add(submission);
                newVal = false;
                break;
            }
        }

        if (newVal) {
            _submissions.add(submission);
            _numSubmissions++;
        }

        _submissions.sort(_personHandler);
    }

    private static Comparator<Submission> _personHandler = new Comparator<Submission>() {
        @Override
        public int compare(Submission s1, Submission s2) {
            return Integer.compare(s1.getId(), s2.getId());
        }
    };

    boolean hasSubmitted(Person p) {
        for (Submission s: _submissions) {
            if (p.getId() == s.getId()) {
                return true;
            }
        }
        return false;
    }

    List<String> getSubmissions() {
        List<String> submissions = new ArrayList<>();

        for (Submission sub: _submissions) {
            String s = "* " + Integer.toString(sub.getId()) + " - " + sub.getMessage();
            submissions.add(s);
        }

        return submissions;
    }

    void createSurvey() {
        if (_survey == null) {
            _survey = new Survey();
            Created create = new Created();
            create.doAction(_survey);
        }
    }

    Survey getSurvey() {
        return _survey;
    }

    String getSurveyStatus(){
        return _survey.getStatus();
    }

    void cancelSurvey() {
        String s = getSurveyStatus();

        if (s.equals("por abrir")) {
            _survey = null;
        }
        else if (s.equals("aberto") && _survey.getNumReply() == 0) {
            _survey = null;
        }
        else {
            Open aberto = new Open();
            aberto.doAction(_survey);
        }
    }

    void submitSurvey(Student s, Answer answer) throws BadEntryException {
        _survey.addAnswer(s, answer);
    }

    List<String> parseSurveyStudent() {
        List<String> surv = new ArrayList<>();

        int counter = 0;
        float totalHours = 0;
        float average = 0;

        if (_survey == null) {
            String s = _discipline.getName() + " - " + _name + " (por abrir)";
        }
        else if (_survey.getStatus().equals("finalizado")) {
            Collection <Answer> replies = _survey.getResults();

            for (Answer a: replies) {
                totalHours += a.getHours();
                counter++;

                average = totalHours/counter;
            }

            surv.add(_discipline.getName() + " - " + _name);
            surv.add(" * Número de respostas: " + Integer.toString(counter));
            surv.add(" * Tempo médio (horas): " + Integer.toString(Math.round(average)));

            return surv;
        }
        
        else {
            surv.add(_discipline.getName() + " - " + _name + " (" + _survey.getStatus() + ")");
        }

        return surv;
    }

    List<String> getInfoTeacher() {
        List <String> info = new ArrayList<>();
        int max = 0;
        int min = 0;
        int counter = 0;
        float average = 0;
        float totalHours = 0;
        boolean first = true;

        if (_survey == null) {
            String s = _discipline.getName() + " - " + _name + " (por abrir)";
        } else if (_survey.getStatus().equals("finalizado")) {
            Collection <Answer> results = _survey.getResults();
        
            for (Answer a: results) {
                if (first) {
                    counter++;
                    totalHours = a.getHours();
                    max = a.getHours();
                    min = a.getHours();
                    first = false;
                }
                else {
                    counter++;
                    totalHours += a.getHours();
                    if (a.getHours() < min) {
                        min = a.getHours();
                    } else if (a.getHours() > max) {
                        max = a.getHours();
                    }
                }   

                average = totalHours/counter; 
            }

            info.add(_discipline.getName() + " - " + _name);
            info.add(" * Número de submissões: " + Integer.toString(_numSubmissions));
            info.add(" * Número de respostas: " + Integer.toString(counter));
            info.add(" * Tempos de resolução (horas) (mínimo, médio, máximo): " + Integer.toString(min) + ", " + Integer.toString(Math.round(average)) + ", " + Integer.toString(max));
        } else {
            String s = _discipline.getName() + " - " + this.getName() + " (" + _survey.getStatus() + ")";
            info.add(s);
        }

        return info;
    }

    List<String> getInfoRepresentative() {
        List<String> allInfo = new ArrayList<>();
        int counter = 0;
        float average = 0;
        float totalHours = 0;

        if (_survey == null) {
            String s = _discipline.getName() + " - " + _name + " (por abrir)";
        } else if (_survey.getStatus().equals("finalizado")) {
            Collection<Answer> results = _survey.getResults();
            

            for (Answer a: results) {
                counter++;
                totalHours += a.getHours();

                average = totalHours/counter;
            }

            String s = _discipline.getName() + " - " + _name + " - " + Integer.toString(counter) + " respostas - " + Integer.toString(Math.round(average)) + " horas";
            allInfo.add(s);
        } else {
            String s = _discipline.getName() + " - " + _name + " (" + _survey.getStatus() + ")";
            allInfo.add(s);
        }

        return allInfo;
    }
}
