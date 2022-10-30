package com.example.waterplant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterplant.R;
import com.example.waterplant.dataBase.UserDetailsDBHandler;
import com.example.waterplant.model.UserDetailsModel;
import com.example.waterplant.utilities.ResourceUtility;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class SignUpActivity extends AppCompatActivity {
    public static final String USER_DETAIL_PREFERENCES = "USER_DETAIL_PREFERENCES";

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
                    UserDetailsDBHandler table = new UserDetailsDBHandler(this);
                    table.createUserDetails(userName, email, password, isSuccessful -> {
                        UserDetailsModel userDetailsModel = new UserDetailsModel(userName, email, password);
                        Toast.makeText(this, R.string.successful_signup_msg, Toast.LENGTH_SHORT).show();

                        ResourceUtility.clearPreferences(this);
                        String userPreferences = new Gson().toJson(userDetailsModel);
                        ResourceUtility.updateUserPreferences(this, USER_DETAIL_PREFERENCES, userPreferences);

                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    });
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