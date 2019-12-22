package com.example.shiftter;

import android.app.Application;

public class CurrentUser extends Application {
    private static String userID;
    private static String memberID;
    private static String currentGroupID;

    @Override
    public void onCreate() {
        super.onCreate();
        userID = "";
        memberID = "";
        currentGroupID = "";
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        CurrentUser.userID = userID;
    }

    public static String getMemberID() {
        return memberID;
    }

    public static void setMemberID(String memberID) {
        CurrentUser.memberID = memberID;
    }

    public static String getCurrentGroupID() {
        return currentGroupID;
    }

    public static void setCurrentGroupID(String currentGroupID) {
        CurrentUser.currentGroupID = currentGroupID;
    }
}

