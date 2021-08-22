package com.amangoyal.finale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class login extends AppCompatActivity {


    //signup button
    Button login_signup;



    //new sign up animation
    ImageView logo ;
    Button signup , login , forgetpw ;
    TextView tag1,tag2;
    TextInputLayout username,password;

    SharedPreferences sharedPreferences;

    public static final String filename = "login";
    public static final String usernameField = "username";
    public static final String passwordField = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        //setting Current state of progressBar as invisible
        progressBarInvisible();

        sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);

        if(sharedPreferences.contains(usernameField)){
            Intent intent = new Intent(getApplicationContext(),Dashboard_LoginedInUser.class);
            startActivity(intent);
            finish();
        }


        //hooks
        login_signup = findViewById(R.id.new_signup_bt);
        logo = findViewById(R.id.logo_id2);
        signup = findViewById(R.id.signin_go_bt);
        login = findViewById(R.id.new_signup_bt);
        username = findViewById(R.id.username_id);
        password = findViewById(R.id.password_id);
        tag1 = findViewById(R.id.logoname_id2);
        tag2 = findViewById(R.id.tag_2);
        forgetpw = findViewById(R.id.forgot_pw_bt);


        //when forget password is clicked
        forgetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,ForgetPw_phoneNo.class);
                startActivity(intent);
            }
        });





        //when sing_up is clicked
        login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this,sign_up.class);

                    startActivity(intent);

            }
        });

    }


   //progressBar methods

   private  void progressBarVisible(){

        //hook
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        //type - change fadingcircle() to anything for more animation
        Sprite doubleBounce = new FadingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        //visibility
        progressBar.setVisibility(View.VISIBLE);

        //if progress bar visible then touch response on the activity will stop until it becomes invisible
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
               WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }
    private  void progressBarInvisible(){

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new FadingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }


    //validating Entered username and password

    private boolean validateUserame(){
        String val = username.getEditText().getText().toString();

        if(val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        }
        else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(){
        String val = password.getEditText().getText().toString();


        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }
        else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
    //when the person click Go
    public void loginUser(View view){
        if(!validatePassword() | !validateUserame()){
            return ;
        }
        else{
            progressBarVisible();
            //after validation the entered username and password are compared with the DB
            isUser();
        }
    }

    private void isUser() {

        //intializing variable , .trim() removes leading and trailing spaces
        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();


        //this goes to the firebase DB , getInstance() points to the parent node , get reference point to the Users , so now we have
        // an variable reference which points to the Users
        DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("users");


        //This query will run a loop in the DB searching for a user with entered username

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
      //  Toast.makeText(login.this,"happy birthday",Toast.LENGTH_LONG).show();


        //if it finds a user snapshot will have some data in it ;

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    username.setError(null);
                    username.setErrorEnabled(false);



                    //if snapshot aka user exsist then we fetch the password from DB and compare with entered password.
                    //it will go to username and then to the password in the database
                    String passwordDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if(passwordDB.equals(userEnteredPassword))
                    {

                        username.setError(null);
                        username.setErrorEnabled(false);

                        //storing data of the user from DB to local
                        String namefromDB = dataSnapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String passwordfromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);
                        String phonefromDB = dataSnapshot.child(userEnteredUsername).child("phone").getValue(String.class);
                        String usernamefromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(),Dashboard_LoginedInUser.class);

                        //passing it to the next intent
                        intent.putExtra("name",namefromDB);
                        intent.putExtra("password",passwordfromDB);
                        intent.putExtra("phone",phonefromDB);
                        intent.putExtra("username",usernamefromDB);

                        progressBarInvisible();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(usernameField,usernamefromDB);
                        editor.putString(passwordField,passwordfromDB);
                        editor.commit();

                        final DatabaseReference reff = FirebaseDatabase.getInstance().getReference("users").child(userEnteredUsername);
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String token = instanceIdResult.getToken();
//                                Log.d("toooooo","token" + token);
                                reff.child("token").setValue(token);

                                // send it to server
                            }
                        });



                        startActivity(intent);
                        finish();
                    }
                    else{
                        progressBarInvisible();
                        password.setError("Wrong Password");
                        //after showing the error the section will get focused
                        password.requestFocus();
                    }

                }
                else {
                    progressBarInvisible();
                    username.setError("No Such User Exsist");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}