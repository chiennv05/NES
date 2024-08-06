package com.example.asm.Login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.asm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    private EditText edEmail, edPass, edConfilm;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        edEmail = findViewById(R.id.edEmail_dk);
        edPass = findViewById(R.id.edPass_dk);
        edConfilm = findViewById(R.id.edConfilm_dk);
        btnRegister = findViewById(R.id.btnDangKyMoi);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String pass = edPass.getText().toString();
                String confilm = edConfilm.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(register.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(register.this, "Định dạng email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(register.this, "Bạn chưa nhập pass", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.length() < 6) {
                    Toast.makeText(register.this, "Bạn phải nhập các ký tự lớn hơn 6", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.matches(".*[A-Z].*") || !pass.matches(".*[a-z].*")) {
                    Toast.makeText(register.this, "pass phải có cả in hoa và in thường", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confilm)) {
                    Toast.makeText(register.this, "Bạn chưa nhập Condilm", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.equals(confilm)) {
                    Toast.makeText(register.this, "Mật khẩu không trùng nhau", Toast.LENGTH_SHORT).show();
                    return;
                }

                String role = "user";

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
                                                        Toast.makeText(register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(register.this, "Lỗi lưu dữ liệu", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(register.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}