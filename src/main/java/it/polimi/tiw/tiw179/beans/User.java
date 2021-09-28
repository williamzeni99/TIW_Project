package it.polimi.tiw.tiw179.beans;

import org.apache.commons.codec.digest.DigestUtils;

public class User {
    private String username;
    private String passwd;

    public User(String username, String passwd){
        this.username=username;
        this.passwd= DigestUtils.sha256Hex(passwd);
    }

    public String getPasswd() {
        return passwd;
    }

    public String getUsername() {
        return username;
    }
}
