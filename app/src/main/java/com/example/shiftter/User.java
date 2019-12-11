package com.example.shiftter;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String firstName, lastName, userName, password;
    private int NumOfGroups;

    public User(){}

    public User(String firstName, String lastName, String userName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        NumOfGroups = 0;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNumOfGroups() {
        return NumOfGroups;
    }

    public void setNumOfGroups(int numOfGroups) {
        NumOfGroups = numOfGroups;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("userName", userName);
        result.put("password", password);
        result.put("NumOfGroups", NumOfGroups);
        return result;
    }
}
