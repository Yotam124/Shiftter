package com.example.shiftter;

import android.app.Application;

public class CurrentUser extends Application {
    private static String userEmail;
    private static String userCodedEmail;
    private static String memberEmail;
    private static WorkGroup currentGroup;

    @Override
    public void onCreate() {
        super.onCreate();
        userEmail = "";
        userCodedEmail = "";
        memberEmail = "";
        currentGroup = new WorkGroup();
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        CurrentUser.userEmail = userEmail;
    }

    public static String getMemberEmail() {
        return memberEmail;
    }

    public static void setMemberEmail(String memberEmail) {
        CurrentUser.memberEmail = memberEmail;
    }

    public static WorkGroup getCurrentGroup() {
        return currentGroup;
    }

    public static void setCurrentGroup(WorkGroup currentGroup) {
        CurrentUser.currentGroup = currentGroup;
    }

    public static String getUserCodedEmail() {
        return userCodedEmail;
    }

    public static void setUserCodedEmail(String userCodedEmail) {
        CurrentUser.userCodedEmail = userCodedEmail;
    }
}

