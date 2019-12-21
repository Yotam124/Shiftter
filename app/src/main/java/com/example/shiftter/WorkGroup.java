package com.example.shiftter;

public class WorkGroup {
    private String groupKey, managerUserID, groupName, dateOfCreation;
    private int numOfMembers;

    WorkGroup(){ }

    WorkGroup(String groupKey, String managerName, String groupName, int numOfMembers, String dateOfCreation){
        this.groupKey = groupKey;
        this.managerUserID = managerName;
        this.groupName = groupName;
        this.numOfMembers = numOfMembers;
        this.dateOfCreation = dateOfCreation;
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

    public String getGroupKey() {
        return groupKey;
    }
    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
