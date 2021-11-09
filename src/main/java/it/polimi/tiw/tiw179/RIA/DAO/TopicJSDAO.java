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
        ids.add(0);
        return ids;

    }

    public boolean idExist(ArrayList<Integer> ids) throws SQLException {
        String query = "SELECT * from Topic where id=?";
        PreparedStatement pstatement = connection.prepareStatement(query);
        ResultSet set;
        for(int id: ids){
            if(id!=0){
                pstatement.setInt(1,id);
                set= pstatement.executeQuery();
                if(!set.next()){
                    return false;
                }

            }
        }
        return true;
    }

    public boolean isMySon(int idFather, int idSon) throws SQLException {
        return getTopicsList(idFather).contains(idSon);
    }

    /**It returns an arraylist of all father's subtopics*/
    private ArrayList<Integer> getTopicsList(int idFather) throws SQLException {
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
