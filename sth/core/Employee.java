package sth.core;

import java.util.ArrayList;
import java.util.List;

public class Employee extends Person implements java.io.Serializable {
    private static final long serialVersionUID = 201810051538L;
    private int _id;
    private String _name;
    private int _phoneNumber;

    public Employee(int number, int phone, String name) {
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
        String s = "FUNCIONÁRIO|" + Integer.toString(_id) + "|"+ Integer.toString(_phoneNumber) + "|" + _name;
        return s;
    }

}
