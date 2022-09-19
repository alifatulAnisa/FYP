package com.example.myfyp.Products;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfyp.R;

import java.util.ArrayList;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.MyViewHolder>{
    private Context context;
    ArrayList<Categories> list;
    CategoryClickListener categoryClickListener;

    public interface CategoryClickListener {
        void selectedCategory (Categories categories);
    }

    public ProductCategoryAdapter(Context ctx, ArrayList<Categories> list, CategoryClickListener categoryClickListener) {
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
    public void onBindViewHolder(@NonNull ProductCategoryAdapter.MyViewHolder holder, int position) {
        Categories category = list.get(position);

        //final FetchData fetchData =

        holder.name.setText(category.getCategoryName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClickListener.selectedCategory(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super (itemView);

            name = itemView.findViewById(R.id.txtCategory);
        }
    }
}
