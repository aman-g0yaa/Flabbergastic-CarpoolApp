package com.amangoyal.finale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class sign_up extends AppCompatActivity {

    //varibles on signup screen
    TextInputLayout regName, regUsername, regPhone, regPassword;
    Button Signup, Loginin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_up);

        progressBarInvisible();
        //hooks
        regPassword = findViewById(R.id.pass_word_id);
        regName = findViewById(R.id.full_name_id);
        regUsername = findViewById(R.id.user_name_id);
        regPhone = findViewById(R.id.phone_number_id);
        Signup = findViewById(R.id.signup_bt);
        Loginin = findViewById(R.id.signin_bt);

        //when already a user is clicked
        Loginin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //it closes the current activity and moves to previous activity
                finish();
            }
        });

    }

    private void progressBarVisible() {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite doubleBounce = new FadingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    private void progressBarInvisible() {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite doubleBounce = new FadingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    //    validating each views
    private boolean validateName() {
        String val = regName.getEditText().getText().toString();

        if (val.isEmpty()) {
            progressBarInvisible();
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);  // this is will remove the error
            regName.setErrorEnabled(false); //this will remove the space which was allocated to display error
            return true;
        }
    }

    private boolean validateUserame() {
        String val = regUsername.getEditText().getText().toString().trim();
        String whitespace = "\\A\\w{4,15}\\z"; //regix for username

        if (val.isEmpty()) {
            progressBarInvisible();
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() > 15) {
            progressBarInvisible();
            regUsername.setError("Max length 15 characters");
            return false;
        } else if (!val.matches(whitespace)) {
            progressBarInvisible();
            regUsername.setError("No whitespace allowed");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String regixpassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            progressBarInvisible();
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(regixpassword)) {
            progressBarInvisible();
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhone() {
        String val = regPhone.getEditText().getText().toString();
        String regixphone = "((\\+*)((0[ -]+)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}"; //regix for indian phone numbers

        if (val.isEmpty()) {
            progressBarInvisible();
            regPhone.setError("Field cannot be empty");
            return false;
        } else if (val.length() > 10 || !val.matches(regixphone)) {
            progressBarInvisible();
            regPhone.setError("Invalid Phone No ");
            return false;
        } else {
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return true;
        }
    }


    //tip :- parameter is View ie it will take all the view compenents as input like textview , edittext view etc
    //when clicked on Go it will store all the data in varibles and pass it to firebase
    //OnClick is set up directly in layout activity_sign_up on the button go

    public void registerUser(View view) {
        progressBarVisible();

        if (!validateName() | !validatePassword() | !validatePhone() | !validateUserame())
            return;


        final String name = regName.getEditText().getText().toString().trim();
        final String username = regUsername.getEditText().getText().toString().trim();
        String phone = regPhone.getEditText().getText().toString().trim();
        final String password = regPassword.getEditText().getText().toString().trim();

        //here checkuser will scan through the db and return whether the entered phone number already exsist or not
        Query checkUser = FirebaseDatabase.getInstance().getReference("users").child(username);




        final String finalPhone = phone;
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()) {

                    //if exsist display error
                    progressBarInvisible();

                    regUsername.setError("Username already exsist");

                } else {

                    Intent intent = new Intent(sign_up.this, Otp.class);

                    //passing it to the next intent
                    intent.putExtra("Flag", "NewUser");
                    intent.putExtra("name", name);
                    intent.putExtra("password", password);
                    intent.putExtra("phone", finalPhone);
                    intent.putExtra("username", username);

                    Toast.makeText(getApplicationContext(), finalPhone, Toast.LENGTH_SHORT).show();


//        Pair[] pairs = new Pair[5];
//        pairs[0] = new Pair<View, String>(regName, "name");
//        pairs[1] = new Pair<View, String>(regUsername, "username");
//        pairs[2] = new Pair<View, String>(regEmail, "email");
//        pairs[3] = new Pair<View, String>(regPassword, "password");
//        pairs[4] = new Pair<View, String>(regPhone, "phone");


                    progressBarInvisible();
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }


}