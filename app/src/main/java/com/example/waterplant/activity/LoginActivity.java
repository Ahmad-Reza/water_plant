package com.example.waterplant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.waterplant.R;
import com.example.waterplant.utilities.ResourceUtility;

public class LoginActivity extends AppCompatActivity {
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        String userName = ResourceUtility.getUserPreferences(this, LoginActivity.USERNAME);
        String password = ResourceUtility.getUserPreferences(this, LoginActivity.PASSWORD);
    }
}