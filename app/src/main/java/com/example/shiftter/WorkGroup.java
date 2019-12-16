package com.example.shiftter;

public class WorkGroup {
    private String managerUserName, groupName;

    WorkGroup(){ }

    WorkGroup(String managerName, String groupName){
        this.managerUserName = managerName;
        this.groupName = groupName;
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

}
