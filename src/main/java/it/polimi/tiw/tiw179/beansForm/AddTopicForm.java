package it.polimi.tiw.tiw179.beansForm;

import it.polimi.tiw.tiw179.ErrorMessage;

public class AddTopicForm {
    private String body;
    private String  id_father;
    private ErrorMessage errorMessage;

    private boolean errorBody=false;
    private boolean errorId=false;

    public AddTopicForm(String body, String id){
        this.id_father=id;
        this.body=body;
        if(body==null || body.isEmpty()){
            errorBody=true;
        }
        if(!isGood(id)){
            errorId=true;
        }
    }

    public String getBody() {
        return body;
    }

    public String getIdFather() {
        return id_father;
    }

    public boolean checkErrorBody() {
        return errorBody;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean checkErrorId() {
        return errorId;
    }

    public boolean isValid(){
        return !(errorBody || errorId);
    }

    private boolean isGood(String id) {
        try {
            Integer.parseInt(id);
        }catch (NumberFormatException e){
            return false;
        }
        if(id==null || id.isEmpty()){
            return false;
        }
        return true;
    }
}
