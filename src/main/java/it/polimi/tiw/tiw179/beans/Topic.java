package it.polimi.tiw.tiw179.beans;

import java.util.ArrayList;

public class Topic {
    private int id;
    private String name;
    private ArrayList<Topic> subtopics;

    public Topic(int id, String name){
        this.id=id;
        this.name=name;
        subtopics= new ArrayList<>();
    }

    private boolean notValidId(int id) {
        while(id>0){
            if(id%10==0) return true;
            id=id/10;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Topic> getSubtopics() {
        return subtopics;
    }

    public void addsubTopic(Topic topic){
        if(subtopics.size()>9){
            throw new IllegalArgumentException();
        }
        subtopics.add(topic);
    }

    public static void print(Topic topic){
        if(topic.getSubtopics().size()==0){
            System.out.println(topic.getId()+". "+topic.getName());
        }else{
            System.out.println(topic.getId()+". "+ topic.getName());
            for (Topic x: topic.getSubtopics()){
                print(x);
            }
        }

    }
}