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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        View view = mInflater.inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolderManager(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderManager holder, int position) {
        GroupMember order = mData.get(position);
        holder.fullName.setText("full name");
        holder.position.setText("position: " + order.getPosition());
        holder.email.setText("email: " + order.getMemberEmail());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolderManager extends RecyclerView.ViewHolder {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        TextView fullName, email,position;
        private Dialog popup = new Dialog(mContext);
        EditText editPosition, editSalary;
        Button deleteMember, editMember;
        ViewHolderManager(View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.TextView1);
            email = itemView.findViewById(R.id.TextView2);
            position = itemView.findViewById(R.id.TextView3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 12/19/2019 retriving data for recycleView (Manager) 
                    popup.setContentView(R.layout.user_handel_popup);
                    deleteMember = (Button) popup.findViewById(R.id.delete_member);
                    editMember = (Button) popup.findViewById(R.id.edit_member);
                    editPosition = (EditText) popup.findViewById(R.id.edit_position);
                    editSalary = (EditText) popup.findViewById(R.id.edit_salary);
                    String memberEmail = fullName.getText().toString();
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


                        }
                    });
                    popup.show();
                }
            });



        }

    }
}
