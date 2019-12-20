package com.example.shiftter;

public class WorkGroup {
    private String managerUserID, groupName;
    private int numOfMembers;

    WorkGroup(){ }

    WorkGroup(String managerName, String groupName, int numOfMembers){
        this.managerUserID = managerName;
        this.groupName = groupName;
        this.numOfMembers = numOfMembers;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getManagerUserID() {
        return managerUserID;
    }

    public void setManagerUserID(String managerUserID) {
        this.managerUserID = managerUserID;
    }

    public int getNumOfMembers() {
        return numOfMembers;
    }

    public void setNumOfMembers(int numOfMembers) {
        this.numOfMembers = numOfMembers;
    }
}
