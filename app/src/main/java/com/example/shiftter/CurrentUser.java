package com.example.shiftter;

import android.app.Application;

public class CurrentUser extends Application {
    private static String userID;
    private static String groupKey;

    @Override
    public void onCreate() {
        super.onCreate();
        userID = "";
        groupKey = "";
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        CurrentUser.userID = userID;
    }

    public static String getGroupKey() {
        return groupKey;
    }

    public static void setGroupKey(String groupKey) {
        CurrentUser.groupKey = groupKey;
    }
}

