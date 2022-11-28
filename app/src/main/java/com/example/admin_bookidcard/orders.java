package com.example.admin_bookidcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class orders extends AppCompatActivity {
    DatabaseReference databaseReference;
    ordersAdapter ordersadapter;
    ArrayList<Ordersmodel> list;
    TextView order;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerorders);
        recyclerView = findViewById(R.id.recylerview);
        databaseReference = FirebaseDatabase.getInstance().getReference("Students");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        ordersadapter  = new ordersAdapter(this, list);
        recyclerView.setAdapter(ordersadapter);
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.child("orders").exists()){
                        String orderid = String.valueOf(ds.child("orders").child("id").getValue());
                        Ordersmodel ordersmodel = ds.child("orders").getValue(Ordersmodel.class) ;
                        list.add(ordersmodel);
                    }
                }
                ordersadapter.notifyDataSetChanged();
            }
        });

    }
}