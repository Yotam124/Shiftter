package com.example.shiftter;

import android.app.Application;

public class CurrentUser extends Application {
    private static String userName, firstName, lastName, currentJob;

    @Override
    public void onCreate() {
        super.onCreate();
        userName = "";
        firstName = "";
        lastName = "";
        currentJob = "";
    }

    public static String getCurrentJob() {
        return currentJob;
    }

    public static void setCurrentJob(String currentJob) {
        CurrentUser.currentJob = currentJob;
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

