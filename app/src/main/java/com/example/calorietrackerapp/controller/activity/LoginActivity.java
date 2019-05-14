package com.example.calorietrackerapp.controller.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.model.entity.AppUser;
import com.example.calorietrackerapp.model.service.UserService;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private EditText userNameText;
    private EditText passwordText;
    private TextInputLayout userNameLayout;
    private TextInputLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        userNameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        userNameLayout = findViewById(R.id.inputLayoutUsername);
        passwordLayout = findViewById(R.id.inputLayoutPassword);

        userNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userNameText.length() != 0) {
                    userNameLayout.setErrorEnabled(false);
                }
            }
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (passwordText.length() != 0) {
                    passwordLayout.setErrorEnabled(false);
                }
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_icons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myLayout), "Wrong username or password. Please try again.", Snackbar.LENGTH_SHORT);
        switch (item.getItemId()) {
            case R.id.tick:
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                String username = userNameText.getText().toString().trim();
                String password = passwordText.getText().toString();
                boolean pass = true;
                if (username.length() == 0) {
                    pass = false;
                    userNameLayout.setError("Username is required");
                    userNameText.requestFocus();
                }
                if (password.length() == 0) {
                    pass = false;
                    passwordLayout.setError("Password is required");
                    passwordText.requestFocus();
                }
                if (password.length() != 0) {
                    boolean result = true;
                    try {
                        result = new ValidateUserAsynctask().execute(username, password).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (result == false) {
                        pass = false;
                        mySnackbar.show();
                    }
                }

                if (pass == true) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }


        }
        return super.onOptionsItemSelected(item);
    }

    private class ValidateUserAsynctask extends AsyncTask<String, Void, Boolean> {
        UserService userService = new UserService();

        @Override
        protected Boolean doInBackground(String... params) {
            boolean valid = userService.validateUser(params[0], params[1]);
            System.out.println(valid);
            if (valid == true) {
                SharedPreferences sharedPref = getSharedPreferences("user_auth", Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPref.edit();
                String userId = userService.getUserIdByUserName(params[0]);
                spEditor.putString("user_id", userId);
                AppUser appUser = userService.findUserById(userId);
                System.out.println(appUser.getAddress());
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                spEditor.putString("user_address", appUser.getAddress());
                spEditor.putString("username", params[0]);


                spEditor.apply();
                //            System.out.println(sharedPref.getString("user_id", null));
            }
            return valid;
        }
    }
}
