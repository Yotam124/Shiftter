package com.example.shiftter;

public class WorkGroup {
    private String managerName, groupName;

    WorkGroup(){ }

    WorkGroup(String managerName, String groupName){
        this.managerName = managerName;
        this.groupName = groupName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
