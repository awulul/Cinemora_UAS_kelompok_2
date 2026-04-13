package com.example.cinemora.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import com.example.cinemora.R;
import com.example.cinemora.database.DatabaseHelper;

public class AccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b){
        View v = i.inflate(R.layout.fragment_account, c, false);

        TextView tv = v.findViewById(R.id.tvUsername);
        TextView tvInitial = v.findViewById(R.id.tvInitial);
        View btnChangePassword = v.findViewById(R.id.btnChangePassword);
        Button btnLogout = v.findViewById(R.id.btnLogout);

        // Get username from Intent (passed from LoginActivity)
        String user = getActivity().getIntent().getStringExtra("username");
        if (user != null && !user.isEmpty()) {
            tv.setText(user);
            tvInitial.setText(user.substring(0, 1).toUpperCase());
        }

        DatabaseHelper db = new DatabaseHelper(getContext());

        btnChangePassword.setOnClickListener(x -> {
            // Because the new UI uses a menu row instead of an EditText/Button, 
            // you might want to show a Dialog to input the new password.
            showUpdatePasswordDialog(user, db);
        });

        btnLogout.setOnClickListener(x -> {
            // Navigate back to LoginActivity
            // Note: Assuming LoginActivity is in com.example.cinemora.ui or similar
            // If it's in a different package, you'll need the correct import.
            try {
                Class<?> loginClass = Class.forName("com.example.cinemora.ui.LoginActivity");
                startActivity(new Intent(getActivity(), loginClass));
                getActivity().finish();
            } catch (ClassNotFoundException e) {
                Toast.makeText(getContext(), "LoginActivity not found", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void showUpdatePasswordDialog(String username, DatabaseHelper db) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Update Password");

        final EditText input = new EditText(getContext());
        input.setHint("Enter new password");
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newPass = input.getText().toString();
            if (!newPass.isEmpty()) {
                db.updatePassword(username, newPass);
                Toast.makeText(getContext(), "Password Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
