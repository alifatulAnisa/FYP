package com.example.myfyp.Products;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfyp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class AddProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button backAddProduct, btnAdd;
    ImageView imgProdView;
    EditText prodName, dateM, dateE, price, quantity, category;
    Spinner cosmetic;
    Uri imageUri;
    Intent intent;

    String item;

    Product product;
    Categories categories;

    DatabaseReference databaseReference;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    NotificationManagerCompat notificationManagerCompat;
    Notification notification;

    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        backAddProduct = findViewById(R.id.btnBackAddProd);
        btnAdd = findViewById(R.id.btnAddProd);
        imgProdView = findViewById(R.id.imgProduct);
        prodName = findViewById(R.id.txtProdName);
        dateM = findViewById(R.id.dateManufactured);
        dateE = findViewById(R.id.dateExpiry);
        quantity = findViewById(R.id.numQuantity);
        category = findViewById(R.id.txtCatName);
        price = findViewById(R.id.price);

        intent = getIntent();

        databaseReference = FirebaseDatabase.getInstance().getReference("Product");

        product = new Product();
        categories = new Categories();

        imgProdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        dateM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddProduct.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        dateM.setText(day+"/"+month+"/"+year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        dateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddProduct.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        dateE.setText(day+"/"+month+"/"+year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    //uploadFirebase(imageUri);
                    saveData(imageUri);
                }else {
                    Toast.makeText(AddProduct.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddProduct.this, ProductCategory.class));
            }
        });

        if (intent != null) {

            categories = (Categories) intent.getSerializableExtra("data");
            String catName = categories.getCategoryName();

            category.setText(catName);
        }
    }

    private void notificationAlert() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myCh")
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setContentTitle("First Notification")
                .setContentText("Body of message");

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imgProdView.setImageURI(imageUri);
        }
    }

    private void saveData(Uri uri) {
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String nameProduct = prodName.getText().toString();
                        String categoryProduct = category.getText().toString();
                        String mDateProduct = dateM.getText().toString();
                        String eDateProduct = dateE.getText().toString();
                        String priceProduct = price.getText().toString();
                        String quantityProduct = quantity.getText().toString();

                        Product prod = new Product(uri.toString(), nameProduct, categoryProduct, mDateProduct, eDateProduct, priceProduct, quantityProduct);
                        databaseReference = FirebaseDatabase.getInstance().getReference("Product");
                        String UserID = databaseReference.push().getKey();
                        databaseReference.child(UserID).setValue(prod);

                        Toast.makeText(AddProduct.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddProduct.this, ProductList.class));

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProduct.this, "Adding product failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = cosmetic.getSelectedItem().toString();
        //choose.setText(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}