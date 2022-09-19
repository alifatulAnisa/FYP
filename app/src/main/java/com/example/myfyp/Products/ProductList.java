package com.example.myfyp.Products;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.myfyp.LoginRegister.Profile;
import com.example.myfyp.NotificationReminder.NotificationSetting;
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

import java.util.ArrayList;
import java.util.Locale;

public class ProductList extends AppCompatActivity implements ProductAdapter.ProductClickListener {
    TextView greeting, prodName, catName, monthsLeft, date, productDisplay;
    RecyclerView recyclerViewProd;
    ProductAdapter productAdapter;
    CategoryAdapter categoryAdapter;
    ArrayList<Product> productList;
    ArrayList<Categories> categoryList;
    SearchView searchProduct;

    BottomNavigationView bottomNavigationView;

    FloatingActionButton actionButton;

    FirebaseUser user;
    DatabaseReference databaseReference;
    String userID;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        greeting = findViewById(R.id.txtGreeting);
        bottomNavigationView = findViewById(R.id.bottomNav);
        actionButton = findViewById(R.id.btnAddNew);
        prodName = findViewById(R.id.txtProductName);
        catName = findViewById(R.id.txtCategoryProduct);
        monthsLeft = findViewById(R.id.txtMonths);
        searchProduct = findViewById(R.id.searchViewProduct);
        recyclerViewProd = findViewById(R.id.recyclerViewProduct);
        date = findViewById(R.id.txtDate);
        productDisplay = findViewById(R.id.txtProductDisplay);

        searchProduct.clearFocus();

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Product");
        userID = user.getUid();

        recyclerViewProd.setHasFixedSize(true);
        recyclerViewProd.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList, this::selectedProduct);
        recyclerViewProd.setAdapter(productAdapter);

        searchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductList.this, ProductCategory.class));
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.list:
                        return true;

                    case R.id.category:
                        startActivity(new Intent(getApplicationContext(), Category.class));
                        overridePendingTransition(0, 0);

                        return true;

                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(), NotificationSetting.class));
                        overridePendingTransition(0, 0);

                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);

                        return true;
                }
                return false;
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Product p = dataSnapshot.getValue(Product.class);
                    productList.add(p);
                    productDisplay.setVisibility(View.GONE);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterList(String newText) {
        ArrayList<Product> filteredList = new ArrayList<>();
        ArrayList<Categories> filteredCat = new ArrayList<>();

        for (Product product : productList) {
            if (product.getProdName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(product);
            }else if (product.getTypeCosmetic().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(product);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else {
            productAdapter.setFilteredList(filteredList);
        }
    }

    @Override
    public void selectedProduct(Product product) {
        startActivity(new Intent(this, ProductDisplay.class).putExtra("data", product));
    }
}