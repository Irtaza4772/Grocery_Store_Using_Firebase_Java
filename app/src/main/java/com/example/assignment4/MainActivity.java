package com.example.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton btnAdd;

    RecyclerView rvItems;

    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

       btnAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               View view = LayoutInflater.from(MainActivity.this)
                       .inflate(R.layout.add_item_design, null);

               TextView tvTimeStamp = view.findViewById(R.id.tvTimeStamp);
               TextInputEditText etItemName = view.findViewById(R.id.etItemName);
               TextInputEditText etItemQuantity = view.findViewById(R.id.etItemQuantity);

               // Get the current date and time
               Date currentDate = new Date();

               // Format the date and time
               SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
               String formattedDate = dateFormat.format(currentDate);

               tvTimeStamp.setText(formattedDate);


               AlertDialog.Builder addNewItem = new AlertDialog.Builder(MainActivity.this)
                       .setTitle("Add New Item").setView(view)
                       .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               String itemName = etItemName.getText().toString().trim();
                               String itemQuantity = etItemQuantity.getText().toString().trim();

                               HashMap<String, Object> data = new HashMap<>();
                               data.put("name", itemName);
                               data.put("quantity", itemQuantity);
                               data.put("timestamp", tvTimeStamp.getText().toString().trim());

                               FirebaseDatabase.getInstance()
                                       .getReference().child("Items")
                                       .push()
                                       .setValue(data)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {
                                               Toast.makeText(MainActivity.this, "Record Added", Toast.LENGTH_SHORT).show();
                                           }
                                       })
                                       .addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                           }
                                       });

                           }
                       })
                       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                           }
                       });
               addNewItem.create();
               addNewItem.show();
           }
       });
    }

    public void init()
    {
        btnAdd = findViewById(R.id.btnAdd);
        rvItems = findViewById(R.id.rvItems);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Items");

        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(query, Item.class)
                        .build();

        adapter = new ItemAdapter(options, this);
        rvItems.setAdapter(adapter);


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
}