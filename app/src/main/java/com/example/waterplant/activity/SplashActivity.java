package com.example.waterplant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterplant.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ExtendedFloatingActionButton signUpBtn = findViewById(R.id.signup_button);
        signUpBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        Button signInBtn = findViewById(R.id.signin_button);
        signInBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}