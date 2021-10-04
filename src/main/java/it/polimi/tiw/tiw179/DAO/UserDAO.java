package it.polimi.tiw.tiw179.DAO;

import it.polimi.tiw.tiw179.beans.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;

public class UserDAO {
    private Connection connection;
    public final static String usernameDB="username";
    public final static String nameDB="name";
    public final static String surnameDB="surname";
    public final static String passwordDB="passwd";


    public UserDAO(Connection connection){
        this.connection=connection;
    }

    public boolean findUser(String username) throws SQLException {
        boolean status = false;

        PreparedStatement preparedStatement = connection.prepareStatement("select * from User where "+usernameDB+" = ?");
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();
        status = rs.next();

        return status;
    }

    public User match(String username, String passwd) throws SQLException {
        User user=null;
        String passwdSha256= DigestUtils.sha256Hex(passwd);
        PreparedStatement preparedStatement = connection.prepareStatement("select * from User where "+usernameDB+" = ? and "+passwordDB+" = ? ");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, passwdSha256);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()){
            user=new User(username, rs.getString(nameDB), rs.getString(surnameDB));
        }

        return user;
    }
}
