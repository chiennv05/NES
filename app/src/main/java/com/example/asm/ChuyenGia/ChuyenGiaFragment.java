package com.example.asm.ChuyenGia;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ChuyenGiaFragment extends Fragment {
    private EditText edEmail, edPass, edConfilm;
    private Button btnDangKyChuyenGia;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chuyen_gia, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDangKyChuyenGia = view.findViewById(R.id.btnDangKyChuyenGia);
        edEmail = view.findViewById(R.id.edEmail_dk);
        edPass = view.findViewById(R.id.edPass_dk);
        edConfilm = view.findViewById(R.id.edConfilm_dk);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btnDangKyChuyenGia.setOnClickListener(v -> {
            String email = edEmail.getText().toString();
            String pass = edPass.getText().toString();
            String confilm = edConfilm.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Định dạng email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                Toast.makeText(getContext(), "Bạn chưa nhập pass", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.length() < 6) {
                Toast.makeText(getContext(), "Bạn phải nhập các ký tự lớn hơn 6", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.matches(".*[A-Z].*") || !pass.matches(".*[a-z].*")) {
                Toast.makeText(getContext(), "pass phải có cả in hoa và in thường", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(confilm)) {
                Toast.makeText(getContext(), "Bạn chưa nhập Condilm", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.equals(confilm)) {
                Toast.makeText(getContext(), "Mật khẩu không trùng nhau", Toast.LENGTH_SHORT).show();
                return;
            }

            String role = "chuyengia";

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userId = mAuth.getCurrentUser().getUid();
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", email);
                                user.put("role", role);

                                firestore.collection("users").document(userId)
                                        .set(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getContext(), "Lỗi lưu dữ liệu", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(getContext(), "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                            }
                        }});
        });

    }
}