package com.example.myfyp.Products;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.myfyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CategoryProductDisplay extends AppCompatActivity {
    Button back, addNewCat;
    TextView prodName, category, date, countdown;
    Intent intent;
    ArrayList<Product> productList;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton actionButton;
    RecyclerView recyclerViewProdCat;
    CategoryProductDisplayAdapter adapter;

    Product product;
    Categories categories;

    DatabaseReference databaseReference;
    String userID;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product_display);

        bottomNavigationView = findViewById(R.id.bottomNav);
        actionButton = findViewById(R.id.btnAddNew);
        addNewCat = findViewById(R.id.btnAddNewCat);
        recyclerViewProdCat = findViewById(R.id.recyclerViewProduct);
        prodName = findViewById(R.id.txtProductName);
        category = findViewById(R.id.txtCategoryProduct);
        date = findViewById(R.id.txtDate);
        countdown = findViewById(R.id.txtMonths);
        back = findViewById(R.id.btnBack);

        intent = getIntent();

        product = new Product();
        categories = new Categories();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Product");

        recyclerViewProdCat.setHasFixedSize(true);
        recyclerViewProdCat.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new CategoryProductDisplayAdapter(this, productList);
        recyclerViewProdCat.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryProductDisplay.this, Category.class));
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Product p = dataSnapshot.getValue(Product.class);
                    productList.add(p);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}