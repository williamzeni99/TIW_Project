package it.polimi.tiw.tiw179.HTMLPure.beans;

public class User {
    private final String username;
    private final String name;
    private final String surname;

    public User(String username, String name, String surname){
        this.username=username;
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }
}
