package com.example.myfyp.Products;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myfyp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomDialog extends AppCompatDialogFragment {
    DatabaseReference databaseReference;

    EditText brand;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.category_dialog, null);

        builder.setView(view)
                .setTitle("Enter New Category")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String brandName = brand.getText().toString();

                        Categories cat = new Categories(brandName);
                        databaseReference = FirebaseDatabase.getInstance().getReference("Category");

                        String UserID = databaseReference.push().getKey();
                        databaseReference.child(UserID).setValue(cat);

                        Toast.makeText(getContext(), "New category added", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(), Category.class));
                    }
                });

        brand = view.findViewById(R.id.txtBrandName);

        return builder.create();
    }

}
