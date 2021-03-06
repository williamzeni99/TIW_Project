package it.polimi.tiw.tiw179;

public enum ErrorMessage {

    Driver("DRIVER ERROR - Can't load database drivers"),
    ConnectionDB("CONNECTION ERROR  - Can't connect to the database"),
    QueryNotGood("Internal server error, retry later"),
    UserNotFound("User not registered"),
    UserNotMatch("The password is incorrect"),
    UserDataRequired("Credentials must be not null"),
    TopicNotFound("Topic you selected is not listed in our databases"),
    TopicNotInt("The father id is not an integer or is empty"),
    TopicNameMissing("Topic name required"),
    NoChangesDetected("No changes stored because no changes were provided to the server"),
    CantMoveinSon("Change dropped. You are trying to move a Topic in one of its subtopic"),
    NoMoreTopic("You cannot add more Topic in this section");

    private final String message;

    ErrorMessage(String s) {
        message=s;
    }

    public String getMessage() {
        return message;
    }
}
