package com.example.assignment4;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ItemAdapter extends FirebaseRecyclerAdapter<Item, ItemAdapter.ItemViewHolder> {

    Context parent;


    public ItemAdapter(@NonNull FirebaseRecyclerOptions<Item> options, Context context) {
        super(options);
        parent = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Item model) {
        holder.tvName.setText(model.getName());
        holder.tvQuantity.setText("Quantity: " +model.getQuantity());
        holder.tvDate.setText(model.getTimestamp());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(parent)
                        .inflate(R.layout.add_item_design, null);

                TextView tvTimeStamp = v.findViewById(R.id.tvTimeStamp);
                TextInputEditText etItemName = v.findViewById(R.id.etItemName);
                TextInputEditText etItemQuantity = v.findViewById(R.id.etItemQuantity);
                etItemName.setText(model.getName());
                etItemQuantity.setText(model.getQuantity());
                // Get the current date and time
                Date currentDate = new Date();

                // Format the date and time
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String formattedDate = dateFormat.format(currentDate);

                tvTimeStamp.setText(formattedDate);

                tvTimeStamp.setText(tvTimeStamp.getText().toString().trim());



                AlertDialog.Builder editItem = new AlertDialog.Builder(parent)
                        .setTitle("Edit").setView(v)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String itemName = etItemName.getText().toString().trim();
                                String itemQuantity = etItemQuantity.getText().toString().trim();
                                String timeStamp = tvTimeStamp.getText().toString().trim();

                                HashMap<String, Object> data = new HashMap<>();
                                data.put("name", itemName);
                                data.put("quantity", itemQuantity);
                                data.put("timestamp", timeStamp);

                                getRef(position).updateChildren(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(parent, "Record Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(parent, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                editItem.create();
                editItem.show();


            }

        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRef(position).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(parent, "Record Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(parent, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_design, parent,false);
        return new ItemViewHolder(v);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvQuantity, tvDate;
        ImageView ivDelete, ivEdit;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }
    }
}
