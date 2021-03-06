package it.polimi.tiw.tiw179.HTMLPure.beans;

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

}
