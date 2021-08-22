package com.amangoyal.finale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


//to understand this refer - https://www.youtube.com/watch?v=sZ8D1-hNeWo query +  FirebaseRecyclerOptions
public class join extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter adapter;

    public static Context contextOfApplication;
    public static Context contextas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //stores context;
        contextOfApplication = getApplicationContext();
        contextas = join.this;

        //this recyclerView stores the reference to the recycler layout and we set it onto current layout.
        recyclerView = (RecyclerView) findViewById(R.id.join_recycle_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM");
        String dt = simpleDateFormat.format(calendar.getTime());
        Log.e("date" , dt);




        //stores reference to pool as query
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("pool").orderByChild("date");


        //here this is a feature by firebase for recycler view , i pass model class and query to it and at the end
        //i get all the data stored in options
        FirebaseRecyclerOptions<ModelClass> options =
                new FirebaseRecyclerOptions.Builder<ModelClass>()
                        .setQuery(query, ModelClass.class)
                        .build();

        //after i get the data i pass options into the adpater so that it covert this data into an view and binds on the view
        adapter = new RecyclerAdapter(options);
        //i set this view (single row) on to the recycler view
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    //return context of this activity
    public static Context getContextOfApplication_join(){
        return contextOfApplication;
    }
    public static Context getContextOfApplication_join2(){
        return contextas;
    }






//        SharedPreferences preferences =getSharedPreferences("login", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        final String a = preferences.getString("username","");
//
//        Query checkUser = FirebaseDatabase.getInstance().getReference("pool");
//
//        checkUser.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for(DataSnapshot x : snapshot.getChildren()) {
//
//
//
//                    String username = x.child("username").getValue(String.class);
//                    String name = x.child("name").getValue(String.class);
//                    String from =x.child("from").getValue(String.class);
//                    String to = x.child("to").getValue(String.class);
//                    String seat = x.child("seat").getValue(String.class);
//                    String date = x.child("date").getValue(String.class);
//                    String time = x.child("time").getValue(String.class);
//                    String CurrentUsernameFromSharedP = a;
//                    String status = "";
//
//                    //Toast.makeText(getApplicationContext(), from, Toast.LENGTH_SHORT).show();
//
//                    from = "From - " + from;
//                    to = "To - " + to;
//
//
//
//                    travelList.add(new ModelClass(name,from,to,seat,date,time,username ,CurrentUsernameFromSharedP));
//                    adapter.notifyDataSetChanged();
//                }
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    // for search icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Enter To Location...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                psearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                psearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void psearch(String s) {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("pool");

        //here this is a feature by firebase for recycler view , i pass model class and query to it and at the end
        //i get all the data stored in options
        FirebaseRecyclerOptions<ModelClass> options =
                new FirebaseRecyclerOptions.Builder<ModelClass>()
                        .setQuery(query.orderByChild("to").startAt(s).endAt(s+"\uf8ff"), ModelClass.class)
                        .build();


        adapter = new RecyclerAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}