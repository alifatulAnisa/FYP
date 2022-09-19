package com.example.myfyp.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myfyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductCategory extends AppCompatActivity implements ProductCategoryAdapter.CategoryClickListener {
    Button addNewCat, back;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton actionButton;
    DatabaseReference databaseReference;
    RecyclerView recyclerViewCat;
    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;
    ProductCategoryAdapter prodCatAdapter;
    ArrayList<Categories> categoryList;
    ArrayList<Product> productsList;
    SearchView searchCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        bottomNavigationView = findViewById(R.id.bottomNav);
        actionButton = findViewById(R.id.btnAddNew);
        addNewCat = findViewById(R.id.btnAddNewCat);
        recyclerViewCat = findViewById(R.id.recyclerViewCategory);
        searchCategory = findViewById(R.id.searchViewCategory);
        back = findViewById(R.id.btnBack);

        searchCategory.clearFocus();


        recyclerViewCat.setHasFixedSize(true);
        recyclerViewCat.setLayoutManager(new LinearLayoutManager(this));
        categoryList = new ArrayList<>();
        prodCatAdapter = new ProductCategoryAdapter(this, categoryList, this::selectedCategory);
        recyclerViewCat.setAdapter(prodCatAdapter);

        /*productsList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productsList, this::selectedProduct);
        recyclerViewCat.setAdapter(categoryAdapter);*/

        databaseReference = FirebaseDatabase.getInstance().getReference("Category");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Categories c = dataSnapshot.getValue(Categories.class);
                    categoryList.add(c);
                }
                prodCatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        /*addNewCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);
            }
        });*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductCategory.this, ProductList.class));
            }
        });
    }
    public void showDialog(View view) {
        CustomDialog custom = new CustomDialog();
        custom.show(getSupportFragmentManager(), "Add New Category");
    }

    private void filterList(String newText) {
        ArrayList<Categories> filteredList = new ArrayList<>();

        for (Categories category : categoryList) {
            if (category.getCategoryName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(category);
            }
        }
        if (filteredList.isEmpty()) {
            //Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else {
            categoryAdapter.setFilteredList(filteredList);
        }
    }

    @Override
    public void selectedCategory(Categories categories) {
        startActivity(new Intent(this, AddProduct.class).putExtra("data", categories));
    }
}