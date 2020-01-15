package com.example.shiftter;

import android.app.Dialog;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftter.ui.shifts.ShiftsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;

public class Ad_RecyclerView_Shifts extends RecyclerView.Adapter<Ad_RecyclerView_Shifts.ViewHolderShifts> {

    private List<Shift> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private String MemberShiftEmail;

    public Ad_RecyclerView_Shifts(Context context, List<Shift> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolderShifts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recyclerview_shifts, parent, false);
        return new ViewHolderShifts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderShifts holder, int position) {
        Shift order = mData.get(position);
        holder.date.setText(order.getDate());
        holder.clockIn.setText(order.getClockIn());
        holder.clockOut.setText(order.getClockOut());
        holder.hours.setText(order.getHoursForShift());
        holder.wage.setText(new DecimalFormat("##.##").format(order.getWage()));
        MemberShiftEmail = order.getEmail();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolderShifts extends RecyclerView.ViewHolder {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        TextView date,clockIn,clockOut,hours,wage;
        String CurrShiftId = "";

        private Dialog popup = new Dialog(mContext);

        public ViewHolderShifts(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.TextViewDate);
            clockIn = itemView.findViewById(R.id.TextViewClockIn);
            clockOut = itemView.findViewById(R.id.TextViewClockOut);
            hours = itemView.findViewById(R.id.TextViewHours);
            wage = itemView.findViewById(R.id.TextViewWage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CurrentUser.getUserEmail().equals(CurrentGroup.getGroupManagerID())){
                        popup.setContentView(R.layout.edit_members_shifts_details);
                        Button SaveChanges = (Button) popup.findViewById(R.id.editMembersShiftDetails_SaveChanges);
                        Button DeleteShift = (Button) popup.findViewById(R.id.editMembersShiftDetails_DeleteShift);
                        EditText editTextDate = (EditText) popup.findViewById(R.id.editMembersShiftDetails_Date);
                        EditText editTextClockIn = (EditText) popup.findViewById(R.id.editMembersShiftDetails_ClockIn);
                        EditText editTextClockOut = (EditText) popup.findViewById(R.id.editMembersShiftDetails_ClockOut);

                        SaveChanges.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String editDate = editTextDate.getText().toString().trim();
                                String editClockIn = editTextClockIn.getText().toString().trim();
                                String editClockOut = editTextClockOut.getText().toString().trim();

                                if (TextUtils.isEmpty(editDate)){
                                    editTextDate.setError("Please enter date");
                                    editTextDate.requestFocus();
                                }else if (TextUtils.isEmpty(editClockIn)){
                                    editTextClockIn.setError("Please enter clock in");
                                    editTextClockIn.requestFocus();
                                }else if (TextUtils.isEmpty(editClockOut)){
                                    editTextClockOut.setError("Please enter clock out");
                                    editTextClockOut.requestFocus();
                                }else{
                                    Functions.UpdateMemberShift(MemberShiftEmail,date.getText().toString(),editDate,editClockIn,editClockOut);
                                    popup.dismiss();
                                    Toast.makeText(mContext,"Shift data changed", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                        DeleteShift.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popup.dismiss();
                                Functions.DeleteMemberShift(MemberShiftEmail,date.getText().toString());
                                Toast.makeText(mContext,"The shift was successfully deleted",Toast.LENGTH_LONG).show();
                            }
                        });


                    }else{
                        Intent calendar = new Intent();
                        ComponentName cn
                                = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
                        calendar.setComponent(cn);
                        mContext.startActivity(calendar);
                    }
                    popup.show();
                }
            });
        }

        public void getShiftId(String userCodedEmail,String GroupId){
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    WGToShiftID wgToShiftID = dataSnapshot.child("Members").child(userCodedEmail).child(GroupId).getValue(WGToShiftID.class);
                    CurrShiftId = wgToShiftID.getShiftID();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });
        }
    }

}
