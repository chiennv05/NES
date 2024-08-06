package com.example.asm.HoatDongTab;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.asm.FileBaseHelper.DataBaseHelper;
import com.example.asm.MainActivity;
import com.example.asm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BuocChanFrag extends Fragment implements SensorEventListener {
    private static final int REQUEST_CODE_ACTIVITY_RECOGNITION = 100;
    private TextView txbuocchan, txkilomet, txcalo, txtime, btnketthuc;
    private Button btnbatdau;
    private long startTime;
    private DataBaseHelper helper;
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;
    private int stepCount = 0;
    private Handler handler;
    private Runnable runnable;

    public BuocChanFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_chaybo, container, false);

        txbuocchan = view.findViewById(R.id.tx_buocchan);
        txkilomet = view.findViewById(R.id.txkilomet);
        txcalo = view.findViewById(R.id.txcalo);
        txtime = view.findViewById(R.id.txtime);
        btnbatdau = view.findViewById(R.id.btnbatdau);
        btnketthuc = view.findViewById(R.id.btnketthuc);

        helper = new DataBaseHelper();
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, REQUEST_CODE_ACTIVITY_RECOGNITION);
        }

        btnbatdau.setOnClickListener(view1 -> startTracking());
        btnketthuc.setOnClickListener(view1 -> stopTracking());

        return view;
    }

    private void startTracking() {
        startTime = System.currentTimeMillis();
        stepCount = 0;
        txbuocchan.setText(String.valueOf(stepCount));
        txkilomet.setText("0.0 km");
        txcalo.setText("0.0 cal");
        txtime.setText("00:00:00");

        sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_UI);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                int seconds = (int) (elapsedTime / 1000) % 60;
                int minutes = (int) (elapsedTime / (1000 * 60)) % 60;
                int hours = (int) (elapsedTime / (1000 * 60 * 60));
                txtime.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    private void stopTracking() {
        sensorManager.unregisterListener(this);
        handler.removeCallbacks(runnable);

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;
        double distance = stepCount * 0.0008;
        double calories = stepCount * 0.05;

        txkilomet.setText(String.format("%.2f km", distance));
        txcalo.setText(String.format("%.2f cal", calories));
        txtime.setText(String.format("%d s", duration));

        saveActivityData(stepCount, distance, calories, duration);
    }







    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] > 0) {
                stepCount++;
                txbuocchan.setText(String.valueOf(stepCount));

                double distance = stepCount * 0.0008;
                double calories = stepCount * 0.05;
                txkilomet.setText(String.format("%.2f km", distance));
                txcalo.setText(String.format("%.2f cal", calories));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

//    private void saveActivityData(int buocchan, double khoangcach, double calori, long thoigian) {
//        FirebaseFirestore db = helper.getDatabase();
//        CollectionReference activities = db.collection("hoatdong");
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("buocchan", buocchan);  // Change key to "buocchan"
//        data.put("calori", calori);
//        data.put("khoangcach", khoangcach);
//        data.put("thoigian", thoigian);
//        data.put("timestamp", System.currentTimeMillis());
//
//        activities.add(data).addOnSuccessListener(documentReference -> {
//            System.out.println("Tài liệu được thêm với ID: " + documentReference.getId());
//        }).addOnFailureListener(e -> {
//            System.out.println("Lỗi khi thêm tài liệu: " + e.getMessage());
//        });
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ACTIVITY_RECOGNITION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Quyền nhận diện hoạt động đã được cấp.");
            } else {
                System.out.println("Quyền nhận diện hoạt động đã bị từ chối.");
            }
        }
    }
    private void saveActivityData(int buocchan, double khoangcach, double calori, long thoigian) {
        FirebaseFirestore db = helper.getDatabase();
        CollectionReference activities = db.collection("hoatdong");

        Map<String, Object> data = new HashMap<>();
        data.put("buocchan", buocchan);
        data.put("calori", calori);
        data.put("khoangcach", khoangcach);
        data.put("thoigian", thoigian);
        data.put("timestamp", System.currentTimeMillis());

        // Lưu ngày vào Firestore
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = sdf.format(new Date());
        data.put("date", date);

        // Thêm UID của người dùng
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            data.put("uid", uid);

            activities.add(data).addOnSuccessListener(documentReference -> {
                System.out.println("Tài liệu được thêm với ID: " + documentReference.getId());
            }).addOnFailureListener(e -> {
                System.out.println("Lỗi khi thêm tài liệu: " + e.getMessage());
            });
        } else {
            System.out.println("Người dùng chưa đăng nhập.");
        }
    }

}
