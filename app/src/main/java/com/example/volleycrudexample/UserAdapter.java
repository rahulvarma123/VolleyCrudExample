package com.example.volleycrudexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    List<UserModel> userList;
    LayoutInflater inflater;
    OnRowButtonClickListener onRowButtonClickListener;

    public UserAdapter(Context context, List<UserModel> userList, UsersListDisplayFragment fragment) {
        this.context = context;
        this.userList = userList;
        inflater = LayoutInflater.from(context);
        onRowButtonClickListener = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        UserModel user = userList.get(position);
        holder.tvId.setText(String.valueOf(user.getUserId()));
        holder.tvPersonName.setText(user.getUserName());
        holder.tvEmail.setText(user.getEmailId());
        holder.tvPassword.setText(user.getPassword());
        holder.tvPhoneNum.setText(user.getPhoneNum());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRowButtonClickListener.onEditClicked(position);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertMessage();

            }

            private void alertMessage() {
                UserModel user = userList.get(position);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete Item");
                alertDialog.setMessage("Do you want delete" + " " + user.getUserId() + " " + "item");
                alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onRowButtonClickListener.onDeleteClicked(position);
                    }
                });
                alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUserList(List<UserModel> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvPersonName, tvEmail, tvPassword, tvPhoneNum;
        ImageView ivDelete, ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvPersonName = itemView.findViewById(R.id.tvPersonName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPassword = itemView.findViewById(R.id.tvPassword);
            tvPhoneNum = itemView.findViewById(R.id.tvPhoneNum);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }
    }

    interface OnRowButtonClickListener {
        public void onEditClicked(int position);

        public void onDeleteClicked(int position);
    }
}
