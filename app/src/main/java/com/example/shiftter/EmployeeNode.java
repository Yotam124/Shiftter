package com.example.shiftter;

public class EmployeeNode {
    private String userName;
    private float salary;

    public EmployeeNode(){
    }
    public EmployeeNode(String userName, float salary){
        this.userName = userName;
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
}
