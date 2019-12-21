package com.example.shiftter;

public class GroupMember {

    private String UserID;
    private String position;
    private float salary;

    public GroupMember(){}

    public GroupMember(String UserID, String position, float salary) {
        this.UserID = UserID;
        this.position = position;
        this.salary = salary;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        this.UserID = userID;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
