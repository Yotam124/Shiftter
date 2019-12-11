package com.example.shiftter;

import java.util.ArrayList;

public class WorkGroup {
    private String managerUserName;
    private String groupName;
    private int groupID;
    private ArrayList<EmployeeNode> employees;

    public WorkGroup(){
    }

    public WorkGroup(String managerUserName, String groupName, int groupID){

        this.managerUserName = managerUserName;
        this.groupName = groupName;
        this.groupID = groupID;
        this.employees = new ArrayList<>();

    }

    public String getManagerUserName() {
        return managerUserName;
    }

    public void setManagerUserName(String managerUserName) {
        this.managerUserName = managerUserName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public ArrayList<EmployeeNode> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<EmployeeNode> employees) {
        this.employees = employees;
    }
}
