package com.example.myfyp.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfyp.Products.Product;
import com.example.myfyp.Products.ProductDisplay;
import com.example.myfyp.Products.ProductList;
import com.example.myfyp.R;
import com.example.myfyp.databinding.ActivityUpdateProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {
    Seller seller;
    Intent intent;
    Button update, back;
    EditText name, email, phone, password;

    String userID;
    FirebaseUser user;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        name = findViewById(R.id.txtUsername);
        email = findViewById(R.id.txtEmail);
        phone = findViewById(R.id.txtPhoneNumber);
        password = findViewById(R.id.txtPassword);
        update = findViewById(R.id.btnUpdateProfile);
        back = findViewById(R.id.btnBack);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Seller");
        userID = user.getUid();

        intent = getIntent();

        seller = new Seller();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfile.this, Profile.class));
            }
        });

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Seller sellerProfile = snapshot.getValue(Seller.class);

                if (sellerProfile != null) {
                    String nameSeller = sellerProfile.username;
                    String userEmail = sellerProfile.email;
                    String userPhone = sellerProfile.phone;
                    String passwordSeller = sellerProfile.password;

                    name.setText(nameSeller);
                    email.setText(userEmail);
                    phone.setText(userPhone);
                    password.setText(passwordSeller);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(UpdateProfile.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(user);
            }
        });
    }

    private void updateProfile(FirebaseUser user) {
        String sellerName = name.getText().toString();
        String sellerEmail = email.getText().toString();
        String sellerPhone = phone.getText().toString();
        String sellerPassword = password.getText().toString();

        Seller seller = new Seller(sellerName, sellerEmail, sellerPhone, sellerPassword);
        DatabaseReference refProfile = FirebaseDatabase.getInstance().getReference("Seller");

        userID = user.getUid();

        refProfile.child(userID).setValue(seller).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(sellerName).build();
                    user.updateProfile(profileUpdate);

                    Toast.makeText(UpdateProfile.this, "Update Profile", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UpdateProfile.this, Profile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(UpdateProfile.this, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}