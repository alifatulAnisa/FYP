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

import com.example.myfyp.Products.ProductList;
import com.example.myfyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    //Variables
    Button registerPage, login;
    EditText email, password;

    private FirebaseAuth mAuth; //Variable of firebase to connect to user authentication in firebase

    //To start activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //Layout for login page
        mAuth = FirebaseAuth.getInstance(); //Declare an instance of FirebaseAuth to register/login for new/existing user

        //initialize variables
        registerPage = findViewById(R.id.btnRegPage);
        login = findViewById(R.id.btnLogin);
        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);

        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
    }

    private void LoginUser() {
        String emailUser = email.getText().toString().trim();
        String passwordUser = password.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()) {
            email.setError("Please provide valid email");
            email.requestFocus();
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

        mAuth.signInWithEmailAndPassword(emailUser, passwordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, ProductList.class));
                }else {
                    Toast.makeText(Login.this, "Failed to login! Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(Login.this, ProductList.class));
            finish();
        }
    }
}