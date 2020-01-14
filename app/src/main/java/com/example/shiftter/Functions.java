package com.example.shiftter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Functions {

    private static DatabaseReference db = FirebaseDatabase.getInstance().getReference();


    //Add Group Member
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

    //Delete Group Member
    public static void DeleteGroupMember(WorkGroup workGroup, String codedMemberEmail){
        db.child("WorkGroups")
                .child(workGroup.getGroupKey())
                .child("ListOfMembers")
                .child(codedMemberEmail).removeValue();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WGToShiftID wgToShiftID = dataSnapshot.child("Members")
                        .child(codedMemberEmail)
                        .child(workGroup.getGroupKey()).getValue(WGToShiftID.class);
                db.child("Members")
                        .child(codedMemberEmail)
                        .child(workGroup.getGroupKey()).removeValue();
                if(dataSnapshot.child("Shifts").child(wgToShiftID.getShiftID()).exists()) {
                    db.child("Shifts").child(wgToShiftID.getShiftID()).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Update Group Member
    public static void UpdateMember(String groupID, String codedMemberEmail, String position, String salary){
        db.child("WorkGroups").child(groupID).child("ListOfMembers").child(codedMemberEmail).child("salary").setValue(salary);
        db.child("WorkGroups").child(groupID).child("ListOfMembers").child(codedMemberEmail).child("position").setValue(position);

    }


    public static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    public static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }


}
