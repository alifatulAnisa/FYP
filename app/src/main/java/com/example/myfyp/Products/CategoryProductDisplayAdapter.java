package com.example.myfyp.Products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CategoryProductDisplayAdapter extends RecyclerView.Adapter<CategoryProductDisplayAdapter.MyViewHolder>{
    Context context;
    ArrayList<Product> list;
    CategoryProductDisplayAdapter.ProductClickListener productClickListener;

    public interface ProductClickListener {
        void selectedProduct (Product product);
    }

    public CategoryProductDisplayAdapter(Context ctx, ArrayList<Product> list) {
        this.context = ctx;
        this.list = list;
    }

    public void setFilteredList(ArrayList<Product> filteredList) {
        this.list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category_product_display_entry, parent, false);
        return new MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CategoryProductDisplayAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product product = list.get(position);

        Calendar calendar = Calendar.getInstance();
        Calendar end_calendar = Calendar.getInstance();

        String pickerDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        end_calendar.set(2015, 10, 6);

        /*category = FirebaseDatabase.getInstance().getReference().child("Category").child("categoryName");
        prodDb = FirebaseDatabase.getInstance().getReference().child("Product").child("typeCosmetic");*/

        //if (product.getCategories().getCategoryName() == product.getTypeCosmetic()) {

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

                    //int years = period.getYears();
                    int months = period.getMonths();
                    int days = period.getDays();

                    holder.monthsCount.setText(months + " Months " + days + " Days");
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
        //}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView productName, productCategory, monthsCount;


        public MyViewHolder(@NonNull View itemView) {
            super (itemView);

            productName = itemView.findViewById(R.id.txtProductName);
            productCategory = itemView.findViewById(R.id.txtCategoryProduct);
            monthsCount = itemView.findViewById(R.id.txtMonths);
        }
    }
}
