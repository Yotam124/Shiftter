package com.example.shiftter;

import android.content.Context;
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

    Ad_RecyclerView_Shifts(Context context, List<Shift> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolderShifts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolderShifts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderShifts holder, int position) {
        Shift order = mData.get(position);
        holder.userName.setText("user name: " + order.getUserName());
        holder.date.setText("date: " + order.getDate());
        holder.clockIn.setText("clock in: " + order.getClockIn());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolderShifts extends RecyclerView.ViewHolder {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        TextView userName, date,clockIn,clockOut;

        public ViewHolderShifts(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.TextView1);
            date = itemView.findViewById(R.id.TextView2);
            clockIn = itemView.findViewById(R.id.TextView3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
