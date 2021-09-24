package it.polimi.tiw.tiw179.beansForm;

public class LoginFom {
    private String username;
    private String passwd;
    private static String errorMessageUser= "Username is required";

    public LoginFom(){
        super();
    }

    public LoginFom(String username, String passwd){
        this.username=username;
        this.passwd=passwd;
    }

    public boolean isFormValid(){
        return username!=null && !username.isEmpty();
    }

    public String getErrorMessageUser() {
        return errorMessageUser;
    }
}
