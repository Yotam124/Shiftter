package com.example.shiftter;

public class GroupMember {

    private String userName;
    private String position;
    private float salary;

    public GroupMember(){}

    public GroupMember(String userName, String position, float salary) {
        this.userName = userName;
        this.position = position;
        this.salary = salary;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
