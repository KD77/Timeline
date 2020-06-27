package org.example;

public class User {

    public String username;
    public int admin;
    private int userID = -1;
    public boolean loggedIn = false;

    public User() {
    }

    public User(String username, int admin, int userID) {
        this.username = username;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int isAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

}
