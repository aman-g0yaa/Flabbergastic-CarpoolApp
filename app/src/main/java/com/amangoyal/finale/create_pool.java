package com.amangoyal.finale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

public class    create_pool extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    TextInputLayout  from, to, displayDate,seat, displayTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button create, date, time;

    String dateData ;
    String timeData;
    String token;
    String unixdate = "";
    String unixtime = "";
    String unix = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pool);

        progressBarInvisible();



        from = findViewById(R.id.from_id);
        to = findViewById(R.id.to_id);
        date = findViewById(R.id.set_date);
        seat = findViewById(R.id.seat_id);
        displayDate = findViewById(R.id.date_id);
        create = findViewById(R.id.create_bt);
        time = findViewById(R.id.set_time);
        displayTime = findViewById(R.id.time_id);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(create_pool.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String mon = "";
                                if((monthOfYear+1) == 1) mon = "Jan";
                                if((monthOfYear+1) == 2) mon = "Feb";
                                if((monthOfYear+1) == 3) mon = "Mar";
                                if((monthOfYear+1) == 4) mon = "April";
                                if((monthOfYear+1) == 5) mon = "May";
                                if((monthOfYear+1) == 6) mon = "June";
                                if((monthOfYear+1) == 7) mon = "July";
                                if((monthOfYear+1) == 8) mon = "Aug";
                                if((monthOfYear+1) == 9) mon = "Sept";
                                if((monthOfYear+1) == 10) mon = "Oct";
                                if((monthOfYear+1) == 11) mon = "Nov";
                                if((monthOfYear+1) == 12) mon = "Dec";

                                dateData = dayOfMonth + " " + mon;

                                displayDate.getEditText().setText(dateData);

                                String mm = String.valueOf(monthOfYear+1);
                                if(monthOfYear+1 <= 9){
                                    mm = "0"+mm;
                                }
                                String dd = String.valueOf(dayOfMonth);
                                if(dayOfMonth<=9){
                                    dd = "0"+dd;
                                }
                                unixdate = mm + "/" + dd + "/" + String.valueOf(year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }

        });


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(create_pool.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {


                               Integer hour = hourOfDay;
                                Integer minutes = minute;
                                String timeSet = "";
                                if (hour > 12) {
                                    hour -= 12;
                                    timeSet = "PM";
                                } else if (hour == 0) {
                                    hour += 12;
                                    timeSet = "AM";
                                } else if (hour == 12){
                                    timeSet = "PM";
                                }else{
                                    timeSet = "AM";
                                }

                                String min = "";
                                if (minutes < 10)
                                    min = "0" + minutes ;
                                else
                                    min = String.valueOf(minutes);

                                String aTime = new StringBuilder().append(hour).append(':')
                                        .append(min ).append(" ").append(timeSet).toString();

                                timeData = aTime;
                                displayTime.getEditText().setText(aTime);

                                String hh = String.valueOf(hour);
                                if(hour <=9){
                                    hh = "0"+hh;
                                }
                                String mm = String.valueOf(minute);
                                if(minute <= 9){
                                    mm = "0" + mm;
                                }
                                unixtime =hh + ":" + mm + ":" + "00";
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }

        });


    }

    private void progressBarVisible() {

        //hook
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        //type - change fadingcircle() to anything for more animation
        Sprite doubleBounce = new FadingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        //visibility
        progressBar.setVisibility(View.VISIBLE);

        //if progress bar visible then touch response on the activity will stop until it becomes invisible
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

    private boolean validate(TextInputLayout t) {


        String val = t.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            progressBarInvisible();
            t.setError("Field cannot be empty");
            return false;
        }
        else if(val.length()>25){
            progressBarInvisible();
            t.setError("Max characters should be less than 25");
            return false;
        }
        else {
            t.setError(null);
            return true;
        }
    }

    private boolean validateSeat(TextInputLayout t) {


        String val = t.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            progressBarInvisible();
            t.setError("Field cannot be empty");
            return false;
        }
            Integer num = Integer.parseInt(val);
     if(num > 10){
            progressBarInvisible();
            t.setError("Max 10");
            return false;
        }
        else {
            t.setError(null);
            return true;
        }
    }

    public void registerUser(View view) {


        progressBarVisible();

        if (!validate(from) | !validate(to) | !validateSeat(seat) | !validate(displayDate) | !validate(displayTime))
            return;

        progressBarInvisible();




        final String username = getIntent().getStringExtra("username");
        final String name =  getIntent().getStringExtra("name");
        final String fromData = from.getEditText().getText().toString().trim();
        final String toData = to.getEditText().getText().toString().trim();
        final String seatData = seat.getEditText().getText().toString().trim();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("pool");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChild(username)){

                    AlertDialog.Builder builder = new AlertDialog.Builder(create_pool.this);
                    builder.setMessage("creating a new pool will override the old pool,Do you still want to create?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("pool");

                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    token = instanceIdResult.getToken();
                                    Log.d("toooooo","token" + token);
                                    reference.child(username).child("token").setValue(token);


                                    // send it to server
                                }
                            });
                            unix = unixdate+ " "+ unixtime;
//                            Log.e("yo yo" , unix);
                            reference.child(username).child("name").setValue(name);
                            reference.child(username).child("username").setValue(username);
                            reference.child(username).child("from").setValue(fromData);
                            reference.child(username).child("to").setValue(toData);
                            reference.child(username).child("seat").setValue(seatData);
                            reference.child(username).child("date").setValue(dateData);
                            reference.child(username).child("unix").setValue(unix);
                            reference.child(username).child("time").setValue(timeData);
                            reference.child(username).child("staticSeat").setValue(seatData);

                            reference.child(username).child("member0").setValue("");
                            reference.child(username).child("statusmember0").setValue("empty");
                            reference.child(username).child("member1").setValue("");
                            reference.child(username).child("statusmember1").setValue("empty");
                            reference.child(username).child("member2").setValue("");
                            reference.child(username).child("statusmember2").setValue("empty");
                            reference.child(username).child("member3").setValue("");
                            reference.child(username).child("statusmember3").setValue("empty");
                            reference.child(username).child("member4").setValue("");
                            reference.child(username).child("statusmember4").setValue("empty");
                            reference.child(username).child("member5").setValue("");
                            reference.child(username).child("statusmember5").setValue("empty");
                            reference.child(username).child("member6").setValue("");
                            reference.child(username).child("statusmember6").setValue("empty");
                            reference.child(username).child("member7").setValue("");
                            reference.child(username).child("statusmember7").setValue("empty");
                            reference.child(username).child("member8").setValue("");
                            reference.child(username).child("statusmember8").setValue("empty");
                            reference.child(username).child("member9").setValue("");
                            reference.child(username).child("statusmember9").setValue("empty");

                            finish();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    });

                    builder.show();

                }
               else {
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("pool");

                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            token = instanceIdResult.getToken();
                            Log.d("toooooo","token" + token);
                            reference.child(username).child("token").setValue(token);


                            // send it to server
                        }
                    });
                    unix = unixdate+ " "+ unixtime;
//                    Log.e("yo yo" , unix);
                    reference.child(username).child("name").setValue(name);
                    reference.child(username).child("username").setValue(username);
                    reference.child(username).child("from").setValue(fromData);
                    reference.child(username).child("to").setValue(toData);
                    reference.child(username).child("seat").setValue(seatData);
                    reference.child(username).child("date").setValue(dateData);
                    reference.child(username).child("unix").setValue(unix);
                    reference.child(username).child("time").setValue(timeData);
                    reference.child(username).child("staticSeat").setValue(seatData);

                    reference.child(username).child("member0").setValue("");
                    reference.child(username).child("statusmember0").setValue("empty");
                    reference.child(username).child("member1").setValue("");
                    reference.child(username).child("statusmember1").setValue("empty");
                    reference.child(username).child("member2").setValue("");
                    reference.child(username).child("statusmember2").setValue("empty");
                    reference.child(username).child("member3").setValue("");
                    reference.child(username).child("statusmember3").setValue("empty");
                    reference.child(username).child("member4").setValue("");
                    reference.child(username).child("statusmember4").setValue("empty");
                    reference.child(username).child("member5").setValue("");
                    reference.child(username).child("statusmember5").setValue("empty");
                    reference.child(username).child("member6").setValue("");
                    reference.child(username).child("statusmember6").setValue("empty");
                    reference.child(username).child("member7").setValue("");
                    reference.child(username).child("statusmember7").setValue("empty");
                    reference.child(username).child("member8").setValue("");
                    reference.child(username).child("statusmember8").setValue("empty");
                    reference.child(username).child("member9").setValue("");
                    reference.child(username).child("statusmember9").setValue("empty");

                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });





    }
}