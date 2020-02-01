package sth.core;

import sth.core.exception.BadEntryException;
import sth.core.exception.NoSuchPersonIdException;

import java.io.IOException;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;


/**
 * School implementation.
 */
public class School implements java.io.Serializable {
    private String _name;

    /** Serial number for serialization. */
    private static final long serialVersionUID = 201810051538L;
    private ArrayList <Person> _people = new ArrayList <>();
    private ArrayList <Course> _courses = new ArrayList <>();

    /**
    * Constructor
    * @param name
    */
    School(String name) {
        _name = name;
    }

    /**
    * @return _name
    */
    String getName() {
        return _name;
    }
  
    /**
    * @param filename
    * @throws BadEntryException
    * @throws IOException
    */
    void importFile(String filename) throws IOException, BadEntryException {
        Parser p = new Parser(this);
        p.parseFile(filename);
    }

    ArrayList<Person> getAllPeople() {
        return _people;
    }

    /**
    * Comparator override to sort Person by Id
    */
    private static Comparator <Person> _personHandler = new Comparator<Person>() {
        @Override
        public int compare(Person p1, Person p2) {
            return Integer.compare( p1.getId(), p2.getId());
        }
    };

    /**
    * Adds person to ArrayList
    * @param person
    * @throws BadEntryException
    */
    void addPerson(Person person) throws BadEntryException {
        for (Person p: _people) {
            if (p != null) {
                if (p.getId() == person.getId()) {
                    throw new BadEntryException("This person already exists");
                }
            }
        }
        _people.add(person);
        _people.sort(_personHandler);
    }

    /**
    * Comparator override to sort Person by name
    */
    private static Comparator<Person> _personSearcher = new Comparator<Person>() {
        @Override
        public int compare(Person p1, Person p2) {
            return p1.getName().compareTo(p2.getName());
        }
    };

    /**
    * Adds Person with the same name to an ArrayList
    * @param name
    * @return List
    */
    List<Person> searchPerson(String name) {
        ArrayList<Person> sameName = new ArrayList<>();

        for (Person p: _people) {
            if(p.getName().contains(name)) {
                if (p instanceof Student) {
                    Student s = (Student) p;
                    sameName.add(s);
                }
                else if (p instanceof Teacher) {
                    Teacher t = (Teacher) p;
                    sameName.add(t);
                }
                else {
                    Employee e = (Employee) p;
                    sameName.add(e);
                }
            }
        }

        sameName.sort(_personSearcher);

        return sameName;
    }

    /**
    * Searches for a Person by their id
    * @param id
    * @return Person
    * @throws NoSuchPersonIdException
    */
    Person getPerson(int id) throws NoSuchPersonIdException {
        for (Person p: _people) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new NoSuchPersonIdException(id);
    }

    /**
    * Checks if Person exists
    * @param id
    * @return boolean
    */
    public boolean personExists(int id) {
        for (Person p: _people) {
            if (p.getId() == id) {
                return true;
            }
        }

        return false;
    }

    /**
    * Comparator override to sort Course by name
    */
    private static Comparator <Course> _courseHandler = new Comparator<Course>() {
        @Override
        public int compare(Course c1, Course c2) {
            return c1.getName().compareTo(c2.getName());
        }
    };

    /**
    * returns Course if it exists, if it doesn't creates Course and returns it
    * @param courseName
    * @return Course
    */
    Course parseCourse(String courseName) {
        for (Course c: _courses) {
            if (c.getName().equals(courseName)) {
                return c;
            }
        }

        Course newCourse = new Course(courseName);
        _courses.add(newCourse);
        _courses.sort(_courseHandler);

        return newCourse;
    }
}
