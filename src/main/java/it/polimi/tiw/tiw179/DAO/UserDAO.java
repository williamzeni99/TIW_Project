package it.polimi.tiw.tiw179.DAO;

import it.polimi.tiw.tiw179.beans.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;

public class UserDAO {
    Connection connection;

    public UserDAO(Connection connection){
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

    public boolean match(String username, String passwd) throws SQLException {
        boolean status = false;
        String passwdSha256= DigestUtils.sha256Hex(passwd);
        PreparedStatement preparedStatement = connection.prepareStatement("select * from User where username = ? and password = ? ");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, passwdSha256);
        ResultSet rs = preparedStatement.executeQuery();
        status = rs.next();

        return status;
    }
}
