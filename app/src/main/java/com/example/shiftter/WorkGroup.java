package com.example.shiftter;

import java.util.ArrayList;

public class WorkGroup {
    private User managerUserName;
    private String groupName;
    private int groupID;
    private ArrayList<String> userName;

    public WorkGroup(){
    }

    public WorkGroup(User managerUserName, String groupName, int groupID, ArrayList<String> userName){

        this.managerUserName = managerUserName;
        this.groupName = groupName;
        this.groupID = groupID;
        this.userName = userName;

    }

    public User getManagerUserName() {
        return managerUserName;
    }

    public void setManagerUserName(User managerUserName) {
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

    public ArrayList<String> getUserName() {
        return userName;
    }

    public void setUserName(ArrayList<String> userName) {
        this.userName = userName;
    }
}
