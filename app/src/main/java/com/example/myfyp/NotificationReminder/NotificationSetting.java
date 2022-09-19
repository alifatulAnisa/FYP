package com.example.myfyp.NotificationReminder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.myfyp.LoginRegister.Profile;
import com.example.myfyp.Products.AddProduct;
import com.example.myfyp.Products.Categories;
import com.example.myfyp.Products.Category;
import com.example.myfyp.Products.CategoryAdapter;
import com.example.myfyp.Products.CategoryProductDisplay;
import com.example.myfyp.Products.Product;
import com.example.myfyp.Products.ProductAdapter;
import com.example.myfyp.Products.ProductCategory;
import com.example.myfyp.Products.ProductList;
import com.example.myfyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotificationSetting extends AppCompatActivity implements NotificationAdapter.NotificationClickListener {
    BottomNavigationView bottomNavigationView;

    FloatingActionButton actionButton;

    ToggleButton btnNotification;
    TextView allow, timeNoti;
    ImageView check;

    DatabaseReference db;

    RecyclerView recyclerViewCat;
    RecyclerView recyclerViewNoti;
    NotificationAdapter notificationAdapter;
    ArrayList<NotiModule> notiList;

    NotiModule notiModule;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        createNotificationChannel();

        bottomNavigationView = findViewById(R.id.bottomNav);
        actionButton = findViewById(R.id.btnAddNew);
        btnNotification = findViewById(R.id.btnToggleNoti);
        allow = findViewById(R.id.txtAllow);
        recyclerViewNoti = findViewById(R.id.recyclerViewNotification);
        recyclerViewCat = findViewById(R.id.recyclerViewCategory);
        timeNoti = findViewById(R.id.txtTime);
        check = findViewById(R.id.imgCheck);

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.notification);

        notiModule = new NotiModule();
        product = new Product();

        recyclerViewNoti.setHasFixedSize(true);
        recyclerViewNoti.setLayoutManager(new LinearLayoutManager(this));
        notiList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this, notiList, this::selectedNotification);
        recyclerViewNoti.setAdapter(notificationAdapter);

        db = FirebaseDatabase.getInstance().getReference("Reminder");

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectTime();
                Toast.makeText(NotificationSetting.this, "Reminder Set!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(NotificationSetting.this, NotificationService.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationSetting.this, 0, intent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();
                long tenSeconds = 1000 * 10;

                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        timeAtButtonClick + tenSeconds,
                        pendingIntent);
            }
        });

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    NotiModule n = dataSnapshot.getValue(NotiModule.class);
                    notiList.add(n);
                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotificationSetting.this, ProductCategory.class));
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder Product Expiry";
            String description = "Channel to reminder expiry date";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("notifyExpiry", name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void selectedNotification(NotiModule notiModule) {
        //startActivity(new Intent(this, NotificationSetting.class).putExtra("noti", notiModule));
    }
}