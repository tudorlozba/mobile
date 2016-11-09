package com.android.phoneagenda;

/**
 * Created by tudorlozba on 02/11/2016.
 */
public class Contact {
    private String id;
    private String name;
    private String number;
    private String email;
    private String dateOfBirth;


    public Contact(){

    }

    public Contact(String name, String number, String email, String dateOfBirth) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
