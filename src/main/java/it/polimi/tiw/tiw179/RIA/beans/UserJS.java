package it.polimi.tiw.tiw179.RIA.beans;

public class UserJS {

    private final String username;
    private final String name;
    private final String surname;


    public UserJS(String username, String name, String surname) {
        this.username = username;
        this.name = name;
        this.surname = surname;
    }

    public String getName(){
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
