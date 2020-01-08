package com.example.shiftter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Functions {

    private static DatabaseReference db = FirebaseDatabase.getInstance().getReference();


    public static void AddGroupMember(String groupID, User userToAdd, String position, String salary){
        String codedEmail = encodeUserEmail(userToAdd.getEmail());
        String memberFullName = userToAdd.getFirstName() + " " + userToAdd.getLastName();
        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = dt.format(date);
        GroupMember groupMember = new GroupMember(userToAdd.getEmail(), memberFullName, position, salary, stringDate);
        db.child("WorkGroups").child(groupID).child("ListOfMembers").child(codedEmail).setValue(groupMember);
        WGToShiftID wgToShiftID = new WGToShiftID(db.push().getKey());
        db.child("Members").child(codedEmail).child(groupID).setValue(wgToShiftID);
    }

    public static void DeleteGroupMember(WorkGroup workGroup, String codedMemberEmail){
        db.child("WorkGroups")
                .child(workGroup.getGroupKey())
                .child("ListOfMembers")
                .child(codedMemberEmail).removeValue();
        db.child("Members")
                .child(codedMemberEmail)
                .child(workGroup.getGroupKey()).removeValue();
    }


    public static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    public static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }


}
