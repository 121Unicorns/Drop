package com.unicorn.vamped;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin, btnOpenSignup, btnForgotPassword, btnGLogin;
    private TextView tvSkip;
    private EditText etEmail, etPassword;
    private String strEmail, strPassword, strTemp;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        if (restorePrefData()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

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

        strTemp = getIntent().getStringExtra("signupemail");
        if (strTemp != null){
            etEmail.setText(strTemp);
        }

        btnOpenSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = etEmail.getText().toString();
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                if (strEmail.length() > 0){
                    intent.putExtra("loginemail", strEmail);
                }
                startActivity(intent);
                finish();
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WARN USERS THAT
                new MaterialAlertDialogBuilder(LoginActivity.this)
                        .setTitle("Skip Registration?")
                        .setMessage("Vamped won't be able to automatically save your data to the cloud. " +
                                "You will be responsible for your own data. Do you want to continue?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                savePrefsData();
                                if (restorePrefData()) {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = etEmail.getText().toString();
                strPassword = etPassword.getText().toString();

                if (strEmail.length() ==0){
                    Snackbar.make(relativeLayout, "Email cannot be empty!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (strPassword.length() ==0){
                    Snackbar.make(relativeLayout, "Password cannot be empty!", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void initViews() {
        btnLogin = findViewById(R.id.btn_signin);
        btnLogin = findViewById(R.id.btn_gsignin);
        btnOpenSignup = findViewById(R.id.btn_opensignup);
        btnForgotPassword = findViewById(R.id.btn_forgotpass);
        tvSkip = findViewById(R.id.tv_skip);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        relativeLayout = findViewById(R.id.loginlayout);
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isSkipSelected = pref.getBoolean("isSkipSelected", false);
        return isSkipSelected;
    }
    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isSkipSelected", true);
        editor.commit();
    }

}