package com.example.shiftter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Ad_RecyclerView extends RecyclerView.Adapter<Ad_RecyclerView.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    // data is passed into the constructor
    public Ad_RecyclerView(Context context, List<String> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String order = mData.get(position);
        holder.myTextView.setText(order);
    }

    // total number of items
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                String groupName = myTextView.getText().toString();

                    // TODO: 12/19/2019 retriving data for recycleView (workgroup list)
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    WorkGroup workGroup;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.child("Members").child(CurrentUser.getUserCodedEmail()).getChildren()) {
                            workGroup = ds.getValue(WorkGroup.class);
                            if (workGroup.getGroupName().equals(groupName)){
                                break;
                            }
                        }

                        if(workGroup.getManagerEmail().equals(CurrentUser.getUserEmail())){

                            Intent intoGroupAsManager = new Intent(mContext, intoWorkGroupAsManager.class);
                            CurrentUser.setCurrentGroup(workGroup);
                            //intoGroupAsManager.putExtra("groupKEY", workGroup.getGroupKey());
                            mContext.startActivity(intoGroupAsManager);
                        }else{
                            Intent intoGroupAsMember = new Intent(mContext, intoWorkGroupAsMember.class);
                            CurrentUser.setCurrentGroup(workGroup);
                            //intoGroupAsMember.putExtra("groupKEY", workGroup.getGroupKey());
                            mContext.startActivity(intoGroupAsMember);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        }
    }

}
