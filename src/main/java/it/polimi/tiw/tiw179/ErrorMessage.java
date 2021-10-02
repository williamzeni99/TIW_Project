package it.polimi.tiw.tiw179;

public enum ErrorMessage {

    Driver("DRIVER ERROR - Can't load database drivers"),
    ConnectionDB("CONNECTION ERROR  - Can't connect to the database"),
    QueryNotGood("The query is not well formed"),
    UserNotFound("The user is not listed in our databases"),
    UserNotMatch("The password is incorrect"),
    TopicNotFound("Topic you selected is not listed in our databases"),
    UsernameRequired("Username required"),
    PasswdRequired("Password required");


    private final String message;

    ErrorMessage(String s) {
        message=s;
    }

    public String getMessage() {
        return message;
    }
}
