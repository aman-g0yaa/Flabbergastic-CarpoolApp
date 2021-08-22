package com.amangoyal.finale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class My_pool extends AppCompatActivity  implements RecyclerViewClickInterface{


    RecyclerView recyclerView;
    RecyclerAdapterMypool adapterMypool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pool);


        recyclerView = (RecyclerView) findViewById(R.id.my_pool_recyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Context applicationContext = HomeFragment.getContextOfApplication_HomeFragment();
        SharedPreferences preferences =applicationContext.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        final String current_user = preferences.getString("username","");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("pool").child(current_user);
        final ArrayList<Pair<String,String>>members = new ArrayList<>();


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {


                //this is how to go to all the children of an node
                for(DataSnapshot x : snapshot.getChildren()){


                    String cur = x.getKey();
                    String val = x.getValue(String.class);


                    // && !snapshot.child("statusmember" + cur.charAt(6)).equals("JOIN")


                    if(cur.length() == 7){

                        if(val.length()!=0){

                            String a = "status" + cur;
                           // Toast.makeText(getApplicationContext(), snapshot.child(a).getValue(String.class) , Toast.LENGTH_SHORT).show();


                            if(!snapshot.child(a).getValue(String.class).equals("REJECTED")) {
                                String val1 = val;
                                String val2 = snapshot.child(a).getValue(String.class);

                                members.add(new Pair<String, String>(val1,val2));
                            }

                        }
                    }
                }
                adapterMypool = new RecyclerAdapterMypool(members , My_pool.this);
                recyclerView.setAdapter(adapterMypool);
                adapterMypool.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void onAccept(final int position , final String user) {

        Context applicationContext = HomeFragment.getContextOfApplication_HomeFragment();
        SharedPreferences preferences =applicationContext.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        final String current_user = preferences.getString("username","");
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("pool").child(current_user);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                for(DataSnapshot x : snapshot.getChildren()){
                    if(x.getValue().toString().equals(user)){
                        String newkey = "status"+x.getKey();
                        ref.child(newkey).setValue("JOINED");
                        adapterMypool.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("users").child(user).child("token");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String token = snapshot.getValue(String.class);
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,
                        "Accepted",
                        "your request to join the pool has been Accepted",
                        getApplicationContext(),
                       My_pool.this);
                notificationsSender.SendNotifications();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        adapterMypool.notifyDataSetChanged();
        finish();
        startActivity(getIntent());

        String a = String.valueOf(position);
        Toast.makeText(applicationContext, a, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onReject(final int position , final String user) {


        final Context applicationContext = HomeFragment.getContextOfApplication_HomeFragment();
        SharedPreferences preferences =applicationContext.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        final String current_user = preferences.getString("username","");
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("pool").child(current_user);

        adapterMypool.removeItem(position);
        adapterMypool.notifyItemRemoved(position);
        adapterMypool.notifyItemRangeChanged(position,adapterMypool.getItemCount());


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                for(DataSnapshot x : snapshot.getChildren()){
                    if(x.getValue().toString().equals(user)){
                        String newkey = "status"+x.getKey();
                        ref.child(newkey).setValue("REJECTED");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("users").child(user).child("token");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String token = snapshot.getValue(String.class);
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,
                        "Rejected",
                        "your request to join the pool has been rejected",
                        getApplicationContext(),
                        My_pool.this);
                notificationsSender.SendNotifications();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        finish();
        startActivity(getIntent());
        String a = String.valueOf(position);
        Toast.makeText(applicationContext, a, Toast.LENGTH_SHORT).show();


    }

}