package com.example.myfyp.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfyp.NotificationReminder.NotificationSetting;
import com.example.myfyp.Products.AddProduct;
import com.example.myfyp.Products.Category;
import com.example.myfyp.Products.ProductAdapter;
import com.example.myfyp.Products.ProductCategory;
import com.example.myfyp.Products.ProductDisplay;
import com.example.myfyp.Products.ProductList;
import com.example.myfyp.R;
import com.example.myfyp.databinding.ActivityUpdateProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity implements ProfileAdapter.ProfileClickListener{
    Button edit, logout;
    TextView greeting, username, email, phone, password;

    BottomNavigationView bottomNavigationView;

    FloatingActionButton actionButton;

    FirebaseUser user;
    DatabaseReference databaseReference;
    String userID;
    Seller seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edit = findViewById(R.id.btnUpdateProfile);
        username = findViewById(R.id.txtUsernameSeller);
        email = findViewById(R.id.txtEmailSeller);
        phone = findViewById(R.id.txtPhoneSeller);
        password = findViewById(R.id.txtPassSeller);

        greeting = findViewById(R.id.txtGreeting);
        bottomNavigationView = findViewById(R.id.bottomNav);
        actionButton = findViewById(R.id.btnAddNew);
        logout = findViewById(R.id.btnLogout);

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Seller");
        userID = user.getUid();

        seller = new Seller();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this, Login.class));
                //finish();
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, ProductCategory.class));
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
                        startActivity(new Intent(getApplicationContext(), Category.class));
                        overridePendingTransition(0, 0);

                        return true;

                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(), NotificationSetting.class));
                        overridePendingTransition(0, 0);

                        return true;

                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Seller sellerProfile = snapshot.getValue(Seller.class);

                if (sellerProfile != null) {
                    String greet = sellerProfile.username;
                    String name = sellerProfile.username;
                    String userEmail = sellerProfile.email;
                    String userPhone = sellerProfile.phone;

                    greeting.setText("Welcome, "+ greet);
                    username.setText(name);
                    email.setText(userEmail);
                    phone.setText(userPhone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UpdateProfile.class));
            }
        });

    }

    @Override
    public void selectedProfile(Seller seller) {
        startActivity(new Intent(this, UpdateProfile.class).putExtra("data", seller));
    }
}