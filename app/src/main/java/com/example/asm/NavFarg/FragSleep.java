package com.example.asm.NavFarg;

import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.asm.R;
import com.example.asm.Adapter.NotificationUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FragSleep extends Fragment {
    private TextView tvSleepGoal;
    private Button btnSetSleepTime;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_giacngu, container, false);
        tvSleepGoal = view.findViewById(R.id.txgiongu);
        btnSetSleepTime = view.findViewById(R.id.btnSetSleepTime);

        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        btnSetSleepTime.setOnClickListener(view1 -> {
            Log.d("FragSleep", "Đang thiết lập giờ ngủ và giờ dậy");
            showTimePickerDialog();
        });

        // Tạo kênh thông báo khi khởi động ứng dụng
        NotificationUtil.createNotificationChannel(getContext());

        loadSleepData();

        return view;
    }



    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (TimePicker view, int hourOfDay, int minute1) -> {
                    Log.d("FragSleep", "Thời gian đã chọn: " + hourOfDay + ":" + minute1);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Lưu giờ ngủ
                    String sleepTime = String.format("%02d:%02d", hourOfDay, minute1);
                    editor.putString("sleep_time", sleepTime);

                    // Lấy giờ hiện tại để thiết lập giờ dậy
                    Calendar now = Calendar.getInstance();
                    int wakeHour = now.get(Calendar.HOUR_OF_DAY);
                    int wakeMinute = now.get(Calendar.MINUTE);
                    String wakeTime = String.format("%02d:%02d", wakeHour, wakeMinute);
                    editor.putString("wake_time", wakeTime);

                    editor.apply();

                    // Lưu giờ ngủ lên Firebase
                    saveSleepTimeToFirebase(sleepTime, wakeTime);

                    loadSleepData();
                    checkSleepSufficiency();

                    sendNotification("Cập nhật giấc ngủ", "Giờ ngủ đã được thiết lập!");
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private void saveSleepTimeToFirebase(String sleepTime, String wakeTime) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Lấy ID người dùng hiện tại

        Map<String, Object> sleepData = new HashMap<>();
        sleepData.put("sleep_time", sleepTime);
        sleepData.put("wake_time", wakeTime);

        db.collection("users").document(userId).collection("sleepData")
                .document("currentSleep") // Có thể đổi tên document tùy ý
                .set(sleepData)
                .addOnSuccessListener(aVoid -> Log.d("FragSleep", "Giờ ngủ đã được lưu lên Firebase"))
                .addOnFailureListener(e -> Log.e("FragSleep", "Lỗi lưu giờ ngủ lên Firebase", e));
    }


    private void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NotificationUtil.CHANNEL_ID,
                    "Thông báo giấc ngủ",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Kênh thông báo giấc ngủ");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), NotificationUtil.CHANNEL_ID)
                .setSmallIcon(R.drawable.calo) // Thay đổi hình ảnh biểu tượng theo nhu cầu của bạn
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(1, builder.build());
    }

    private void loadSleepData() {
        String sleepTime = sharedPreferences.getString("sleep_time", "Chưa thiết lập");
        String wakeTime = sharedPreferences.getString("wake_time", "Chưa thiết lập");
        //giờ ngủ và giờ dậy ở đây
        tvSleepGoal.setText("Giờ ngủ: " + sleepTime);

    }

    private void checkSleepSufficiency() {
        String sleepTimeStr = sharedPreferences.getString("sleep_time", "00:00");
        String wakeTimeStr = sharedPreferences.getString("wake_time", "00:00");

        String[] sleepTimeParts = sleepTimeStr.split(":");
        String[] wakeTimeParts = wakeTimeStr.split(":");

        int sleepHour = Integer.parseInt(sleepTimeParts[0]);
        int sleepMinute = Integer.parseInt(sleepTimeParts[1]);
        int wakeHour = Integer.parseInt(wakeTimeParts[0]);
        int wakeMinute = Integer.parseInt(wakeTimeParts[1]);

        Calendar sleepTime = Calendar.getInstance();
        sleepTime.set(Calendar.HOUR_OF_DAY, sleepHour);
        sleepTime.set(Calendar.MINUTE, sleepMinute);

        Calendar wakeTime = Calendar.getInstance();
        wakeTime.set(Calendar.HOUR_OF_DAY, wakeHour);
        wakeTime.set(Calendar.MINUTE, wakeMinute);

        // Nếu thời gian thức trước thời gian ngủ, thêm một ngày cho thời gian thức
        if (wakeTime.before(sleepTime)) {
            wakeTime.add(Calendar.DATE, 1);
        }

        long sleepDurationMillis = wakeTime.getTimeInMillis() - sleepTime.getTimeInMillis();
        int sleepDurationHours = (int) (sleepDurationMillis / (1000 * 60 * 60));
        int sleepDurationMinutes = (int) ((sleepDurationMillis / (1000 * 60)) % 60);

        float totalSleepHours = sleepDurationHours + (sleepDurationMinutes / 60.0f);

        // Thay đổi điều kiện kiểm tra đủ giấc để phù hợp hơn
        if (totalSleepHours >= 6.5 && totalSleepHours <= 8) {
            Toast.makeText(getContext(), "Bạn đã ngủ đủ giấc!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Bạn chưa ngủ đủ giấc", Toast.LENGTH_LONG).show();
        }
    }
}
