package sth.core;

import sth.core.exception.BadEntryException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Survey implements java.io.Serializable {
    private static final long serialVersionUID = 201810051538L;
    private List <Integer> _studentId = new ArrayList <>();
    private Collection<Answer> _reply = new ArrayList <>();
    private State _status;
    private int _numReply = 0;

    public Survey() {
        _status = null;
    }

    String getStatus() {
        return _status.toString();
    }

    void create(State state) {
        _status = state;
    }

    void open(State state) {
        if (_status.toString().equals("por abrir") || _status.toString().equals("fechado") || _status.toString().equals("aberto")) {
            _status = state;
        }
    }

    void close(State state) {
        if (_status.toString().equals("aberto") || _status.toString().equals("fechado")) {
            _status = state;
        }
    }

    void finalCondition(State state) {
        if (_status.toString().equals("fechado") || _status.toString().equals("finalizado")) {
            _status = state;
        }
    }

    void addAnswer(Student s, Answer answer) throws BadEntryException{
        if (_status.toString().equals("aberto")) {
            for (int i: _studentId) {
                if (s.getId() == i) {
                    throw new BadEntryException("This student already answered");
                }
            }

            _reply.add(answer);
            _studentId.add(s.getId());
            _numReply++;
        }
        else {
            throw new BadEntryException("The survey is not open");
        }
    }

    int getNumReply() {
        return _numReply;
    }

    Collection <Answer> getResults() {
        return _reply;
    }
}
