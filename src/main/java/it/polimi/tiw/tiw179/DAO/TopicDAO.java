package it.polimi.tiw.tiw179.DAO;
import it.polimi.tiw.tiw179.beans.Topic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TopicDAO {
    private Connection connection;

    public TopicDAO(Connection connection) {
        this.connection = connection;
    }

    /**It returns all the topics in the database with their subtopics in a default Topic(0, "")*/
    public Topic getTopics() throws SQLException {
        Topic defaultTopic= new Topic(0,"");
        return iterateTopics(defaultTopic);
    }

    private Topic iterateTopics(Topic father) throws SQLException {
        ResultSet result= getSons(father.getId());
        boolean check= result.next();

        for(int i=0; i<9 && check; i++, check=result.next()){
            Topic newFather= new Topic(result.getInt("id"), result.getString("name"));
            father.addsubTopic(iterateTopics(newFather));
        }

        return father;
    }

    public void addTopicDB(String id_father, String name) throws SQLException, IllegalArgumentException{
        int value= getNextValue(Integer.parseInt(id_father));
        String query="insert into Topic values (?, ?)";
        PreparedStatement preparedStatement= connection.prepareStatement(query);
        preparedStatement.setInt(1, value);
        preparedStatement.setString(2, name);
        preparedStatement.executeUpdate();
        if(value>9){
            query="insert into Subtopic values (?, ? )";
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(id_father));
            preparedStatement.setInt(2,value);
            preparedStatement.executeUpdate();
        }

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

    private ResultSet getSons(int id) throws SQLException {
        String query= "";
        PreparedStatement pstatement;
        if(id==0){
            query= "select * from Topic where id<10 order by id";
            pstatement = connection.prepareStatement(query);
        }
        else{
            query = "SELECT id, name from Subtopic,Topic where id=id_son and id_father=? order by id";
            pstatement = connection.prepareStatement(query);
            pstatement.setInt(1,id);
        }

        return pstatement.executeQuery();
    }

    public ArrayList<Integer> getTopicsList(int idFather) throws SQLException {
        ArrayList<Integer> topiclist= new ArrayList<>();
        getNextTopicIds(idFather, topiclist);
        return topiclist;
    }

    private void getNextTopicIds(int idFather, ArrayList<Integer> topiclist) throws SQLException {
        ResultSet resultSet= getSons(idFather);
        while(resultSet.next()){
            topiclist.add(resultSet.getInt("id"));
            getNextTopicIds(resultSet.getInt("id"), topiclist);
        }
    }
}
