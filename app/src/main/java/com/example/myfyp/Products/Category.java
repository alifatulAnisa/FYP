package com.example.myfyp.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfyp.LoginRegister.Profile;
import com.example.myfyp.NotificationReminder.NotificationSetting;
import com.example.myfyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Category extends AppCompatActivity implements CategoryAdapter.CategoryClickListener {
    Button addNewCat;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton actionButton;
    DatabaseReference databaseReference;
    RecyclerView recyclerViewCat;
    CategoryAdapter categoryAdapter;
    ArrayList<Categories> categoryList;
    SearchView searchCategory;
    Button delete;
    TextView catName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        bottomNavigationView = findViewById(R.id.bottomNav);
        actionButton = findViewById(R.id.btnAddNew);
        addNewCat = findViewById(R.id.btnAddNewCat);
        recyclerViewCat = findViewById(R.id.recyclerViewCategory);
        searchCategory = findViewById(R.id.searchViewCategory);
        delete = findViewById(R.id.imgDelete);
        catName = findViewById(R.id.txtCategory);

        searchCategory.clearFocus();

        recyclerViewCat.setHasFixedSize(true);
        recyclerViewCat.setLayoutManager(new LinearLayoutManager(this));
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryList, this::selectedCategory);
        recyclerViewCat.setAdapter(categoryAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Category");

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.category);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Categories c = dataSnapshot.getValue(Categories.class);
                    categoryList.add(c);
                }
                categoryAdapter.notifyDataSetChanged();
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

        addNewCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Category.this, ProductCategory.class));
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.list:
                        startActivity(new Intent(getApplicationContext(), ProductList.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.category:
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
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else {
            categoryAdapter.setFilteredList(filteredList);
        }
    }

    @Override
    public void selectedCategory(Categories categories) {
        startActivity(new Intent(this, CategoryProductDisplay.class).putExtra("data", categories));
    }
}