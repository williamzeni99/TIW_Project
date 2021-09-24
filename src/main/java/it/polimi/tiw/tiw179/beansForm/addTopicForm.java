package it.polimi.tiw.tiw179.beansForm;

public class addTopicForm {
    private String body;
    private String id;
    private String errorBodyMessage;
    private String errorIdMessage;

    public addTopicForm(){
        super();
    }

    public addTopicForm(String body, String id){
        this.setBody(body);
        this.setId(id);
    }

    public void setBody(String body) {
        this.body = body;
        if(body.isEmpty() || body==null){
            errorBodyMessage="Topic's name is required";
        }
    }

    public void setId(String id) {
        this.id = id;
        try {
            Integer.parseInt(id);
        }catch (NumberFormatException e){
            errorIdMessage="The category id shuould be an integer";
        }
        if(id==null || id.isEmpty()){
            errorIdMessage="The category id is required";
        }

    }
}
