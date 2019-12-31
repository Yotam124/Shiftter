package com.example.shiftter;

public class GroupMember {

    private String MemberEmail;
    private String position;
    private String salary;
    private String entryDate;

    public GroupMember(){}

    public GroupMember(String MemberEmail, String position, String salary, String entryDate) {
        this.MemberEmail = MemberEmail;
        this.position = position;
        this.salary = salary;
        this.entryDate = entryDate;
    }

    public String getMemberEmail() {
        return MemberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.MemberEmail = memberEmail;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }
}
