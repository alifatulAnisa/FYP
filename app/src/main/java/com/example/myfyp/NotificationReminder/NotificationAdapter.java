package com.example.myfyp.NotificationReminder;

import static android.content.Context.ALARM_SERVICE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfyp.Products.Categories;
import com.example.myfyp.Products.CategoryAdapter;
import com.example.myfyp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context context;
    ArrayList<NotiModule> list;
    NotificationClickListener notificationClickListenerClickListener;

    public interface NotificationClickListener {
        void selectedNotification (NotiModule notiModule);
    }

    public NotificationAdapter(Context context, ArrayList<NotiModule> list, NotificationClickListener notificationClickListenerClickListener) {
        this.context = context;
        this.list = list;
        this.notificationClickListenerClickListener = notificationClickListenerClickListener;
    }

    public void setList (ArrayList<NotiModule> list) {
        this.list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notification_entry, parent, false);
        return new MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NotiModule noti = list.get(position);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Product").child("expiry");
        Calendar calendar = Calendar.getInstance();
        String pickerDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


        holder.time.setText(noti.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationClickListenerClickListener.selectedNotification(noti);
                holder.check.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        ImageView check;

        public MyViewHolder(@NonNull View itemView) {
            super (itemView);

            time = itemView.findViewById(R.id.txtTime);
            check = itemView.findViewById(R.id.imgCheck);

            check.setVisibility(View.INVISIBLE);
        }
    }

}
