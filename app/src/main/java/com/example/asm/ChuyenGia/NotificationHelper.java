package com.example.asm.ChuyenGia;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.asm.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "MyAppChannel";
    private final Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void showNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo kênh thông báo nếu đang chạy trên Android Oreo trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My App Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("My App Notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        // Xây dựng thông báo với biểu tượng nhỏ
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.calo) // Thay thế ic_notification bằng tên tài nguyên của bạn
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(1, builder.build());
    }
}
