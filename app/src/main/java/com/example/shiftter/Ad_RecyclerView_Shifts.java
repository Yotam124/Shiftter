package com.example.shiftter;

import android.app.Dialog;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

                    String lol =date.getText().toString();

                    Toast.makeText(mContext,lol + "",Toast.LENGTH_LONG).show();

                    if (CurrentGroup.getGroupManagerID() == CurrentUser.getUser().getUserID()){
                        popup.setContentView(R.layout.edit_members_shifts_details);
                        Button SaveChanges = (Button) popup.findViewById(R.id.editMembersShiftDetails_SaveChanges);
                        Button DeleteShift = (Button) popup.findViewById(R.id.editMembersShiftDetails_DeleteShift);
                        EditText editTextDate = (EditText) popup.findViewById(R.id.editMembersShiftDetails_Date);
                        EditText editTextClockIn = (EditText) popup.findViewById(R.id.editMembersShiftDetails_ClockIn);
                        EditText editTextClockOut = (EditText) popup.findViewById(R.id.editMembersShiftDetails_ClockOut);

                        DeleteShift.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popup.dismiss();
                                getShiftId(CurrentUser.getUserCodedEmail(),CurrentGroup.getGroupID());
                                //Functions.DeleteMemberShift(CurrShiftId,);
                            }
                        });
                    }
                    Intent calendar = new Intent();
                    ComponentName cn
                            = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
                    calendar.setComponent(cn);
                    mContext.startActivity(calendar);
                }
            });
        }

        public void getShiftId(String userEmail,String GroupId){
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    WGToShiftID s = dataSnapshot.child("Members").child(userEmail).child(GroupId).getValue(WGToShiftID.class);
                    CurrShiftId = s.getShiftID();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });
        }
    }

}
