package com.example.shiftter;

import android.app.Application;

public class CurrentGroup extends Application {

    private static String groupID;
    private static String GroupMame;
    private static String GroupManagerID;

    @Override
    public void onCreate() {
        super.onCreate();
        groupID = "";
        GroupMame = "";
        GroupManagerID = "";
    }

    public static String getGroupID() {
        return groupID;
    }

    public static void setGroupID(String groupID) {
        CurrentGroup.groupID = groupID;
    }

    public static String getGroupMame() {
        return GroupMame;
    }

    public static void setGroupMame(String groupMame) {
        GroupMame = groupMame;
    }

    public static String getGroupManagerID() {
        return GroupManagerID;
    }

    public static void setGroupManagerID(String groupManagerID) {
        GroupManagerID = groupManagerID;
    }
}
