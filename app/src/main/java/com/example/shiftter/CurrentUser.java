package com.example.shiftter;

import android.app.Application;

public class CurrentUser extends Application {
    private static String userName, firstName, lastName;

    @Override
    public void onCreate() {
        super.onCreate();
        userName = "";
        firstName = "";
        lastName = "";
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        CurrentUser.userName = userName;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        CurrentUser.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        CurrentUser.lastName = lastName;
    }
}

