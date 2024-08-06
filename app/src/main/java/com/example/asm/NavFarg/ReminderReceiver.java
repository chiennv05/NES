package com.example.asm.NavFarg;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Đã đến lúc viết lời biết ơn!", Toast.LENGTH_LONG).show();
    }
}

