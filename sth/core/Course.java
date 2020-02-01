package sth.core;

import sth.core.exception.BadEntryException;
import sth.core.exception.NoSuchPersonIdException;
import java.util.Collection;
import java.util.ArrayList;

public class Course implements java.io.Serializable {
    private static final long serialVersionUID = 201810051538L;
    private String _name;
    private Collection <Person> _representatives = new ArrayList <>();
    private Collection <Discipline> _disciplines = new ArrayList <>();
    private Collection <Person> _students = new ArrayList <>();

    public Course(String name) {
        _name = name;
    }

    String getName() {
        return _name;
    }

    void addStudent(Student s) throws BadEntryException {
        if (s.isRepresentative()) {
            this.addRepresentative(s);
        }
        else {
            for (Person p: _students) {
                if (p.getId() == s.getId()) {
                    throw new BadEntryException("There's already a Student with this id in this course");
                }
            }
            _students.add(s);
        }
    }

    void addRepresentative(Student s) throws BadEntryException {
        if (_representatives.size() < 7) {
            for (Person p: _representatives) {
                if (p.getId() == s.getId()) {
                    throw new BadEntryException("There's already a representative with this id");
                }
            }
            _representatives.add(s);
            for (Discipline d: _disciplines) {
                d.addToList(s);   
            }
            
        } else {
            throw new BadEntryException("There are already 7 representatives");
        }
    }

    void removeRepresentative(Student s) throws NoSuchPersonIdException {
        if (_representatives.contains(s)) {
            s.unsetRepresentative();

            _students.add(s);
        } else{
            throw new NoSuchPersonIdException(s.getId());
        } 
    }

    void addDiscipline(Discipline d) throws BadEntryException {
        for (Discipline dis : _disciplines) {
            if (dis.getName().equals(d.getName())) {
                throw new BadEntryException("There's already a Discipline with this name");
            }
        }

        _disciplines.add(d);
    }

    Discipline getDiscipline(String dis) {
        for (Discipline d: _disciplines) {
            if (d.getName().equals(dis)) {
                return d;
            }
        }
        return null;
    } 

    Boolean hasDiscipline(String dis) {
        for (Discipline d: _disciplines) {
            if (d.getName().equals(dis)) {
                return true;
            }
        }
        return false;
    } 

    Discipline parseDiscipline (String dis) throws BadEntryException {
        for (Discipline d: _disciplines){
            if (d.getName().equals(dis)) {
                return d;
            }
        }

        Discipline discipline = new Discipline(dis, this);
        this.addDiscipline(discipline);

        return discipline;
    }
}
