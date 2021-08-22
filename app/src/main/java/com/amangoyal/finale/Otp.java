package com.amangoyal.finale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.view.WindowManager;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity {


    //Firebase variables for accesing rootnode : usefull while storing data in fire base
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    //Varibles used for OTP verification
    String CodebySystem;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    PinView pinView;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        progressBarInvisible();

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //hook
        pinView = findViewById(R.id.pinview_id);
        textView = findViewById(R.id.display_phoneid);




        //retreving the phone number
        //Remember here there can be two conditions where
        // 1 - you called is activity to add new user
        // 2 - you called is activity to verify forgot password
        String phone = getIntent().getStringExtra("phone");
        phone = "+" + "91" + phone;


        //phone number display
        textView.setText(phone);

        //Otp verification Starts
        SendotpCode(phone);

    }

    private  void progressBarVisible(){

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new FadingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        progressBar.setVisibility(View.VISIBLE);
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




    private void SendotpCode(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit :time limit for the user to enter OTP
                        .setActivity(this)              // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    //here we will override 3 methods
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                //code send by the system is stored in a variable
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    CodebySystem = s;

                }

                //if verification gets complete firebase will send an sms which we will directly display in the pin view
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinView.setText(code);
                        verifyCode(code);
                    }


                }
                //if failed we will display the same error which cause the verification to fail
                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(Otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            };


    //if the user manually enters an otp code it will get stored in a variable n send to verify code for verification
    public void checkOtp(View view) {
        progressBarVisible();
        String code = pinView.getText().toString();
        if (!code.isEmpty()) {
            verifyCode(code);
        }
    }

    //here we check whether code given by firebase and system generated code is same or not
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodebySystem, code);
        signInWithPhoneAuthCredential(credential);
    }
    //verify code will generate a credential and this will be send to signinwithphoneauthcredential method
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //this will check whether the task is successful or not
                        String flag = getIntent().getStringExtra("Flag");
                        if (task.isSuccessful()) {

                            //if the this activity is called to verify the phone to add new user
                            if(flag.equals("NewUser")) {
                                rootNode = FirebaseDatabase.getInstance();
                                reference = rootNode.getReference("users");

                                String name = getIntent().getStringExtra("name");
                                String phone = getIntent().getStringExtra("phone");
                                String username = getIntent().getStringExtra("username");
                                String password = getIntent().getStringExtra("password");
                                phone = "+" + "91" + phone;


                                //passing all the values to the DB with username being child of db with data all below it .
                                UserHelperClass helperclass = new UserHelperClass(name, username, phone, password);
                                reference.child(username).setValue(helperclass);
                                Toast.makeText(Otp.this, "Verification Successful", Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(Otp.this, login.class);
                                progressBarInvisible();
                                startActivity(intent);
                                finish();
                            }
                            //if the this activity is called to verify the phone to change the password
                            else {
                                    Intent intent = new Intent(Otp.this , NewPassword.class);
                                     String phone = getIntent().getStringExtra("Phone");
                                     phone = "+" + "91" + phone;
                                     String username = getIntent().getStringExtra("username");
                                     intent.putExtra("username",username);
                                    intent.putExtra("PhoneNo",phone);
                                    progressBarInvisible();
                                    startActivity(intent);
                                    finish();
                            }

                        } else {


                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                progressBarInvisible();
                                Toast.makeText(Otp.this, "Verification not Completed! Try Again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


}