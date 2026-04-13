package com.example.cinemora.ui;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cinemora.R;
import com.example.cinemora.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_register);

        EditText u = findViewById(R.id.etUsername);
        EditText p = findViewById(R.id.etPassword);
        Button btn = findViewById(R.id.btnRegister);

        DatabaseHelper db = new DatabaseHelper(this);

        btn.setOnClickListener(v->{
            if(db.register(u.getText().toString(), p.getText().toString())){
                Toast.makeText(this,"Berhasil Register",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}