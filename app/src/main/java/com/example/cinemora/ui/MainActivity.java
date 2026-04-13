package com.example.cinemora.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.cinemora.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav = findViewById(R.id.bottomNav);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new HomeFragment()).commit();

        nav.setOnItemSelectedListener(i->{
            Fragment f=null;
            if(i.getItemId()==R.id.menu_home) f=new HomeFragment();
            if(i.getItemId()==R.id.menu_explore) f=new ExploreFragment();
            if(i.getItemId()==R.id.menu_account) f=new AccountFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,f).commit();
            return true;
        });
    }
}