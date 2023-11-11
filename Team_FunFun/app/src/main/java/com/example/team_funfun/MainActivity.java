package com.example.team_funfun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    CalendarFragment calendarFragment;
    CategoryFragment categoryFragment;
Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        calendarFragment = new CalendarFragment();
        categoryFragment = new CategoryFragment();

        NavigationBarView navigationBarView = findViewById(R.id.bottomNavBar);
        navigationBarView.setSelectedItemId(R.id.home);

        // Start Home Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, homeFragment).commit();

        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, homeFragment).commit();
                        return true;
                } else if(item.getItemId() == R.id.calendar) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, calendarFragment).commit();
                        return true;
                } else if(item.getItemId() == R.id.category) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, categoryFragment).commit();
                        return true;
                } else {
                    return false;
                }
            }
        });
    }
}

