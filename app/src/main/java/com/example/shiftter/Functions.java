package com.example.shiftter;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
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

    public static void UpdateMemberShift(String memberEmail,String date, String newDate, String newClockIn, String newClockOut){
        String codedEmail = encodeUserEmail(memberEmail);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WGToShiftID wgToShiftID = dataSnapshot.child("Members").child(codedEmail).child(CurrentGroup.getGroupID())
                        .getValue(WGToShiftID.class);
                Shift shift = dataSnapshot.child("Shifts").child(wgToShiftID.getShiftID()).child(date).getValue(Shift.class);
                DeleteMemberShift(memberEmail,date);
                shift.setDate(newDate);
                shift.setClockIn(newClockIn);
                shift.setClockOut(newClockOut);

                Date startDate = null,endDate = null;
                String hoursForShift;

                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                try {
                    endDate = format.parse(newClockOut);
                    startDate = format.parse(newClockIn);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long difference = endDate.getTime() - startDate.getTime();

                if(difference<0)
                {
                    Date dateMin = null,dateMax = null;
                    try {
                        dateMin = format.parse("00:00:00");
                        dateMax = format.parse("24:00:00");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    difference=(dateMax.getTime()-startDate.getTime())+(endDate.getTime()-dateMin.getTime());

                }
                int days = (int) (difference / (1000*60*60*24));
                int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);

                if (hours < 10){
                    if (min <10){
                        hoursForShift = "0" + hours + ":0" + min;
                    }else{
                        hoursForShift = "0" + hours + ":" + min;
                    }
                }else{
                    if (min <10){
                        hoursForShift = hours + ":0" + min;
                    }else{
                        hoursForShift = hours + ":" + min;
                    }
                }
                shift.setHoursForShift(hoursForShift+"");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GroupMember gm = dataSnapshot.child("WorkGroups").child(CurrentGroup.getGroupID())
                                .child("ListOfMembers").child(encodeUserEmail(memberEmail)).getValue(GroupMember.class);
                        double wage = Double.parseDouble(gm.getSalary());
                        wage = (wage / 60 * min) + (wage * hours);
                        shift.setWage(wage);
                        if(!dataSnapshot.child("Shifts").child(wgToShiftID.getShiftID()).child(newDate).exists()){
                            db.child("Shifts").child(wgToShiftID.getShiftID()).child(newDate).setValue(shift);
                        }else if(!dataSnapshot.child("Shifts").child(wgToShiftID.getShiftID()).child(newDate+"NO2").exists()){
                            shift.setDate(newDate+"NO2");
                            db.child("Shifts").child(wgToShiftID.getShiftID()).child(shift.getDate() ).setValue(shift);
                        }
                    };

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void DeleteMemberShift(String memberEmail, String date){
        String codedEmail = encodeUserEmail(memberEmail);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WGToShiftID wg = dataSnapshot.child("Members").child(codedEmail).child(CurrentGroup.getGroupID())
                        .getValue(WGToShiftID.class);
                db.child("Shifts").child(wg.getShiftID()).child(date).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    public static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }


}
