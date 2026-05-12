package com.example.cinemora.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cinemora.R;
import com.example.cinemora.api.ApiService;
import com.example.cinemora.api.RetrofitClient;
import com.example.cinemora.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private final String SB_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InF6c2hkb3R3amtlcmd6cm5tbm5vIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgyMzQzMzMsImV4cCI6MjA5MzgxMDMzM30.TuUHiUiCE2qhSmb1qCM3RhMxlpTkH1j0wGmiBkYhjEE";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_register);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService api = RetrofitClient.getSupabaseClient().create(ApiService.class);
            User user = new User(username, password);

            api.registerUser(SB_KEY, "Bearer " + SB_KEY, user).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Berhasil Register!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.e("SupabaseError", "Error Code: " + response.code());
                        Toast.makeText(RegisterActivity.this, "Gagal Daftar (Code: " + response.code() + ")", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("SupabaseError", "Failure: " + t.getMessage());
                    Toast.makeText(RegisterActivity.this, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
