package com.example.myfyp.Products;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.iwgang.countdownview.CountdownView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>{
    Context context;
    ArrayList<Product> list;
    ProductClickListener productClickListener;

    public interface ProductClickListener {
        void selectedProduct (Product product);
    }

    public ProductAdapter(Context ctx, ArrayList<Product> list, ProductClickListener productClickListener) {
        this.context = ctx;
        this.list = list;
        this.productClickListener = productClickListener;
    }

    public void setFilteredList(ArrayList<Product> filteredList) {
        this.list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_entry, parent, false);
        return new MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product product = list.get(position);
        Calendar calendar = Calendar.getInstance();

        String pickerDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        holder.productName.setText(product.getProdName());
        holder.productCategory.setText(product.getTypeCosmetic());
        holder.monthsCount.setText(product.getExpiry(pickerDateString));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date now = new Date();
            Date expiryProd = sdf.parse(product.expiry);

            long start = now.getTime();
            long expiryDate = expiryProd.getTime();

            if (start <= expiryDate) {

                Period period = new Period(start, expiryDate, PeriodType.yearMonthDay());

                int months = period.getMonths();
                int days = period.getDays();

                holder.monthsCount.setText(months + " Months " + days + " Days");
            }else if (start >= expiryDate){
                holder.monthsCount.setText("Product Expired");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productClickListener.selectedProduct(product);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView productImage, productName, productCategory, productMDate, productEDate, productPrice, productQuantity, monthsCount;
        Button delete;

        public MyViewHolder(@NonNull View itemView) {
            super (itemView);

            productImage = itemView.findViewById(R.id.imgProductR);
            productName = itemView.findViewById(R.id.txtProductName);
            productCategory = itemView.findViewById(R.id.txtCategoryProduct);
            productMDate = itemView.findViewById(R.id.dateManufacturedR);
            productEDate = itemView.findViewById(R.id.txtDate);
            productQuantity = itemView.findViewById(R.id.numQuantityR);
            delete = itemView.findViewById(R.id.btnDeleteProduct);
            monthsCount = itemView.findViewById(R.id.txtMonths);
            productPrice = itemView.findViewById(R.id.price);
        }
    }
}
