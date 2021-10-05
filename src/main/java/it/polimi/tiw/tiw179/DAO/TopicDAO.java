package it.polimi.tiw.tiw179.DAO;

import com.mysql.cj.jdbc.CallableStatement;
import it.polimi.tiw.tiw179.beans.Topic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopicDAO {
    private Connection connection;

    public TopicDAO(Connection connection) {
        this.connection = connection;
    }

    /**It returns all the topics in the database with their subtopics*/
    public Topic getTopics() throws SQLException {
        Topic defaultTopic= new Topic(0,"");
        return findAllTopics(defaultTopic);
    }

    public Topic findAllTopics(Topic father) throws SQLException {
        String query= "";
        PreparedStatement pstatement;
        if(father.getId()==0){
            query= "select * from Topic where id<10 order by id";
            pstatement = connection.prepareStatement(query);
        }
        else{
            query = "SELECT id, name from Subtopic,Topic where id=id_son and id_father=? order by id";
            pstatement = connection.prepareStatement(query);
            pstatement.setInt(1,father.getId());
        }
        ResultSet result= pstatement.executeQuery();
        boolean check= result.next();

        for(int i=0; i<9 && check; i++, check=result.next()){
            Topic newFather= new Topic(result.getInt("id"), result.getString("name"));
            father.addsubTopic(findAllTopics(newFather));
        }

        return father;
    }
}
