package com.vrgc.eguidance.Activity.Doctor;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrgc.eguidance.Adapter.ScheduleAdapter;
import com.vrgc.eguidance.Model.ScheduleModel;
import com.vrgc.eguidance.R;
import com.vrgc.eguidance.Utils.ReminderReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewScheduleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ScheduleModel> scheduleList = new ArrayList<>();
    ScheduleAdapter adapter;
    String doctorId;
    DatabaseReference bookedRef, userRef;
    EditText filterDate;
    Button btnClearFilter;
    String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        recyclerView = findViewById(R.id.scheduleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleAdapter(scheduleList, this);
        recyclerView.setAdapter(adapter);

        filterDate = findViewById(R.id.filterDate);
        btnClearFilter = findViewById(R.id.btnClearFilter);

        doctorId = FirebaseAuth.getInstance().getUid();
        bookedRef = FirebaseDatabase.getInstance().getReference("booked");
        userRef = FirebaseDatabase.getInstance().getReference("users");

        showDatePicker();
        btnClearFilter.setOnClickListener(v -> {
            selectedDate = "";
            filterDate.setText("");
            loadSchedule(); // reset
        });

        loadSchedule(); // initial load with realtime updates
    }

    private void showDatePicker() {
        filterDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
                selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                filterDate.setText(selectedDate);
                loadSchedule(); // reapply filter
            }, year, month, day).show();
        });
    }

    private void loadSchedule() {
        bookedRef.orderByChild("doctorId").equalTo(doctorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scheduleList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (!"accepted".equals(snap.child("status").getValue(String.class))) continue;

                    String date = snap.child("counseling_date").getValue(String.class);
                    if (!selectedDate.isEmpty() && !selectedDate.equals(date)) continue; // filter logic

                    String userId = snap.child("userId").getValue(String.class);
                    String condition = snap.child("user_condition").getValue(String.class);
                    String time = snap.child("time").getValue(String.class);

                    ScheduleModel model = new ScheduleModel();
                    model.date = date;
                    model.time = time;
                    model.userId = userId;
                    model.condition = condition;

                    userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnap) {
                            model.userName = userSnap.child("name").getValue(String.class);
                            scheduleReminder(ViewScheduleActivity.this, model.date, model.time, model.userName);
                            scheduleList.add(model);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void scheduleReminder(Context context, String dateStr, String timeStr, String userName) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            Date sessionTime = sdf.parse(dateStr + " " + timeStr);

            long sessionMillis = sessionTime.getTime();
            long reminderTime = sessionMillis - (15 * 60 * 1000); // 15 minutes before

            if (reminderTime <= System.currentTimeMillis()) return; // skip if past

            Intent intent = new Intent(context, ReminderReceiver.class);
            intent.putExtra("msg", "You have a session with " + userName + " at " + timeStr);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    (int) reminderTime,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (!alarmManager.canScheduleExactAlarms()) {
                    Toast.makeText(this, "Exact alarms not allowed. Enable in Settings > Apps > Special access > Exact alarms", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
