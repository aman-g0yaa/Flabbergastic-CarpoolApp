package com.amangoyal.finale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.amangoyal.finale.My_pool;
import com.amangoyal.finale.R;
import com.amangoyal.finale.create_pool;
import com.amangoyal.finale.join;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {


    Button create , join ,mypool;

    public static Context contextOfApplication;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");

        join =root.findViewById(R.id.join_bt);
        create =root.findViewById(R.id.create_bt);
        mypool=root.findViewById(R.id.my_pool_bt);

        //storing context of the activity to use in shared preferences.
        contextOfApplication = getContext();
        SharedPreferences preferences =contextOfApplication.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        final String a = preferences.getString("username","");

        final String[] name = new String[1];
        final String[] password = new String[1];
        final String[] email = new String[1];
        final String[] phone = new String[1];

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild(a);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                name[0] = snapshot.child(a).child("name").getValue(String.class);
                 password[0] = snapshot.child(a).child("password").getValue(String.class);
                 email[0] = snapshot.child(a).child("email").getValue(String.class);
                phone[0] = snapshot.child(a).child("phone").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_join_pool = new Intent(getActivity(), com.amangoyal.finale.join.class);
                startActivity(to_join_pool);

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_create_pool = new Intent(getActivity() , create_pool.class);
                to_create_pool.putExtra("username" , a);
                to_create_pool.putExtra("name" , name[0]);
                to_create_pool.putExtra("password" , password[0]);
                to_create_pool.putExtra("email" , email[0]);
                to_create_pool.putExtra("phone" , phone[0]);
                startActivity(to_create_pool);
            }
        });


        mypool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , My_pool.class);
                startActivity(intent);
            }
        });


        return root;
    }

    // a getter method which return the context of this activity
    // this will be used for shared preferences across diff non activity classes
    public static Context getContextOfApplication_HomeFragment(){
        return contextOfApplication;
    }


}