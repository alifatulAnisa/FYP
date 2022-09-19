package com.example.myfyp.Products;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{
    private Context context;
    ArrayList<Categories> list;
    CategoryClickListener categoryClickListener;
    DatabaseReference ref;

    public interface CategoryClickListener {
        void selectedCategory (Categories categories);
    }

    public CategoryAdapter(Context ctx, ArrayList<Categories> list, CategoryClickListener categoryClickListener) {
        this.context = ctx;
        this.list = list;
        this.categoryClickListener = categoryClickListener;
    }

    public void setFilteredList(ArrayList<Categories> filteredList) {
        this.list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category_entry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Categories category = list.get(position);
        ref = FirebaseDatabase.getInstance().getReference("Product").child("typeCosmetic");

        holder.name.setText(category.getCategoryName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (category.getCategoryName().equals(ref)) {
                        categoryClickListener.selectedCategory(category);
                    }else {
                            Toast.makeText(context, "No product(s) in this category", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted data can't be undo");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query query = ref.child("Category").orderByChild("categoryName").equalTo(String.valueOf(category.getCategoryName()));

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    dataSnapshot.getRef().removeValue();

                                    Toast.makeText(context.getApplicationContext(), "Category Deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, Category.class);
                                    context.startActivity(intent);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context.getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        Button delete;

        public MyViewHolder(@NonNull View itemView) {
            super (itemView);

            name = itemView.findViewById(R.id.txtCategory);
            delete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
