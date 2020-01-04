package com.example.shiftter;

public class Shift {
    private String userName;
    private String date;
    private String clockIn;
    private String clockOut;

    public Shift(){
    }
    public Shift(String userName, String date, String clockIn, String clockOut){
        this.userName = userName;
        this.date = date;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClockIn() {
        return clockIn;
    }

    public void setClockIn(String clockIn) {
        this.clockIn = clockIn;
    }

    public String getClockOut() {
        return clockOut;
    }

    public void setClockOut(String clockOut) {
        this.clockOut = clockOut;
    }
}
