package com.example.shiftter;

public class GroupMember {

    private String MemberEmail;
    private String memberFullName;
    private String position;
    private String salary;
    private String entryDate;

    public GroupMember(){}

    public GroupMember(String MemberEmail, String memberFullName, String position, String salary, String entryDate) {
        this.MemberEmail = MemberEmail;
        this.memberFullName = memberFullName;
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

    public String getMemberFullName() {
        return memberFullName;
    }

    public void setMemberFullName(String memberFullName) {
        this.memberFullName = memberFullName;
    }
}
