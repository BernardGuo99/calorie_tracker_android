package com.example.calorietrackerapp.controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.restclient.UserService;
import com.example.calorietrackerapp.restclient.entity.AppUser;
import com.example.calorietrackerapp.restclient.entity.Credential;
import com.example.calorietrackerapp.utils.PasswordHash256;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText editTextBirthday;
    private ViewFlipper viewFlipper;
    private Button btnNext;
    private Button btnPrevious;
    private Button btnGo;

    private TextInputLayout inputLayoutPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        btnNext = findViewById(R.id.b_next);
        btnPrevious = findViewById(R.id.b_previous);
        btnGo = findViewById(R.id.b_go);
        List<String> list = new ArrayList<String>();
        list.add("Sedentary");
        list.add("Lightly Active");
        list.add("Moderately Active");
        list.add("Moderately to Highly Active");
        list.add("Highly Active");
        final Spinner sMovie = (Spinner) findViewById(R.id.activity_level_spinner);
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMovie.setAdapter(spinnerAdapter);

        editTextBirthday = (EditText) findViewById(R.id.birthday);
        viewFlipper = findViewById(R.id.view_flipper);
        editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR) - 18;
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignUpActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editTextBirthday.setText(sdf.format(cal.getTime()));
            }
        };

        inputLayoutPassword = (TextInputLayout) findViewById(R.id.inputLayoutPassword);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.custom_icons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finish_register:
                if(inputLayoutPassword.getEditText().getText().length() < 8){
                    inputLayoutPassword.setError("ccc");
                }else{
                    viewFlipper.showNext();
                    item.setVisible(false);
                    Button button = findViewById(R.id.b_next);
                    button.setVisibility(View.VISIBLE);


                    getSupportActionBar().setTitle("Set Up Your Profile");
                    //startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    return true;
                }

        }
        return super.onOptionsItemSelected(item);
    }

    public void nextView(View view) {
        //Log.i("cnm", String.valueOf(viewFlipper.getDisplayedChild()));
        if (viewFlipper.getDisplayedChild() == 1) {
            btnPrevious.setVisibility(View.VISIBLE);
        }
        if (viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 2) {
            btnNext.setVisibility(View.GONE);
            btnGo.setVisibility(View.VISIBLE);
        }
//       Log.i("cnmb",String.valueOf(viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 2));

        viewFlipper.showNext();
    }

    public void previousView(View view) {
        if (viewFlipper.getDisplayedChild() == 2) {
            btnPrevious.setVisibility(View.GONE);
        }
        if (viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 1) {
            //viewFlipper.startFlipping();
            btnNext.setVisibility(View.VISIBLE);
            btnGo.setVisibility(View.GONE);
        }
        viewFlipper.showPrevious();
    }

    public void goToMain(View view) {
        CreateUserAndCredentialAsyncTask createAsyncTask = new CreateUserAndCredentialAsyncTask();
        EditText firstNameText = findViewById(R.id.first_name);
        EditText surnameText = findViewById(R.id.surname);
        EditText emailText = findViewById(R.id.email);
        EditText dateOfBirthText = findViewById(R.id.birthday);

        String genderSelected = "";
        RadioGroup genderRadioGroup = findViewById(R.id.radio_gender);
        RadioButton maleButton = findViewById(R.id.radio_male);
        RadioButton femaleButton = findViewById(R.id.radio_female);
        int checkedId = genderRadioGroup.getCheckedRadioButtonId();
        if (checkedId == maleButton.getId()) {
            genderSelected = "M";
        } else {
            genderSelected = "F";
        }

        EditText addressText = findViewById(R.id.address);
        EditText postcodeText = findViewById(R.id.postcode);

        String levelOfActivity = "0";
        Spinner levelOfActivityText = findViewById(R.id.activity_level_spinner);
        switch (levelOfActivityText.getSelectedItem().toString()) {
            case "Sedentary":
                levelOfActivity = "1";
                break;
            case "Lightly Active":
                levelOfActivity = "2";
                break;
            case "Moderately Active":
                levelOfActivity = "3";
                break;
            case "Moderately to Highly Active":
                levelOfActivity = "4";
                break;
            case "Highly Active":
                levelOfActivity = "5";
                break;
            default:
                break;
        }

        EditText stepsPerMileText = findViewById(R.id.steps);
        EditText heightText = findViewById(R.id.height);
        EditText weightText = findViewById(R.id.weight);
        EditText usernameText = findViewById(R.id.username);
        EditText passwordText = findViewById(R.id.password);


        createAsyncTask.execute(firstNameText.getText().toString(),
                surnameText.getText().toString(),
                emailText.getText().toString(),
                dateOfBirthText.getText().toString(),
                genderSelected,
                addressText.getText().toString(),
                postcodeText.getText().toString(),
                levelOfActivity,
                stepsPerMileText.getText().toString(),
                heightText.getText().toString(),
                weightText.getText().toString(),
                usernameText.getText().toString(),
                passwordText.getText().toString());

        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
    }


    private class CreateUserAndCredentialAsyncTask extends AsyncTask<String, Void, String> {
        UserService userService = new UserService();


        @Override
        protected String doInBackground(String... params) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
            AppUser appUser = new AppUser(UUID.randomUUID().toString());
            appUser.setFirstname(params[0]);
            appUser.setSurname(params[1]);
            appUser.setEmail(params[2]);
            try {
                appUser.setDateOfBirth(sdf.parse(params[3]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            appUser.setGender(params[4]);
            appUser.setAddress(params[5]);
            appUser.setPostcode(params[6]);
            appUser.setLevelOfActivity(Integer.parseInt(params[7]));
            appUser.setStepsPerMile(Integer.parseInt(params[8]));
            appUser.setHeight(Double.parseDouble(params[9]));
            appUser.setWeight(Double.parseDouble(params[10]));
            userService.createAppUser(appUser);

            Credential credential = new Credential(UUID.randomUUID().toString());
            credential.setUserId(appUser);
            credential.setUserName(params[11]);
            credential.setPasswordHash(PasswordHash256.passWordHash(params[12]));
            credential.setSignUpDate(new Date());
            userService.createCredential(credential);
            return "Course was added";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private class checkUserNameExistAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return "cccc";
        }
    }
}
