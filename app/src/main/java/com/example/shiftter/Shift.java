package com.example.shiftter;

import java.sql.Time;
import java.util.Date;

public class Shift {
    private String userName;
    private WorkGroup workGroup;
    private Date date;
    private Time clockIn;
    private Time clockOut;

    public Shift(){
    }
    public Shift(String userName, WorkGroup workGroup, Date date, Time clockIn, Time clockOut){
        this.userName = userName;
        this.workGroup = workGroup;
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

    public Time getClockIn() {
        return clockIn;
    }

    public void setClockIn(Time clockIn) {
        this.clockIn = clockIn;
    }

    public Time getClockOut() {
        return clockOut;
    }

    public void setClockOut(Time clockOut) {
        this.clockOut = clockOut;
    }
}
