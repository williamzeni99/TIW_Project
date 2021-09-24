package it.polimi.tiw.tiw179.beans;

public class User {
    private String username;
    private String passwd;

    public User(String username, String passwd){
        this.username=username;
        this.passwd=passwd;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getUsername() {
        return username;
    }
}
