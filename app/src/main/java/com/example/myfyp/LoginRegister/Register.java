package com.example.myfyp.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {
    Button loginPage, register;
    EditText username, email, phone, password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        loginPage = findViewById(R.id.btnLoginPage);
        register = findViewById(R.id.btnRegister);
        username = findViewById(R.id.txtUsername);
        email = findViewById(R.id.txtEmail);
        phone = findViewById(R.id.txtPhone);
        password = findViewById(R.id.txtPassword);

        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });
    }

    private void RegisterUser() {
        String name = username.getText().toString().trim();
        String emailUser = email.getText().toString().trim();
        String phoneUser = phone.getText().toString().trim();
        String passwordUser = password.getText().toString().trim();

        if (name.isEmpty()) {
            username.setError("Username is required");
            username.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()) {
            email.setError("Please provide valid email");
            email.requestFocus();
            return;
        }

        if (phoneUser.isEmpty()) {
            phone.setError("Phone number is required");
            phone.requestFocus();
            return;
        }

        if (passwordUser.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if (passwordUser.length() < 6) {
            password.setError("Password Must be >= 6 Characters");
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailUser, passwordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Seller seller = new Seller(name, emailUser, phoneUser, passwordUser);

                    FirebaseDatabase.getInstance().getReference("Seller").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(seller)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, Login.class));
                            }else {
                                Toast.makeText(Register.this, "Register Failed! Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}