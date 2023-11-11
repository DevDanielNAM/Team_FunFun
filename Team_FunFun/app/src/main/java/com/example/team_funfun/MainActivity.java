package com.example.team_funfun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    CalendarFragment calendarFragment;
    CategoryFragment categoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        calendarFragment = new CalendarFragment();
        categoryFragment = new CategoryFragment();

        NavigationBarView navigationBarView = findViewById(R.id.bottomNavBar);

        // 초기 Bottom Navigation Bar 아이콘 Home으로 선택
        navigationBarView.setSelectedItemId(R.id.home);

        // Home Fragment로 시작
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        // bottomNavBar로부터 이벤트를 전달받아 ID값에 따른 Fragment 처리
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                } else if(item.getItemId() == R.id.calendar) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, calendarFragment).commit();
                        return true;
                } else if(item.getItemId() == R.id.category) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, categoryFragment).commit();
                        return true;
                } else {
                    return false;
                }
            }
        });
    }
}

