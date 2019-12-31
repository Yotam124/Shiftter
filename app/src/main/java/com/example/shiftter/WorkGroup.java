package com.example.shiftter;

public class WorkGroup {
    private String groupID, groupName,  managerEmail, dateOfCreation;
    private int numOfMembers;

    WorkGroup(){ }

    public WorkGroup(String groupID, String groupName, String managerEmail, int numOfMembers, String dateOfCreation){
        this.groupID = groupID;
        this.groupName = groupName;
        this.managerEmail = managerEmail;
        this.numOfMembers = numOfMembers;
        this.dateOfCreation = dateOfCreation;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public int getNumOfMembers() {
        return numOfMembers;
    }

    public void setNumOfMembers(int numOfMembers) {
        this.numOfMembers = numOfMembers;
    }

    public String getGroupKey() {
        return groupID;
    }
    public void setGroupKey(String groupKey) {
        this.groupID = groupKey;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
