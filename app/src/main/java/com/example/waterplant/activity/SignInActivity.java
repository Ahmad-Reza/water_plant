package com.example.waterplant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterplant.R;
import com.example.waterplant.dataBase.UserDetailsDBHandler;
import com.example.waterplant.model.UserDetailsModel;
import com.example.waterplant.utilities.ResourceUtility;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.List;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        View loadingLayout = findViewById(R.id.loading_layout);

        String userPreferences = ResourceUtility.getUserPreferences(this, SignUpActivity.USER_DETAIL_PREFERENCES);
        if (userPreferences != null && !userPreferences.isEmpty()) {
            Gson gson = new Gson();
            UserDetailsModel existenceUser = gson.fromJson(userPreferences, UserDetailsModel.class);

            UserDetailsDBHandler userData = new UserDetailsDBHandler(this);
            List<UserDetailsModel> userDetailsModels = userData.fetchUserDetails();
            for (UserDetailsModel userDetails : userDetailsModels) {
                if (userDetails.getUsername().equals(existenceUser.getUsername())
                        && userDetails.getPassword().equals(existenceUser.getPassword())) {
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
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
                loadingLayout.setVisibility(View.VISIBLE);

                UserDetailsDBHandler userData = new UserDetailsDBHandler(this);
                List<UserDetailsModel> userDetailsModels = userData.fetchUserDetails();
                if (userDetailsModels != null && !userDetailsModels.isEmpty()) {
                    for (UserDetailsModel userDetailsModel : userDetailsModels) {
                        if (userDetailsModel.getUsername().equals(userName) && userDetailsModel.getPassword().equals(password)) {
                            ResourceUtility.clearPreferences(this);

                            String userDetailPreference = new Gson().toJson(userDetailsModel);
                            ResourceUtility.updateUserPreferences(this, SignUpActivity.USER_DETAIL_PREFERENCES, userDetailPreference);

                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        if (userDetailsModels.indexOf(userDetailsModel) == userDetailsModels.size() - 1) {
                            passwordLayout.setError(getString(R.string.invalid_authorization_error));
                        }
                    }
                } else {
                    passwordLayout.setError(getString(R.string.invalid_authorization_error));
                }
                loadingLayout.setVisibility(View.GONE);
            }
        });

        Button signUp = findViewById(R.id.signup_button);
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}