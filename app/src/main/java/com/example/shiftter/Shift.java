package com.example.shiftter;

import java.util.Date;

public class Shift {
    private String userName;
    private WorkGroup workGroup;
    private Date date;
    private String clockIn;
    private String clockOut;
    private String shiftID;

    public Shift(){
    }
    public Shift(String userName, WorkGroup workGroup, Date date, String clockIn, String clockOut, String shiftID){
        this.userName = userName;
        this.workGroup = workGroup;
        this.date = date;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.shiftID = shiftID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public WorkGroup getWorkGroup() {
        return workGroup;
    }

    public void setWorkGroup(WorkGroup workGroup) {
        this.workGroup = workGroup;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public String getShiftID() { return shiftID; }

    public void setShiftID(String shiftID) { this.shiftID = shiftID; }
}
