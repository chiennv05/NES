package com.example.asm.ThongBao;


    import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

    public class NotificationUtil {
        public static final String CHANNEL_ID = "SLEEP_NOTIFICATION_CHANNEL";

        public static void createNotificationChannel(Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Thông báo giấc ngủ",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                channel.setDescription("Kênh thông báo giấc ngủ");
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

