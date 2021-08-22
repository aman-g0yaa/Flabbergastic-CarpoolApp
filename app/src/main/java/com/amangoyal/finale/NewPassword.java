package com.amangoyal.finale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewPassword extends AppCompatActivity {

    TextInputLayout newPasswordLayout , confirmPasswordLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //hooks
        newPasswordLayout = findViewById(R.id.NewpwID);
        confirmPasswordLayout = findViewById(R.id.ConfirmpwID);


    }
    private boolean validatePassword(TextInputLayout regPassword) {
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
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(regixpassword)) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }
    //will check whether entered two password are same or not
    public boolean equality(TextInputLayout regPassword , String password , String confirmPassword) {
        if(confirmPassword.equals(password)){
            return true;
        }
        regPassword.setError("Enter same password");
        return false;

    }

    public void SetNewPassword(View view) {

        if (!validatePassword(newPasswordLayout) | !validatePassword(confirmPasswordLayout))
            return;

        String newPassword = newPasswordLayout.getEditText().getText().toString().trim();
        String confirmPassword = confirmPasswordLayout.getEditText().getText().toString().trim();
       // Toast.makeText(getApplicationContext(),newPassword,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),confirmPassword,Toast.LENGTH_SHORT).show();
        String username = getIntent().getStringExtra("username");
        if(!equality(confirmPasswordLayout,newPassword,confirmPassword))
          return;

        //to DB and change password
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(username).child("password").setValue(newPassword);
        Intent intent = new Intent(getApplicationContext(), ForgotPasswordSuccesfull.class);
        startActivity(intent);
        finish();

    }
}