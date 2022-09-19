package com.example.myfyp.LoginRegister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfyp.Products.Product;
import com.example.myfyp.Products.ProductAdapter;
import com.example.myfyp.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder>{
    Context context;
    ArrayList<Seller> list;
    ProfileClickListener profileClickListener;

    public ProfileAdapter(Context ctx, ArrayList<Seller> list, ProfileClickListener profileClickListener) {
        this.context = ctx;
        this.list = list;
        this.profileClickListener = profileClickListener;
    }

    public interface ProfileClickListener {
        void selectedProfile (Seller seller);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_update_profile, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.MyViewHolder holder, int position) {
        Seller seller = list.get(position);

        holder.name.setText(seller.getUsername());
        holder.email.setText(seller.getEmail());
        holder.phone.setText(seller.getPhone());
        holder.password.setText(seller.getPassword());
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileClickListener.selectedProfile(seller);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, email, phone, password;
        Button update;

        public MyViewHolder(@NonNull View itemView) {
            super (itemView);

            name = itemView.findViewById(R.id.txtUsername);
            email = itemView.findViewById(R.id.txtEmail);
            phone = itemView.findViewById(R.id.txtPhoneNumber);
            password = itemView.findViewById(R.id.txtPassword);
            update = itemView.findViewById(R.id.btnUpdateProfile);
        }
    }
}
