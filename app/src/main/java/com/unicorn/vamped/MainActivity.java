package com.unicorn.vamped;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    BottomNavigationView navigation;
    ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).addToBackStack(null).commit();

        initViews();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }

    private void initViews(){
        btnMenu = findViewById(R.id.btn_menu);
        navigation = findViewById(R.id.bottom_nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.calendar:
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();
                    return true;
                case R.id.community:
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new CommunityFragment()).commit();
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        //---------------GETTING PREFERENCES FOR ALL THE OTHER DATA-------------------------
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String defPeriodLength = sharedPreferences.getString("periodlength", "5");
        String defLutealLength = sharedPreferences.getString("luteallength", "12");
        String defCycleLength = sharedPreferences.getString("cyclelength", "28");

        //---------------GETTING PREFERENCES FOR DARK MODE CAUSE IT REFUSES TO WORK-------------------------
        SharedPreferences sh = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        int isDarkModeOn = sh.getInt("isDMOn", AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(isDarkModeOn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }
}