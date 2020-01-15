package com.example.shiftter;

public class Shift {
    private String email;
    private String date;
    private String clockIn;
    private String clockOut;
    private String hoursForShift;
    private double wage;

    public Shift(){
    }
    public Shift(String email, String date, String clockIn, String clockOut, String hoursForShift,double wage){
        this.email = email;
        this.date = date;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.hoursForShift = hoursForShift;
        this.wage = wage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getHoursForShift() {
        return hoursForShift;
    }

    public void setHoursForShift(String hoursForShift) {
        this.hoursForShift = hoursForShift;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }
}