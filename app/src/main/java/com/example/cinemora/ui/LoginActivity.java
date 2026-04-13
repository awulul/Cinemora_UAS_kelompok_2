package com.example.cinemora.ui;

import android.content.*;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cinemora.R;
import com.example.cinemora.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    EditText etUser, etPass;
    DatabaseHelper db;

    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView btnReg = findViewById(R.id.btnToRegister);

        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v->{
            if(db.login(etUser.getText().toString(), etPass.getText().toString())){
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("username", etUser.getText().toString());
                startActivity(i);
            } else {
                Toast.makeText(this,"Harus register dulu!",Toast.LENGTH_SHORT).show();
            }
        });

        btnReg.setOnClickListener(v->startActivity(new Intent(this, RegisterActivity.class)));
    }
}