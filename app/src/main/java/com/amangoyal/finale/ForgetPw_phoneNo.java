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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgetPw_phoneNo extends AppCompatActivity {

    //only one view
    TextInputLayout phone,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pw_phone_no);

        progressBarInvisible();

        //hooks
        phone = findViewById(R.id.ForgotpwID);
        name = findViewById(R.id.ForgotpwNameID);


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

    private boolean validatePhone() {
        String val = phone.getEditText().getText().toString().trim();
        String regixphone = "((\\+*)((0[ -]+)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}"; //regix for indian phone numbers

        if (val.isEmpty()) {
            progressBarInvisible();
            phone.setError("Field cannot be empty");
            return false;
        } else if (val.length() > 10 || !val.matches(regixphone)) {
            progressBarInvisible();
            phone.setError("Invalid Phone No ");
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }

    public void registerUser(View view) {
        progressBarVisible();
        if (!validatePhone())
            return;

        final String val = name.getEditText().getText().toString();
        final String phoneNumber = phone.getEditText().getText().toString();

        final String ccPhoneNumber = "+91"+phoneNumber;

        //it will search through the db for same phone as entere
        Query checkUser = FirebaseDatabase.getInstance().getReference("users").child(val);
        //u can only get the refernce to a value from DB if u need the string value u need to add listener

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //if phone number exists
                String DBphone = snapshot.child("phone").getValue(String.class);
                if (snapshot.exists() && DBphone.equals(ccPhoneNumber)) {

                    //Toast.makeText(getApplicationContext(), Dbpone[0], Toast.LENGTH_SHORT).show();
                    //why username ? becoz username is unique name which act as the parent to all other values and to verify otp we need it
                    name.setError(null);
                    name.setErrorEnabled(false);
                    phone.setError(null);
                    phone.setErrorEnabled(false);
                    Intent intent = new Intent(ForgetPw_phoneNo.this, Otp.class);
                    String username = val;

                    //we send the username , phone number and the flag to otp activity
                    intent.putExtra("username", username);
                    intent.putExtra("Flag", "PasswordReset");

                    intent.putExtra("phone", phoneNumber);
                    progressBarInvisible();
                    startActivity(intent);

                } else {
                    progressBarInvisible();
                    if(!DBphone.equals(ccPhoneNumber))
                    {
                        phone.setError("No such user exist!");
                        phone.requestFocus();
                    }
                    name.setError("No such user exist!");
                    name.requestFocus();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
}