package com.vrgc.eguidance.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vrgc.eguidance.Model.UserModel;
import com.vrgc.eguidance.R;

import java.util.HashMap;
import java.util.List;

public class UserManageAdapter extends RecyclerView.Adapter<UserManageAdapter.UserViewHolder> {

    List<UserModel> userList;
    Context context;

    public UserManageAdapter(List<UserModel> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.txt1.setText(user.name + " (" + user.role + ")");
        holder.txt2.setText(user.email + " | " + user.phone);

        holder.itemView.setOnClickListener(v -> showUpdateDeleteDialog(user));
    }

    private void showUpdateDeleteDialog(UserModel user) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_user, null);
        EditText edtName = dialogView.findViewById(R.id.editUserName);
        EditText edtPhone = dialogView.findViewById(R.id.editUserPhone);

        edtName.setText(user.name);
        edtPhone.setText(user.phone);

        new AlertDialog.Builder(context)
                .setTitle("Edit/Delete User")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newName = edtName.getText().toString().trim();
                    String newPhone = edtPhone.getText().toString().trim();
                    updateUser(user.uid, newName, newPhone);
                })
                .setNegativeButton("Delete", (dialog, which) -> deleteUser(user.uid))
                .setNeutralButton("Cancel", null)
                .show();
    }

    private void updateUser(String uid, String name, String phone) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid);
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("phone", phone);
        ref.updateChildren(updates).addOnSuccessListener(unused ->
                Toast.makeText(context, "User updated", Toast.LENGTH_SHORT).show()
        );
    }

    private void deleteUser(String uid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid);
        ref.removeValue().addOnSuccessListener(unused ->
                Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txt1, txt2;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(android.R.id.text1);
            txt2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
