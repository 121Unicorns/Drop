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

public class SignupActivity extends AppCompatActivity {
    private Button btnOpenLogin, btnSignup, btnGSignup;
    private TextView tvSkip;
    private EditText etName, etEmail, etPassword, etCPassword;
    private String strName, strEmail, strPassword, strCPassword, strTemp;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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

        strTemp = getIntent().getStringExtra("loginemail");
        if (strTemp != null){
            etEmail.setText(strTemp);
        }

        btnOpenLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = etEmail.getText().toString();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                if (strEmail.length() > 0){
                    intent.putExtra("signupemail", strEmail);
                }
                startActivity(intent);
                finish();
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WARN USERS THAT
                new MaterialAlertDialogBuilder(SignupActivity.this)
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

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = etName.getText().toString();
                strEmail = etEmail.getText().toString();
                strPassword = etPassword.getText().toString();
                strCPassword = etCPassword.getText().toString();

                if (strName.length() ==0){
                    Snackbar.make(relativeLayout, "Name cannot be empty!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (strEmail.length() ==0){
                    Snackbar.make(relativeLayout, "Email cannot be empty!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (strPassword.length() ==0){
                    Snackbar.make(relativeLayout, "Password cannot be empty!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (strCPassword.length() ==0){
                    Snackbar.make(relativeLayout, "Please confirm password!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (!strCPassword.equals(strPassword)){
                    Snackbar.make(relativeLayout, "Passwords do not match!", BaseTransientBottomBar.LENGTH_LONG).show();
                }
                Snackbar.make(relativeLayout, "Success!", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

        btnGSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initViews(){
        btnOpenLogin = findViewById(R.id.btn_openlogin);
        btnSignup = findViewById(R.id.btn_signup);
        btnGSignup = findViewById(R.id.btn_gsignup);
        tvSkip = findViewById(R.id.tv_skip);
        etName = findViewById(R.id.et_fullname);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etCPassword = findViewById(R.id.et_cpassword);
        relativeLayout = findViewById(R.id.signuplayout);
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