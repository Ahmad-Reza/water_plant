package com.example.waterplant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterplant.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> onBackPressed());

        TextInputLayout userNameLayout = findViewById(R.id.username_layout);
        TextInputLayout emailLayout = findViewById(R.id.email_layout);
        TextInputLayout passwordLayout = findViewById(R.id.password_layout);
        TextInputLayout repeatPassLayout = findViewById(R.id.repeat_password_layout);

        TextInputEditText userNameInput = findViewById(R.id.username_input);
        TextInputEditText emailInput = findViewById(R.id.email_input);
        TextInputEditText passwordInput = findViewById(R.id.password_input);
        TextInputEditText repeatPassInput = findViewById(R.id.repeat_password_input);

        ExtendedFloatingActionButton signUpBtn = findViewById(R.id.signup_button);
        signUpBtn.setOnClickListener(view -> {
            String userName = userNameInput.getText() != null ? userNameInput.getText().toString() : "";
            String email = emailInput.getText() != null ? emailInput.getText().toString() : "";
            String password = passwordInput.getText() != null ? passwordInput.getText().toString() : "";
            String repeatPass = repeatPassInput.getText() != null ? repeatPassInput.getText().toString() : "";

            if (userName.isEmpty()) {
                userNameLayout.setError(getString(R.string.require_field_error));

            } else if (email.isEmpty()) {
                emailLayout.setError(getString(R.string.require_field_error));

            } else if (password.isEmpty()) {
                passwordLayout.setError(getString(R.string.require_field_error));

            } else if (repeatPass.isEmpty()) {
                repeatPassLayout.setError(getString(R.string.require_field_error));

            } else {
                if (repeatPass.equals(password)) {
                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(intent);

                } else {
                    repeatPassLayout.setError(getString(R.string.not_match_error));
                }
            }
        });

        Button signIn = findViewById(R.id.signin_button);
        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }
}