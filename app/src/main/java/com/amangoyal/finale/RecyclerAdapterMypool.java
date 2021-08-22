package com.amangoyal.finale;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class RecyclerAdapterMypool extends RecyclerView.Adapter {


    ArrayList<Pair<String, String>> data;

    private RecyclerViewClickInterface recyclerViewClickInterface;

    public RecyclerAdapterMypool(ArrayList<Pair<String, String>> data , RecyclerViewClickInterface recyclerViewClickInterface ) {

        this.data = data;
        this.recyclerViewClickInterface =  recyclerViewClickInterface;
    }


    //this is a method which return 1,0 to view holder as view type according to condition so later we can use these for multiple view
    @Override
    public int getItemViewType(int position) {
        if (data.get(position).second.equals("JOINED")) {
            return 1;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_design_my_pool_recycler, parent, false);
            return new holder1(view);
        } else {
            View view = inflater.inflate(R.layout.item_design_my_pool_recycler_2, parent, false);
            return new holder2(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if(data.get(position).second.equals("JOINED")){
            holder2 two = (holder2) holder;
            two.name.setText(data.get(position).first);
        }
        else {
            holder1 one = (holder1) holder;
            one.name.setText(data.get(position).first);
        }

    }

    public void removeItem(int position) {

        if(data.size() <= position+1){
        data.remove(position);
        notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    //we need two view holder as there is 2 views
    class holder1 extends RecyclerView.ViewHolder {

        TextView name;
        Button accept_bt;
        Button reject_bt;



        public holder1(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.my_ride_name_id);
            accept_bt = (Button) itemView.findViewById(R.id.my_ride_accept_id);
            reject_bt = (Button) itemView.findViewById(R.id.my_ride_reject_id);


            accept_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onAccept(getAbsoluteAdapterPosition() , name.getText().toString());
                }
            });

            reject_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onReject(getAbsoluteAdapterPosition(), name.getText().toString());
                }
            });

        }


    }
    class holder2 extends RecyclerView.ViewHolder {

        TextView name;

        public holder2(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.my_ride_name_id2);
        }
    }
}
