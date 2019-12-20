package com.example.shiftter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Ad_RecyclerView_Manager extends RecyclerView.Adapter<Ad_RecyclerView_Manager.ViewHolderManager>  {

    private List<String> mData;
    private LayoutInflater mInflater;
    private Context mContext;


    Ad_RecyclerView_Manager(Context context, List<String> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolderManager onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolderManager(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderManager holder, int position) {
        String order = mData.get(position);
        holder.myTextView.setText(order);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolderManager extends RecyclerView.ViewHolder {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        TextView myTextView;
        private Dialog popup = new Dialog(mContext);
        EditText editPosition, editSalary;
        Button deleteMember, editMember;
        ViewHolderManager(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 12/19/2019 retriving data for recycleView (Manager) 
                    /*popup.setContentView(R.layout.user_handel_popup);
                    deleteMember = (Button) popup.findViewById(R.id.delete_member);
                    editMember = (Button) popup.findViewById(R.id.edit_member);
                    editPosition = (EditText) popup.findViewById(R.id.edit_position);
                    editSalary = (EditText) popup.findViewById(R.id.edit_salary);

                    String memberName = myTextView.getText().toString();
                    CurrentUser.setCurrentMember(memberName);

                    deleteMember.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                            db.child("WorkGroups").child(CurrentUser.getEmail()).child(CurrentUser.getCurrentJob())
                                    .child("Members").child(memberName).removeValue();
                            db.child("Users").child(CurrentUser.getCurrentMember()).child("Groups")
                                    .child(CurrentUser.getCurrentJob()).removeValue();
                        }
                    });

                    editMember.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    popup.show();*/
                }
            });

        }
    }
}
