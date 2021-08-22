package com.amangoyal.finale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Dashboard_LoginedInUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard__logined_in_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);


        SharedPreferences preferences =getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        final String a = preferences.getString("username","");

//        long unixTime = System.currentTimeMillis();
//        Log.e("gandi" , String.valueOf(unixTime/1000));
//
//        Calendar c2 = Calendar.getInstance();
//
//        long val2 =  c2.getTime().getTime();
//
//        Log.e("yo yo " , String.valueOf(val2));
//




//        try {
//            long epoch = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse("01/01/1970 01:00:00").getTime() / 1000;
//            Log.e("yo yo value" , String.valueOf(epoch));
//        } catch (ParseException e) {
//            Log.e("yo yo error" , e+"sss");
//        }

        final ArrayList<String> ar = new ArrayList<>();

        final DatabaseReference reff = FirebaseDatabase.getInstance().getReference("pool");
        reff.addValueEventListener(new ValueEventListener() {



            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot x : snapshot.getChildren()){

                    if(x.hasChild("unix")){
                        String unix = x.child("unix").getValue(String.class);

                        Calendar c1 = Calendar.getInstance();

                        int month = Integer.parseInt(unix.substring(0,2));
                        int date = Integer.parseInt(unix.substring(3,5));
                        int year = Integer.parseInt(unix.substring(6,10));

                        int hh = Integer.parseInt(unix.substring(11,13));
                        int mm = Integer.parseInt(unix.substring(14,16));
                        c1.set(year,month,date ,hh,mm,00);

                        Date tt = c1.getTime();
                        long val =tt.getTime();

                        Calendar c2 = Calendar.getInstance();

                        long val2 =  c2.getTime().getTime();
//                        Log.e("val1" , String.valueOf(val));
//                        Log.e("val2" , String.valueOf(val2));
                        if(val < val2) {
//                            Log.e("key" , x.getKey());
                            ar.add(x.getKey());
                        }
                    }

                }

                for(int i=0;i<ar.size();i++){
                    String ele = ar.get(i);
                    reff.child(ele).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChild(a)){

                    reff.child(a).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            int count = 0;

                            for(DataSnapshot x : snapshot.getChildren()){
                                if(x.getKey().charAt(0) == 's' && x.getKey().charAt(1)=='t' && x.getValue(String.class).equals("JOINED")){
                                    count++;
                                }
                            }


                            int StaticSeat  = Integer.parseInt(snapshot.child("staticSeat").getValue(String.class));

                            reff.child(a).child("seat").setValue(String.valueOf(StaticSeat - count));

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        final TextView navUsername = (TextView) headerView.findViewById(R.id.nav_header_textView);





        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(a).child("name");


       ref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {


              navUsername.setText(snapshot.getValue(String.class));


           }

           @Override
           public void onCancelled(@NonNull @NotNull DatabaseError error) {

           }
       });
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_slideshow:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HelpFragment()).commit();
                break;
            case R.id.nav_gallery:

                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard_LoginedInUser.this);
                builder.setTitle("Logout");
                builder.setMessage("are you sure?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(Dashboard_LoginedInUser.this, "hello", Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences =getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent logout = new Intent(getApplicationContext(), login.class);
                        startActivity(logout);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();

                break;
        }

      drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}