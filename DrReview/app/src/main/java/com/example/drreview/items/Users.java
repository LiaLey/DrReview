package com.example.drreview.items;

//stores the data of the users
public class Users {

    public String username, email;


    //empty constructor needed for firebase
    public Users(){ }

    public Users(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
