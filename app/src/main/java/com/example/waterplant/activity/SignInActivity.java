package com.example.waterplant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterplant.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> onBackPressed());

        Button forgotBtn = findViewById(R.id.forgot_button);
        forgotBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        TextInputLayout userNameLayout = findViewById(R.id.username_layout);
        TextInputEditText userNameInput = findViewById(R.id.username_input);

        TextInputLayout passwordLayout = findViewById(R.id.password_layout);
        TextInputEditText passwordInput = findViewById(R.id.password_input);

        ExtendedFloatingActionButton signInBtn = findViewById(R.id.signin_button);
        signInBtn.setOnClickListener(view -> {
            String userName = userNameInput.getText() != null ? userNameInput.getText().toString() : "";
            String password = passwordInput.getText() != null ? passwordInput.getText().toString() : "";
            if (userName.isEmpty()) {
                userNameLayout.setError(getString(R.string.require_field_error));

            } else if (password.isEmpty()) {
                passwordLayout.setError(getString(R.string.require_field_error));

            } else {
                if (userName.equals("ahmad") && password.equals("ahmad//123")) {
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);

                } else {
                    passwordLayout.setError(getString(R.string.invalid_authorization_error));
                }
            }
        });

        Button signUp = findViewById(R.id.signup_button);
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}