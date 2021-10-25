package it.polimi.tiw.tiw179.RIA.DAO;

import it.polimi.tiw.tiw179.HTMLPure.beans.Topic;
import it.polimi.tiw.tiw179.RIA.beans.TopicJS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TopicJSDAO {
    private Connection connection;

    public TopicJSDAO(Connection connection){
        this.connection=connection;
    }

    /**It returns all the topics in the database with their subtopics in a default Topic(0, "")*/
    public TopicJS getTopics() throws SQLException {
        TopicJS defaultTopic= new TopicJS(0,"");
        return iterateTopics(defaultTopic);
    }

    private TopicJS iterateTopics(TopicJS father) throws SQLException {
        ResultSet result= getSons(father.getId());
        boolean check= result.next();

        for(int i=0; i<9 && check; i++, check=result.next()){
            TopicJS newFather= new TopicJS(result.getInt("id"), result.getString("name"));
            father.addsubTopic(iterateTopics(newFather));
        }

        return father;
    }

    private ResultSet getSons(int id) throws SQLException {
        String query = "SELECT id, name from Topic where idFather=? order by id";
        PreparedStatement pstatement = connection.prepareStatement(query);
        pstatement.setInt(1,id);
        return pstatement.executeQuery();
    }

    /**It returns the ids of all the topic*/
    public ArrayList<Integer> getTopicIds() throws SQLException {
        String query= "SELECT id from Topic order by id";
        PreparedStatement preparedStatement= connection.prepareStatement(query);
        ResultSet set= preparedStatement.executeQuery();
        ArrayList<Integer> ids= new ArrayList<>();
        while (set.next()){
            ids.add(set.getInt("id"));
        }
        return ids;

    }

    public boolean idExist(ArrayList<Integer> id) throws SQLException {
        ArrayList<Integer> allids=getTopicIds();
        allids.add(0);
        for(int x: id){
            if(!allids.contains(x)){
                return false;
            }
        }
        return true;
    }

    /**Adds a Topic to the database with their connections in subtopics*/
    public void addTopicDB(String id_father, String name) throws SQLException, IllegalArgumentException{
        int value= getNextValue(Integer.parseInt(id_father));
        String query="insert into Topic values (?, ?, ?)";
        PreparedStatement preparedStatement= connection.prepareStatement(query);
        preparedStatement.setInt(1, value);
        preparedStatement.setString(2, name);
        preparedStatement.setInt(3, Integer.parseInt(id_father));
        preparedStatement.executeUpdate();
    }

    private int getNextValue(int x) throws SQLException {
        ResultSet set=getSons(x);
        int i=1;
        while(set.next()){
            if(set.getInt("id")!=x*10+i){
                return x*10+i;
            }
            i++;
        }
        if((x*10+i)%10==0){
            throw new IllegalArgumentException();
        }
        return x*10+i;
    }
}
