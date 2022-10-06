package com.example.waterplant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.waterplant.R;
import com.example.waterplant.utilities.ResourceUtility;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO We will fix it later
//        String userName = ResourceUtility.getUserPreferences(this, LoginActivity.USERNAME);
//        String password = ResourceUtility.getUserPreferences(this, LoginActivity.PASSWORD);

        ImageView backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> onBackPressed());

        TextInputLayout userNameLayout = findViewById(R.id.username_layout);
        TextInputEditText userNameInput = findViewById(R.id.username_input);

        TextInputLayout passwordLayout = findViewById(R.id.password_layout);
        TextInputEditText passwordInput = findViewById(R.id.password_input);

        ExtendedFloatingActionButton signUpBtn = findViewById(R.id.signin_button);
        signUpBtn.setOnClickListener(view -> {
            String userName = userNameInput.getText() != null ? userNameInput.getText().toString() : "";
            String password = passwordInput.getText() != null ? passwordInput.getText().toString() : "";

            if (userName.isEmpty()) {
                userNameLayout.setError(getString(R.string.require_field_error));

            } else if (password.isEmpty()) {
                passwordLayout.setError(getString(R.string.require_field_error));

            } else {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}