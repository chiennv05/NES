package com.example.asm.HoatDongTab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.Adapter.ChayBoAdapter;
import com.example.asm.Model.BietOn;
import com.example.asm.Model.ChayBo;
import com.example.asm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThangFrag extends Fragment {

    private RecyclerView recyclerView;
    private ChayBoAdapter myAdapter; // Changed variable name to match your adapter class
    private List<ChayBo> myDataList;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_thang, container, false);

        // Khởi tạo RecyclerView và Adapter
        recyclerView = view.findViewById(R.id.rv_thang);
        myDataList = new ArrayList<>();
        myAdapter = new ChayBoAdapter(myDataList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Lấy dữ liệu từ Firestore và cập nhật Adapter
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = sdf.format(new Date());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            db.collection("hoatdong")
                    .whereEqualTo("date", date)
                    .whereEqualTo("uid", uid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                myDataList.clear(); // Xóa dữ liệu cũ nếu có
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Kiểm tra giá trị trước khi sử dụng
                                    Long stepCountLong = document.getLong("buocchan");
                                    int stepCount = (stepCountLong != null) ? stepCountLong.intValue() : 0;
                                    Double distance = document.getDouble("khoangcach");
                                    Double calories = document.getDouble("calori");
                                    Long durationLong = document.getLong("thoigian");
                                    long duration = (durationLong != null) ? durationLong : 0;

                                    // Cập nhật dữ liệu và thêm vào danh sách
                                    ChayBo chayBo = new ChayBo(stepCount,
                                            (distance != null) ? distance : 0.0,
                                            (calories != null) ? calories : 0.0,
                                            duration);
                                    myDataList.add(chayBo);
                                }
                                myAdapter.notifyDataSetChanged(); // Cập nhật adapter với dữ liệu mới
                            } else {
                                Toast.makeText(getContext(), "Lỗi khi lấy dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Người dùng chưa đăng nhập.", Toast.LENGTH_SHORT).show();
        }
    }


}
