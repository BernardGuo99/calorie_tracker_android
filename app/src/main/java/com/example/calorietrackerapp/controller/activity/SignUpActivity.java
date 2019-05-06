package com.example.calorietrackerapp.controller.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.calorietrackerapp.utils.InputValidator;
import com.example.calorietrackerapp.utils.PasswordHash256;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class SignUpActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText editTextBirthday;
    private ViewFlipper viewFlipper;
    private Button btnNext;
    private Button btnPrevious;
    private Button btnGo;

    private TextInputLayout inputLayoutPassword;
    private TextInputLayout emailLayout;
    private TextInputLayout userNameLayout;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextUserName;

    private EditText firstNameText;
    private EditText surnameText;
    private EditText emailText;
    private EditText dateOfBirthText;
    private RadioGroup genderRadioGroup;
    private RadioButton maleButton;
    private RadioButton femaleButton;
    private EditText addressText;
    private EditText postcodeText;
    private Spinner levelOfActivityText;
    private EditText stepsPerMileText;
    private EditText heightText;
    private EditText weightText;
    private EditText usernameText;
    private EditText passwordText;
    private TextInputLayout firstNameLayout;
    private TextInputLayout surnameLaylout;
    private TextInputLayout birthdayLayout;
    private TextInputLayout heightLayout;
    private TextInputLayout weightLayout;
    private TextInputLayout stepsLayout;
    private TextInputLayout postcodeLayout;
    private TextInputLayout addressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        btnNext = findViewById(R.id.b_next);
        btnPrevious = findViewById(R.id.b_previous);
        btnGo = findViewById(R.id.b_go);
        List<String> list = new ArrayList<>();
        list.add("Sedentary");
        list.add("Lightly Active");
        list.add("Moderately Active");
        list.add("Moderately to Highly Active");
        list.add("Highly Active");
        final Spinner level = (Spinner) findViewById(R.id.activity_level_spinner);
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(spinnerAdapter);

        firstNameText = findViewById(R.id.first_name);
        surnameText = findViewById(R.id.surname);
        emailText = findViewById(R.id.email);
        dateOfBirthText = findViewById(R.id.birthday);
        genderRadioGroup = findViewById(R.id.radio_gender);
        maleButton = findViewById(R.id.radio_male);
        femaleButton = findViewById(R.id.radio_female);
        addressText = findViewById(R.id.address);
        postcodeText = findViewById(R.id.postcode);
        levelOfActivityText = findViewById(R.id.activity_level_spinner);
        stepsPerMileText = findViewById(R.id.steps);
        heightText = findViewById(R.id.height);
        weightText = findViewById(R.id.weight);
        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        firstNameLayout = findViewById(R.id.inputLayoutFirstName);
        surnameLaylout = findViewById(R.id.inputLayoutSurname);
        birthdayLayout = findViewById(R.id.inputLayoutBirthday);
        heightLayout = findViewById(R.id.inputLayoutHeight);
        weightLayout = findViewById(R.id.inputLayoutWeight);
        stepsLayout = findViewById(R.id.inputLayoutSteps);
        postcodeLayout = findViewById(R.id.inputLayoutPostcode);
        addressLayout = findViewById(R.id.inputLayoutAddress);


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
        editTextPassword = findViewById(R.id.password);
        editTextEmail = findViewById(R.id.email);
        emailLayout = findViewById(R.id.inputLayoutEmail);

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Boolean result = true;
                try {
                    result = new CheckEmailExistAsyncTask().execute(editTextEmail.getText().toString()).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (result == false) {
                    emailLayout.setError("Email address already in use");
                } else {
                    emailLayout.setErrorEnabled(false);
                }
            }
        });


        editTextUserName = findViewById(R.id.username);
        userNameLayout = findViewById(R.id.inputLayoutUsername);
        editTextUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Boolean result = true;
                try {
                    result = new CheckUserNameAsyncTask().execute(editTextUserName.getText().toString()).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (result == false) {
                    userNameLayout.setError("Username already in use");
                } else {
                    userNameLayout.setErrorEnabled(false);
                }
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextPassword.length() != 0) {
                    inputLayoutPassword.setErrorEnabled(false);
                }
            }
        });

        firstNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (firstNameText.length() != 0) {
                    firstNameLayout.setErrorEnabled(false);
                }
            }
        });

        surnameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (surnameText.length() != 0) {
                    surnameLaylout.setErrorEnabled(false);
                }
            }
        });

        editTextBirthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextBirthday.length() != 0) {
                    birthdayLayout.setErrorEnabled(false);
                }
            }
        });

        heightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (heightText.length() != 0) {
                    heightLayout.setErrorEnabled(false);
                }
            }
        });

        weightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (weightText.length() != 0) {
                    weightLayout.setErrorEnabled(false);
                }
            }
        });

        stepsPerMileText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (stepsPerMileText.length() != 0) {
                    stepsLayout.setErrorEnabled(false);
                }
            }
        });

        postcodeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (postcodeText.length() != 0) {
                    postcodeLayout.setErrorEnabled(false);
                }
            }
        });

        addressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (addressText.length() != 0) {
                    addressLayout.setErrorEnabled(false);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_icons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tick:
//                SharedPreferences sharedPref = getSharedPreferences("user_auth", Context.MODE_PRIVATE);
//                System.out.println(sharedPref.getString("user_id","wcnm"));
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String username = editTextUserName.getText().toString().trim();
                boolean pass = true;
                if (username.length() == 0) {
                    pass = false;
                    editTextUserName.requestFocus();
                    userNameLayout.setError("Username is required");
                } else if (username.length() < 4) {
                    pass = false;
                    editTextUserName.requestFocus();
                    userNameLayout.setError("Username must be at least 4 characters long");
                } else if (username.length() > 20) {
                    pass = false;
                    editTextUserName.requestFocus();
                    userNameLayout.setError("Username must be within 20 characters long");
                } else if (userNameLayout.getError() != null) {
                    pass = false;
                    userNameLayout.requestFocus();
                }
                if (password.length() == 0) {
                    pass = false;
                    editTextPassword.requestFocus();
                    inputLayoutPassword.setError("Password is required");
                } else if (password.length() < 8) {
                    pass = false;
                    editTextPassword.requestFocus();
                    inputLayoutPassword.setError("Password must be at least 8 characters long");
                } else if (password.length() > 20) {
                    pass = false;
                    editTextPassword.requestFocus();
                    inputLayoutPassword.setError("Password must be within 20 characters long");
                }
                if (email.length() == 0) {
                    pass = false;
                    emailLayout.setError("Email is required");
                } else if (!InputValidator.validateEmail(email)) {
                    pass = false;
                    emailLayout.setError("Not a valid Email format");
                    emailLayout.requestFocus();
                }
                if (pass == true) {
                    viewFlipper.showNext();
                    item.setVisible(false);
                    Button button = findViewById(R.id.b_next);
                    button.setVisibility(View.VISIBLE);
                    getSupportActionBar().setTitle("Set Up Your Profile");
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void nextView(View view) {
        boolean pass = true;
        if (viewFlipper.getDisplayedChild() == 1) {
            String firstName = firstNameText.getText().toString().trim();
            String surname = surnameText.getText().toString().trim();
            String birthday = editTextBirthday.getText().toString().trim();
            String height = heightText.getText().toString().trim();
            String weight = weightText.getText().toString().trim();
            if (firstName.length() == 0) {
                pass = false;
                firstNameLayout.setError("First name is required");
                firstNameText.requestFocus();
            } else if (firstName.length() < 2) {
                pass = false;
                firstNameLayout.setError("First name must be at least 2 characters long");
                firstNameText.requestFocus();
            } else if (firstName.length() > 20) {
                pass = false;
                firstNameLayout.setError("First name must be within 20 characters long");
                firstNameText.requestFocus();
            }
            if (surname.length() == 0) {
                pass = false;
                surnameLaylout.setError("Surname is required");
                surnameText.requestFocus();
            } else if (surname.length() < 2) {
                pass = false;
                surnameLaylout.setError("Surname must be at least 2 characters long");
                firstNameText.requestFocus();
            } else if (surname.length() > 20) {
                pass = false;
                surnameLaylout.setError("Surname must be within 20 characters long");
                surnameText.requestFocus();
            }
            if (birthday.length() == 0) {
                pass = false;
                birthdayLayout.setError("Birthday is required");
                editTextBirthday.requestFocus();
            }
            if (height.length() == 0) {
                pass = false;
                heightLayout.setError("Height is required");
                heightText.requestFocus();
            }
            if (weight.length() == 0) {
                pass = false;
                weightLayout.setError("Weight is required");
                weightText.requestFocus();
            }


            if (pass == true) {
                btnPrevious.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
                btnGo.setVisibility(View.VISIBLE);
                viewFlipper.showNext();
            }

        }

    }

    public void previousView(View view) {
        if (viewFlipper.getDisplayedChild() == 2) {
            btnNext.setVisibility(View.VISIBLE);
            btnPrevious.setVisibility(View.GONE);
            btnGo.setVisibility(View.GONE);
        }
        viewFlipper.showPrevious();
    }

    public void goToMain(View view) {
        boolean pass = true;
        String steps = stepsPerMileText.getText().toString().trim();
        String postcode = postcodeText.getText().toString().trim();
        String address = addressText.getText().toString().trim();
        if (steps.length() == 0) {
            pass = false;
            stepsLayout.setError("Steps Per Mile is required");
            stepsPerMileText.requestFocus();
        }
        if (postcode.length() == 0) {
            pass = false;
            postcodeLayout.setError("Postcode is required");
            postcodeText.requestFocus();
        }
        if (address.length() == 0) {
            pass = false;
            addressLayout.setError("Address is required");
            addressText.requestFocus();
        } else if (address.length() > 200) {
            pass = false;
            addressLayout.setError("Address should be within 200 characters long");
            addressText.requestFocus();
        }

        if (pass == true) {
            CreateUserAndCredentialAsyncTask createAsyncTask = new CreateUserAndCredentialAsyncTask();


            String genderSelected = "";

            int checkedId = genderRadioGroup.getCheckedRadioButtonId();
            if (checkedId == maleButton.getId()) {
                genderSelected = "M";
            } else {
                genderSelected = "F";
            }


            String levelOfActivity = "0";
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
    }


    private class CreateUserAndCredentialAsyncTask extends AsyncTask<String, Void, String> {
        UserService userService = new UserService();


        @Override
        protected String doInBackground(String... params) {
            SharedPreferences sharedPref = getSharedPreferences("user_auth", Context.MODE_PRIVATE);
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

            SharedPreferences.Editor spEditor = sharedPref.edit();
            spEditor.putString("user_id", appUser.getUserId());
            spEditor.apply();

            return "Course was added";
        }
    }

    private class CheckEmailExistAsyncTask extends AsyncTask<String, Void, Boolean> {
        UserService userService = new UserService();

        @Override
        protected Boolean doInBackground(String... params) {
            return userService.checkEmailExistence(params[0]);
        }
    }

    private class CheckUserNameAsyncTask extends AsyncTask<String, Void, Boolean> {
        UserService userService = new UserService();

        @Override
        protected Boolean doInBackground(String... params) {
            return userService.checkUserNameExistence(params[0]);
        }
    }
}
