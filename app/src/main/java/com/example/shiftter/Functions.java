package com.example.shiftter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Functions {

    private static DatabaseReference db = FirebaseDatabase.getInstance().getReference();


    public static boolean AddGroupMember(String groupID, WorkGroup workGroup, GroupMember groupMember){
        String codedEmail = groupMember.getMemberEmail();
        db.child("WorkGroups").child(groupID).child("ListOfMembers").child(codedEmail).setValue(groupMember);
        db.child("Members").child(codedEmail).child(groupID).setValue(workGroup);
        return true;
    }

    public static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    public static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }


}
