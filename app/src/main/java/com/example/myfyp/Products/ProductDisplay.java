package com.example.myfyp.Products;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.example.myfyp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProductDisplay extends AppCompatActivity {
    Product product;
    Intent intent;
    TextView productName, manufacturedDate, expiryDate, price, quantity, choose;
    ImageView productImage;
    Button back, update, delete;
    EditText category;
    Uri imageUri;

    private int year, month, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        productImage = findViewById(R.id.imgProductR);
        productName = findViewById(R.id.txtProdNameR);
        manufacturedDate = findViewById(R.id.dateManufacturedR);
        expiryDate = findViewById(R.id.dateExpiryR);
        quantity = findViewById(R.id.numQuantityR);
        back = findViewById(R.id.btnBackProduct);
        update = findViewById(R.id.btnUpdateProduct);
        delete = findViewById(R.id.btnDeleteProduct);
        category = findViewById(R.id.txtCatName);
        price = findViewById(R.id.priceR);

        intent = getIntent();

        product = new Product();

        //productCategory.setOnItemSelectedListener(this);

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDisplay.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted data can't be undo");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query query = ref.child("Product").orderByChild("prodName").equalTo(product.prodName);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    dataSnapshot.getRef().removeValue();

                                    Toast.makeText(ProductDisplay.this, "Product Deleted", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ProductDisplay.this, ProductList.class));
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ProductDisplay.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        manufacturedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                date = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ProductDisplay.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        month = month + 1;
                        manufacturedDate.setText(date+"/"+month+"/"+year);
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });

        expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                date = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ProductDisplay.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        month = month + 1;
                        expiryDate.setText(date+"/"+month+"/"+year);
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<>();
                map.put("prodName", productName.getText().toString());
                map.put("typeCosmetic", category.getText().toString());
                map.put("mDate", manufacturedDate.getText().toString());
                map.put("expiry", expiryDate.getText().toString());
                map.put("price", price.getText().toString());
                map.put("prodQuantity", quantity.getText().toString());

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query query = ref.child("Product").orderByChild("prodName").equalTo(product.prodName);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                dataSnapshot.getRef().updateChildren(map);

                                Toast.makeText(ProductDisplay.this, "Product Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ProductDisplay.this, ProductList.class));
                            }
                        }else {
                            Toast.makeText(ProductDisplay.this, "Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDisplay.this, ProductList.class));
            }
        });

        if (intent != null) {
            product = (Product) intent.getSerializableExtra("data");
            Calendar calendar = Calendar.getInstance();
            String pickerDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

            Glide.with(getApplicationContext()).load(product.getImage()).into(productImage);
            String name = product.getProdName();
            //String image = product.getImage();
            String type = product.getTypeCosmetic();
            String mDate = product.getmDate();
            String eDate = product.getExpiry(pickerDateString);
            String priceProd = product.getPrice();
            String quantityProd = product.getProdQuantity();

            //productImage.setImageURI(Uri.parse(image));
            productName.setText(name);
            category.setText(type);
            manufacturedDate.setText(mDate);
            expiryDate.setText(eDate);
            price.setText(priceProd);
            quantity.setText(quantityProd);
        }
    }

    /*private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            productImage.setImageURI(imageUri);
        }
    }

    /*private void updateData() {
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + ".");
        fileRef.putFile(im).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("image", uri.toString());
                        map.put("prodName", productName.getText().toString());
                        map.put("typeCosmetic", category.getText().toString());
                        map.put("mDate", manufacturedDate.getText().toString());
                        map.put("expiry", expiryDate.getText().toString());
                        map.put("prodQuantity", quantity.getText().toString());

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query query = ref.child("Product").orderByChild("prodName").equalTo(product.prodName);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        dataSnapshot.getRef().updateChildren(map);

                                        Toast.makeText(ProductDisplay.this, "Product Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ProductDisplay.this, ProductList.class));
                                    }
                                }else {
                                    Toast.makeText(ProductDisplay.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDisplay.this, "Adding product failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

}