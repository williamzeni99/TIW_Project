package it.polimi.tiw.tiw179.DAO;

import it.polimi.tiw.tiw179.beans.Topic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopicDAO {
    private Connection con;

    public TopicDAO(Connection connection) {
        this.con = connection;
    }

    public List<Topic> findAllTopics() throws SQLException {
        List<Topic> topics = new ArrayList<>();
        String query = "SELECT * FROM Subtopic order by id_father, id_son"; //fino a qui ho un idea, poi ora inizia il tostino, non so ancora come riempire bene le topic in modo efficiente (possibilità di fare un join per avere gli altri dati necessari)
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            result = pstatement.executeQuery();
            while (result.next()) {

                /*Topic t = new Topic();
                t.setId(result.getInt("idTopic"));
                t.setName(result.getString("name"));
                topics.add(t);

                 */
            }
        } catch (SQLException e) {
            throw new SQLException(e);

        } finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (Exception e1) {
                throw new SQLException("Cannot close result");
            }
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException("Cannot close statement");
            }
        }
        return topics;
    }

    public Topic findTopicById(int topicId) throws SQLException {
        Topic t = null;
        String query = "SELECT * FROM topic where idTopic = ?";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            pstatement.setInt(1, topicId);
            result = pstatement.executeQuery();
            while (result.next()) {
                /*t = new Topic();
                t.setId(result.getInt("idTopic"));
                t.setName(result.getString("name"));

                 */
            }
        } catch (SQLException e) {
            throw new SQLException(e);

        } finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (Exception e1) {
                throw new SQLException("Cannot close result");
            }
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException("Cannot close statement");
            }
        }
        return t;
    }

}
