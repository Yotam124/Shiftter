package com.example.shiftter;

import java.util.ArrayList;

public class WorkGroup {
    private String managerName, groupName;
    private ArrayList<String> workList;

    WorkGroup(){ }

    WorkGroup(String managerName, String groupName){
        this.managerName = managerName;
        this.groupName = groupName;
        workList = new ArrayList<>();
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

    public ArrayList<String> getWorkList() {
        return workList;
    }

    public void setWorkList(ArrayList<String> workList) {
        this.workList = workList;
    }
}
