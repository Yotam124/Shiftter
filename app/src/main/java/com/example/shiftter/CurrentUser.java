package com.example.shiftter;

import android.app.Application;

public class CurrentUser extends Application {
    private static String userID;

    @Override
    public void onCreate() {
        super.onCreate();
        userID = "";
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        CurrentUser.userID = userID;
    }

}

