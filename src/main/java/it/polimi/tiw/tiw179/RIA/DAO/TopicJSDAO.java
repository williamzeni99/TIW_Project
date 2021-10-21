package it.polimi.tiw.tiw179.RIA.DAO;

import it.polimi.tiw.tiw179.HTMLPure.beans.Topic;
import it.polimi.tiw.tiw179.RIA.beans.TopicJS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
