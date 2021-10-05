package it.polimi.tiw.tiw179.DAO;
import it.polimi.tiw.tiw179.beans.Topic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TopicDAO {
    private Connection connection;

    public TopicDAO(Connection connection) {
        this.connection = connection;
    }

    /**It returns all the topics in the database with their subtopics*/
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
        if(value%10==0) throw new IllegalArgumentException();
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
        String query;
        if(x==0){
            query="SELECT max(id) as x from Topic where id<10";
        }
        else {
            query= "Select max(id_son) as x from Subtopic where id_father=?";
        }
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        if(x!=0){
            preparedStatement.setInt(1,x);
        }
        ResultSet set=preparedStatement.executeQuery();
        boolean status= set.next();
        if(status && set.getInt("x")!=0){
            return set.getInt("x")+1;
        }
        else{
            return x*10+1;
        }

    }

    public ResultSet getSons(int id) throws SQLException {
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
}
