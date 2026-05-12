package com.example.cinemora.ui;

import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cinemora.R;
import com.example.cinemora.api.ApiService;
import com.example.cinemora.api.RetrofitClient;
import com.example.cinemora.model.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etUser, etPass;
    // Data Supabase Anda
    private final String SB_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InF6c2hkb3R3amtlcmd6cm5tbm5vIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgyMzQzMzMsImV4cCI6MjA5MzgxMDMzM30.TuUHiUiCE2qhSmb1qCM3RhMxlpTkH1j0wGmiBkYhjEE";

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView btnReg = findViewById(R.id.btnToRegister);

        btnLogin.setOnClickListener(v -> {
            String username = etUser.getText().toString().trim();
            String password = etPass.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            loginToSupabase(username, password);
        });

        btnReg.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void loginToSupabase(String username, String password) {
        ApiService api = RetrofitClient.getSupabaseClient().create(ApiService.class);

        // Supabase menggunakan filter 'eq.nilai' untuk mencari data yang sama
        api.loginUser(SB_KEY, "Bearer " + SB_KEY, "eq." + username, "eq." + password)
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<User> users = response.body();

                            if (!users.isEmpty()) {
                                Toast.makeText(LoginActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                i.putExtra("username", username);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Username atau Password salah!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Gagal Login (Error: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.e("LoginError", t.getMessage());
                        Toast.makeText(LoginActivity.this, "Koneksi Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
