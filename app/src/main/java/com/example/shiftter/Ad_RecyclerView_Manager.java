package com.example.shiftter;

import android.app.Dialog;
import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Ad_RecyclerView_Manager extends RecyclerView.Adapter<Ad_RecyclerView_Manager.ViewHolderManager>  {

    private List<GroupMember> mData;
    private LayoutInflater mInflater;
    private Context mContext;


    Ad_RecyclerView_Manager(Context context, List<GroupMember> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolderManager onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recyclerview_into_work_group_as__manager, parent, false);
        return new ViewHolderManager(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderManager holder, int position) {
        GroupMember order = mData.get(position);
        holder.fullName.setText(order.getMemberFullName());
        holder.position.setText(order.getPosition());
        holder.email.setText(order.getMemberEmail());
        holder.entryDate.setText(order.getEntryDate());
        holder.salary.setText(order.getSalary());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolderManager extends RecyclerView.ViewHolder {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        private Dialog popup = new Dialog(mContext);

        TextView fullName, email,position, salary, entryDate;

        EditText editPosition, editSalary;
        Button deleteMember, editMember;
        ViewHolderManager(View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.TextView1);
            email = itemView.findViewById(R.id.TextView2);
            position = itemView.findViewById(R.id.TextView3);
            salary = itemView.findViewById(R.id.TextView4);
            entryDate = itemView.findViewById(R.id.TextView5);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 12/19/2019 retriving data for recycleView (Manager) 
                    popup.setContentView(R.layout.user_handel_popup);
                    deleteMember = (Button) popup.findViewById(R.id.delete_member);
                    editMember = (Button) popup.findViewById(R.id.edit_member);
                    editPosition = (EditText) popup.findViewById(R.id.edit_position);
                    editSalary = (EditText) popup.findViewById(R.id.edit_salary);
                    String memberEmail = email.getText().toString();
                    String codedMemberEmail = Functions.encodeUserEmail(memberEmail);
                    // TODO: 12/22/2019 continue after dealing with add user
                    deleteMember.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                            Functions.DeleteGroupMember(CurrentUser.getCurrentGroup(), codedMemberEmail);
                            // TODO: 12/26/2019 Notify dataChanged
                        }
                    });


                    editMember.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String editPositionS = editPosition.getText().toString().trim();
                            String editSalaryS = editSalary.getText().toString().trim();
                            if (TextUtils.isEmpty(editPositionS)){
                                editPosition.setError("Please enter position");
                                editPosition.requestFocus();
                            }else if (TextUtils.isEmpty(editSalaryS)){
                                editSalary.setError("Please enter salary");
                                editSalary.requestFocus();
                            }else {
                                db.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Functions.DeleteGroupMember(CurrentUser.getCurrentGroup(),Functions.encodeUserEmail(memberEmail));
                                        User mUser = dataSnapshot.child("Users").child(Functions.encodeUserEmail(memberEmail)).getValue(User.class);
                                        Functions.AddGroupMember(CurrentUser.getCurrentGroup().getGroupKey(), mUser,editPositionS,editSalaryS);
                                        popup.dismiss();
                                        Toast.makeText(mContext,"Member data changed", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }


                        }
                    });
                    popup.show();
                }
            });



        }

    }
}
