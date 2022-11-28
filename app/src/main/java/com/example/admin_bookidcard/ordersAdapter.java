package com.example.admin_bookidcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ordersAdapter  extends RecyclerView.Adapter<ordersAdapter.MyViewHolder> {
    Context context;
    ArrayList<Ordersmodel> list;

    public ordersAdapter(Context context, ArrayList<Ordersmodel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_orders,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Ordersmodel ordersmodel = list.get(position);
      holder.userid.setText("User Id : " + ordersmodel.getId());
        holder.printbtn.setVisibility(View.VISIBLE);
        holder.databaseReference = FirebaseDatabase.getInstance().getReference("Students");
        holder.printbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.databaseReference.child(ordersmodel.getId()).child("orders").child("printed").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        holder.printbtn.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "status updated to printed", Toast.LENGTH_SHORT).show();
                        holder.deliverbtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        holder.deliverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.databaseReference.child(ordersmodel.getId()).child("orders").child("delivered").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        holder.deliverbtn.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "status updated to delivered", Toast.LENGTH_SHORT).show();
                        holder.completebtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        holder.completebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.databaseReference.child(ordersmodel.getId()).child("orders").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        holder.completebtn.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, ordersmodel.getId()+" orders has been completed", Toast.LENGTH_SHORT).show();
                        holder.userid.setText(ordersmodel.getId()+"   completed delivery");
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userid , productitem;
        RecyclerView recyclerView;
        ordersAdapter ordersadapter;
        View rootview;
        Ordersmodel ordersmodel;
        DatabaseReference databaseReference;
        Button printbtn, deliverbtn, completebtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userid = itemView.findViewById(R.id.user);
            printbtn =   itemView.findViewById(R.id.printedbtn);
            deliverbtn = itemView.findViewById(R.id.deliveredbtn);
            completebtn = itemView.findViewById(R.id.completedbtn);
            recyclerView =itemView.findViewById(R.id.recylerview);


        }
    }
}
