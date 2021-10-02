package it.polimi.tiw.tiw179.beansForm;

import it.polimi.tiw.tiw179.ErrorMessage;

public class LoginFom {
    private String username;
    private ErrorMessage error;

    private boolean UserError= false;
    private boolean PasswdError = false;

    public LoginFom(String username, String passwd){
        this.username=username;
        if(username==null || username.isEmpty()){
            UserError=true;

        }
        if(passwd==null || passwd.isEmpty()){
            PasswdError =true;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getErrorMessage(){
        return error.getMessage();
    }

    public void setError(ErrorMessage error) {
        this.error = error;
    }

    public boolean isFormValid(){
        return !UserError && !PasswdError;
    }

    public boolean checkUserError() {
        return UserError;
    }

    public boolean checkPasswdError() {
        return PasswdError;
    }


}
