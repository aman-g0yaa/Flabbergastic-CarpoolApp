package com.amangoyal.finale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
//In this class we will have three sections
//1 - oncreate , 2-holder , 3-binds
//1 - creates a single row view , 2 - bring its reference into out hand , 3-using that reference we put the data from model class
//this process keeps repeating and more views are made my 1 again n again

//Link -https://www.youtube.com/watch?v=sZ8D1-hNeWo

//line 16
//Here the imp stuff to understand is , in this class we are telling ki u will get a model class (jissmeh data hai)
// and a holder were u want to put that data
public class RecyclerAdapter extends FirebaseRecyclerAdapter<ModelClass,RecyclerAdapter.Holder> {


    //constructor -- to get data and initialized it into options
    //so when we send data into the adapter from join class it gets stored in options

    //super() is used to call the constructor of the super class (class which it got extended to Fire...)
    // if u pass parameter inside the super(int i) then it will call the parameterised constructor of the super class.
    public RecyclerAdapter(@NonNull FirebaseRecyclerOptions<ModelClass> options) {
        super(options);

    }



    String token;
    Integer flag = 0;
    String s = "";
    String temp = "member2";


    //Number 1
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //here we create(inflate) new view (rows)

        //create new row / view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        //added the layout to the view which we created
        View view = layoutInflater.inflate(R.layout.item_design_join, parent, false);

        // here we create an object of the class Holder and pass this view/skeleton to it such that it puts reference of the elements to it.
        Holder holder = new Holder(view);

        return holder;
    }



    // Number 2
    //Here this class stores the reference of all elements of that single view created
    class Holder extends RecyclerView.ViewHolder {

        //declaration of elements
        private TextView  name_data;
        private TextView  from_data;
        private TextView to_data;
        private TextView  seat_data;
        private TextView date_data;
        private TextView time_data;
        private Button join_bt;


        //constructor --- intializes all the elements with the references(id)
        //now we got reference of the elements of that view in our hand aka holder
        public Holder(@NonNull View itemView) {
            super(itemView);

            name_data = itemView.findViewById(R.id.name_text_id);
            from_data = itemView.findViewById(R.id.from_text);
            to_data = itemView.findViewById(R.id.to_text_id);
            seat_data = itemView.findViewById(R.id.seat_text_id);
            date_data = itemView.findViewById(R.id.date_text_id);
            time_data= itemView.findViewById(R.id.time_text_id);
            join_bt = itemView.findViewById(R.id.status_bt_id);



        }
    }


    //Number 3
    //now we have everything - a complete body with a view and reference , data to be added from model class(options)
    //so now we need to bind the data from model class using its setters
    @Override
    protected void onBindViewHolder(@NonNull final Holder holder, final int i, @NonNull final ModelClass modelClass) {

        holder.name_data.setText(modelClass.getName());
        holder.from_data.setText(modelClass.getFrom());
        holder.to_data.setText(modelClass.getTo());
        holder.date_data.setText(modelClass.getDate());
        holder.time_data.setText(modelClass.getTime());
        holder.seat_data.setText(modelClass.getSeat());

        //here i take context from HomeFragment because this being a non activity class i wont have a context
        Context applicationContext = HomeFragment.getContextOfApplication_HomeFragment();
        //i get the current username from shared preferences
        SharedPreferences preferences =applicationContext.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        final String current_user = preferences.getString("username","");

        final Context c = join.getContextOfApplication_join();



        //if you want to send notification to all users then use bellow code only
////                FirebaseMessaging.getInstance().subscribeToTopic("all");
////                FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all","baba","ramdev",c, (Activity) join.getContextOfApplication_join2());
////                notificationsSender.SendNotifications();
//
//
//
//            }
//        });

        final DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("pool").child(modelClass.getUsername());
//        Toast.makeText(c,current_user,Toast.LENGTH_SHORT).show();



            if (current_user.equals(modelClass.getUsername())) {

                holder.join_bt.setText("DELETE");
            }
            else if(holder.join_bt.getText().toString().equals("status")){

                flag=0;
                s = "";
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            if(snap.getValue().toString().equals(current_user)){
                                flag=1;

                                DatabaseReference ref = reference.child("status"+(snap.getKey()));
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                        s = (String) snapshot.getValue();
                                        holder.join_bt.setText(s);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                break;

                            }
                            else {
                                holder.join_bt.setText("JOIN");
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            holder.join_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
                    Integer seat = Integer.parseInt(holder.seat_data.getText().toString());
                    String status = holder.join_bt.getText().toString();

                    //ref of the pool we clicked on
                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("pool").child(modelClass.getUsername());

//                    Toast.makeText(c, modelClass.getUsername(), Toast.LENGTH_SHORT).show();

                    if (status.equals("DELETE")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.date_data.getContext());
                        builder.setTitle("Delete panel");
                        builder.setMessage("Delete...?");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("pool").child(getRef(i).getKey()).removeValue();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        builder.show();
                    } else if (status.equals("JOIN")) {

                        if (seat > 0) {
                            holder.join_bt.setText("REQUESTED");

                            //adding a new child to the pool of us since we clicked join

                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                    for (DataSnapshot snap : snapshot.getChildren()) {
                                        String check = snap.getKey();

                                        if(check.charAt(0) == 'm' && snap.getValue().equals("")){
//                                            Toast.makeText(c, check, Toast.LENGTH_SHORT).show();
                                            temp = check;
                                            ref.child(temp).setValue(current_user);
                                            ref.child("status"+(temp)).setValue("REQUESTED");
//                                        Toast.makeText(c, temp, Toast.LENGTH_SHORT).show();
                                            break;
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference reff = FirebaseDatabase.getInstance().getReference("pool").child(modelClass.getUsername()).child("token");
                            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                    token = snapshot.getValue(String.class);
                                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,
                                            "Request to join your pool",
                                            current_user,
                                            c,
                                            (Activity) join.getContextOfApplication_join2());
                                    notificationsSender.SendNotifications();
                                }
                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });

                        } else {
                            Toast.makeText(v.getContext(), "The pool is full", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
    }





}
