package it.polimi.tiw.tiw179.RIA.beans;

import java.util.ArrayList;

public class TopicJS {
    private int id;
    private String name;
    private ArrayList<TopicJS> subtopics;

    public TopicJS(int id, String name){
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

    public ArrayList<TopicJS> getSubtopics() {
        return subtopics;
    }

    public void addsubTopic(TopicJS topic){
        if(subtopics.size()>9){
            throw new IllegalArgumentException();
        }
        subtopics.add(topic);
    }
}
