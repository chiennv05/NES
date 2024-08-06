package com.example.asm.NavFarg;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.Adapter.BietOnAdapter;
import com.example.asm.Model.BietOn;
import com.example.asm.R;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FargBieton extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    TextView btnadd;
    RecyclerView rs_bieton;
    Context context;
    BietOnAdapter adapter;
    List<BietOn> list;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.farg_bieton, container, false);

        // Khởi tạo RecyclerView và adapter
        rs_bieton = view.findViewById(R.id.rs_bieton);
        list = new ArrayList<>();
        adapter = new BietOnAdapter(context, list);
        rs_bieton.setLayoutManager(new LinearLayoutManager(context));
        rs_bieton.setAdapter(adapter);

        // Khởi tạo Firestore và Auth
        FirebaseApp.initializeApp(requireContext());
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Lấy danh sách biệt ơn
        getListBietOn();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnadd = view.findViewById(R.id.btnadd);



        btnadd.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.add_bieton, null, false);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.show();

            EditText edtloibieton = dialogView.findViewById(R.id.edLoiBietOn_add);
            Button btnthem = dialogView.findViewById(R.id.btnSave_add);

            btnthem.setOnClickListener(view3 -> {
                String loibieton = edtloibieton.getText().toString();
                if (loibieton.isEmpty()) {
                    Toast.makeText(context, "Trống", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat time = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String date = time.format(new Date());

                    BietOn bietOn = new BietOn(date, loibieton);

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String username = user.getUid();
                        db.collection("bieton1").document(username).collection("loibieton")
                                .add(bietOn)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d("Success", "DocumentSnapshot successfully written!");
                                    list.add(bietOn);
                                    adapter.notifyItemInserted(list.size() - 1); // Thông báo Adapter có mục mới
                                    Toast.makeText(context, "Lưu thành công", Toast.LENGTH_SHORT).show();
                                    // Chờ 1 chút để Toast hiển thị trước khi đóng dialog
                                    new android.os.Handler().postDelayed(dialog::dismiss, 500);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Lỗi", e.getMessage());
                                    Toast.makeText(context, "Lỗi khi lưu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(context, "Không thể xác định người dùng", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    // Hàm lấy danh sách biệt ơn từ Firestore
    private void getListBietOn() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userID = user.getUid(); // Lấy id người dùng hiện tại

            db.collection("bieton1").document(userID).collection("loibieton").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    list.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        BietOn bietOn = document.toObject(BietOn.class);
                        list.add(bietOn);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Lỗi", "Lỗi khi tải dữ liệu", task.getException());
                    Toast.makeText(context, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Không thể xác định người dùng", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCustomToast(String message) {
        LayoutInflater inflater = LayoutInflater.from(context); // Sử dụng context từ Fragment
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
