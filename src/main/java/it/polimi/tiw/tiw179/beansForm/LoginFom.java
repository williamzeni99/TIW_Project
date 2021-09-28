package it.polimi.tiw.tiw179.beansForm;

import it.polimi.tiw.tiw179.ErrorMessage;

public class LoginFom {
    private String username;

    private ErrorMessage errorMessageUser= null;
    private ErrorMessage errorMessagePasswd= null;

    public LoginFom(){
        super();
    }

    public LoginFom(String username, String passwd){
        this.username=username;
        if(username==null){
            errorMessageUser=ErrorMessage.UsernameRequired;
        }
        if(passwd==null){
            errorMessagePasswd=ErrorMessage.PasswdRequired;
        }
    }

    public boolean isFormValid(){
        return errorMessageUser!=null && errorMessagePasswd!=null;
    }

    public void setErrorMessagePasswd(ErrorMessage errorMessagePasswd) {
        this.errorMessagePasswd = errorMessagePasswd;
    }

    public void setErrorMessageUser(ErrorMessage errorMessageUser) {
        this.errorMessageUser = errorMessageUser;
    }
}
