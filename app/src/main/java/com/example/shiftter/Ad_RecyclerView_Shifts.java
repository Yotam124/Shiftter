package com.example.shiftter;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolderShifts extends RecyclerView.ViewHolder {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        TextView date,clockIn,clockOut,hours;

        public ViewHolderShifts(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.TextViewDate);
            clockIn = itemView.findViewById(R.id.TextViewClockIn);
            clockOut = itemView.findViewById(R.id.TextViewClockOut);
            hours = itemView.findViewById(R.id.TextViewHours);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent calendar = new Intent();
                    ComponentName cn
                            = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
                    calendar.setComponent(cn);
                    mContext.startActivity(calendar);
                }
            });
        }
    }


}
