package it.polimi.tiw.tiw179.HTMLPure.DAO;
import it.polimi.tiw.tiw179.HTMLPure.beans.Topic;

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

    private ResultSet getSons(int id) throws SQLException {
        String query = "SELECT id, name from Topic where idFather=? order by id";
        PreparedStatement pstatement = connection.prepareStatement(query);
        pstatement.setInt(1,id);
        return pstatement.executeQuery();
    }

    /**It returns an arraylist of all father's subtopics*/
    public ArrayList<Integer> getTopicsList(int idFather) throws SQLException {
        ArrayList<Integer> topiclist= new ArrayList<>();
        topiclist.add(idFather);
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

    public boolean idExist(ArrayList<Integer> id) throws SQLException {
        ArrayList<Integer> allids=getTopicsList(0);
        for(int x: id){
            if(!allids.contains(x)){
                return false;
            }
        }
        return true;
    }

    /**Actually it moves all father's subtopics to a new directory */
    public void moveTopic(int idToMove, int idWhereToMove) throws SQLException {
        String query="Select idFather from Topic where id=?";
        PreparedStatement preparedStatement= connection.prepareStatement(query);
        preparedStatement.setInt(1, idToMove);
        ResultSet set=preparedStatement.executeQuery();
        set.next();
        int oldFather= set.getInt("idFather");

        //get the father directory that then has to be updated

        String query2= "UPDATE Topic SET id = ?, idFather=? WHERE (id = ?)";
        int newid=getNextValue(idWhereToMove);
        preparedStatement=connection.prepareStatement(query2);
        preparedStatement.setInt(1,newid);
        preparedStatement.setInt(2,idWhereToMove);
        preparedStatement.setInt(3,idToMove);
        preparedStatement.executeUpdate();
        //first dir id updated

        updateSons(idToMove,newid);
        updateIds(oldFather, idToMove);
    }

    private void updateSons(int exId, int newId) throws SQLException {
        ResultSet set=getSons(exId);

        while (set.next()){
            String query= "update Topic set id=?, idFather=? where id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            int nextid=getNextValue(newId);
            preparedStatement.setInt(1, nextid);
            preparedStatement.setInt(2, newId);
            preparedStatement.setInt(3, set.getInt("id"));
            preparedStatement.executeUpdate();
            updateSons(set.getInt("id"), nextid);
        }
    }

    private void updateIds(int oldFather, int idToMove) throws SQLException {
        ResultSet set=getSons(oldFather);
        while(set.next()){
            if(set.getInt("id")>idToMove){
                String query= "update Topic set id=?, idFather=? where id=?";
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                int nextid=getNextValue(oldFather);
                preparedStatement.setInt(1, nextid);
                preparedStatement.setInt(2, oldFather);
                preparedStatement.setInt(3, set.getInt("id"));
                preparedStatement.executeUpdate();
                updateSons(set.getInt("id"), nextid);
            }
        }
    }
}