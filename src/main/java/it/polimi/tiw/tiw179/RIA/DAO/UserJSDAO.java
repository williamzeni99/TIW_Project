package it.polimi.tiw.tiw179.RIA.DAO;

import it.polimi.tiw.tiw179.HTMLPure.beans.User;
import it.polimi.tiw.tiw179.RIA.beans.UserJS;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserJSDAO {
    private Connection connection;

    public UserJSDAO(Connection connection){
        this.connection=connection;
    }

    public boolean findUser(String username) throws SQLException {
        boolean status = false;

        PreparedStatement preparedStatement = connection.prepareStatement("select * from User where username = ?");
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();
        status = rs.next();

        return status;
    }

    public UserJS match(String username, String passwd) throws SQLException {
        UserJS user=null;
        PreparedStatement preparedStatement = connection.prepareStatement("select * from User where username = ? and passwd = ? ");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, passwd);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()){
            user=new UserJS(username, rs.getString("name"), rs.getString("surname"));
        }

        return user;
    }

}
