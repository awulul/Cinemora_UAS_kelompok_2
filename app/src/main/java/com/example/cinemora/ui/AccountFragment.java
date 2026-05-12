package com.example.cinemora.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.cinemora.R;
import com.example.cinemora.api.ApiService;
import com.example.cinemora.api.RetrofitClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private final String SB_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InF6c2hkb3R3amtlcmd6cm5tbm5vIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgyMzQzMzMsImV4cCI6MjA5MzgxMDMzM30.TuUHiUiCE2qhSmb1qCM3RhMxlpTkH1j0wGmiBkYhjEE";

    @Override
    public View onCreateView(@NonNull LayoutInflater i, ViewGroup c, Bundle b){
        View v = i.inflate(R.layout.fragment_account, c, false);

        TextView tv = v.findViewById(R.id.tvUsername);
        TextView tvInitial = v.findViewById(R.id.tvInitial);
        View btnChangePassword = v.findViewById(R.id.btnChangePassword);
        Button btnLogout = v.findViewById(R.id.btnLogout);

        String user = "";
        if (getActivity() != null && getActivity().getIntent() != null) {
            user = getActivity().getIntent().getStringExtra("username");
        }
        
        if (user != null && !user.isEmpty()) {
            tv.setText(user);
            tvInitial.setText(user.substring(0, 1).toUpperCase());
        }

        final String finalUser = user;
        btnChangePassword.setOnClickListener(x -> {
            if (finalUser != null && !finalUser.isEmpty()) {
                showUpdatePasswordDialog(finalUser);
            } else {
                Toast.makeText(getContext(), "User tidak terdeteksi!", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(x -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return v;
    }

    private void showUpdatePasswordDialog(String username) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_password, null);
        TextInputEditText etNewPassword = dialogView.findViewById(R.id.etNewPassword);

        new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setPositiveButton("UPDATE", (dialog, which) -> {
                    String newPass = etNewPassword.getText().toString().trim();
                    if (!newPass.isEmpty()) {
                        updatePasswordInSupabase(username, newPass);
                    } else {
                        Toast.makeText(getContext(), "Password baru tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("BATAL", null)
                .show();
    }

    private void updatePasswordInSupabase(String username, String newPassword) {
        ApiService api = RetrofitClient.getSupabaseClient().create(ApiService.class);

        Map<String, String> body = new HashMap<>();
        body.put("password", newPassword);

        api.updatePassword(SB_KEY, "Bearer " + SB_KEY, "eq." + username, body)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Password berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("SupabaseError", "Update Gagal: " + response.code());
                            Toast.makeText(getContext(), "Gagal update (Error: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Koneksi Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
